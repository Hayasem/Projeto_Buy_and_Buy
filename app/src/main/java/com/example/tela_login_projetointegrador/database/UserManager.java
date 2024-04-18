package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Usuario;

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

            cursor.close();
            cursor = null;

            return usuarios;
        }
        return null;
    }
}
