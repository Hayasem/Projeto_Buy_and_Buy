package com.example.tela_login_projetointegrador.Adapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.R;
import org.jetbrains.annotations.NotNull;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameView;
    TextView descriptionView;
    TextView priceView;

    public MyViewHolder(@NotNull View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.img_product);
        nameView = itemView.findViewById(R.id.product_name);
        descriptionView = itemView.findViewById(R.id.product_description);
        priceView = itemView.findViewById(R.id.product_price);
    }
}
