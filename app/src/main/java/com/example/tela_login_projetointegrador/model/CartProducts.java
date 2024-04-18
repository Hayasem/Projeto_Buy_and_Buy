package com.example.tela_login_projetointegrador.model;

public class CartProducts {
    int idCarrinho;
    int idProduto;
    int idUsuario;
    int idPedido;
    float preco_uni;
    float total;
    String data_add;
    boolean noCarrinho;

    public CartProducts(int idCarrinho, int idProduto, int idUsuario, int idPedido, float preco_uni, float total, String data_add, boolean noCarrinho) {
        this.idCarrinho = idCarrinho;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.idPedido = idPedido;
        this.preco_uni = preco_uni;
        this.total = total;
        this.data_add = data_add;
        this.noCarrinho = noCarrinho;
    }

    public CartProducts() {
    }

    public int getIdCarrinho() {
        return idCarrinho;
    }

    public void setIdCarrinho(int idCarrinho) {
        this.idCarrinho = idCarrinho;
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

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public float getPreco_uni() {
        return preco_uni;
    }

    public void setPreco_uni(float preco_uni) {
        this.preco_uni = preco_uni;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getData_add() {
        return data_add;
    }

    public void setData_add(String data_add) {
        this.data_add = data_add;
    }

    public boolean isNoCarrinho() {
        return noCarrinho;
    }

    public void setNoCarrinho(boolean noCarrinho) {
        this.noCarrinho = noCarrinho;
    }
}
