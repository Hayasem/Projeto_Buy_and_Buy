package com.example.tela_login_projetointegrador.model;

public class Telefone {
    String idTelefone;
    String numero;
    String idUsuario;

    public Telefone(String numero, String idUsuario, String userId) {
        this.numero = numero;
        this.idUsuario = idUsuario;
    }

    public String getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(String idTelefone) {
        this.idTelefone = idTelefone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
