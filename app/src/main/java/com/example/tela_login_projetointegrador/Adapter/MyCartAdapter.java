package com.example.tela_login_projetointegrador.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.CartProducts;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class MyCartAdapter extends RecyclerView.Adapter<MyViewCartHolder> {
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
}

