package com.example.customer_management.impl;

import com.example.customer_management.dto.*;
import com.example.customer_management.entity.Customer;
import com.example.customer_management.entity.CustomerAddress;
import com.example.customer_management.entity.CustomerMobile;
import com.example.customer_management.entity.FamilyMember;
import com.example.customer_management.repository.*;
import com.example.customer_management.service.CustomerService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMobileRepository customerMobileRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            CustomerMobileRepository customerMobileRepository,
            CustomerAddressRepository customerAddressRepository,
            FamilyMemberRepository familyMemberRepository) {

        this.customerRepository = customerRepository;
        this.customerMobileRepository = customerMobileRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.familyMemberRepository = familyMemberRepository;
    }

    // create customer
    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {
        if (customerRepository.existsByNic(requestDto.getNic())) {
            throw new RuntimeException("NIC already exists");
        }
        Customer customer = new Customer();
        customer.setNic(requestDto.getNic());
        customer.setName(requestDto.getName());
        customer.setDob(requestDto.getDob());

        Customer savedCustomer = customerRepository.save(customer);

        Long customerId = savedCustomer.getId();

        // 3. Save Mobiles
        if (requestDto.getMobiles() != null) {
            for (MobileDto m : requestDto.getMobiles()) {
                CustomerMobile mobile = new CustomerMobile();
                mobile.setMobile(m.getMobile());
                mobile.setCustomerId(customerId);

                customerMobileRepository.save(mobile);
            }
        }

        // 4. Save Addresses
        if (requestDto.getAddresses() != null) {
            for (AddressDto a : requestDto.getAddresses()) {
                CustomerAddress address = new CustomerAddress();
                address.setAddressLine(a.getAddressLine());
                address.setCustomerId(customerId);

                customerAddressRepository.save(address);
            }
        }

        // 5. Save Family Members (IDs only)
        if (requestDto.getFamilyMemberIds() != null) {
            for (Long fId : requestDto.getFamilyMemberIds()) {

                Customer family = customerRepository.findById(fId)
                        .orElseThrow(() -> new RuntimeException("Family member not found: " + fId));

                FamilyMember fm = new FamilyMember();
                fm.setCustomerId(customerId);
                fm.setMemberName(family.getName());
                fm.setRelation("FAMILY");

                familyMemberRepository.save(fm);
            }
        }

        return mapToResponse(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return mapToResponse(customer);
    }

    // update customer
    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto requestDto) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(requestDto.getName());
        customer.setDob(requestDto.getDob());

        if (!customer.getNic().equals(requestDto.getNic())) {
            if (customerRepository.existsByNic(requestDto.getNic())) {
                throw new RuntimeException("NIC already exists");
            }
            customer.setNic(requestDto.getNic());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }

    // get all customers
    @Override
    public List<CustomerResponseDto> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // BULK UPLOAD
    @Override
    @Transactional
    public BulkUploadResultDto bulkUploadCustomers(MultipartFile file) {

        int success = 0;
        int failed = 0;
        int batchSize = 500;

        List<Customer> batchList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                try {
                    String name = getCellValue(row.getCell(0));
                    String dob = getCellValue(row.getCell(1));
                    String nic = getCellValue(row.getCell(2));

                    String mobile1 = getCellValue(row.getCell(3));
                    String mobile2 = getCellValue(row.getCell(4));
                    String address1 = getCellValue(row.getCell(5));
                    String address2 = getCellValue(row.getCell(6));

                    // VALIDATION
                    if (name.isEmpty() || dob.isEmpty() || nic.isEmpty()) {
                        failed++;
                        continue;
                    }

                    Customer customer;

                    // CREATE OR UPDATE
                    if (customerRepository.existsByNic(nic)) {
                        customer = customerRepository.findByNic(nic).get();
                    } else {
                        customer = new Customer();
                    }

                    customer.setName(name);
                    customer.setDob(dob);
                    customer.setNic(nic);

                    batchList.add(customer);

                    // BATCH SAVE CUSTOMER
                    if (batchList.size() == batchSize) {
                        customerRepository.saveAll(batchList);

                        // AFTER SAVE → handle mobiles/addresses
                        processChildData(batchList, sheet, i - batchList.size() + 1);

                        batchList.clear();
                    }

                    success++;

                } catch (Exception e) {
                    failed++;
                }
            }

            // Save remaining
            if (!batchList.isEmpty()) {
                customerRepository.saveAll(batchList);
                processChildData(batchList, sheet, sheet.getLastRowNum() - batchList.size() + 1);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file");
        }

        BulkUploadResultDto result = new BulkUploadResultDto();
        result.setTotalRecords(success + failed);
        result.setSuccessCount(success);
        result.setFailedCount(failed);
        result.setMessage("Bulk upload with mobiles & addresses completed");

        return result;
    }

    private void processChildData(List<Customer> customers, Sheet sheet, int startRow) {

        for (int i = 0; i < customers.size(); i++) {

            Customer customer = customers.get(i);
            Long customerId = customer.getId();

            Row row = sheet.getRow(startRow + i);

            String mobile1 = getCellValue(row.getCell(3));
            String mobile2 = getCellValue(row.getCell(4));
            String address1 = getCellValue(row.getCell(5));
            String address2 = getCellValue(row.getCell(6));

            // DELETE OLD (for update case)
            customerMobileRepository.deleteByCustomerId(customerId);
            customerAddressRepository.deleteByCustomerId(customerId);

            // SAVE MOBILES
            if (!mobile1.isEmpty()) {
                CustomerMobile m1 = new CustomerMobile();
                m1.setCustomerId(customerId);
                m1.setMobile(mobile1);
                customerMobileRepository.save(m1);
            }

            if (!mobile2.isEmpty()) {
                CustomerMobile m2 = new CustomerMobile();
                m2.setCustomerId(customerId);
                m2.setMobile(mobile2);
                customerMobileRepository.save(m2);
            }

            // SAVE ADDRESSES
            if (!address1.isEmpty()) {
                CustomerAddress a1 = new CustomerAddress();
                a1.setCustomerId(customerId);
                a1.setAddressLine(address1);
                customerAddressRepository.save(a1);
            }

            if (!address2.isEmpty()) {
                CustomerAddress a2 = new CustomerAddress();
                a2.setCustomerId(customerId);
                a2.setAddressLine(address2);
                customerAddressRepository.save(a2);
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null)
            return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    // MAPPER =
    private CustomerResponseDto mapToResponse(Customer customer) {

        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setDob(customer.getDob());
        dto.setNic(customer.getNic());

        Long customerId = customer.getId();

        // Load mobiles
        List<MobileDto> mobileDtos = customerMobileRepository
                .findByCustomerId(customerId)
                .stream()
                .map(m -> {
                    MobileDto md = new MobileDto();
                    md.setId(m.getId());
                    md.setMobile(m.getMobile());
                    return md;
                })
                .collect(Collectors.toList());

        dto.setMobiles(mobileDtos);

        // Load addresses
        List<AddressDto> addressDtos = customerAddressRepository
                .findByCustomerId(customerId)
                .stream()
                .map(a -> {
                    AddressDto ad = new AddressDto();
                    ad.setId(a.getId());
                    ad.setAddressLine(a.getAddressLine());
                    return ad;
                })
                .collect(Collectors.toList());

        dto.setAddresses(addressDtos);

        // Load family members
        List<FamilyMemberDto> familyDtos = familyMemberRepository
                .findByCustomerId(customerId)
                .stream()
                .map(f -> {
                    FamilyMemberDto fd = new FamilyMemberDto();
                    fd.setId(f.getId());
                    fd.setMemberName(f.getMemberName());
                    fd.setRelation(f.getRelation());
                    return fd;
                })
                .collect(Collectors.toList());

        dto.setFamilyMembers(familyDtos);

        return dto;
    }

}
