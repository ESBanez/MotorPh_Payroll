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
import java.text.SimpleDateFormat;
import java.util.*;

public class Payslip_MotorPh {
    private static final String ATTENDANCE_FILE = "AttendanceRecord.csv";  // Path to attendance file
    private static final String EMPLOYEE_FILE = "EmployeeDetailsUTF.csv";      // Path to employee file

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter Employee No.: ");
        String empID = scanner.nextLine();
        
        System.out.print("Enter month (YYYY-MM): ");
        String monthYear = scanner.nextLine();

        double netSalary = computeMonthlySalary(empID, monthYear);
        if (netSalary > 0) {
            System.out.printf("\n✅ Payslip generated for Employee No. %s for %s%n", empID, monthYear);
        }
    }

    public static double computeMonthlySalary(String empID, String monthYear) {
        double totalWorkHours = 0.0;
        double hourlyRate = getHourlyRate(empID);

        if (hourlyRate == 0.0) {
            System.out.println("⚠ Employee not found or invalid hourly rate.");
            return 0.0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;

                String recordEmpID = data[0].trim();
                String recordDate = data[3].trim();
                String logInTime = data[4].trim();
                String logOutTime = data[5].trim();

                if (!recordEmpID.equals(empID) || !recordDate.startsWith(monthYear)) continue;

                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    Date logIn = timeFormat.parse(logInTime);
                    Date logOut = timeFormat.parse(logOutTime);

                    long diffMillis = logOut.getTime() - logIn.getTime();
                    double hoursWorked = diffMillis / (1000.0 * 60 * 60); // Convert to hours
                    totalWorkHours += hoursWorked;
                } catch (Exception e) {
                    System.out.println("⚠ Skipping invalid time entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠ Error reading attendance file: " + e.getMessage());
        }

        double grossSalary = totalWorkHours * hourlyRate;
        return applyDeductions(empID, grossSalary);
    }

    public static double getHourlyRate(String empID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 19 && data[0].trim().equals(empID)) {
                    try {
                        return Double.parseDouble(data[data.length - 1].trim().replaceAll("[\",]", ""));
                    } catch (NumberFormatException e) {
                        System.out.println("⚠ Invalid hourly rate for Employee ID " + empID);
                        return 0.0;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("⚠ Error reading employee file: " + e.getMessage());
        }
        return 0.0;
    }

    public static double applyDeductions(String empID, double grossSalary) {
        double sss = getSSSContribution(grossSalary);
        double philHealth = getPhilHealthContribution(grossSalary);
        double pagIbig = getPagIbigContribution(grossSalary);
        double taxableIncome = grossSalary - (sss + philHealth + pagIbig);
        double tax = getTaxAmount(taxableIncome);

        double netSalary = taxableIncome - tax;
        
        System.out.printf("\n========== 🧾 PAYSLIP ==========\n");
        System.out.printf("👤 Employee ID: %s%n", empID);
        System.out.printf("💰 Gross Salary: PHP %.2f%n", grossSalary);
        System.out.printf("📉 SSS Deduction: PHP %.2f%n", sss);
        System.out.printf("📉 PhilHealth Deduction: PHP %.2f%n", philHealth);
        System.out.printf("📉 Pag-IBIG Deduction: PHP %.2f%n", pagIbig);
        System.out.printf("📈 Taxable Income: PHP %.2f%n", taxableIncome);
        System.out.printf("💸 Withholding Tax: PHP %.2f%n", tax);
        System.out.printf("🤑 Net Salary: PHP %.2f%n", netSalary);
        System.out.printf("================================\n");

        return netSalary;
    }

    public static double getSSSContribution(double salary) {
        if (salary <= 3250) return 135.00;
        else if (salary > 24750) return 1125.00;

        double[] salaryBrackets = {3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250,
                                   8750, 9250, 9750, 10250, 10750, 11250, 11750, 12250, 12750, 13250,
                                   13750, 14250, 14750, 15250, 15750, 16250, 16750, 17250, 17750, 18250,
                                   18750, 19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250,
                                   23750, 24250, 24750};
        double[] contributions = {157.50, 180, 202.50, 225, 247.50, 270, 292.50, 315, 337.50, 360,
                                  382.50, 405, 427.50, 450, 472.50, 495, 517.50, 540, 562.50, 585,
                                  607.50, 630, 652.50, 675, 697.50, 720, 742.50, 765, 787.50, 810,
                                  832.50, 855, 877.50, 900, 922.50, 945, 967.50, 990, 1012.50, 1035,
                                  1057.50, 1080, 1102.50};

        for (int i = 0; i < salaryBrackets.length; i++) {
            if (salary <= salaryBrackets[i]) return contributions[i];
        }
        return 1125.00;
    }

    public static double getPhilHealthContribution(double salary) {
        return Math.min(Math.max(salary * 0.03, 300), 1800) / 2;  // 50% is employee share
    }

    public static double getPagIbigContribution(double salary) {
        return Math.min(salary * 0.02, 100);  // Max contribution is PHP 100
    }

    public static double getTaxAmount(double taxableIncome) {
        if (taxableIncome <= 20832) return 0;
        else if (taxableIncome <= 33333) return (taxableIncome - 20833) * 0.20;
        else if (taxableIncome <= 66667) return 2500 + (taxableIncome - 33333) * 0.25;
        else if (taxableIncome <= 166667) return 10833 + (taxableIncome - 66667) * 0.30;
        else if (taxableIncome <= 666667) return 40833.33 + (taxableIncome - 166667) * 0.32;
        else return 200833.33 + (taxableIncome - 666667) * 0.35;
    }
}
