package model.customers;

public class NonMember extends Customer {
    public NonMember(String creditCard, String email, String account, Integer paymentId) {
        super(creditCard, email, account, paymentId);
    }

    @Override
    public void makePayment() {

    }
}
