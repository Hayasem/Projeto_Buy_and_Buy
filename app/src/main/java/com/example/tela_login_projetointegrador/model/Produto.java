package com.example.tela_login_projetointegrador.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Produto implements Serializable {
    int idProduto;
    int idUsuario;
    String titulo;
    String descricao;
    int idCategoria;
    float preco;
    int status;
    String imagem;

    public Produto(int idProduto, int idUsuario, String titulo, String descricao, int idCategoria, float preco, int status,String imagem) {
        this.idProduto = idProduto;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.idCategoria = idCategoria;
        this.preco = preco;
        this.status = status;
        this.imagem = imagem;
    }

    public Produto( String titulo, String descricao, int idCategoria, float preco, int status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.idCategoria = idCategoria;
        this.preco = preco;
        this.status = status;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }


}
