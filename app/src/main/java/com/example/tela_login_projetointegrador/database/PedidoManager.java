package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Pedido;

public class PedidoManager {
    SQLiteDatabase db;

    public PedidoManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Pedido getPedido(){
        Cursor cursor = db.rawQuery("SELECT * FROM PEDIDO", null);
        if (cursor.moveToFirst()){
            Pedido pedido = new Pedido();

            pedido.setIdPedido(cursor.getInt(0));
            pedido.setData(cursor.getString(1));
            pedido.setValorTotal(cursor.getFloat(2));
            pedido.setIdProduto(cursor.getInt(3));
            pedido.setIdUsuario(cursor.getInt(4));

            cursor.close();
            cursor = null;
            return pedido;
        }
        return null;
    }
}
