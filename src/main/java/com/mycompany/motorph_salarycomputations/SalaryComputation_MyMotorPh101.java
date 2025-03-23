package com.mycompany.motorph_salarycomputations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalaryComputation_MyMotorPh101 {
    
    private static final String FILE_NAME = "EmployeeDetailsUTF.csv";
    private static final String ATTENDANCE = "AttendanceRecord.csv";

    public static double computeSalaryOnSpecificDate(String empID, String specificDate) {
        double hourlyRate = 0.0;
        double totalWorkHoursOnSpecificDate = 0.0; // Total hours worked

        // Read employee file to get hourly rate
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 19 && data[0].trim().equals(empID)) {
                    try {
                        // Ensure the last column is properly cleaned
                        String hourlyRateString = data[data.length - 1].trim();
                        hourlyRateString = hourlyRateString.replaceAll("[\",]", ""); // Remove commas and quotes

                        hourlyRate = Double.parseDouble(hourlyRateString);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing hourly rate for Employee ID " + empID + ": " + data[data.length - 1]);
                        return 0.0;
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee file: " + e.getMessage());
            return 0.0;
        }

        // Read attendance records and calculate total work hours
        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE))) {
            String line;
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;

                String recordEmpID = data[0].trim();
                String date = data[3].trim();
                String logInTime = data[4].trim();
                String logOutTime = data[5].trim();

                if (!recordEmpID.equals(empID)) continue;
                if (!date.equals(specificDate)) continue; // Match specific date

                try {
                    Date logIn = timeFormat.parse(logInTime);
                    Date logOut = timeFormat.parse(logOutTime);

                    // Compute hours worked correctly
                    long diffMillis = logOut.getTime() - logIn.getTime();
                    double hoursWorked = (diffMillis / (1000.0 * 60 * 60)); // Convert milliseconds to hours
                    hoursWorked = Math.round(hoursWorked * 100.0) / 100.0; // Round to 2 decimal places

                    totalWorkHoursOnSpecificDate += hoursWorked;
                } catch (Exception e) {
                    System.out.println("Skipping invalid time entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file: " + e.getMessage());
        }

        // Compute Salary for the specific date
        double salaryForSpecificDate = totalWorkHoursOnSpecificDate * hourlyRate;

        // Debugging Output
        System.out.println("\n=== Salary Computation Debugging ===");
        System.out.println("Employee ID: " + empID);
        System.out.println("Date: " + specificDate);
        System.out.println("Total Work Hours: " + totalWorkHoursOnSpecificDate);
        System.out.println("Hourly Rate: PHP " + hourlyRate);
        System.out.println("Salary for " + specificDate + ": PHP " + salaryForSpecificDate);
        System.out.println("===================================");

        return salaryForSpecificDate;
    }

    public static void main(String[] args) {
        // Test Case for Employee 10001 on June 3, 2024
        double salary = computeSalaryOnSpecificDate("10001", "2024-06-03");
        System.out.println("Final Salary on 2024-06-03: PHP " + salary);
    }
}
