package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Pedido_itens;

public class PedidosItensManager {
    SQLiteDatabase db;

    public PedidosItensManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Pedido_itens getItensPedidos(){
        Cursor cursor = db.rawQuery("SELECT * FROM PEDIDO_ITENS", null);
        if (cursor.moveToFirst()){
            Pedido_itens itens = new Pedido_itens();

            itens.setIdItensPedidos(cursor.getInt(0));
            itens.setIdPedido(cursor.getInt(1));
            itens.setData(cursor.getString(2));
            itens.setValorUnit(cursor.getFloat(3));
            itens.setIdProduto(cursor.getInt(4));
            itens.setQuantidade(cursor.getInt(5));

            cursor.close();
            cursor = null;
            return itens;
        }
        return null;
    }
}
