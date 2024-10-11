package com.example.tela_login_projetointegrador.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Patterns;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Patterns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {


    /*Validações Usuários*/

    public static boolean isEmailvalido(String email){
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isCampoVazio(String campo){
        return campo.isEmpty();
    }

    public static boolean isCPFValido(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        return cpf.matches("\\d{11}");
    }
    public static boolean isCepValido(String cep) {
        cep = cep.replaceAll("\\D", "");
        return cep.matches("\\d{8}");
    }

    public static boolean isValidaCelular(String numero) {
        numero = numero.replace("-","").replace(" ","");
        numero = numero.replaceAll("\\D", "");
        return numero.matches("\\d{11}");
    }

    public static String limparTelefone(String numero) {
       return numero.replace("-","").replace(" ","");
    }
}
