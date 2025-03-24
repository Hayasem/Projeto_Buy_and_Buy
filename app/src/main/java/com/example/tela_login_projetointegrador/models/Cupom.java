package com.example.tela_login_projetointegrador.models;

public class Cupom {
    int idCupom;
    String codCupom;
    int idUsuario;
    boolean valido;

    public Cupom(int idCupom, String codCupom, int idUsuario, boolean valido) {
        this.idCupom = idCupom;
        this.codCupom = codCupom;
        this.idUsuario = idUsuario;
        this.valido = valido;
    }

    public Cupom() {
    }

    public int getIdCupom() {
        return idCupom;
    }

    public void setIdCupom(int idCupom) {
        this.idCupom = idCupom;
    }

    public String getCodCupom() {
        return codCupom;
    }

    public void setCodCupom(String codCupom) {
        this.codCupom = codCupom;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }
}
