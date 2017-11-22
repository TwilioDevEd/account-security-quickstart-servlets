package com.twilio.accountsecurity.servlets.requests;

import javax.validation.constraints.NotNull;

public class StartPhoneVerificationRequest {

    @NotNull
    private String phoneNumber;
    @NotNull
    private String countryCode;
    @NotNull
    private String via;

    public StartPhoneVerificationRequest() {
    }

    public StartPhoneVerificationRequest(String phoneNumber, String countryCode, String via) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.via = via;
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

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StartPhoneVerificationRequest that = (StartPhoneVerificationRequest) o;

        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
            return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null)
            return false;
        return via != null ? via.equals(that.via) : that.via == null;
    }

    @Override
    public int hashCode() {
        int result = phoneNumber != null ? phoneNumber.hashCode() : 0;
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (via != null ? via.hashCode() : 0);
        return result;
    }
}
