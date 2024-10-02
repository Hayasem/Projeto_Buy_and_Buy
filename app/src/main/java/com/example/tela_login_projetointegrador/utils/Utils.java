package com.example.tela_login_projetointegrador.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {


    public static boolean isCampoVazio(String campo) {
        return campo.isEmpty();
    }

    public static boolean isCPFValido(String cpf) {
        return cpf.matches("\\d{11}");
    }


    public static boolean isCepValido(String cep) {
        return cep.matches("\\d{8}");
    }

    public static boolean isValidaCelular(String numero) {
        numero = numero.replace("-", "").replace(" ", "");
        return numero.matches("\\d{11}");
    }

    public static String limparTelefone(String numero) {
        return numero.replace("-", "").replace(" ", "");
    }

    public static Bitmap loadImageFromInternalStorage(String path) {
        try {
            File imageFile = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();


        }
        return null;
    }

    public static String obterDataHoraAtual() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formato);
    }
}
