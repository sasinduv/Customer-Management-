package com.example.customer_management.impl;

import com.example.customer_management.dto.*;
import com.example.customer_management.entity.Customer;
import com.example.customer_management.entity.CustomerAddress;
import com.example.customer_management.entity.CustomerMobile;
import com.example.customer_management.entity.FamilyMember;
import com.example.customer_management.repository.*;
import com.example.customer_management.service.CustomerService;
import org.springframework.stereotype.Service;

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
    public String bulkUploadCustomers() {
        // We will implement Excel upload using Apache POI later
        return "Bulk upload feature will be implemented using Excel (Apache POI)";
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
