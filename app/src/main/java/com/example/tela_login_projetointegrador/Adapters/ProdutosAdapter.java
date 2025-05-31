package com.example.tela_login_projetointegrador.Adapters;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.tela_login_projetointegrador.listeners.ProductsListener;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.Produto;
import com.example.tela_login_projetointegrador.utils.Utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdutosAdapter extends ArrayAdapter<Produto> {

    private Context myContext;
    private int myResource;
    private ProductsListener productsListener;
    private final FragmentManager fragmentManager;

    public ProdutosAdapter(Context context, int myResource, List<Produto> produtos, ProductsListener productsListener, FragmentManager supportFragmentManager){
        super(context,myResource,produtos);
        this.myContext = context;
        this.myResource = myResource;
        this.productsListener = productsListener;
        this.fragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View itemView, @NonNull ViewGroup parent) {


        if(itemView==null){
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            itemView = inflater.inflate(myResource, parent, false);
        }

        ImageView imageView = itemView.findViewById(R.id.img_product);
        TextView nameView = itemView.findViewById(R.id.product_name);
        TextView descriptionView = itemView.findViewById(R.id.product_description);
        TextView priceView = itemView.findViewById(R.id.product_price);

        Produto produto = getItem(position);

        // Verifique se o produto não é nulo antes de acessar suas propriedades
        if (produto != null) {
            // Aqui você pode carregar a imagem a partir do caminho salvo
            Glide.with(myContext)
                    .load(produto.getImagem())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(imageView);

            nameView.setText(produto.getNomeProduto());
            descriptionView.setText(produto.getDescricao());
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            priceView.setText(format.format(produto.getPreco()));
        }

        itemView.setOnClickListener(v -> {
            Produto produtos = getItem(position);
            if (produtos != null) {
                // Notifique a fragment
                productsListener.onProdutoSelected(produtos);
            }
        });

        return itemView;
    }
}
