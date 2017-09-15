//import java.io.*;

public class Employee {

    String name;
    int age;
    String designation;
    double salary;
    static int numEmployees;
  
    // This is the constructor of the class Employee
    public Employee(String name){
       this.name = name;
       numEmployees++;
    }
    // Assign the age of the Employee  to the variable age.
    public void empAge(int empAge){
       age =  empAge;
    }
    /* Assign the designation to the variable designation.*/
    public void empDesignation(String empDesig){
       designation = empDesig;
    }
    /* Assign the salary to the variable salary.*/
    public void empSalary(double empSalary){
       salary = empSalary;
    }
    /* Print the Employee details */
    public void printEmployee(){
       System.out.println("Name:"+ name );
       System.out.println("Age:" + age );
       System.out.println("Designation:" + designation );
       System.out.println("Salary:" + salary);
    }
    /* Print the total number of employees */
    public void printTotal() {
        System.out.println("Total employees:" + numEmployees);
    }
                      
}
