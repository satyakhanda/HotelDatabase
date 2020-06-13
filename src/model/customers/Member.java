package model.customers;

public class Member extends Customer {
    private int points;
    public Member(int creditCard, String email, Account account, Payment paymentId, int points) {
        super(creditCard, email, account, paymentId);
        this.points = points;
    }

    @Override
    public void makePayment() {

    }
}
