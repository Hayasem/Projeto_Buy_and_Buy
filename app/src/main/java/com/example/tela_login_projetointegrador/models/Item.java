package com.example.tela_login_projetointegrador.models;

public class Item {
    String title;
    int quantity;
    String currency_id = "BRL";
    float unit_price;

    public Item(String title, int quantity, float unit_price) {
        this.title = title;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }
}
