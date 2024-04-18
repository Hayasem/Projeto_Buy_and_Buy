package com.example.tela_login_projetointegrador.model;

public class Categoria {
    int idCategoria;
    String nome_categoria;

    public Categoria(int idCategoria, String nome_categoria) {
        this.idCategoria = idCategoria;
        this.nome_categoria = nome_categoria;
    }

    public Categoria() {
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome_categoria() {
        return nome_categoria;
    }

    public void setNome_categoria(String nome_categoria) {
        this.nome_categoria = nome_categoria;
    }
}
