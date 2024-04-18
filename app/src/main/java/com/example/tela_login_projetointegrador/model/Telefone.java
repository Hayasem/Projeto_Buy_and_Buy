package com.example.tela_login_projetointegrador.model;

public class Telefone {
    int idTelefone;
    String numero;
    int idUsuario;

    public Telefone(int idTelefone, String numero, int idUsuario) {
        this.idTelefone = idTelefone;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
