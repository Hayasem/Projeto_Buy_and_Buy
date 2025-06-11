package com.example.tela_login_projetointegrador.models;

import java.util.List;

public class PreferenciaRequest {
    List<Item> items;
    BackUrls back_urls;
    String auto_return = "approved";

    public PreferenciaRequest(List<Item> items, BackUrls back_urls) {
        this.items = items;
        this.back_urls = back_urls;
    }
}
