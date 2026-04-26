package com.example.customer_management.dto;

import java.util.List;
import javax.validation.constraints.*;

public class CustomerRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Date of Birth is required")
    private String dob;

    @NotBlank(message = "NIC is required")
    private String nic;

    @Email(message = "Email is invalid")
    private String email;

    private List<MobileDto> mobiles;
    private List<AddressDto> addresses;
    private List<Long> familyMemberIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public List<MobileDto> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<MobileDto> mobiles) {
        this.mobiles = mobiles;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }

    public List<Long> getFamilyMemberIds() {
        return familyMemberIds;
    }

    public void setFamilyMemberIds(List<Long> familyMemberIds) {
        this.familyMemberIds = familyMemberIds;
    }

    
}
