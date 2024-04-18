package com.example.tela_login_projetointegrador.model;

public class Avaliacao {
    int idAvaliacao;
    int idUsuario;
    int idProduto;
    int avaliacao;
    String comentario;
    String dataAval;

    public Avaliacao() {
    }

    public Avaliacao(int idAvaliacao, int idUsuario, int idProduto, int avaliacao, String comentario, String dataAval) {
        this.idAvaliacao = idAvaliacao;
        this.idUsuario = idUsuario;
        this.idProduto = idProduto;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
        this.dataAval = dataAval;
    }

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDataAval() {
        return dataAval;
    }

    public void setDataAval(String dataAval) {
        this.dataAval = dataAval;
    }
}
