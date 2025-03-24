    package com.example.tela_login_projetointegrador.models;

    public class CadastroProdutos {
        private String idUsuario;
        private String idProdutoCadastrado;
        private String nomeProduto;
        private String precoProduto;
        private String categoriaProduto;
        private String descricaoProduto;
        private int quantidade;

       private boolean disponivelProduto;

        public CadastroProdutos(boolean disponivelProduto, String descricaoProduto, String categoriaProduto, String precoProduto, String nomeProduto, String idProdutoCadastrado, String idUsuario, int quantidade) {
            this.disponivelProduto = disponivelProduto;
            this.descricaoProduto = descricaoProduto;
            this.categoriaProduto = categoriaProduto;
            this.precoProduto = precoProduto;
            this.nomeProduto = nomeProduto;
            this.idProdutoCadastrado = idProdutoCadastrado;
            this.idUsuario = idUsuario;
            this.quantidade = quantidade;
        }

        public CadastroProdutos(){

        }
        public String getNomeProduto() {
            return nomeProduto;
        }

        public void setNomeProduto(String nomeProduto) {
            this.nomeProduto = nomeProduto;
        }

        public String getPrecoProduto() {
            return precoProduto;
        }

        public void setPrecoProduto(String precoProduto) {
            this.precoProduto = precoProduto;
        }

        public String getCategoriaProduto() {
            return categoriaProduto;
        }

        public void setCategoriaProduto(String categoriaProduto) {
            this.categoriaProduto = categoriaProduto;
        }

        public String getDescricaoProduto() {
            return descricaoProduto;
        }

        public void setDescricaoProduto(String descricaoProduto) {
            this.descricaoProduto = descricaoProduto;
        }

        public boolean isDisponivelProduto() {
            return disponivelProduto;
        }

        public void setDisponivelProduto(boolean disponivelProduto) {
            this.disponivelProduto = disponivelProduto;
        }

        public String getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(String idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getIdProdutoCadastrado() {
            return idProdutoCadastrado;
        }

        public void setIdProdutoCadastrado(String idProdutoCadastrado) {
            this.idProdutoCadastrado = idProdutoCadastrado;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }
    }
