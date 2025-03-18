/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph_salarycomputations;

/**
 *
 * @author Creative 1
 */

import java.io.*;
import java.util.Scanner;

public class EmployeeFileHandling {
    private static final String FILE_NAME = "employees.txt";

    // Add employee
    public static void addEmployee(String id, String name, double salary) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(id + "," + name + "," + salary);
            writer.newLine();
            System.out.println("Employee record added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // View all employees
    public static void viewEmployees() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\nEmployee Records:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Employee ID: ");
        String id = scanner.nextLine();
        System.out.println("Enter Employee Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Salary: ");
        double salary = scanner.nextDouble();

        addEmployee(id, name, salary);
        viewEmployees();

        scanner.close();
    }
}