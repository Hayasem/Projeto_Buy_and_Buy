package com.example.tela_login_projetointegrador.utils;

public class Utils {


    public static boolean isCampoVazio(String campo){
        return campo.isEmpty();
    }

    public static boolean isCPFValido(String cpf) {
        return cpf.matches("\\d{11}");
    }


    public static boolean isCepValido(String cep) {
        return cep.matches("\\d{8}");
    }

    public static boolean isValidaCelular(String numero) {
        numero = numero.replace("-","").replace(" ","");
        return numero.matches("\\d{11}");
    }

    public static String limparTelefone(String numero) {
       return numero.replace("-","").replace(" ","");
    }


}
