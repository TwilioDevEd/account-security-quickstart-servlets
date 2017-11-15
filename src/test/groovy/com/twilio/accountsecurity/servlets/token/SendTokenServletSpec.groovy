package com.twilio.accountsecurity.servlets.token

import com.twilio.accountsecurity.exceptions.TokenVerificationException
import com.twilio.accountsecurity.services.TokenService
import com.twilio.accountsecurity.servlets.SessionManager
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class SendTokenServletSpec extends Specification {

    TokenService tokenService = Mock()
    SessionManager sessionManager = Mock()

    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    HttpSession session = Mock()
    PrintWriter responseWritter = Mock()

    @Subject def subject = new SendTokenServlet(tokenService, sessionManager)

    def "doPost - return 500 for user not logged in"() {
        when:
        subject.doPost(request, response)

        then:
        1 * sessionManager.getLoggedUsername(request) >> Optional.empty()
        1 * response.setStatus(500)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print("You are not logged in")
    }

    def "doPost - sms - return 500 for TokenVerificationException"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getServletPath() >> '/api/token/sms'
        1 * sessionManager.getLoggedUsername(request) >> Optional.of('username')
        1 * tokenService.sendSmsToken('username') >> { throw new TokenVerificationException('message')}
        1 * response.setStatus(500)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print('message')
    }

    def "doPost - sms - return 200"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getServletPath() >> '/api/token/sms'
        1 * sessionManager.getLoggedUsername(request) >> Optional.of('username')
        1 * tokenService.sendSmsToken('username')
        1 * response.setStatus(200)
    }

    def "doPost - voice - return 200"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getServletPath() >> '/api/token/voice'
        1 * sessionManager.getLoggedUsername(request) >> Optional.of('username')
        1 * tokenService.sendVoiceToken('username')
        1 * response.setStatus(200)
    }

    def "doPost - onetouch - return 200"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getSession() >> session
        1 * session.setAttribute('onetouchUUID', 'uuid')
        1 * request.getServletPath() >> '/api/token/onetouch'
        1 * sessionManager.getLoggedUsername(request) >> Optional.of('username')
        1 * tokenService.sendOneTouchToken('username') >> 'uuid'
        1 * response.setStatus(200)
    }
}
