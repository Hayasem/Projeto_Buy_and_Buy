package com.example.tela_login_projetointegrador.models;

public class PedidoItens {
    int idItensPedidos;
    String idPedido;

    float valorUnit;
    String idProduto;
    int quantidade;

    public PedidoItens(String idPedido, float valorUnit, String idProduto, int quantidade) {
        this.idPedido = idPedido;
        this.valorUnit = valorUnit;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public PedidoItens() {
    }

    public int getIdItensPedidos() {
        return idItensPedidos;
    }

    public void setIdItensPedidos(int idItensPedidos) {
        this.idItensPedidos = idItensPedidos;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public float getValorUnit() {
        return valorUnit;
    }

    public void setValorUnit(float valorUnit) {
        this.valorUnit = valorUnit;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
