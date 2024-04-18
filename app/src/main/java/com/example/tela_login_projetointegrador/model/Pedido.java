package com.example.tela_login_projetointegrador.model;

public class Pedido {
    int idPedido;
    String data;
    float valorTotal;
    int idProduto;
    int idUsuario;

    public Pedido() {
    }

    public Pedido(int idPedido, String data, float valorTotal, int idProduto, int idUsuario) {
        this.idPedido = idPedido;
        this.data = data;
        this.valorTotal = valorTotal;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
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

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
