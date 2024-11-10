package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tela_login_projetointegrador.StatusPedido;
import com.example.tela_login_projetointegrador.model.Pedido;
import com.example.tela_login_projetointegrador.model.Pedido_itens;
import com.example.tela_login_projetointegrador.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class PedidoManager {
    SQLiteDatabase db;

    public PedidoManager(SQLiteDatabase db) {
        this.db = db;
    }
    public Pedido getPedido(){
        Cursor cursor = db.rawQuery("SELECT * FROM PEDIDO", null);
        if (cursor.moveToFirst()){
            Pedido pedido = new Pedido();

            pedido.setIdPedido(cursor.getString(0));
            pedido.setData(cursor.getString(1));
            pedido.setIdUsuario(cursor.getInt(2));
            pedido.setStatusPedido(cursor.getString(3));

            cursor.close();
            return pedido;
        }
        return null;
    }

    /*
      public boolean compararSenha(String email, String senha){
        Cursor cursor = db.rawQuery("SELECT hash_senha, salt FROM USUARIO WHERE email = ?",
                new String[]{email});
     */

    public List<Pedido> getPedidoByStatus(StatusPedido statusPedido){
        Cursor cursor = db.rawQuery("SELECT * FROM PEDIDO WHERE statusPedido= ?", new String[]{String.valueOf(statusPedido)});

        List<Pedido> pedidosList = new ArrayList<>();
        if (cursor.moveToNext()){
            Pedido pedido = new Pedido();
            pedido.setIdPedido(cursor.getString(0));
            pedido.setData(cursor.getString(1));
            pedido.setIdUsuario(cursor.getInt(2));
            pedido.setStatusPedido(cursor.getString(3));
            pedidosList.add(pedido);

        }
        cursor.close();
        return pedidosList;
    }



    public long cadastrarPedido(Pedido pedido) {
        ContentValues values = new ContentValues();
        values.put("data", pedido.getData());
        values.put("idUsuario",1);
        values.put("statusPedido", pedido.getStatusPedido());

        return db.insert("PEDIDO", null, values);
    }
}
