package model.customers;

public abstract class Customer {
    private String creditCard;
    private String email;
    private String account;
    private int paymentID;

    public Customer(String creditCard, String email, String account, int paymentId) {
        this.creditCard = creditCard;
        this.email = email;
        this.account = account;
        this.paymentID = paymentId;
    }

    public String getCreditCard() {return this.creditCard;}

    public String getEmail() {return this.email;}

    public String getAccount() {return this.account;}

    public int getPaymentID() {return this.paymentID;}

    public abstract void makePayment();

    
}
