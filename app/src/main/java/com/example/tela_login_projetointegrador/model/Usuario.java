package com.example.tela_login_projetointegrador.model;

public class Usuario {
    private String idUsuario;
    private String idTelefone;
    String nome;
    String cpf;
    String email;
    String senha;
    String cep;
    String hash_senha;
    String dataReg;
    String salt;

    public Usuario(String idUsuario, String idTelefone, String nome, String cpf, String email, String senha, String cep, String hash_senha, String dataReg, String salt) {
        this.idUsuario = idUsuario;
        this.idTelefone = idTelefone;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cep = cep;
        this.hash_senha = hash_senha;
        this.dataReg = dataReg;
        this.salt = salt;
    }

    public Usuario(String idUsuario, String nome, String cpf, String email, String senha, String cep,
                   String hash_senha, String dataReg) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cep = cep;
        this.hash_senha = hash_senha;
        this.dataReg = dataReg;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(String idTelefone) {
        this.idTelefone = idTelefone;
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

    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
}
