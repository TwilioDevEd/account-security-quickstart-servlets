package com.twilio.accountsecurity.servlets.responses;

public class OneTouchStatusResponse {

    private String approvalRequestStatus;

    public OneTouchStatusResponse(String approvalRequestStatus) {
        this.approvalRequestStatus = approvalRequestStatus;
    }

    public OneTouchStatusResponse() {
    }

    public String getApprovalRequestStatus() {
        return approvalRequestStatus;
    }

    public void setApprovalRequestStatus(String approvalRequestStatus) {
        this.approvalRequestStatus = approvalRequestStatus;
    }
}
