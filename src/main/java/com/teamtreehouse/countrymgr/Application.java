package com.teamtreehouse.countrymgr;

import com.teamtreehouse.countrymgr.model.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    // Hold a reusable reference to a SessionFactory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    public static void main(String[] args){
        Country country = new Country.CountryBuilder("")
                .withInternetUsers(getInternetUsers)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();
        save(country);

        // Display a list of countries
        fetchAllCountries().forEach(System.out::println);
    }

    // Fetch all countries from the database
    private static List<Country> fetchAllCountries(){
        // Open a session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<Country> criteria = builder.createQuery(Country.class);

        // Specify criteria root
        criteria.from(Country.class);

        // Execute query
        List<Country> countries = session.createQuery(criteria).getResultList();

        // Close the session
        session.close();

        return countries;
    }

    private static void save(Country country){
        // Open session
        Session session = sessionFactory.openSession();

        // Begin Transaction
        session.beginTransaction();

        // Use the session to save the Country
        session.save(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }
}
