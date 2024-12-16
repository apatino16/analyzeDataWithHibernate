# Analyze Data with Hibernate

A command-line Java application that allows users to interact with a public dataset from the **World Bank**, stored in an **H2 database**. The application enables users to view, analyze, edit, add, and delete country records while calculating key statistics, sppecifically the relationship between internet usage and adult literacy. 

This project was created as part of Unit 5 of my Java Web Development Techdegree at Treehouse.

---

## **Table of Contents**
- [Project Overview](#project-overview)
- [Features](#features)
- [Requirements](#requirements)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Code Structure](#code-structure)
- [Technologies Used](#technologies-used)

---

## **Project Overview**
This project uses a combination of two World Bank indicators from 2013:
- **Internet Users (%)**: Number of internet users per 100 people.
- **Adult Literacy Rate (%)**: Number of literate adults per 100 people.

The goal is to provide a tool to **explore** and **analyze** this data.

---

## **Features**
1. **View All Countries**:
   - Displays all country data in a clean tabular format.
   - Columns include country code, name, internet users, and adult literacy rate.
   - Rounds numeric values to two decimal places and clearly marks missing values as `--`.

2. **Display Basic Statistics**:
   - Calculate and display the **maximum** and **minimum** values for:
     - Internet Users (%)
     - Adult Literacy Rate (%)

3. **Display Averages**:
   - Calculate and display the **average** (mean) value for each indicator using **Java Streams**.

4. **Edit Existing Country Data**:
   - Modify values for a specific country based on its code.

5. **Add a New Country**:
   - Add a new country record using the **Builder Pattern** for object creation.

6. **Delete a Country**:
   - Remove a country record by specifying its code.

7. **Menu System**:
   - A command-line interface allows users to navigate between features.
   - Runs continuously until the user selects the "Quit" option.

---

## **Requirements**
Before running this application, ensure the following are installed:
1. **Java Development Kit (JDK)**: Version 11 or later.
2. **Gradle**: For building and running the project.
3. **IntelliJ IDEA** (or any other Java IDE, optional for development).

---

## **Setup Instructions**
Follow these steps to set up and run the application:

### **1. Project Structure**
Ensure your project has the following structure:

```
project-folder/
├── build.gradle
├── settings.gradle
├── data/
│   └── worldbank.mv.db         # H2 database file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/teamtreehouse/countrymgr/
│   │   │       ├── Application.java
│   │   │       └── model/Country.java
│   │   └── resources/
│   │       └── hibernate.cfg.xml
└── README.md
```

---

### **2. Configure Gradle**
Ensure your `build.gradle` file includes the following dependencies:
```
plugins {
    id 'java'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.hibernate.orm:hibernate-core:6.5.2.Final'
    implementation 'com.h2database:h2:2.2.224'
    implementation 'jakarta.persistence:jakarta.persistence-api:3.1.0'
}
```

---

### **3. Verify Hibernate Configuration**
Check the `hibernate.cfg.xml` file located in `src/main/resources`:
```xml
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">org.h2.Driver</property>
        <property name="hibernate.connection.url">jdbc:h2:file:./data/worldbank</property>
        <property name="hibernate.connection.username">sa</property>
        <property name="hibernate.dialect">org.hibernate.dialect.H2Dialect</property>
        <property name="hibernate.hbm2ddl.auto">none</property>
        <mapping class="com.teamtreehouse.countrymgr.model.Country"/>
    </session-factory>
</hibernate-configuration>
```

---

### **4. Build the Project**
Navigate to your project directory and build the application using Gradle:
```
gradle build
```

---

## **Running the Application**
To run the application:
```
gradle run
```

Alternatively, run it directly from your IDE by executing the `Application.java` file.

---

## **Usage Guide**
When you run the program, a menu will appear:

```
1. View all countries
2. Display analysis
3. Edit a country
4. Add a new country
5. Delete a country
6. Display averages
7. Quit
Choose an option: 
```

- **Option 1**: Displays all country data in a formatted table.
- **Option 2**: Displays the maximum and minimum values for each indicator.
- **Option 3**: Edits an existing country's data based on its code.
- **Option 4**: Adds a new country to the database.
- **Option 5**: Deletes a country by its code.
- **Option 6**: Displays the average (mean) for each indicator.
- **Option 7**: Exits the program.

---

## **Code Structure**

- **`Application.java`**:
  - Main menu logic.
  - Implements features such as viewing, adding, editing, and deleting countries.

- **`Country.java`**:
  - The entity class representing the `COUNTRY` table.
  - Includes fields: `code`, `name`, `internetUsers`, and `adultLiteracyRate`.
  - Implements the **Builder Pattern**.

- **`hibernate.cfg.xml`**:
  - Configures Hibernate and connects to the H2 database.

---

## **Technologies Used**
- **Java 11+**
- **Hibernate ORM**: Version 6.5.2.Final
- **H2 Database**: Version 2.2.224
- **Gradle**: Build and dependency management
- **Java Streams**: For calculating statistics (max, min, averages)

---
