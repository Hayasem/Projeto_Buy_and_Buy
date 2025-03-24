package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.models.HolderNotificacao;

public class NotificacaoManager {
    SQLiteDatabase db;

    public NotificacaoManager(SQLiteDatabase db) {
        this.db = db;
    }
    public HolderNotificacao getNotificacao(){
        Cursor cursor = db.rawQuery("SELECT * FROM NOTIFICACAO", null);
        if (cursor.moveToFirst()){
            HolderNotificacao notificacao = new HolderNotificacao();

            notificacao.setIdNotificacao(cursor.getInt(0));
            notificacao.setIdUsuario(cursor.getInt(1));
            notificacao.setTitulo(cursor.getString(2));
            notificacao.setDescricao(cursor.getString(3));
            notificacao.setData_notif(cursor.getString(4));
            notificacao.setVisualizado(cursor.getString(5).equals("true"));

            cursor.close();
            cursor = null;
            return notificacao;
        }
        return null;
    }
}
