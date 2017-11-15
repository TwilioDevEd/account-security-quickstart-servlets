package com.twilio.accountsecurity.services

import com.authy.AuthyApiClient
import com.authy.api.User
import com.authy.api.Users
import com.twilio.accountsecurity.exceptions.UserExistsException
import com.twilio.accountsecurity.exceptions.UserRegistrationException
import com.twilio.accountsecurity.models.UserModel
import com.twilio.accountsecurity.models.UserRoles
import com.twilio.accountsecurity.repository.UserRepository
import com.twilio.accountsecurity.servlets.requests.UserRegisterRequest
import spock.lang.Specification
import spock.lang.Subject

class RegisterServiceSpec extends Specification {

    @Subject RegisterService service

    PasswordEncoder passwordEncoder = Mock()
    UserRepository userRepository = Mock()
    AuthyApiClient authyApiClient = Mock()
    Users users = Mock()

    def username = 'username'
    def authyId = 123
    def password = 'password'
    def request = new UserRegisterRequest(username, 'email', password, '1', '1234567')

    def setup() {
        service = new RegisterService(userRepository, passwordEncoder, authyApiClient)
    }

    def 'register should fail when the username already exist'() {
        given:
        1 * userRepository.findByUsername(username) >> new UserModel()

        when:
        service.register(request)

        then:
        thrown UserExistsException
    }

    def 'register should fail for Authy create failure'() {
        given:
        def authyUser = new User()
        def error = new com.authy.api.Error()
        error.message = 'authy error'
        authyUser.setError(error)

        def expectedUser = request.toModel()
        expectedUser.setAuthyId(authyId)
        expectedUser.setRole(UserRoles.ROLE_USER)
        1 * userRepository.findByUsername(username) >> null
        1 * authyApiClient.getUsers() >> users
        1 * users.createUser(request.email, request.phoneNumber, request.countryCode) >>
                authyUser
        0 * userRepository.create(_)

        when:
        service.register(request)

        then:
        UserRegistrationException e = thrown()
        e.getMessage() == 'authy error'
    }

    def 'register should save new user'() {
        given:
        def authyUser = new User()
        authyUser.setId(authyId)
        def expectedUser = request.toModel()
        expectedUser.setAuthyId(authyId)
        expectedUser.setRole(UserRoles.ROLE_USER)
        def salt = [0] as byte[]
        1 * passwordEncoder.getSalt() >> salt
        1 * passwordEncoder.encode(password, salt)
        1 * userRepository.findByUsername(username) >> null
        1 * authyApiClient.getUsers() >> users
        1 * users.createUser(request.email, request.phoneNumber, request.countryCode) >>
                authyUser
        1 * userRepository.create(expectedUser)

        when:
        service.register(request)

        then:
        notThrown UserExistsException
    }

}
