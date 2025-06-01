package com.example.tela_login_projetointegrador.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable; // Boa prática para passar entre Fragments

public class ProdutosCarrinho implements Serializable {
    String idUsuario, idCarrinho, idProduto, nomeVendedor, nomeProduto, data_add, imageUrl;
    int quantidade;
    Float preco; // O tipo do atributo é Float
    @Exclude // ALTERAÇÃO: Adicionar essa anotação para não salvar no Firebase
    private int estoqueDisponivel; // ALTERAÇÃO: Campo para armazenar o estoque real do produto

    public ProdutosCarrinho(){
        // Construtor vazio essencial para Firebase
    }

    // Construtor completo: Ajustado para usar Float para preco
    public ProdutosCarrinho(String idUsuario, String idCarrinho, String idProduto, String nomeVendedor, String nomeProduto, String data_add, String imageUrl, Float preco, int quantidade) {
        this.idUsuario = idUsuario;
        this.idCarrinho = idCarrinho;
        this.idProduto = idProduto;
        this.nomeVendedor = nomeVendedor;
        this.nomeProduto = nomeProduto;
        this.data_add = data_add;
        this.imageUrl = imageUrl;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    // Construtor usado em FragmentProdutoDetalhe: Ajustado para usar Float para preco
    public ProdutosCarrinho(String idCarrinhoExistente, String idProduto, String nomeProduto, String imageUrl, Float preco, int quantidade, String idUsuario) {
        this.idCarrinho = idCarrinhoExistente;
        this.idProduto = idProduto;
        this.nomeProduto  = nomeProduto;
        this.imageUrl = imageUrl;
        this.quantidade = quantidade;
        this.preco = preco;
        this.idUsuario = idUsuario;
        this.nomeVendedor = null;
    }

    public ProdutosCarrinho(String idUsuario, String idCarrinho, String idProduto, String nomeVendedor, String nomeProduto, String data_add, String imageUrl, int quantidade, Float preco) {
        this.idUsuario = idUsuario;
        this.idCarrinho = idCarrinho;
        this.idProduto = idProduto;
        this.nomeVendedor = nomeVendedor;
        this.nomeProduto = nomeProduto;
        this.data_add = data_add;
        this.imageUrl = imageUrl;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public ProdutosCarrinho(String idCarrinho, String idProduto, String nomeProduto, String imageUrl, Float preco, int quantidade, String idUsuario, String nomeVendedor) {
        this.idCarrinho = idCarrinho;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.imageUrl = imageUrl;
        this.preco = preco;
        this.quantidade = quantidade;
        this.idUsuario = idUsuario;
        this.nomeVendedor = nomeVendedor;
    }
    // --- GETTERS E SETTERS ---

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getIdCarrinho() { return idCarrinho; }
    public void setIdCarrinho(String idCarrinho) { this.idCarrinho = idCarrinho; }

    public String getIdProduto() { return idProduto; }
    public void setIdProduto(String idProduto) { this.idProduto = idProduto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public String getNomeVendedor() { return nomeVendedor; }
    public void setNomeVendedor(String nomeVendedor) { this.nomeVendedor = nomeVendedor; }

    public String getData_add() { return data_add; }
    public void setData_add(String data_add) { this.data_add = data_add; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Float getPreco() { return preco; }
    // Setter de preco: Ajustado para usar Float como parâmetro
    public void setPreco(Float preco) { // <--- CORREÇÃO AQUI
        this.preco = preco;
    }
    // ALTERAÇÃO: Getter e Setter para estoqueDisponivel
    @Exclude // Para que o Firebase não tente serializar este campo
    public int getEstoqueDisponivel() {
        return estoqueDisponivel;
    }

    @Exclude // Para que o Firebase não tente serializar este campo
    public void setEstoqueDisponivel(int estoqueDisponivel) {
        this.estoqueDisponivel = estoqueDisponivel;
    }
}