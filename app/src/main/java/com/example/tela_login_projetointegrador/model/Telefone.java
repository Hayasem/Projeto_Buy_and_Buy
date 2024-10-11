package com.example.tela_login_projetointegrador.model;

public class Telefone {
    int idTelefone;
    String numero;
    long idUsuario;

    public Telefone(String numero, String idUsuario) {
        this.numero = numero;
        this.idUsuario = idUsuario;
    }

    public Telefone() {
    }

    public int getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(int idTelefone) {
        this.idTelefone = idTelefone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
