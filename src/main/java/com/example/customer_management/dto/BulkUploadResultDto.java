package com.example.customer_management.dto;

public class BulkUploadResultDto {
    
    private int totalRecords;
    private int successCount;
    private int failedCount;
    private String message;
    
    public int getTotalRecords() {
        return totalRecords;
    }
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
    public int getSuccessCount() {
        return successCount;
    }
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    public int getFailedCount() {
        return failedCount;
    }
    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    
}
