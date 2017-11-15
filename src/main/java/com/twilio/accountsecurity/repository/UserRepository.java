package com.twilio.accountsecurity.repository;

import com.twilio.accountsecurity.models.UserModel;

import javax.persistence.NoResultException;

public class UserRepository extends Repository<UserModel> {
    public UserRepository() {
        super(UserModel.class);
    }

    public UserModel findByUsername(String username) {
        UserModel user = null;

        try {
            user = (UserModel) em.createQuery(
                    String.format("SELECT u FROM %s u WHERE u.username = :username", entityName))
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
        }

        return user;
    }
}
