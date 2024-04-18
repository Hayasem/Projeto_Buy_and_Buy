package com.example.tela_login_projetointegrador.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Product;

public class ProductManager {
    private SQLiteDatabase db;

    public ProductManager(SQLiteDatabase db) {
        this.db = db;
    }

    public Product getProdutos() {

        Cursor cursor = db.rawQuery("SELECT * FROM PRODUTO", null);

        if(cursor.moveToFirst()){

            Product produtos = new Product();

            produtos.setIdProduto(cursor.getInt(0));
            produtos.setIdUsuario(cursor.getInt(1));
            produtos.setTitulo(cursor.getString(2));
            produtos.setDescricao(cursor.getString(3));
            produtos.setIdCategoria(cursor.getInt(4));
            produtos.setPreco(cursor.getFloat(5));
            produtos.setStatus(cursor.getString(6).equals("true"));

            cursor.close();
            cursor = null;
            return produtos;
        }

        return  null;
    }
}
