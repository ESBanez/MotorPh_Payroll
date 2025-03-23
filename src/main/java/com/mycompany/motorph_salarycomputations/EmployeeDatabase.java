package com.mycompany.motorph_salarycomputations;

import java.sql.*;
import java.util.Scanner;

public class EmployeeDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/mymotorph_employee_database"; 
    private static final String USER = "Ederick";  
    private static final String PASSWORD = "09214991122+Kerd";  // REMOVE hardcoded password in production!

    // Fetch Employee Details by ID
    public static void getEmployeeById(int employeeId) {
        String query = "SELECT LastName,"
                + " FirstName,"
                + " Address,"
                + " Birthday,"
                + " HourlyRate,"
                + " SSSID,"
                + " PhilHealthID,"
                + " TINID,"
                + " PagibigID,"
                + " BasicSalary FROM motorph_employee_data WHERE EmployeeID = ?";
        
       

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nEmployee Details:");
                System.out.println("Last Name: " + rs.getString("LastName"));
                System.out.println("First Name: " + rs.getString("FirstName"));
                System.out.println("Address: " + rs.getString("Address"));
                System.out.println("Birthday: " + rs.getString("Birthday"));
                System.out.println("Basic Salary: " + rs.getDouble("BasicSalary"));
                System.out.println("Hourly Rate: " + rs.getDouble("HourlyRate"));
                System.out.println("SSS Number: " + rs.getString("SSSID"));  // FIXED: Changed to getString()
                System.out.println("PhilHealth Number: " + rs.getString("PhilHealthID")); // FIXED
                System.out.println("TIN Number: " + rs.getString("TINID")); // FIXED
                System.out.println("Pagibig Number: " + rs.getString("PagibigID")); // FIXED
                
            } else {
                System.out.println("There is no employee with ID " + employeeId + ": not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        }
    }
    
    public static void getAttendanceById(int employeeId) {
    String query = "SELECT WorkDate, LogIn, LogOut FROM motorph_attendancerecord WHERE EmployeeID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nAttendance Records:");
            boolean hasRecords = false;

            
            
            
            while (rs.next()) {
                hasRecords = true;
                System.out.println("Work Date: " + rs.getDate("WorkDate"));
                System.out.println("Log In: " + rs.getTime("LogIn"));
                System.out.println("Log Out: " + rs.getTime("LogOut"));
                System.out.println("-------------------------");
            }

            if (!hasRecords) {
                System.out.println("No attendance records found for Employee ID " + employeeId);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving attendance data: " + e.getMessage());
        }
    }

    // Main Method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Employee ID to search: ");
        int employeeId = scanner.nextInt();

        // Fetch Employee Details (if you want to display these)
        getEmployeeById(employeeId);

        // Fetch Attendance Records
        getAttendanceById(employeeId);
        
        scanner.close();
    }
}
