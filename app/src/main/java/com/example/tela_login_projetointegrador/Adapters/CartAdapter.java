package com.example.tela_login_projetointegrador.Adapters;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
    private TextView totalPriceView;


    public CartAdapter(List<ProdutosCarrinho> listaProdutosCarrinho, TextView totalPriceView) {
        this.listaProdutosCarrinho = listaProdutosCarrinho;
        this.totalPriceView = totalPriceView;

    }

    public void atualizarLista(List<ProdutosCarrinho> novaLista){
        this.listaProdutosCarrinho = novaLista;
        notifyDataSetChanged();
    }


    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public MyViewCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewCartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_itens_layout, parent, false));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewCartHolder holder, int position) {
        ProdutosCarrinho produtoNoCarrinho = listaProdutosCarrinho.get(position);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(produtoNoCarrinho.getIdUsuario());
        userRef.child("nome").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nomeVendedor = snapshot.getValue(String.class);
                    holder.nameSeller.setText(nomeVendedor);
                }else{
                    holder.nameSeller.setText("Vendedor desconhecido");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                holder.nameSeller.setText("Erro ao carregar vendedor");
            }
        });

        holder.nameSeller.setText(produtoNoCarrinho.getNomeVendedor());
        holder.nameView.setText(produtoNoCarrinho.getNomeProduto());
        holder.priceView.setText(String.format("%.2f", produtoNoCarrinho.getPreco()));
        holder.quantityView.setText(String.valueOf(produtoNoCarrinho.getQuantidade()));
        Glide.with(holder.itemView.getContext())
                .load(produtoNoCarrinho.getimagemUrl())
                .placeholder(R.drawable.img_backpack)
                .into(holder.imageView);

        holder.plusView.setOnClickListener(v -> {
            adicionarProduto(produtoNoCarrinho);
        });
        holder.minusView.setOnClickListener(v -> {
            removerProduto(produtoNoCarrinho, v.getContext());
        });
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
    private void removerProduto(ProdutosCarrinho produto, Context context) {
        if (produto.getQuantidade() == 1) {
            // Exibir alerta antes de remover o item do carrinho
            exibirAlertaRemocaoCarrinho(produto, context);
        } else {
            // Se a quantidade for maior que 1, apenas diminui normalmente
            int novaQuantidade = produto.getQuantidade() - 1;
            atualizarQuantidade(produto, novaQuantidade);
        }
    }

    public void     exibirAlertaRemocaoCarrinho(ProdutosCarrinho produto, Context context){
        new AlertDialog.Builder(context)
                .setTitle("Remover item")
                .setMessage("Você deseja remover esse item do carrinho?")
                .setPositiveButton("Sim", ((dialog, which) -> removerProdutoDoCarrinho(produto)))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
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
                .child((product.getIdCarrinho()));

        cartRef.child("quantidade").setValue(novaQuantidade)
                .addOnSuccessListener(aVoid -> {
                    int position = listaProdutosCarrinho.indexOf(product);
                    if (position != -1) {
                        product.setQuantidade(novaQuantidade);
                        notifyItemChanged(position); // Notifica o RecyclerView da alteração
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
                .child(product.getIdCarrinho());

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
        double totalCarrinho = 0.0;
        for (ProdutosCarrinho produto : listaProdutosCarrinho) {
            totalCarrinho += produto.getPreco() * produto.getQuantidade();
        }
        if (totalPriceView != null) {
            if (listaProdutosCarrinho.isEmpty()) {
                totalPriceView.setText("Carrinho vazio");
            } else {
                totalPriceView.setText(String.format("R$ %.2f", totalCarrinho));
            }
        }
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