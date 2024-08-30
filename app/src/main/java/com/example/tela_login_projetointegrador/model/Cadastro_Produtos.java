    package com.example.tela_login_projetointegrador.model;

    public class Cadastro_Produtos {
        private String tituloProduto;
        private String precoProduto;
        private String categoriaProduto;
        private String descricaoProduto;

       private boolean disponivelProduto;

        public Cadastro_Produtos(String tituloProduto, String precoProduto, String categoriaProduto, String descricaoProduto, boolean disponivelProduto) {
            this.tituloProduto = tituloProduto;
            this.precoProduto = precoProduto;
            this.categoriaProduto = categoriaProduto;
            this.descricaoProduto = descricaoProduto;
            this.disponivelProduto = disponivelProduto;
        }
        public Cadastro_Produtos(){

        }
        public String getTituloProduto() {
            return tituloProduto;
        }

        public void setTituloProduto(String tituloProduto) {
            this.tituloProduto = tituloProduto;
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
    }
