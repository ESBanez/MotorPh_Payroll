/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.motorph_salarycomputations;

import java.util.Scanner;

/**
 *
 * @author Creative 1
 */
public class MotorPh_SalaryComputations {

    public static void main(String[] args) {
        Scanner inp = new Scanner (System.in);
        
        long empNum;
        String empName;
        double hourSalary, weeklyTime, weeklySalary, overTime;
        double regPay, overtimePay, netPay;
        
        System.out.printf("Enter Employee No. : ");
        
        empNum = inp.nextLong();
        
        System.out.printf("Enter hourly salary : ");
        
        hourSalary = inp.nextDouble();
        
        System.out.printf("Enter weekly Time : " );
        
        weeklyTime = inp.nextDouble();
        
        if (empNum == 10001) {
           empName = "Jason bat";
        }
        else if (empNum == 10002) {
            empName = "Primo Leon";
        }
        else if (empNum == 10003) {
            empName = "Zach Ephraim";
        }
        else if (empNum == 10004){
            empName = "Klarizza Hernandez";
        }
        else {
            empName = "Unknown di ka nakikilala ni system.";
        }
        
        
        
        if (weeklyTime < 100) {
            weeklySalary = weeklyTime;
            overTime = 0;
            regPay = hourSalary * weeklyTime;
            overtimePay = 0;
            netPay = regPay;
        }
        else {
            weeklySalary = 200;
            overTime = weeklyTime - 40;
            regPay = hourSalary * weeklySalary; 
            overtimePay  = hourSalary * overTime;
            netPay = regPay + overtimePay;
        }
        
        System.out.println("----------------------");
        System.out.println("Payroll System");       
        System.out.println("----------------------");
        System.out.println("Emplyee Name: " + empName);
        System.out.println("Emplyee Name: " + empNum);
        System.out.println("Hourly Salary: " + hourSalary);
        System.out.println("Weekly Time: " + weeklyTime);
        System.out.println("Regular Pay: " + regPay);
        System.out.println("Overtime Pay: " + overtimePay);
        System.out.println("Net Pay: " + netPay);
        
    }
}
