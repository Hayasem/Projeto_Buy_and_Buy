package com.example.tela_login_projetointegrador.models;

public class HolderNotificacao {
     int idNotificacao;
     int idUsuario;
     String titulo;
     String descricao;
     String data_notif;
     boolean visualizado;

    public HolderNotificacao(int idNotificacao, int idUsuario, String titulo, String descricao, String data_notif, boolean visualizado) {
        this.idNotificacao = idNotificacao;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data_notif = data_notif;
        this.visualizado = visualizado;
    }

    public HolderNotificacao() {
    }

    public int getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(int idNotificacao) {
        this.idNotificacao = idNotificacao;
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

    public String getData_notif() {
        return data_notif;
    }

    public void setData_notif(String data_notif) {
        this.data_notif = data_notif;
    }

    public boolean isVisualizado() {
        return visualizado;
    }

    public void setVisualizado(boolean visualizado) {
        this.visualizado = visualizado;
    }

}
