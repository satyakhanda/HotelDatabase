package model.employees;

public abstract class Employee {
    private int employeeID;
    private String employeeName;
    private HotelModel hotelID;

    public Employee (int employeeID, String employeeName, HotelModel hotelID) {
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

    public HotelModel getHotelID() {
        return hotelID;
    }
}
