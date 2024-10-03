package com.example.tela_login_projetointegrador.database;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tela_login_projetointegrador.backendactivitys.MainActivity;
import com.example.tela_login_projetointegrador.model.Telefone;
import com.example.tela_login_projetointegrador.model.Usuario;

import java.io.File;
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
        values.put("tentativas_login", usuario.getTentativasLogin());
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
            usuarios.setTentativasLogin(cursor.getInt(10));

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
//Método para saber se o usuário está bloqueado:
//--------------------------------------------------------------------------------------------------
public boolean estaBloqueado(String email) {
    String query = "SELECT bloqueado_ate FROM USUARIO WHERE email = ?";
    Cursor cursor = db.rawQuery(query, new String[]{email});
    if (cursor.moveToFirst()) {
        String bloqueadoAteStr = cursor.getString(cursor.getColumnIndexOrThrow("bloqueado_ate")); // Obtem a string
        if (bloqueadoAteStr != null && !bloqueadoAteStr.isEmpty()){
            long bloqueadoAte = Long.parseLong(bloqueadoAteStr); // Converte a string para long
            long agora = System.currentTimeMillis(); // Obtém o tempo atual
            cursor.close();
            return bloqueadoAte > agora; // Compara
        }
    }
    cursor.close();
    return false; // Se não houver resultados, a conta não está bloqueada
}

    // Método para incrementar tentativas de login
    public void incrementarTentativas(String email) {
        String query = "UPDATE USUARIO SET tentativas_login = tentativas_login + 1 WHERE email = ?";
        db.execSQL(query, new String[]{email});
    }

    // Método para resetar tentativas após login bem-sucedido
    public void resetarTentativas(String email) {
        String query = "UPDATE USUARIO SET tentativas_login = 0, bloqueado_ate = NULL WHERE email = ?";
        db.execSQL(query, new String[]{email});
    }

    // Método para bloquear a conta após 3 tentativas
    public void bloquearConta(String email) {
        long tempoDesbloqueio = System.currentTimeMillis() + (15 * 60 * 1000); // Bloqueia por 15 minutos
        String query = "UPDATE USUARIO SET bloqueado_ate = ? WHERE email = ?";
        db.execSQL(query, new Object[]{String.valueOf(tempoDesbloqueio), email});
    }

    // Método para obter número de tentativas
    public int obterTentativas(String email) {
        String query = "SELECT tentativas_login FROM USUARIO WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        int tentativas = 0;
        if (cursor.moveToFirst()) {
            tentativas = cursor.getInt(0);
        }
        cursor.close();
        return tentativas;
    }
//Método para deslogar o usuário da aplicação:
//--------------------------------------------------------------------------------------------------
    public void deslogarUsuario(){
        ContentValues values = new ContentValues();
        values.put("logged_in", 0);
        db.update("USUARIO", values, "logged_in = 1", null);
    }
//Método criptografar a senha do usuário:
//--------------------------------------------------------------------------------------------------
    public String gerarSalt(){
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
