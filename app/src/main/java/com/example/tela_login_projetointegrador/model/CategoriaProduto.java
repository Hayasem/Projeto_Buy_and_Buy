package com.example.tela_login_projetointegrador.model;

public class CategoriaProduto {
    private int id;
    private String descricao;

    public CategoriaProduto(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;  // Isso é o que será mostrado no Spinner
    }
}
