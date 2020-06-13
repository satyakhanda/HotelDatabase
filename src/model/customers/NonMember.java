package model.customers;

public class NonMember extends Customer {
    public NonMember(int creditCard, String email, Account account, Payment paymentId) {
        super(creditCard, email, account, paymentId);
    }

    @Override
    public void makePayment() {

    }
}
