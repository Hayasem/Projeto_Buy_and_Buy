package com.example.tela_login_projetointegrador;

import com.example.tela_login_projetointegrador.model.Produto;

import java.util.List;

public interface ProdutosInterface {
    void getProdutos(List<Produto> produtos);

    void onProdutoSelected(Produto produto);
}
