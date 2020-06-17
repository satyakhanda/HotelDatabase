package model.customers;

public class Member extends Customer {
    private float points;
    public Member(String creditCard, String email, String account, Integer paymentId, float points) {
        super(creditCard, email, account, paymentId);
        this.points = points;
    }

    @Override
    public void makePayment() {

    }

    public float getPoints() {
        return this.points;
    }
}
