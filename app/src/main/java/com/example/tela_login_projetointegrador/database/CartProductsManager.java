package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.CartProducts;

public class  CartProductsManager {
    SQLiteDatabase db;

    public CartProductsManager(SQLiteDatabase db) {
        this.db = db;
    }
    public CartProducts getCartProducts(){
        Cursor cursor = db.rawQuery("SELECT * FROM CARRINHO", null);
        if (cursor.moveToFirst()){
            CartProducts carrinho = new CartProducts(idProduto, titulo, preco, quantidade);

            carrinho.setIdCarrinho(cursor.getInt(0));
            carrinho.setIdProduto(cursor.getInt(1));
            carrinho.setIdUsuario(cursor.getInt(2));
            carrinho.setIdPedido(cursor.getInt(3));
            carrinho.setPreco_uni(cursor.getFloat(4));
            carrinho.setTotal(cursor.getFloat(5));
            carrinho.setData_add(cursor.getString(6));
            carrinho.setNoCarrinho(cursor.getString(7).equals("true"));

            cursor.close();
            cursor = null;
            return carrinho;
        }
        return null;
    }
}
