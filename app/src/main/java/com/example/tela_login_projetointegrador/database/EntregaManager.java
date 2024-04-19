package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Entrega;
import com.example.tela_login_projetointegrador.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EntregaManager {
    SQLiteDatabase db;

    public EntregaManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Entrega getEndereco(){
        Cursor cursor = db.rawQuery("SELECT * FROM END_ENTREGA", null);
        if (cursor.moveToFirst()){
            Entrega endereco = new Entrega();

            endereco.setIdEntrega(cursor.getInt(0));
            endereco.setIdUsuario(cursor.getInt(1));
            endereco.setRua(cursor.getString(2));
            endereco.setNumero(cursor.getInt(3));
            endereco.setBairro(cursor.getString(4));
            endereco.setCidade(cursor.getString(5));
            endereco.setEstado(cursor.getString(6));
            endereco.setPais(cursor.getString(7));
            endereco.setCep(cursor.getString(8));

            cursor.close();
            cursor = null;
            return endereco;
        }
        return null;
    }
}
