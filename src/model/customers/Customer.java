package model.customers;

public abstract class Customer {
    private int creditCard;
    private String email;
    private Account account;
    private Payment paymentID;

    public Customer(int creditCard, String email, Account account, Payment paymentId) {
        this.creditCard = creditCard;
        this.email = email;
        this.account = account;
        this.paymentID = paymentId;
    }

    public int getCreditCard() {return this.creditCard;}

    public String getEmail() {return this.email;}

    public Account getAccount() {return this.account;}

    public String getPaymentID() {return this.paymentID.getPaymentID();}

    public abstract void makePayment();

    
}
