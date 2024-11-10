package com.example.tela_login_projetointegrador.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Produto implements Serializable {

    public Map<String, Object> toMap() {
        Map<String, Object> mapaProduto = new HashMap<>();
        mapaProduto.put("idProduto", idProduto);
        mapaProduto.put("idUsuario", idUsuario);
        mapaProduto.put("titulo", titulo);
        mapaProduto.put("descricao", descricao);
        mapaProduto.put("idCategoria", idCategoria);
        mapaProduto.put("preco", preco);
        mapaProduto.put("status", status);
        mapaProduto.put("imagem", imagem);
        return mapaProduto;
    }
    String idProduto;
    String idUsuario;
    String titulo;
    String descricao;
    int idCategoria;
    float preco;
    int status;
    String imagem;

    public Produto(String idProduto, String idUsuario, String titulo, String descricao, int idCategoria, float preco, int status, String imagem) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.idCategoria = idCategoria;
        this.preco = preco;
        this.status = status;
        this.imagem = imagem;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }


}
