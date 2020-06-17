package model.customers;

public abstract class Customer {
    private String creditCard;
    private String email;
    private String account;
    private Integer paymentID;

    public Customer(String creditCard, String email, String account, Integer paymentId) {
        this.creditCard = creditCard;
        this.email = email;
        this.account = account;
        this.paymentID = paymentId;
    }

    public String getCreditCard() {return this.creditCard;}

    public String getEmail() {return this.email;}

    public String getAccount() {return this.account;}

    public Integer getPaymentID() {return this.paymentID;}

    public abstract void makePayment();

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }
}
