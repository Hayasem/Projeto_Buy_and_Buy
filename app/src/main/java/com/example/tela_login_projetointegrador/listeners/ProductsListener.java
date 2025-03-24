package com.example.tela_login_projetointegrador.listeners;

import com.example.tela_login_projetointegrador.models.Produto;

import java.util.List;

public interface ProductsListener {
    void getProdutos(List<Produto> produtos);

    void onProdutoSelected(Produto produto);
}
