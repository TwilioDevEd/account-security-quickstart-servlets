package com.twilio.accountsecurity.servlets.phoneverification

import com.twilio.accountsecurity.exceptions.PhoneVerificationException
import com.twilio.accountsecurity.services.PhoneVerificationService
import com.twilio.accountsecurity.servlets.SessionManager
import com.twilio.accountsecurity.servlets.requests.CheckPhoneVerificationRequest
import com.twilio.accountsecurity.servlets.requests.RequestParser
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class CheckPhoneVerificationServletSpec extends Specification {

    PhoneVerificationService phoneVerificationService = Mock()
    SessionManager sessionManager = Mock()

    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    HttpSession session = Mock()
    PrintWriter responseWritter = Mock()

    @Subject def subject = new CheckPhoneVerificationServlet(phoneVerificationService, new RequestParser())

    def jsonRequest = '{"countryCode":"1","phoneNumber":"123","token":"token"}'
    def requestObject = new CheckPhoneVerificationRequest("123", "1", "token")


    def "doPost - returns 200"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonRequest))
        1 * phoneVerificationService.check(requestObject)
        1 * response.setStatus(200)
    }

    def "doPost - returns 500 for PhoneVerificationException"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonRequest))
        1 * phoneVerificationService.check(requestObject) >> {throw new PhoneVerificationException("message")}
        1 * response.setStatus(500)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print('message')
    }
}
