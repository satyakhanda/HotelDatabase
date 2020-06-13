package model.customers;

public class Account {
    private String username;
    private String password;
    private int creditCard;

    public Account(String username, String password, int creditCard) {
        this.username = username;
        this.password = password;
        this.creditCard = creditCard;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getCreditCard() {
        return this.creditCard;
    }
}
