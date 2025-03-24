package com.example.tela_login_projetointegrador.listeners;

import com.example.tela_login_projetointegrador.models.Produto;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;

import java.util.List;

public interface CartListener {
    void getProdutosCarrinho(List<ProdutosCarrinho> produtosCarrinhoList);
    void onProdutosCarrinho(String message);
}
