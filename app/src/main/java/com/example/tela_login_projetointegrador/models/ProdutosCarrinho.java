package com.example.tela_login_projetointegrador.models;

import android.widget.ImageView;

public class ProdutosCarrinho {
    String idUsuario, idCarrinho, idProduto, nomeVendedor, nomeProduto, data_add, imagemUrl;
    int quantidade;
    float totalPrice, preco;


    public ProdutosCarrinho(){
    }

    public ProdutosCarrinho(String idUsuario, String idCarrinho, String idProduto, String nomeVendedor, String nomeProduto, String data_add, String imagemUrl, float preco, int quantidade, float totalPrice) {
        this.idUsuario = idUsuario;
        this.idCarrinho = idCarrinho;
        this.idProduto = idProduto;
        this.nomeVendedor = nomeVendedor;
        this.nomeProduto = nomeProduto;
        this.data_add = data_add;
        this.imagemUrl = imagemUrl;
        this.preco = preco;
        this.quantidade = quantidade;
        this.totalPrice = totalPrice;
    }

    public ProdutosCarrinho(String idCarrinhoExistente, String idProduto, String nomeProduto, String imagemUrl, float preco, int quantidade,String idUsuario) {
        this.idCarrinho = idCarrinhoExistente;
        this.idProduto = idProduto;
        this.nomeProduto  =nomeProduto;
        this.imagemUrl = imagemUrl;
        this.quantidade = quantidade;
        this.preco = preco;
        this.idUsuario = idUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getData_add() {
        return data_add;
    }

    public void setData_add(String data_add) {
        this.data_add = data_add;
    }

    public String getimagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

}
