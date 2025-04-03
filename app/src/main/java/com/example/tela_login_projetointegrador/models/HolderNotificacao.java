package com.example.tela_login_projetointegrador.models;

public class HolderNotificacao {
     int idNotificacao;
     int idUsuario;
     String image_notif;
     String titulo;
     String descricao;
     String data_notif;
     String hora_notif;

    public HolderNotificacao(int idNotificacao, int idUsuario, String image_notif, String titulo, String descricao, String data_notif, String hora_notif) {
        this.idNotificacao = idNotificacao;
        this.idUsuario = idUsuario;
        this.image_notif = image_notif;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data_notif = data_notif;
        this.hora_notif = hora_notif;
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

    public String getImage_notif() {
        return image_notif;
    }

    public void setImage_notif(String image_notif) {
        this.image_notif = image_notif;
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

    public String getHora_notif() {
        return hora_notif;
    }

    public void setHora_notif(String hora_notif) {
        this.hora_notif = hora_notif;
    }
}
