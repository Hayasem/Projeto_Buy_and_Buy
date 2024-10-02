package com.example.tela_login_projetointegrador.model;

public class Pedido {
    int idPedido;
    String data;
    int idUsuario;
    String statusPedido;

    public Pedido() {
    }

    public Pedido( String data, int idUsuario,String statusPedido) {
        this.data = data;
        this.idUsuario = idUsuario;
        this.statusPedido = statusPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }
}
