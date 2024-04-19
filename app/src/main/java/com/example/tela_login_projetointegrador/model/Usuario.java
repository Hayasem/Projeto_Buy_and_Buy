package com.example.tela_login_projetointegrador.model;

public class Usuario {
    int idUsuario;
    String nome;
    String cpf;
    String dataReg;
    String email;
    String senha;
    String hash_senha;
    String salt;

    public Usuario(int idUsuario, String nome, String cpf, String dataReg, String email, String senha,
                   String hash_senha, String salt) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.cpf = cpf;
        this.dataReg = dataReg;
        this.email = email;
        this.senha = senha;
        this.hash_senha = hash_senha;
        this.salt = salt;
    }
    public Usuario(){

    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getDataReg() {
        return dataReg;
    }

    public void setDataReg(String dataReg) {
        this.dataReg = dataReg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getHash_senha() {
        return hash_senha;
    }

    public void setHash_senha(String hash_senha) {
        this.hash_senha = hash_senha;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
