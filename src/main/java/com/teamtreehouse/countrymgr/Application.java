package com.teamtreehouse.countrymgr;

import com.teamtreehouse.countrymgr.model.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Application {
    // Hold a reusable reference to a SessionFactory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while(running) {

            // Main menu
            System.out.println("1. View all countries");
            System.out.println("2. Display analysis");
            System.out.println("3. Edit a country");
            System.out.println("4. Add a new country");
            System.out.println("5. Delete a country");
            System.out.println("6. Display Averages");
            System.out.println("7. Quit");
            System.out.print("Choose an option: ");

            try {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> displayCountries(fetchAllCountries());
                    case 2 -> displayStatistics(fetchAllCountries());
                    case 3 -> {
                        System.out.println("Enter the code of the country to edit:");
                        String code = scanner.nextLine().toUpperCase();
                        Country country = findCountryByCode(code);
                        if (country != null) {
                            System.out.println("Editing Country: " + country);
                            editCountry(country, scanner);
                        } else {
                            System.out.println("Country not found.");
                        }
                    }
                    case 4 -> addCountry(scanner);
                    case 5 -> {
                        System.out.println("Enter the code of the country to delete: ");
                        String countryToDelete = scanner.nextLine().toUpperCase();
                        deleteCountry(countryToDelete);
                    }
                    case 6 -> displayAverages(fetchAllCountries());
                    case 7 -> {
                        System.out.println("Goodbye!");
                        running = false; // Exit the loop
                    }
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine();
            }
        }
        scanner.close();
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

    private static Country findCountryByCode(String code){
        // Open a Session
        Session session = sessionFactory.openSession();

        // Retrieve the persistent object (or null if not found)
        Country country = session.get(Country.class, code);

        // Close the session
        session.close();

        // Return the object
        return country;
    }

    private static void update(Country country){
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the contact
        session.merge(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

    }

    private static String save(Country country){
        // Open session
        Session session = sessionFactory.openSession();

        // Begin Transaction
        session.beginTransaction();

        // Use the session to save the Country
        String code = (String) session.save(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

        return code;
    }

    private static void editCountry(Country country, Scanner scanner) {
        System.out.println("Press Enter to keep the current value.");
        System.out.print("New name [" + country.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()){
            country.setName(name);
        }

        System.out.print("New internet users [" + formatNumber(country.getInternetUsers()) + "]: ");
        String internetUsersInput = scanner.nextLine();
        if (!internetUsersInput.isEmpty()){
            try{
                country.setInternetUsers(Float.parseFloat(internetUsersInput));
            } catch (NumberFormatException e){
                System.out.println("Invalid number format. Keeping current value.");
            }
        }

        System.out.print("New adult literacy rate [" + formatNumber(country.getAdultLiteracyRate()) + "]: ");
        String literacyRateInput = scanner.nextLine();
        if (!literacyRateInput.isEmpty()){
            try{
                country.setAdultLiteracyRate(Float.parseFloat(literacyRateInput));
            } catch (NumberFormatException e){
                System.out.println("Invalid number format. Keeping current value");
            }
        }

        // Save updates
        update(country);
        System.out.println("Country updated successfully: " + country);
    }

    private static void addCountry(Scanner scanner) {
        System.out.println("Enter details for the new country:");

        // Prompt for country fields
        System.out.print("Code (3 characters): ");
        String code = scanner.nextLine().toUpperCase();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Internet Users (%): ");
        String internetUsersInput = scanner.nextLine();
        Float internetUsers = null;
        if (!internetUsersInput.isEmpty()) {
            try {
                internetUsers = Float.parseFloat(internetUsersInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for Internet Users. Setting to null.");
            }
        }

        System.out.print("Adult Literacy Rate (%): ");
        String literacyRateInput = scanner.nextLine();
        Float adultLiteracyRate = null;
        if (!literacyRateInput.isEmpty()) {
            try {
                adultLiteracyRate = Float.parseFloat(literacyRateInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for Adult Literacy Rate. Setting to null.");
            }
        }

        // Use the Builder Patter to create a new Country Object
        Country newCountry = new Country.CountryBuilder(code, name)
                .withInternetUsers(internetUsers)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();
        // Save the new country to the database
        save(newCountry);

        System.out.println("New country added successfully: " + newCountry);
    }

    private static void deleteCountry(String code) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Country country = session.get(Country.class, code);
        if (country != null){
            session.remove(country);
            session.getTransaction().commit();
            System.out.println("Country deleted successfully.");
        } else {
            System.out.println("Country not found");
        }

        session.close();
    }

    // Calculate averages
    private static void displayAverages(List<Country> countries) {
        System.out.println("\nAnalysis: Averages");
        System.out.println("----------------------------------------------------------------------------------------");

        double avgInternetUsers = countries.stream()
                .filter(c -> c.getInternetUsers() != null)
                .mapToDouble(Country::getInternetUsers)
                .average()
                .orElse(0.0);

        double avgLiteracyRate = countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null)
                .mapToDouble(Country::getAdultLiteracyRate)
                .average()
                .orElse(0.0);

        System.out.printf("%-35s %-10.2f%n", "Average Internet Users (%)", avgInternetUsers);
        System.out.printf("%-35s %-10.2f%n", "Average Adult Literacy Rate (%)", avgLiteracyRate);
    }

    private static String formatNumber (Float number) {
        return number == null ? "--" : String.format("%.2f", number);
    }
}
