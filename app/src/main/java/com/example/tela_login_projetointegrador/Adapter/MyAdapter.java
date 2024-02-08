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

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Product> produtos;
    List<CartProducts> products;
    public MyAdapter(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }
    public MyAdapter(List<CartProducts> products) {
        this.context = context;
        this.products = products;
    }
    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate
                (R.layout.products_itens, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imageView.setImageResource(produtos.get(position).getFoto());
        holder.nameView.setText(produtos.get(position).getNome());
        holder.descriptionView.setText(produtos.get(position).getDescricao());
        holder.priceView.setText(produtos.get(position).getPreco());

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}
