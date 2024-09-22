package com.example.tela_login_projetointegrador.database;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tela_login_projetointegrador.backendactivitys.MainActivity;
import com.example.tela_login_projetointegrador.model.Telefone;
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
    private final SQLiteDatabase db;

    public UserManager(SQLiteDatabase db) {
        this.db = db;
    }


    public long cadastrarUsuario(Usuario usuario){
        String salt = gerarSalt();
        String hashSenha = gerarHashSenha(usuario.getSenha(), salt);
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("cpf", usuario.getCpf());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("cep", usuario.getCep());
        values.put("data_reg", getDataAtual());
        values.put("hash_senha", hashSenha);
        values.put("salt", salt);
        return db.insert("USUARIO", null, values);
    }
    public Usuario consultarUsuario(int idUsuario){
        Cursor cursor = db.rawQuery("SELECT * FROM USUARIO WHERE idUsuario = ?",
                new String[]{String.valueOf(idUsuario)});
        if (cursor.moveToFirst()){

            Usuario usuarios = new Usuario();
            usuarios.setIdUsuario(cursor.getInt(0));
            usuarios.setIdTelefone(cursor.getInt(1));
            usuarios.setNome(cursor.getString(2));
            usuarios.setCpf(cursor.getString(3));
            usuarios.setEmail(cursor.getString(4));
            usuarios.setSenha(cursor.getString(5));
            usuarios.setCep(cursor.getString(6));
            usuarios.setHash_senha(cursor.getString(7));         
            usuarios.setDataReg(cursor.getString(8));
            usuarios.setSalt(cursor.getString(9));

            cursor.close();
            cursor = null;

            return usuarios;
        }
        return null;
    }

// Método para salvar o email e senha no Banco de Dados:
//---------------------------------------------------------------------------------------------
    public boolean compararSenha(String email, String senha){
        Cursor cursor = db.rawQuery("SELECT hash_senha, salt FROM USUARIO WHERE email = ?",
                new String[]{email});
        if (cursor.moveToFirst()){
            String hashArmazenado = cursor.getString(cursor.getColumnIndexOrThrow("hash_senha"));
            String salt = cursor.getString(cursor.getColumnIndexOrThrow("salt"));
            String hashSenhaDigitada = gerarHashSenha(senha, salt);

            cursor.close();
            return hashArmazenado.equals(hashSenhaDigitada);
        }
        return false;
    }

//Método para verificar se o email inserido pelo usuário já está cadastrado:
//----------------------------------------------------------------------------------------------
    public boolean isEmailCadastrado(String email){
        String query = "SELECT 1 FROM USUARIO WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean emailExiste = cursor.moveToFirst();
        cursor.close();
        return emailExiste;
    }
//---------------------------------------------------------------------------------------------
    public void deslogarUsuario(){
        ContentValues values = new ContentValues();
        values.put("logged_in", 0);
        db.update("USUARIO", values, "logged_in = 1", null);
    }
    public String gerarSalt(){
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

    public void setUserLogado(String email) {
        ContentValues values = new ContentValues();
        values.put("logged_in", 1);
        db.update("USUARIO", values, "email = ?", new String[]{email});
    }
}
