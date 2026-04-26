package com.example.customer_management.dto;

import java.util.List;

public class CustomerResponseDto {
    
    private Long id;
    private String name;
    private String email;
    private String dob;
    private String nic;

    private List<MobileDto> mobiles;
    private List<AddressDto> addresses; 
    private List<FamilyMemberDto> familyMembers;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public List<FamilyMemberDto> getFamilyMembers() {
        return familyMembers;
    }
    public void setFamilyMembers(List<FamilyMemberDto> familyMembers) {
        this.familyMembers = familyMembers;
    }
}
