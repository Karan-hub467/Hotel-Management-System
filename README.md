# Hotel Management System

## Overview

Hotel Management System is a desktop-based application developed to streamline hotel operations such as room booking, customer management, check-in/check-out processes, and billing. The system helps improve efficiency and reduces manual record keeping.

## Features

* Room Reservation Management
* Customer Record Management
* Check-In Functionality
* Check-Out Functionality
* Automated Bill Generation
* Room Availability Tracking
* Database Management

## Technologies Used

* Java
* MySQL

## Installation

### Prerequisites

* Java JDK 8 or later
* MySQL Server
* IDE (NetBeans, IntelliJ IDEA, or Eclipse)

### Clone Repository

```bash
git clone <repository-url>
cd Hotel-Management-System
```

### Create Database

```sql
CREATE DATABASE hotel_management;
```

### Import Database

Import the provided SQL file into MySQL.

### Configure Database Connection

Update database credentials inside the Java project.

### Run Application

Open the project in your IDE and run:

```bash
Main.java
```

## Project Structure

```text
Hotel-Management-System/
│
├── src/
│   ├── Customer/
│   ├── Rooms/
│   ├── Billing/
│   ├── Reservation/
│
├── database/
│   └── hotel_management.sql
│
├── images/
├── README.md
```

## Usage

1. Add customer information.
2. Check room availability.
3. Reserve rooms.
4. Perform check-in/check-out operations.
5. Generate customer bills.

## Future Enhancements

* Online Booking Portal
* Payment Gateway Integration
* Reports Dashboard
* Multi-Hotel Management
* Email Confirmation System
