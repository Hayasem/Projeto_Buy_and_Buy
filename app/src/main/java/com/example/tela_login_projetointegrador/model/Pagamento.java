package com.example.tela_login_projetointegrador.model;

public class Pagamento {
    int idPagamento;
    int idPedido;
    String met_pagamento;
    boolean status;
    float valor;
    int idCupom;

    public Pagamento(int idPagamento, int idPedido, String met_pagamento, boolean status, float valor, int idCupom) {
        this.idPagamento = idPagamento;
        this.idPedido = idPedido;
        this.met_pagamento = met_pagamento;
        this.status = status;
        this.valor = valor;
        this.idCupom = idCupom;
    }

    public Pagamento() {
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getMet_pagamento() {
        return met_pagamento;
    }

    public void setMet_pagamento(String met_pagamento) {
        this.met_pagamento = met_pagamento;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getIdCupom() {
        return idCupom;
    }

    public void setIdCupom(int idCupom) {
        this.idCupom = idCupom;
    }
}
