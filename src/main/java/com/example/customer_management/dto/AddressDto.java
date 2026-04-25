package com.example.customer_management.dto;

public class AddressDto {
    
    private Long id;
    private String addressLine;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAddressLine() {
        return addressLine;
    }
    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    
}
