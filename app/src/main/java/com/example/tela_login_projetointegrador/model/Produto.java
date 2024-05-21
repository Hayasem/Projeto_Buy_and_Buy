package com.example.tela_login_projetointegrador.model;

public class Produto {
    int idProduto;
    int idUsuario;
    String titulo;
    String descricao;
    int idCategoria;
    float preco;
    int status;
    int quantidade;

    public Produto(int idProduto, int idUsuario, String titulo, String descricao, int idCategoria, float preco, int status, int quantidade) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.idCategoria = idCategoria;
        this.preco = preco;
        this.status = status;
        this.quantidade = quantidade;
    }

    public Produto( String titulo, String descricao, int idCategoria, float preco, int status, int quantidade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.idCategoria = idCategoria;
        this.preco = preco;
        this.status = status;
        this.quantidade = quantidade;
    }

    public Produto() {
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
