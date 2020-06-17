package model.customers;

public class Member extends Customer {
    private int points;
    public Member(String creditCard, String email, String account, Integer paymentId, int points) {
        super(creditCard, email, account, paymentId);
        this.points = points;
    }

    @Override
    public void makePayment() {

    }
}
