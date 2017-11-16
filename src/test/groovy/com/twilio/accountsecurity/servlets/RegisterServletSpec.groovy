package com.twilio.accountsecurity.servlets

import com.twilio.accountsecurity.exceptions.UserExistsException
import com.twilio.accountsecurity.services.RegisterService
import com.twilio.accountsecurity.servlets.requests.RequestParser
import com.twilio.accountsecurity.servlets.requests.UserRegisterRequest
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RegisterServletSpec extends Specification {

    RegisterService registerService = Mock()
    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    PrintWriter responseWritter = Mock()
    SessionManager sessionManager = Mock()

    @Subject def registerServlet = new RegisterServlet(registerService,
            sessionManager, new RequestParser())

    def jsonRequest = '{' +
            '"username": "name",' +
            '"password": "pass",' +
            '"email": "email",' +
            '"countryCode": "1",' +
            '"phoneNumber": "123"' +
            '}'
    def username = "name"
    def registerRequest = new UserRegisterRequest(username, "email", "pass", "1", "123")

    def setup() {

    }

    def "doPost - returns 200"() {
        when:
        registerServlet.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonRequest))
        1 * registerService.register(registerRequest)
        1 * response.setStatus(200)
        1 * sessionManager.logInFirstStep(request, username)
    }

    def "doPost - returns 412"() {
        when:
        registerServlet.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonRequest))
        1 * registerService.register(registerRequest) >> { throw new UserExistsException()}
        1 * response.setStatus(412)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print("User already exists")
        0 * sessionManager.logInFirstStep(request, username)
    }
}
