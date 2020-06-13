package model.customers;

public class Payment {
    private String paymentID;
    private float roomCost;
    private float additionalCost;

    public Payment(String paymentID, float roomCost, float additionalCost) {
        this.paymentID = paymentID;
        this.roomCost = roomCost;
        this.additionalCost = additionalCost;
    }

    public String getPaymentID() {
        return this.paymentID;
    }

    public float getRoomCost() {
        return this.roomCost;
    }

    public float getAdditionalCost() {
        return this.additionalCost;
    }
}
