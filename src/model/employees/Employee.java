package model.employees;

public class Employee {
    private int employeeID;
    private String employeeName;
    private int hotelID;

    public Employee (int employeeID, String employeeName, int hotelID) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.hotelID = hotelID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getHotelID() {
        return hotelID;
    }
}
