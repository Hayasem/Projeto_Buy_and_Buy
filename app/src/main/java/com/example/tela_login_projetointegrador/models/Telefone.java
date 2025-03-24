package com.example.tela_login_projetointegrador.models;

public class Telefone {
    String idTelefone;
    String idUsuario;
    String contato;


    public Telefone(String idTelefone, String idUsuario, String contato ) {
        this.idTelefone = idTelefone;
        this.idUsuario = idUsuario;
        this.contato = contato;
    }


    public String getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(String idTelefone) {
        this.idTelefone = idTelefone;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
