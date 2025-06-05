package com.example.tela_login_projetointegrador.Adapters;

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
import android.widget.CheckBox; // Adicione este import para o CheckBox

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy; // Adicione este import
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewCartHolder>{
    private List<ProdutosCarrinho> listaProdutosCarrinho = new ArrayList<>();
    private TextView totalPriceView; // Este TextView será atualizado pelo Fragment agora, não pelo Adapter
    private OnCartItemChangeListener listener; // Nova interface de callback

    // Nova interface de callback para o Fragment
    public interface OnCartItemChangeListener {
        void onQuantityChanged(ProdutosCarrinho item, int newQuantity);
        void onShowAlertDialog(ProdutosCarrinho item, Context context);
    }

    // ALTERAÇÃO: Construtor que recebe a interface de callback
    public CartAdapter(TextView totalPriceView, OnCartItemChangeListener listener) {
        this.totalPriceView = totalPriceView;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewCartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_itens_layout, parent, false));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewCartHolder holder, int position) {
        ProdutosCarrinho item = listaProdutosCarrinho.get(position);

        // Preenche o nome do vendedor (já vem do objeto ProdutosCarrinho)
        holder.nameSeller.setText("Vendedor: " + (item.getNomeVendedor() != null ? item.getNomeVendedor() : "Desconhecido"));

        holder.nameView.setText(item.getNomeProduto());
        // Formate o preço usando NumberFormat para o padrão monetário correto
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.priceView.setText(format.format(item.getPreco()));

        holder.quantityView.setText(String.valueOf(item.getQuantidade()));

        // Carrega a imagem
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Adicione caching
                    .placeholder(R.drawable.img_backpack) // Placeholder padrão
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.img_backpack);
        }

        // Lógica para aumentar a quantidade
        holder.plusView.setOnClickListener(v -> {
            int currentQuantity = item.getQuantidade();
            int newQuantity = currentQuantity + 1;
            int estoqueDisponivel = item.getEstoqueDisponivel(); // Obtém o estoque do objeto ProdutosCarrinho

            if (listener != null) {
                if (newQuantity <= estoqueDisponivel) {
                    listener.onQuantityChanged(item, newQuantity);
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Quantidade máxima em estoque atingida: " + estoqueDisponivel, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lógica para diminuir a quantidade
        holder.minusView.setOnClickListener(v -> {
            int currentQuantity = item.getQuantidade();
            if (listener != null) {
                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;
                    listener.onQuantityChanged(item, newQuantity);
                } else {
                    // Se a quantidade é 1, chama o Fragment para exibir o alerta de remoção
                    listener.onShowAlertDialog(item, v.getContext());
                }
            }
        });

        // Lógica para o botão "Excluir" (exclude_tv)
        if (holder.excludeTv != null) {
            holder.excludeTv.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShowAlertDialog(item, v.getContext());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaProdutosCarrinho.size();
    }

    // Método para atualizar a lista de produtos no adaptador
    public void atualizarLista(List<ProdutosCarrinho> novaLista){
        this.listaProdutosCarrinho.clear();
        this.listaProdutosCarrinho.addAll(novaLista);
    }

    // ViewHolder
    public static class MyViewCartHolder extends RecyclerView.ViewHolder {
        ImageView imageView, plusView, minusView;
        TextView nameView, quantityView, priceView, nameSeller, excludeTv, buyTv;
        CheckBox checkboxProduct; // Adicione o CheckBox aqui

        public MyViewCartHolder(@NonNull View itemView){
            super(itemView);
            // Mapeando os componentes do seu cart_itens_layout.xml
            checkboxProduct = itemView.findViewById(R.id.checkbox_product); // ID do CheckBox
            nameSeller = itemView.findViewById(R.id.seller_name);
            imageView = itemView.findViewById(R.id.img_product);
            nameView = itemView.findViewById(R.id.product_name);
            excludeTv = itemView.findViewById(R.id.exclude_tv); // Botão/TextView "Excluir"
            buyTv = itemView.findViewById(R.id.buy_tv); // Botão/TextView "Comprar"
            minusView = itemView.findViewById(R.id.minus_icon);
            quantityView = itemView.findViewById(R.id.product_quantity);
            plusView = itemView.findViewById(R.id.plus_icon);
            priceView = itemView.findViewById(R.id.product_price);
        }
    }
}