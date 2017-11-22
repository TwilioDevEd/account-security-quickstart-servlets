package com.twilio.accountsecurity.servlets.requests;

import javax.validation.constraints.NotNull;

public class CheckPhoneVerificationRequest {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String countryCode;
    @NotNull
    private String token;

    public CheckPhoneVerificationRequest() {
    }

    public CheckPhoneVerificationRequest(String phoneNumber, String countryCode, String token) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckPhoneVerificationRequest that = (CheckPhoneVerificationRequest) o;

        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
            return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null)
            return false;
        return token != null ? token.equals(that.token) : that.token == null;
    }

    @Override
    public int hashCode() {
        int result = phoneNumber != null ? phoneNumber.hashCode() : 0;
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }
}
