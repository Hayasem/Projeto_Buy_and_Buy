package com.example.tela_login_projetointegrador.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.CartProducts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyViewCartHolder> {
    Context context;
    List<CartProducts> products;

    public MyCartAdapter(Context context, List<CartProducts> products) {
        this.context = context;
        this.products = products;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewCartHolder holder, int position) {
        CartProducts product = products.get(position);

        holder.nameView.setText("Produto: " + product.getNomeProduto()); // Exemplo, use nome real
        holder.priceView.setText("R$ " + product.getPreco_uni());
        holder.quantityView.setText(String.valueOf(product.getQuantidade())); // Exibir quantidade correta

        holder.plusView.setOnClickListener(v -> alterarQuantidade(product,(product.getQuantidade() + 1)));
        holder.minusView.setOnClickListener(v -> {
            if (product.getQuantidade() > 1) {
                alterarQuantidade(product, (product.getQuantidade() - 1));
            }else{
                removerDoCarrinho(product.getIdProduto());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    private void alterarQuantidade(CartProducts product, int novaQuantidade) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e("Firebase", "Usuário não autenticado ao alterar quantidade");
            return;
        }
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carrinho")
                .child(auth.getUid())
                .child(product.getIdProduto());

        if (novaQuantidade > 0) {
            cartRef.child("quantidade").setValue(novaQuantidade)
                    .addOnSuccessListener(aVoid -> {
                        int position = products.indexOf(product);
                        product.setQuantidade(novaQuantidade);
                        notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Erro ao alterar a quantidade", e));
        } else {
            cartRef.removeValue() // Remove do Firebase se a quantidade for 0
            .addOnSuccessListener(aVoid -> {
                // Atualizar localmente o adapter e remover o item
                int position = products.indexOf(product);
                products.remove(position);
                notifyItemRemoved(position);
            })
                    .addOnFailureListener(e -> Log.e("Firebase", "Erro ao remover o item", e));
        }
    }

    @NonNull
    @Override
    public MyViewCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewCartHolder(LayoutInflater.from(context).inflate
                (R.layout.cart_itens_layout, parent, false));
    }

    private void removerDoCarrinho(String idProduto){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e("Firebase", "Usuário não autenticado ao remover do carrinho");
            return;
        }
        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("carrinho")
                .child(auth.getUid())
                .child(idProduto);
        cartRef.removeValue();
    }
}

