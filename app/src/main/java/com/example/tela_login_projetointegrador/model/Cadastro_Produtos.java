    package com.example.tela_login_projetointegrador.model;

    public class Cadastro_Produtos {
        private String tituloProduto;
        private String precoProduto;
        private String categoriaProduto;
        private String descricaoProduto;
        private int imagensProduto;
        private int quantidadeProduto;

        public Cadastro_Produtos(String tituloProduto, String precoProduto,
                                 String categoriaProduto, String descricaoProduto,
                                 int imagensProduto) {
            this.tituloProduto = tituloProduto;
            this.precoProduto = precoProduto;
            this.categoriaProduto = categoriaProduto;
            this.descricaoProduto = descricaoProduto;
            this.imagensProduto = imagensProduto;
            this.quantidadeProduto = quantidadeProduto;
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

        public int getImagensProduto() {
            return imagensProduto;
        }

        public void setImagensProduto(int imagensProduto) {
            this.imagensProduto = imagensProduto;
        }

        public int getQuantidadeProduto() {
            return quantidadeProduto;
        }

        public void setQuantidadeProduto(int quantidadeProduto) {
            this.quantidadeProduto = quantidadeProduto;
        }
    }
