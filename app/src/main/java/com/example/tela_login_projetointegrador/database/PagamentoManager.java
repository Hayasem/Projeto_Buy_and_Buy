package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.models.Pagamento;

public class PagamentoManager {
    SQLiteDatabase db;

    public PagamentoManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Pagamento getPagamento(){
        Cursor cursor = db.rawQuery("SELECT * FROM PEDIDO", null);
        if (cursor.moveToFirst()){
            Pagamento pagamento = new Pagamento();

            pagamento.setIdPagamento(cursor.getInt(0));
            pagamento.setIdPedido(cursor.getInt(1));
            pagamento.setMet_pagamento(cursor.getString(2));
            pagamento.setStatus(cursor.getString(3).equals("true"));
            pagamento.setValor(cursor.getFloat(4));
            pagamento.setIdCupom(cursor.getInt(5));

            cursor.close();
            cursor = null;
            return pagamento;
        }
        return null;
    }
}
