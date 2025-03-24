package com.example.tela_login_projetointegrador.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Produto implements Serializable {

    public Map<String, Object> toMap() {
        Map<String, Object> mapaProduto = new HashMap<>();
        mapaProduto.put("idProduto", idProduto);
        mapaProduto.put("idUsuario", idUsuario);
        mapaProduto.put("nomeProduto", nomeProduto);
        mapaProduto.put("descricao", descricao);
        mapaProduto.put("idCategoria", idCategoria);
        mapaProduto.put("preco", preco);
        mapaProduto.put("imagem", imagem);
        mapaProduto.put("emEstoque", emEstoque);
        mapaProduto.put("quantidade", quantidade);
        return mapaProduto;
    }
    String idProduto;
    String idUsuario;
    int idCategoria;
    String nomeProduto;
    String descricao;
    float preco;
    String imagem;
    boolean emEstoque;
    int quantidade;

    public Produto(String idProduto, String idUsuario, int idCategoria, String nomeProduto, String descricao, float preco, String imagem, boolean emEstoque, int quantidade) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
        this.emEstoque = emEstoque;
        this.quantidade = quantidade;
    }

    public Produto() {
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

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isEmEstoque() {
        return emEstoque;
    }

    public void setEmEstoque(boolean emEstoque) {
        this.emEstoque = emEstoque;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
