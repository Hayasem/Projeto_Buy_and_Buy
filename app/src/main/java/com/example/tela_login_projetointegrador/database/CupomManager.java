package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Cupom;

public class CupomManager {
    SQLiteDatabase db;

    public CupomManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Cupom getCupom(){
        Cursor cursor = db.rawQuery("SELECT * FROM CUPOM_DESC", null);
        if (cursor.moveToFirst()){
            Cupom cupom = new Cupom();

            cupom.setIdCupom(cursor.getInt(0));
            cupom.setCodCupom(cursor.getString(1));
            cupom.setIdUsuario(cursor.getInt(2));
            cupom.setValido(cursor.getString(3).equals("true"));

            cursor.close();
            cursor = null;
            return cupom;
        }
        return null;
    }
}
