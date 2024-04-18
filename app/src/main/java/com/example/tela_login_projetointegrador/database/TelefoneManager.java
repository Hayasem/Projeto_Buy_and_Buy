package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Telefone;

public class TelefoneManager {
    SQLiteDatabase db;

    public TelefoneManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Telefone getTelefone(){
        Cursor cursor = db.rawQuery("SELECT * FROM TELEFONE",  null);
        if (cursor.moveToFirst()){
            Telefone telefone = new Telefone();

            telefone.setIdTelefone(cursor.getInt(0));
            telefone.setNumero(cursor.getString(1));
            telefone.setIdUsuario(cursor.getInt(2));

            cursor.close();
            cursor = null;
            return telefone;
        }
        return null;
    }
}
