package com.twilio.accountsecurity.servlets.token

import com.twilio.accountsecurity.exceptions.AuthenticationException
import com.twilio.accountsecurity.exceptions.TokenVerificationException
import com.twilio.accountsecurity.services.TokenService
import com.twilio.accountsecurity.servlets.SessionManager
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class OneTouchStatusServletTest extends Specification {

    TokenService tokenService = Mock()
    SessionManager sessionManager = Mock()

    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    HttpSession session = Mock()
    PrintWriter responseWritter = Mock()

    @Subject def subject = new OneTouchStatusServlet(tokenService, sessionManager)

    def "doPost - returns 200"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getSession() >> session
        1 * session.getAttribute("onetouchUUID") >> "uuid"
        1 * tokenService.retrieveOneTouchStatus("uuid") >> true
        1 * sessionManager.logInSecondStep(request)
        1 * response.setStatus(200)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print('true')
    }

    def "doPost - returns 500 for TokenVerificationException"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getSession() >> session
        1 * session.getAttribute("onetouchUUID") >> "uuid"
        1 * tokenService.retrieveOneTouchStatus("uuid") >> {throw new TokenVerificationException("message")}
        1 * response.setStatus(500)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print("message")
    }

    def "doPost - returns 401 for AuthenticationException"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getSession() >> session
        1 * session.getAttribute("onetouchUUID") >> "uuid"
        1 * tokenService.retrieveOneTouchStatus("uuid") >> true
        1 * sessionManager.logInSecondStep(request) >> {throw new AuthenticationException("message")}
        1 * response.setStatus(401)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print('message')
    }
}
