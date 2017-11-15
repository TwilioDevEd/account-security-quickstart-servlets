package com.twilio.accountsecurity.servlets

import com.fasterxml.jackson.databind.ObjectMapper
import com.twilio.accountsecurity.exceptions.UserExistsException
import com.twilio.accountsecurity.services.RegisterService
import com.twilio.accountsecurity.servlets.requests.UserRegisterRequest
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.charset.StandardCharsets

class RegisterServletSpec extends Specification {

    RegisterService registerService = Mock()
    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    PrintWriter responseWritter = Mock()

    @Subject def registerServlet = new RegisterServlet(registerService, new ObjectMapper())

    def jsonRequest = '{' +
            '"username": "name",' +
            '"password": "pass",' +
            '"email": "email",' +
            '"countryCode": "1",' +
            '"phoneNumber": "123"' +
            '}'
    def registerRequest = new UserRegisterRequest("name", "email", "pass", "1", "123")

    def setup() {

    }

    def "register - returns 200"() {
        when:
        registerServlet.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonRequest))
        1 * registerService.register(registerRequest)
        1 * response.setStatus(200)
    }

    def "register - returns 412"() {
        when:
        registerServlet.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonRequest))
        1 * registerService.register(registerRequest) >> { throw new UserExistsException()}
        1 * response.setStatus(412)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print("User already exists")
    }
}
