package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tela_login_projetointegrador.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private SQLiteDatabase db;
    private DatabaseConnection dbHelper; // Adicionamos a conexão correta

    public ProductManager(Context context) {
        dbHelper = new DatabaseConnection(context);
    }

    public Produto getProdutos() {
        db = dbHelper.getReadableDatabase(); // Obtendo o banco corretamente
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUTO", null);

        if (cursor.moveToFirst()) {
            Produto produto = new Produto();
            produto.setIdProduto(cursor.getString(0));
            produto.setIdUsuario(cursor.getString(1));
            produto.setTitulo(cursor.getString(2));
            produto.setDescricao(cursor.getString(3));
            produto.setIdCategoria(cursor.getInt(4));
            produto.setPreco(cursor.getFloat(5));
            produto.setStatus(cursor.getInt(6));
            produto.setImagem(cursor.getString(7));

            cursor.close();
            return produto;
        }
        cursor.close();
        return null;
    }

    public List<Produto> getListProdutos() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUTO", null);

        List<Produto> listProdutos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Produto produto = new Produto();
            produto.setIdProduto(cursor.getString(0));
            produto.setIdUsuario(cursor.getString(1));
            produto.setTitulo(cursor.getString(2));
            produto.setDescricao(cursor.getString(3));
            produto.setIdCategoria(cursor.getInt(4));
            produto.setPreco(cursor.getFloat(5));
            produto.setStatus(cursor.getInt(6));
            produto.setImagem(cursor.getString(7));
            listProdutos.add(produto);
        }

        cursor.close();
        return listProdutos;
    }

    public void cadastrarProduto(Produto produto) {
        db = dbHelper.getWritableDatabase(); // Correção: obtendo banco de escrita
        ContentValues values = new ContentValues();
        values.put("titulo", produto.getTitulo());
        values.put("idUsuario", 1); // Para teste, sempre salvando com ID 1
        values.put("descricao", produto.getDescricao());
        values.put("idCategoria", produto.getIdCategoria());
        values.put("preco", produto.getPreco());
        values.put("status", produto.isStatus());
        values.put("imagem", produto.getImagem());

        long idProduto = db.insert("PRODUTO", null, values);
        System.out.println("Produto cadastrado com ID: " + idProduto);
        db.close(); // Fechando o banco para evitar vazamentos
    }

    // 🔥 Método corrigido para limpar todos os produtos
    public void limparTodosProdutos() {
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM PRODUTO"); // Agora com o nome correto da tabela
        db.close();
    }
}
