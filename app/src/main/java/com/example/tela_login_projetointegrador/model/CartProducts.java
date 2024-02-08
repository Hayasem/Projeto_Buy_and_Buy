package com.example.tela_login_projetointegrador.model;

public class CartProducts {
    int foto;
    String nome;
    int maisIcone;
    int menosIcone;
    String quantidade;
    String preco;

    public CartProducts(int foto, String nome, int maisIcone, int menosIcone, String quantidade, String preco) {
        this.foto = foto;
        this.nome = nome;
        this.maisIcone = maisIcone;
        this.menosIcone = menosIcone;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMaisIcone() {
        return maisIcone;
    }

    public void setMaisIcone(int maisIcone) {
        this.maisIcone = maisIcone;
    }

    public int getMenosIcone() {
        return menosIcone;
    }

    public void setMenosIcone(int menosIcone) {
        this.menosIcone = menosIcone;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
