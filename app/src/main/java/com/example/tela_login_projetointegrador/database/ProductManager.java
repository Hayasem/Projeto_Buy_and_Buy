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

    public Produto consultarProduto(int idProduto) {
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUTO WHERE status = 1", null);
        if(cursor.moveToFirst()){

            Produto produtos = new Produto();
            produtos.setIdProduto(cursor.getInt(0));
            produtos.setIdUsuario(cursor.getInt(1));
            produtos.setTitulo(cursor.getString(2));
            produtos.setDescricao(cursor.getString(3));
            produtos.setIdCategoria(cursor.getInt(4));
            produtos.setPreco(cursor.getFloat(5));
            produtos.setStatus(cursor.getInt(6));
            produtos.setQuantidade(cursor.getInt(7));

            cursor.close();
            cursor = null;

            return produtos;
        }
        return  null;
    }
    public long cadastrarProduto(Produto produto) {
        ContentValues values = new ContentValues();
        values.put("titulo", produto.getTitulo());
        values.put("idUsuario",1); // para questao de teste salvando sempre 1
        values.put("descricao", produto.getDescricao());
        values.put("idCategoria", produto.getIdCategoria());
        values.put("preco", produto.getPreco());
        values.put("status", produto.isStatus());
        values.put("quantidade", produto.getQuantidade());
        return db.insert("PRODUTO", null, values);
    }

    public void removerProduto(){
        ContentValues values = new ContentValues();
        values.put("status", 0);
        db.update("PRODUTO", values, "status = 0", null);
    }

}
