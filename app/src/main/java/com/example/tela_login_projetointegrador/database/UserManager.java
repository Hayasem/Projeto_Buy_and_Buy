package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Entrega;
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
    private SQLiteDatabase db;

    public UserManager(SQLiteDatabase db) {
        this.db = db;
    }

        public Usuario getUsuario(String email){
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
    public long cadastrarUsuario(Usuario usuario, Telefone telefone){
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("cpf", usuario.getCpf());
        values.put("data_reg", getDataAtual());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());

        String salt = gerarSalt();
        String hashSenha = gerarHashSenha(usuario.getSenha(), salt);

        values.put("hash_senha", hashSenha);
        values.put("salt", salt);
        long idUsuario = db.insert("USUARIO", null, values);

        if (idUsuario != -1) {
            cadastrarTelefone(idUsuario, telefone);
        }
        return idUsuario;
    }
    private void cadastrarTelefone(long idUsuario, Telefone telefone) {
        ContentValues values = new ContentValues();
        values.put("numero", telefone.getNumero());
        values.put("idUsuario", idUsuario);
        db.insert("telefone", null, values);
    }
    public boolean autenticarUsuario(String email, String senha){
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
}
