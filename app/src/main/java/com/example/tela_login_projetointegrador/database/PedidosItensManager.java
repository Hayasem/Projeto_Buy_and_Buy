package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tela_login_projetointegrador.models.PedidoItens;

public class PedidosItensManager {
    SQLiteDatabase db;

    public PedidosItensManager(SQLiteDatabase db) {
        this.db = db;
    }
    public PedidoItens getItensPedidos(){
        Cursor cursor = db.rawQuery("SELECT * FROM PEDIDO_ITENS", null);
        if (cursor.moveToFirst()){
            PedidoItens itens = new PedidoItens();

            itens.setIdItensPedidos(cursor.getInt(0));
            itens.setIdPedido(cursor.getString(1));
            itens.setValorUnit(cursor.getFloat(3));
            itens.setIdProduto(cursor.getString(4));
            itens.setQuantidade(cursor.getInt(5));

            cursor.close();
            return itens;
        }
        return null;
    }


    public void cadastrarItensPedido(PedidoItens pedidoItens) {
        ContentValues values = new ContentValues();
        values.put("idPedido", pedidoItens.getIdPedido());
        values.put("valorUnit", pedidoItens.getValorUnit());
        values.put("idProduto", pedidoItens.getIdProduto());
        values.put("quantidade", pedidoItens.getQuantidade());

        long idItem = db.insert("PEDIDO_ITENS", null, values);
        System.out.println("itemID: "+idItem);
    }
}
