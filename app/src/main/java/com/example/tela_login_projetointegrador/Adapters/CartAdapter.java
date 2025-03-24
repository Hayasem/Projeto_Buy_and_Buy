package com.example.tela_login_projetointegrador.Adapters;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.fragments.FragmentProdutoDetalhe;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewCartHolder> {
    private List<ProdutosCarrinho> listaProdutosCarrinho;
    private double totalCarrinho = 0.0;

    public CartAdapter(List<ProdutosCarrinho> listaProdutosCarrinho) {
        this.listaProdutosCarrinho = listaProdutosCarrinho;

    }
    public void atualizarLista(List<ProdutosCarrinho> novaLista){
        this.listaProdutosCarrinho = novaLista;
        notifyDataSetChanged();
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public MyViewCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new MyViewCartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.products_cart_itens, parent, false));
    }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull MyViewCartHolder holder, int position) {
            ProdutosCarrinho produtoNoCarrinho = listaProdutosCarrinho.get(position);



            holder.plusView.setOnClickListener(v -> {
                adicionarProduto(produtoNoCarrinho);
            });
            holder.minusView.setOnClickListener(v -> {
               removerProduto(produtoNoCarrinho);
            });

            holder.nameView.setText(produtoNoCarrinho.getNomeProduto());
            holder.priceView.setText(String.format("%.2f", produtoNoCarrinho.getPreco()));
            holder.quantityView.setText(String.valueOf(produtoNoCarrinho.getQuantidade()));
            Glide.with(holder.itemView.getContext()).load(produtoNoCarrinho.getimagemUrl()).into(holder.imageView);

        }

    @Override
    public int getItemCount() {
        return listaProdutosCarrinho.size();
    }


    private void adicionarProduto(ProdutosCarrinho produto) {
        int novaQuantidade = produto.getQuantidade() + 1;
        atualizarQuantidade(produto, novaQuantidade);
    }

    // Método para diminuir a quantidade do produto no carrinho
    private void removerProduto(ProdutosCarrinho produto) {
        int novaQuantidade = produto.getQuantidade() - 1;
        if (novaQuantidade > 0) {
            atualizarQuantidade(produto, novaQuantidade);
        } else {
            // Remove o produto caso a quantidade chegue a 0
            removerProdutoDoCarrinho(produto);
        }
    }

    private void atualizarQuantidade(ProdutosCarrinho product, int novaQuantidade) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e("Firebase", "Usuário não autenticado ao alterar quantidade");
            return;
        }
            String userId = auth.getUid();
            String productId = product.getIdCarrinho();
            if (userId == null || productId == null) {
                Log.e("Firebase", "ID do usuário ou ID do produto é nulo.");
                return;
            }
        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("carrinho")
                .child(userId)
                .child(productId);

        cartRef.child("quantidade").setValue(novaQuantidade)
                .addOnSuccessListener(aVoid -> {
                    int position = listaProdutosCarrinho.indexOf(product);
                    if (position != -1) {
                        product.setQuantidade(novaQuantidade);
                        notifyItemChanged(position);
                    }
                    calcularTotal();
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Erro ao alterar a quantidade", e));
    }
    private void removerProdutoDoCarrinho(ProdutosCarrinho product) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();
        if (userId == null) {
            Log.e("Firebase", "Usuário não autenticado ao remover produto");
            return;
        }
        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("carrinho")
                .child(userId)
                .child(product.getIdProduto());

        cartRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    int position = listaProdutosCarrinho.indexOf(product);
                    if (position != -1) {
                        listaProdutosCarrinho.remove(position);
                        notifyItemRemoved(position); // Atualiza a lista e notifica a remoção
                    }
                    calcularTotal();
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Erro ao remover o produto", e));
    }
    private void calcularTotal() {
        totalCarrinho = 0.0;
        for (ProdutosCarrinho produto : listaProdutosCarrinho) {
            totalCarrinho += produto.getPreco() * produto.getQuantidade();
        }
        // Atualizar o total na UI, se necessário
        Log.d("TotalCarrinho", "Valor Total: " + totalCarrinho);
    } 
        public static class MyViewCartHolder extends RecyclerView.ViewHolder {
            ImageView imageView, plusView, minusView;
            TextView nameView, quantityView, priceView, nameSeller, excludeTv, buyTv;

            public MyViewCartHolder(@NotNull View itemView){
                super(itemView);
                excludeTv = itemView.findViewById(R.id.exclude_tv);
                buyTv = itemView.findViewById(R.id.buy_tv);
                nameSeller = itemView.findViewById(R.id.seller_name);
                imageView = itemView.findViewById(R.id.img_product);
                nameView = itemView.findViewById(R.id.product_name);
                plusView = itemView.findViewById(R.id.plus_icon);
                minusView = itemView.findViewById(R.id.minus_icon);
                quantityView = itemView.findViewById(R.id.product_quantity);
                priceView = itemView.findViewById(R.id.product_price);
            }
        }
    }
