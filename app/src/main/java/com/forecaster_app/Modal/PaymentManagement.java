package com.forecaster_app.Modal;

public class PaymentManagement {
    private String date;
    private String category_type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

    public PaymentManagement(String date, String category_type) {
        this.date = date;
        this.category_type = category_type;
    }
}
