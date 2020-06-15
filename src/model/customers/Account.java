package model.customers;

public class Account {
    private String username;
    private String password;
    private String creditCard;

    public Account(String username, String password, String creditCard) {
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

    public String getCreditCard() {
        return this.creditCard;
    }
}
