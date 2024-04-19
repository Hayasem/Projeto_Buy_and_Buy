package com.example.tela_login_projetointegrador.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Locale;

public class UserManager {
    private SQLiteDatabase db;

    public UserManager(SQLiteDatabase db) {
        this.db = db;
    }

    public Usuario getUsuario(){
        Cursor cursor = db.rawQuery("SELECT * FROM USUARIO", null);
        if (cursor.moveToFirst()){
            Usuario usuarios = new Usuario();

            usuarios.setIdUsuario(cursor.getInt(0));
            usuarios.setNome(cursor.getString(1));
            usuarios.setCpf(cursor.getString(2));
            usuarios.setDataReg(cursor.getString(3));
            usuarios.setEmail(cursor.getString(4));
            usuarios.setSenha(cursor.getString(5));
            usuarios.setHash_senha(cursor.getString(6));
            usuarios.setSalt(cursor.getString(7));

            cursor.close();
            cursor = null;

            return usuarios;
        }
        return null;
    }
    public void cadastrarUsuario(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("NOME", usuario.getNome());
        values.put("CPF", usuario.getCpf());
        values.put("DATA_REG", getDataAtual());
        values.put("EMAIL", usuario.getEmail());
        values.put("SENHA", usuario.getSenha());

        String salt = gerarSalt();
        String hashSenha = gerarHashSenha(usuario.getSenha(), salt);

        values.put("HASH_SENHA", hashSenha);
        values.put("SALT", salt);

        db.insert("USUARIO", null, values);
    }
    public boolean autenticarUsuario(String email, String senha){
        Cursor cursor = db.rawQuery("SELECT * FROM USUARIO WHERE email = ?",
                new String[]{email});
        if (cursor.moveToFirst()){
            @SuppressLint("Range") String hashArmazenado = cursor.getString(cursor.getColumnIndex("hash_senha"));
            @SuppressLint("Range") String salt = cursor.getString(cursor.getColumnIndex("salt"));

            String hashSenhaDigitada = gerarHashSenha(senha, salt);

            cursor.close();
            return hashArmazenado.equals(hashSenhaDigitada);
        }
        return false;
    }
    private String gerarSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    private String gerarHashSenha(String senha, String salt){
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
    private String getDataAtual(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
