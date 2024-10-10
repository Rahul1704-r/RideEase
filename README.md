# RideEase 🚗📲

RideEase is the backend service for a ride-booking application that connects riders with drivers in real time. This backend-driven app allows users to request rides, manage payments, track ride progress, and more. Manages the whole lifecycle from when the rider requests a ride until the driver ends the ride and the payment process is completed. The project is built with Spring Boot and integrates with OSRM for map and routing services.

# Features
  •	User Authentication: Secure user registration, login, and authorization.  
	•	Ride Booking: Request and manage ride bookings.  
	•	Real-Time Ride Tracking: Monitor ride status in real time.  
	•	Payment Management: Integrated wallet system to handle ride payments.  
	•	Ratings: Rate drivers and riders after each ride.  
	•	Distance Calculation: Calculate the best route using the OSRM API.  

# Technologies Used  
  •	Backend: Spring Boot  
	•	Database: PostgreSQL  
	•	API: OSRM API for routing and distance calculation  
	•	Other: DTOs, ResponseEntity handling, Validation Constraints  



# Getting Started

## Prerequisites

### Before you can run this project locally, ensure you have the following installed:    
  •	Java 17 or above  
	•	PostgreSQL  
	•	Maven  
	•	Git  

# Installation
1.	Clone the repository:

  	    https://github.com/Rahul1704-r/RideEase.git

3.	Set up the PostgreSQL database:  
	•	Create a PostgreSQL database called rideease_db.  
	•	Update the application.properties with your PostgreSQL credentials:  

          spring.datasource.url=jdbc:postgresql://localhost:5432/rideease_db
          spring.datasource.username=your_username
          spring.datasource.password=your_password

4.	Install dependencies and build the project:
   
            mvn clean install

5.	Run the Spring Boot application:
   
       	   mvn spring-boot:run

   

