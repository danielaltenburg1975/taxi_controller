# Taxi-Controller

## Taxi Controller:A Comprehensive Solution for Small Taxi Companies and Ride-Hailing Providers

The Taxi Controller is a specially designed, web-based full-stack application aimed at helping small taxi
companies and ride-hailing providers in Germany manage their operations more efficiently. This application allows for
easy acceptance of appointments from both customers and drivers, captures customer, vehicle, and driver data, and additionally
provides extensive time tracking for drivers.

## Key Features:
- Appointment Acceptance: Customers and drivers can easily accept new appointments, optimizing
  scheduling and ensuring smooth operations.

- Data Management: Capture and store customer information as well as details about vehicles and drivers.
  The Taxi Controller offers a user-friendly platform for efficient management of all relevant data.

- Comprehensive Time Tracking: The application offers detailed time tracking for drivers,
  enabling precise monitoring of working hours and accurate billing.

## Location-Independent Operation:
The Taxi Controller is accessible location-independently via a web server. This means you can access the application 
from anywhere as long as you have an internet connection. This ensures maximum flexibility and significantly simplifies 
the management of your taxi company.

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/zeitmanager-1.0-SNAPSHOT.jar`

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorial at [vaadin.com/docs/latest/tutorial/overview](https://vaadin.com/docs/latest/tutorial/overview).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/docs/latest/components](https://vaadin.com/docs/latest/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Build any UI without custom CSS by discovering Vaadin's set of [CSS utility classes](https://vaadin.com/docs/styling/lumo/utility-classes). 
- Find a collection of solutions to common use cases at [cookbook.vaadin.com](https://cookbook.vaadin.com/).
- Find add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin).
