package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Categoria;

public class CategoriaManager {
    SQLiteDatabase db;

    public CategoriaManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Categoria getCategoria(){
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORIA", null);
        if (cursor.moveToFirst()){
            Categoria categoria = new Categoria();

            categoria.setIdCategoria(cursor.getInt(0));
            categoria.setNome_categoria(cursor.getString(1));

            cursor.close();
            cursor = null;
            return categoria;
        }
        return null;
    }
}
