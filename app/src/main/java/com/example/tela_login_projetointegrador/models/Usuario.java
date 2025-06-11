package com.example.tela_login_projetointegrador.models;

public class Usuario {
    private String idUsuario;
    private String idContato;
    String nome;
    String cpf;
    String email;
    String cep;
    String dataReg;


    public Usuario(){}

    public Usuario(String idUsuario, String idContato, String nome, String cpf, String email, String cep, String dataReg){
        this.idUsuario = idUsuario;
        this.idContato = idContato;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cep = cep;
        this.dataReg = dataReg;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdContato() {
        return idContato;
    }

    public void setIdContato(String idTelefone) {
        this.idContato = idTelefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }


    public String getDataReg() {
        return dataReg;
    }

    public void setDataReg(String dataReg) {
        this.dataReg = dataReg;
    }

}


