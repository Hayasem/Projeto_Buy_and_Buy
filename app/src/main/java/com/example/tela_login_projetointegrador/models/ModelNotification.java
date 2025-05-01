package com.example.tela_login_projetointegrador.models;

public class ModelNotification {
    private String idNotificacao;
    private String titulo;
    private String mensagem;
    private long timestamp;

    public ModelNotification(String idNotificacao, String titulo, String mensagem, long timestamp) {
        this.idNotificacao = idNotificacao;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
    }

    public ModelNotification() {
    }

    public String getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(String idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
