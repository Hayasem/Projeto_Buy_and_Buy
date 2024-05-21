package com.example.tela_login_projetointegrador.backendactivitys;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.ProductManager;
import com.example.tela_login_projetointegrador.model.Produto;
import com.example.tela_login_projetointegrador.model.Telefone;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

public class TelaCadastroProdutos extends AppCompatActivity {
    private FormCadastro cf;
    private ProductManager productManager;

    private EditText edit_titulo_produto, edit_preco_produto, edit_categoria_produto,
            edit_descricao_produto, edit_quantidade_produto;
    private Button btn_confirmar_cadastro_produto;

    String[] mensagens = {"Preencha todos os campos!", "Produto Cadastrado com sucesso!", "Insira uma disponibilidade válida!"};

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cadastro_de_produtos);
        iniciarComponentes();

        DatabaseConnection databaseConnection = new DatabaseConnection(this);
        SQLiteDatabase db = databaseConnection.getWritableDatabase();
        productManager = new ProductManager(db);

        btn_confirmar_cadastro_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = edit_titulo_produto.getText().toString();
                String descricao = edit_descricao_produto.getText().toString();
                String preco = edit_preco_produto.getText().toString();
                String categoria = edit_categoria_produto.getText().toString();
                String quantidade  = edit_quantidade_produto.getText().toString();

                if (titulo.isEmpty() || descricao.isEmpty() || preco.isEmpty() || categoria.isEmpty() || quantidade.isEmpty()) {
                    exibirSnackbar(mensagens[0], v);

                } else if ("0".equals(quantidade)) {
                    exibirSnackbar(mensagens[2], v);

                } else{
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GREEN);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                    int quantidadeInt = Integer.parseInt(quantidade);
                    int status = quantidadeInt >= 1 ? 1 : 0;

                    Produto produto = new Produto();
                    produto.setTitulo(titulo);
                    produto.setDescricao(descricao);
                    produto.setPreco(Float.parseFloat(preco));
                    produto.setIdCategoria(Integer.parseInt(categoria));
                    produto.setQuantidade(Integer.parseInt(quantidade));
                    produto.setStatus(status);


                   long produtoId = productManager.cadastrarProduto(produto);
                    if (produtoId != -1){
                        Log.i("Cadastro de produto realizado com sucesso!", "O produto foi cadastrado!");
                    }
                }
            }
        });
    }
    private void exibirSnackbar(String erro, View view) {
        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }
    private void iniciarComponentes(){
        edit_titulo_produto = findViewById(R.id.edit_nome_produto);
        edit_preco_produto = findViewById(R.id.edit_preco_produto);
        edit_categoria_produto = findViewById(R.id.edit_categoria_produto);
        edit_descricao_produto = findViewById(R.id.edit_descricao_produto);
        edit_quantidade_produto = findViewById(R.id.edit_quantidade_produto);
        btn_confirmar_cadastro_produto = findViewById(R.id.btn_confirmar_cadastro_produto);
    }
}
