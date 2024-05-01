package com.example.tela_login_projetointegrador.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.Cadastro_Produtos;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentCadastroDeProdutos extends Fragment {
    private TextView tituloCadastroProdutos;
    private EditText nomeProduto, precoProduto, categoriaProduto, quantidadeProduto, descricaoProduto;
    private Button BtnconfirmarCadastroProduto;
    private List<Cadastro_Produtos> cadastroProdutosList = new ArrayList<>();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_de_produtos, container, false);
        tituloCadastroProdutos = view.findViewById(R.id.tv_informe_produto);
        nomeProduto = view.findViewById(R.id.edit_nome_produto);
        precoProduto = view.findViewById(R.id.edit_preco_produto);
        categoriaProduto = view.findViewById(R.id.edit_categoria_produto);
        quantidadeProduto = view.findViewById(R.id.edit_quantidade_produto);
        descricaoProduto = view.findViewById(R.id.edit_descricao_produto);
        BtnconfirmarCadastroProduto = view.findViewById(R.id.btn_confirmar_cadastro_produto);

        BtnconfirmarCadastroProduto.setOnClickListener(v -> {
            String tituloProduto = nomeProduto.getText().toString();
            String preco = precoProduto.getText().toString();
            String categoria = categoriaProduto.getText().toString();
            String descricao = descricaoProduto.getText().toString();
            int quantidade = Integer.parseInt(quantidadeProduto.getText().toString());

            if (!tituloProduto.isEmpty() && !preco.isEmpty() && !categoria.isEmpty() && !descricao.isEmpty() && quantidade > 0) {
                Cadastro_Produtos cadastroProdutos = new Cadastro_Produtos(tituloProduto, preco, categoria, descricao, quantidade);
                cadastroProdutosList.add(cadastroProdutos);
                limparCampos();
            }else{
                Snackbar.make(requireView(), "Preencha todos os campos corretamente", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private void limparCampos() {
        nomeProduto.setText("");
        precoProduto.setText("");
        categoriaProduto.setText("");
        quantidadeProduto.setText("");
        descricaoProduto.setText("");
    }

}
