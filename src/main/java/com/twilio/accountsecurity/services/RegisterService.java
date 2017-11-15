package com.twilio.accountsecurity.services;

import com.authy.AuthyApiClient;
import com.authy.api.User;
import com.twilio.accountsecurity.exceptions.UserExistsException;
import com.twilio.accountsecurity.exceptions.UserRegistrationException;
import com.twilio.accountsecurity.models.UserModel;
import com.twilio.accountsecurity.models.UserRoles;
import com.twilio.accountsecurity.servlets.requests.UserRegisterRequest;
import com.twilio.accountsecurity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.twilio.accountsecurity.services.Settings.authyId;

public class RegisterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private AuthyApiClient authyClient;

    public RegisterService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthyApiClient authyClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authyClient = authyClient;
    }

    public RegisterService() {
        this.userRepository = new UserRepository();
        this.passwordEncoder = new PasswordEncoder();
        this.authyClient = new AuthyApiClient(authyId());
    }

    public void register(UserRegisterRequest request) {
        UserModel userModel = userRepository.findByUsername(request.getUsername());

        if(userModel != null) {
            LOGGER.warn(String.format("User already exist: {}", request.getUsername()));
            throw new UserExistsException();
        }

        User authyUser = authyClient.getUsers().createUser(request.getEmail(),
                request.getPhoneNumber(),
                request.getCountryCode());

        if(authyUser.getError() != null) {
            throw new UserRegistrationException(authyUser.getError().getMessage());
        }

        byte[] salt = passwordEncoder.getSalt();
        UserModel newUserModel = request.toModel(passwordEncoder.encode(request.getPassword(), salt));
        newUserModel.setRole(UserRoles.ROLE_USER);
        newUserModel.setAuthyId(authyUser.getId());
        newUserModel.setSalt(new String(salt));
        userRepository.create(newUserModel);
    }
}
