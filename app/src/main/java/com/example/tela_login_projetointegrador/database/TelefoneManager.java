package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Telefone;
import com.example.tela_login_projetointegrador.model.Usuario;

public class TelefoneManager {
    private SQLiteDatabase db;

    public TelefoneManager(SQLiteDatabase db) {
        this.db = db;
    }

    public long cadastrarTelefone(Telefone telefone) {
        Usuario usuario = new Usuario();
        ContentValues values = new ContentValues();
        values.put("numero", telefone.getNumero());
        values.put("idUsuario", usuario.getIdUsuario());
        long resultadoTelefone = db.insert("TELEFONE", null, values);

        if (resultadoTelefone != -1) {
        }
        return resultadoTelefone;
    }
    public Telefone cunsultarTelefone(){
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
