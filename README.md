# Currency Converter API

This project provides a Currency Converter API that allows users to convert amounts between different currencies and fetch real-time exchange rates.

Overview
The Currency Converter API helps users:

Get real-time exchange rates for a given base currency.
Convert an amount from one currency to another.
The API interacts with an external exchange rates service to provide real-time currency conversion data. Users can input the amount and currencies (from and to) they want to convert, and the system will return the converted amount.

Getting Started
Installation
Clone the Repository:

Clone the repository from GitHub to your local machine.
Build the Project:

Use Maven to build the project:
Copy
Edit
mvn clean install
Run the Application:

Start the Spring Boot application:
arduino
Copy
Edit
mvn spring-boot:run
Access the Application:

The application will be available at:
arduino
Copy
Edit
http://localhost:8080
API Endpoints
The Currency Converter API provides the following two endpoints:

1. Get Exchange Rates
   Endpoint: /api/rates

Method: GET

Description: Fetches the current exchange rates for a given base currency. If no base currency is specified, the default will be USD.

Parameters:

base (optional): The base currency. Defaults to "USD" if not provided.
Response: The exchange rates for the requested base currency, including rates for major currencies such as EUR, GBP, and others.

2. Convert Currency
   Endpoint: /api/convert

Method: POST

Description: Converts a specified amount from one currency to another based on current exchange rates.

Request Body:

from: The currency you want to convert from.
to: The currency you want to convert to.
amount: The amount of money to convert.
Response: The converted amount in the target currency, including the original amount and the currencies involved.

Swagger UI
Swagger UI provides a web interface to interact with the API. Once the application is running, you can view and test the API endpoints using the Swagger interface.

URL: http://localhost:8080/swagger-ui/
Features:
View API documentation.
Test the endpoints directly from the browser.
Testing
Unit Testing
JUnit is used for testing the application. Tests are implemented for both the service and controller layers.

Key Test Cases
Convert Amount Success: Verifies that the conversion of currency works correctly with valid input.
Invalid From Currency: Verifies that an error is thrown when an invalid source currency is provided.
Invalid To Currency: Verifies that an error is thrown when an invalid target currency is provided.
Project Structure
The project follows a typical Spring Boot structure:

com.currencyexchange.currency_converter: The main package containing all the classes.
config: Contains the configuration for the application, including Swagger configuration.
controller: Contains the REST controllers that handle incoming requests.
exception: Contains custom exceptions used by the application.
model: Contains the data models used by the API (e.g., request and response models).
service: Contains the business logic of the application.
resources:
application.properties: Contains configuration properties for the application.
Reference Documentation
For further reference, please consider the following sections:

Official Apache Maven documentation
Spring Boot Maven Plugin Reference Guide
Create an OCI image
Spring Web
Conclusion
This Currency Converter API provides users with an easy and efficient way to convert currencies in real-time. By interacting with the API through the provided endpoints, users can get exchange rates and perform currency conversions with ease.

If you have any further questions or issues, feel free to reach out or contribute to the project!
