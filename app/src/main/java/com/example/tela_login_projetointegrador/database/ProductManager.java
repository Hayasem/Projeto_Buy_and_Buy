package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tela_login_projetointegrador.model.Produto;

public class ProductManager {
    private SQLiteDatabase db;

    public ProductManager(SQLiteDatabase db) {
        this.db = db;
    }

    public Produto getProdutos() {

        Cursor cursor = db.rawQuery("SELECT * FROM PRODUTO", null);

        if(cursor.moveToFirst()){

            Produto produtos = new Produto();

            produtos.setIdProduto(cursor.getInt(0));
            produtos.setIdUsuario(cursor.getInt(1));
            produtos.setTitulo(cursor.getString(2));
            produtos.setDescricao(cursor.getString(3));
            produtos.setIdCategoria(cursor.getInt(4));
            produtos.setPreco(cursor.getFloat(5));
            produtos.setStatus(cursor.getInt(6));

            cursor.close();
            cursor = null;
            return produtos;
        }

        return  null;
    }
    public void cadastrarProduto(Produto produto) {
        ContentValues values = new ContentValues();
        values.put("titulo", produto.getTitulo());
        values.put("descricao", produto.getDescricao());
        values.put("idCategoria", produto.getIdCategoria());
        values.put("preco", produto.getPreco());
        values.put("status", produto.isStatus());

        long idProduto = db.insert("PRODUTO", null, values);
    }

}
