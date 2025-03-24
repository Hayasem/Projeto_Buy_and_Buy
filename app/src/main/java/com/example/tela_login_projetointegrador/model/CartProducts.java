package com.example.tela_login_projetointegrador.model;

public class CartProducts {
    String nomeVendedor;
    String nomeProduto;
    int quantidade;
    String idCarrinho;
    String idProduto;
    String idUsuario;
    String idPedido;
    float preco_uni;
    float total;
    String data_add;
    boolean noCarrinho;

    public CartProducts(String idCarrinho, String idProduto, String idUsuario, String idPedido, float preco_uni, float total, String data_add, boolean noCarrinho, String nomeVendedor,
                        String nomeProduto, int quantidade) {
        this.idCarrinho = idCarrinho;
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.idPedido = idPedido;
        this.preco_uni = preco_uni;
        this.total = total;
        this.data_add = data_add;
        this.noCarrinho = noCarrinho;
        this.nomeVendedor = nomeVendedor;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
    }

    public CartProducts(String idProduto, String titulo, float preco, int quantidade) {
        this.idProduto = idProduto;
        this.nomeProduto = titulo;
        this.preco_uni = preco;
        this.quantidade = quantidade;
    }


    public String getIdCarrinho() {
        return idCarrinho;
    }

    public void setIdCarrinho(String idCarrinho) {
        this.idCarrinho = idCarrinho;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
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
