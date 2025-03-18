package com.example.tela_login_projetointegrador.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Patterns;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
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
        // Remover caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // CPF deve ter 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        // Cálculo do primeiro dígito verificador
        int firstDigit = calculateDigit(cpf.substring(0, 9), 10);
        // Cálculo do segundo dígito verificador
        int secondDigit = calculateDigit(cpf.substring(0, 9) + firstDigit, 11);

        // Verifica se os dígitos verificadores estão corretos
        return cpf.equals(cpf.substring(0, 9) + firstDigit + secondDigit);
    }

    private static int calculateDigit(String base, int weight) {
        int sum = 0;

        for (char digit : base.toCharArray()) {
            sum += Character.getNumericValue(digit) * weight--;
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
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
    public static String gerarSalt(){
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            random = new SecureRandom(); // fallback para o padrão
        }
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    public static String gerarHashSenha(String senha, String salt){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((senha + salt).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2*hash.length);
            for(byte b : hash){
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }
}
