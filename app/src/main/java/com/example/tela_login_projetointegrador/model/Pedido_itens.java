package com.example.tela_login_projetointegrador.model;

public class Pedido_itens {
    int idItensPedidos;
    int idPedido;
    String data;
    float valorUnit;
    int idProduto;
    int quantidade;

    public Pedido_itens(int idItensPedidos, int idPedido, String data, float valorUnit, int idProduto, int quantidade) {
        this.idItensPedidos = idItensPedidos;
        this.idPedido = idPedido;
        this.data = data;
        this.valorUnit = valorUnit;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public Pedido_itens() {
    }

    public int getIdItensPedidos() {
        return idItensPedidos;
    }

    public void setIdItensPedidos(int idItensPedidos) {
        this.idItensPedidos = idItensPedidos;
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

    public float getValorUnit() {
        return valorUnit;
    }

    public void setValorUnit(float valorUnit) {
        this.valorUnit = valorUnit;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
