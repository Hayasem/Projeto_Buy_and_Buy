package com.example.tela_login_projetointegrador.Adapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.R;
import org.jetbrains.annotations.NotNull;

public class MyViewCartHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameView;
    ImageView plusView;
    ImageView minusView;
    TextView quantityView;
    TextView priceView;

    public MyViewCartHolder(@NotNull View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.img_product);
        nameView = itemView.findViewById(R.id.product_name);
        plusView = itemView.findViewById(R.id.plus_icon);
        minusView = itemView.findViewById(R.id.minus_icon);
        quantityView = itemView.findViewById(R.id.product_quantity);
        priceView = itemView.findViewById(R.id.product_price);
    }
}
