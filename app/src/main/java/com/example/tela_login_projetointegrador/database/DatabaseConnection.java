package com.example.tela_login_projetointegrador.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class DatabaseConnection extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "buyandbuy";

    public DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Método para criar o banco de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        String USUARIO_TABLE = "CREATE TABLE USUARIO(" +
                "idUsuario INTEGER primary key AUTOINCREMENT," +
                "nome TEXT," +
                "cpf TEXT," +
                "dataReg TEXT," +
                "email TEXT)";
        Log.i("test01", "O cliente foi registrado");

        String TELEFONE_TABLE = "CREATE TABLE TELEFONE(" +
                "idTelefone INTEGER primary key AUTOINCREMENT," +
                "numero TEXT," +
                "idUsuario INTEGER," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario))";

        String END_ENTREGA_TABLE = "CREATE TABLE END_ENTREGA(" +
                "idEntrega INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario TEXT," +
                "rua TEXT," +
                "numero INTEGER," +
                "bairro TEXT," +
                "cidade TEXT," +
                "estado TEXT," +
                "pais TEXT," +
                "cep TEXT," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario))";

        String NOTIFICACAO_TABLE = "CREATE TABLE NOTIFICACAO(" +
                "idNotificacao INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario INTEGER," +
                "titulo TEXT," +
                "descricao TEXT," +
                "data_notif TEXT," +
                "visualizado INTEGER," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario))";

        String CUPOM_DESC_TABLE = "CREATE TABLE CUPOM_DESC(" +
                "idCupom INTEGER PRIMARY KEY AUTOINCREMENT," +
                "codCupom TEXT," +
                "idUsuario INTEGER," +
                "valido INTEGER," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario))";

        String CATEGORIA_TABLE = "CREATE TABLE CATEGORIA(" +
                "idCategoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome_categoria TEXT)";

        String PRODUTO_TABLE = "CREATE TABLE PRODUTO(" +
                "idProduto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario INTEGER," +
                "titulo TEXT," +
                "descricao TEXT," +
                "idCategoria INTEGER," +
                "preco REAL," +
                "status INTEGER," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario))";

        String AVALIACAO_TABLE = "CREATE TABLE AVALIACAO(" +
                "idAvaliacao INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario INTEGER," +
                "idProduto INTEGER," +
                "avaliacao INTEGER," +
                "comentario TEXT," +
                "dataAval TEXT," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario)," +
                "FOREIGN KEY (idProduto) REFERENCES PRODUTO(idProduto))";

        String PEDIDO_TABLE = "CREATE TABLE PEDIDO(" +
                "idPedido INTEGER PRIMARY KEY AUTOINCREMENT," +
                "data TEXT," +
                "valorTotal REAL," +
                "idProduto INTEGER," +
                "idUsuario INTEGER," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario)," +
                "FOREIGN KEY (idProduto) REFERENCES PRODUTO(idProduto))";

        String CARRINHO_TABLE = "CREATE TABLE CARRINHO(" +
                "idCarrinho INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idProduto INTEGER," +
                "idUsuario INTEGER," +
                "idPedido INTEGER," +
                "preco_uni REAL," +
                "total REAL," +
                "data_add TEXT," +
                "noCarrinho INTEGER," +
                "FOREIGN KEY (idProduto) REFERENCES PRODUTO(idProduto)," +
                "FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario)," +
                "FOREIGN KEY (idPedido) REFERENCES PEDIDO(idPedido))";

        String PAGAMENTO_TABLE = "CREATE TABLE PAGAMENTO(" +
                "idPagamento INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idPedido INTEGER," +
                "met_pagamento TEXT," +
                "status INTEGER," +
                "valor REAL," +
                "idCupom INTEGER," +
                "FOREIGN KEY (idPedido) REFERENCES PEDIDO(idPedido)," +
                "FOREIGN KEY (idCupom) REFERENCES CUPOM(idCupom))";

        String PEDIDO_ITENS_TABLE = "CREATE TABLE PEDIDO_ITENS(" +
                "idItensPedidos INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idPedido INTEGER," +
                "data TEXT," +
                "valorUnit REAL," +
                "idProduto INTEGER," +
                "quantidade INTEGER," +
                "FOREIGN KEY (idProduto) REFERENCES PRODUTO(idProduto)," +
                "FOREIGN KEY (idPedido) REFERENCES PEDIDO(idPedido))";

        db.execSQL(PEDIDO_ITENS_TABLE);
        db.execSQL(PAGAMENTO_TABLE);
        db.execSQL(PEDIDO_TABLE);
        db.execSQL(CARRINHO_TABLE);
        db.execSQL(PRODUTO_TABLE);
        db.execSQL(CATEGORIA_TABLE);
        db.execSQL(USUARIO_TABLE);
        db.execSQL(TELEFONE_TABLE);
        db.execSQL(END_ENTREGA_TABLE);
        db.execSQL(NOTIFICACAO_TABLE);
        db.execSQL(CUPOM_DESC_TABLE);
        db.execSQL(AVALIACAO_TABLE);
    }

    //Método para atualizar o banco de dados
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
