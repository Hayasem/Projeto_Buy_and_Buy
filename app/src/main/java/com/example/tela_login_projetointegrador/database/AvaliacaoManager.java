package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.models.Avaliacao;

public class AvaliacaoManager {
    SQLiteDatabase db;

    public AvaliacaoManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Avaliacao getAvaliacao(){
        Cursor cursor = db.rawQuery("SELECT * FROM AVALIACAO", null);
        if (cursor.moveToFirst()){
            Avaliacao avaliacao = new Avaliacao();

            avaliacao.setIdAvaliacao(cursor.getInt(0));
            avaliacao.setIdUsuario(cursor.getInt(1));
            avaliacao.setIdProduto(cursor.getInt(2));
            avaliacao.setAvaliacao(cursor.getInt(3));
            avaliacao.setComentario(cursor.getString(4));
            avaliacao.setDataAval(cursor.getString(5));

            cursor.close();
            cursor = null;
            return avaliacao;
        }
        return null;
    }
}
