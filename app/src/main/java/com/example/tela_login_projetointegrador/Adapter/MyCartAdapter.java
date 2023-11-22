package com.example.tela_login_projetointegrador.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.CartProducts;
import com.example.tela_login_projetointegrador.model.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyViewCartHolder> {
    Context context;
    List<CartProducts> products;
    public MyCartAdapter(Context context, List<CartProducts> products) {
        this.context = context;
        this.products = products;
    }
    @NonNull
    @NotNull
    @Override
    public MyViewCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewCartHolder(LayoutInflater.from(context).inflate
                (R.layout.products_cart_itens, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewCartHolder holder, int position) {
        holder.imageView.setImageResource(products.get(position).getFoto());
        holder.nameView.setText(products.get(position).getNome());
        holder.plusView.setImageResource(products.get(position).getMaisIcone());
        holder.minusView.setImageResource(products.get(position).getMenosIcone());
        holder.quantityView.setText(products.get(position).getQuantidade());
        holder.priceView.setText(products.get(position).getPreco());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
