package com.twilio.accountsecurity.repository;


import com.twilio.accountsecurity.config.Settings;
import io.github.cdimascio.dotenv.Dotenv;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class Repository<T> {

    protected final EntityManager em;
    protected final String entityName;

    public Repository(Class<T> entity) {

        Map<String, String> properties = getProperties();
        em = Persistence
                .createEntityManagerFactory("account-security-quickstart", properties)
                .createEntityManager();

        this.entityName = entity.getSimpleName();
    }

    public List<T> findAll() {
        Query query = em.createQuery(
                String.format("SELECT e FROM %s e", entityName));

        return query.getResultList();
    }

    public T find(int id) {
        T entity = (T) em.find(Object.class, id);
        em.refresh(entity);

        return entity;
    }

    public T create(T entity) {
        getTransaction().begin();
        em.persist(entity);
        getTransaction().commit();

        return entity;
    }

    public T update(T entity) {
        getTransaction().begin();
        T updatedEntity = em.merge(entity);
        getTransaction().commit();

        return updatedEntity;
    }

    public void delete(T entity) {
        getTransaction().begin();
        em.remove(entity);
        getTransaction().commit();
    }

    private EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    private Map<String, String> getProperties() {
        Dotenv dotenv = Settings.getDotenv();
        Map<String, String> config = new HashMap<>();

        if (dotenv.get("JDBC_URL") != null) {
            config.put("javax.persistence.jdbc.url", dotenv.get("JDBC_URL"));
        }

        if (dotenv.get("DB_USER") != null) {
            config.put("javax.persistence.jdbc.user", dotenv.get("DB_USER"));
        }

        if (dotenv.get("DB_PASSWORD") != null) {
            config.put("javax.persistence.jdbc.password", dotenv.get("DB_PASSWORD"));
        }

        return config;
    }
}
