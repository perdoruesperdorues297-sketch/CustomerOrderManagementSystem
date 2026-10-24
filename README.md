
# Customer & Order Management System

A Java console-based application for managing customers, products, orders, inventory, and business reports.

## Features

### Customer Management
- Register new customers
- View all customers
- Search customers by ID
- Edit customer information
- Delete customers when they have no associated orders

### Product Management
- Register new products
- View all products
- Search products by ID
- Edit product information
- Delete products when permitted
- Validate prices and stock quantities

### Order Management
- Create orders for registered customers
- Validate product availability and order quantities
- Track order statuses
- View order history
- Calculate order totals using the recorded unit price

### Reports & Analytics
- Sales summary
- Order statistics by status
- Top-selling products
- Customer activity
- Inventory and low-stock reports
- Sales reports by date range

### Data Storage
- Save customers, products, and orders to local files
- Load saved data when the application starts
- Preserve data between program sessions

## Technologies

- Java
- Object-Oriented Programming (OOP)
- File Handling
- Collections
- Date Handling
- Exception Handling

## Project Structure

```text
CustomerOrderManagementSystem/
├── Main.java
├── Customer.java
├── Product.java
├── Order.java
├── DataStorage.java
├── data/
│   ├── customers.txt
│   ├── products.txt
│   └── orders.txt
├── README.md
└── .gitignore
```

## Requirements

- Java Development Kit (JDK)
- A terminal or Java-compatible IDE

## How to Run

Open a terminal in the project folder.

Compile the application:

```bash
javac Main.java Customer.java Product.java Order.java DataStorage.java
```

Run the application:

```bash
java Main
```

## Purpose

This project demonstrates beginner-level Java development through a practical business management application. It combines object-oriented programming, data validation, file persistence, inventory management, order processing, and business analytics.

## Project Status

Core functionality and business reports have been implemented and tested. Further improvements may be added as the project is prepared for portfolio presentation.
