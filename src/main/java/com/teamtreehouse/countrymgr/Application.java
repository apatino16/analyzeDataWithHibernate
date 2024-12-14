package com.teamtreehouse.countrymgr;

import com.teamtreehouse.countrymgr.model.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Application {
    // Hold a reusable reference to a SessionFactory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    // TODO: This data should come from the database? Not manually
    public static void main(String[] args){

        // Fetch all countries
        List<Country> countries = fetchAllCountries();

        // Display all countries
        displayCountries(countries);

        // Display basic statistics
        displayStatistics(countries);

        /*
        Country country = new Country.CountryBuilder("USA", "United States")
                .withInternetUsers(getInternetUsers)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();
        save(country);

        // Display a list of countries
        fetchAllCountries().forEach(System.out::println); */
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

    private static void displayCountries(List<Country> countries){
        System.out.printf("%-3s %-32s %-11s %-11s%n", "Code", "Name", "Internet Users (%)", "Adult Literacy Rate (%)");
        System.out.println("----------------------------------------------------------------------------------------");

        for (Country country : countries){
            System.out.printf("%-3s %-32s %-11s %-11s%n",
                    country.getCode(),
                    country.getName(),
                    formatNumber(country.getInternetUsers()),
                    formatNumber(country.getAdultLiteracyRate()));
        }
    }

    private static String formatNumber (Float number) {
        return number == null ? "--" : String.format("%.2f", number);
    }

    private static void displayStatistics(List<Country> countries) {
        System.out.println("\nAnalysis: Basic Statistics");
        System.out.println("----------------------------------------------------------------------------------------");

        // Internet User
        Optional<Country> maxInternetUsers = countries.stream()
                .filter(c -> c.getInternetUsers() != null)
                .max(Comparator.comparing(Country::getInternetUsers));

        Optional<Country> minInternetUsers = countries.stream()
                .filter(c -> c.getInternetUsers() != null)
                .min(Comparator.comparing(Country::getInternetUsers));

        // Adult Literacy Rate
        Optional<Country> maxLiteracyRate = countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null)
                .max(Comparator.comparing(Country::getAdultLiteracyRate));

        Optional<Country> minLiteracyRate = countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null)
                .min(Comparator.comparing(Country::getAdultLiteracyRate));

        // Display Statistics
        System.out.printf("%-35s %-10s %-10s%n", "Indicator", "Max", "Min");
        System.out.println("----------------------------------------------------------------------------------------");

        System.out.printf("%-35s %-10s %-10s%n",
                "Internet Users (%)",
                maxInternetUsers.map(c-> formatNumber(c.getInternetUsers())).orElse("--"),
                minInternetUsers.map(c-> formatNumber(c.getInternetUsers())).orElse("--"));

        System.out.printf("%-35s %-10s %-10s%n",
                "Adult Literacy Rate (%)",
                maxLiteracyRate.map(c-> formatNumber(c.getAdultLiteracyRate())).orElse("--"),
                minInternetUsers.map(c-> formatNumber(c.getAdultLiteracyRate())).orElse("--"));
        
    }
}
