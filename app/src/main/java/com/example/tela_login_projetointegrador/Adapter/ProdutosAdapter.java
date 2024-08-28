package com.example.tela_login_projetointegrador.Adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tela_login_projetointegrador.ProdutosInterface;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.Produto;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class ProdutosAdapter extends ArrayAdapter<Produto> {

    private Context myContext;
    private int myResource;
    private ProdutosInterface produtosInterface;

    public ProdutosAdapter(Context context, int myResource, List<Produto> produtos, ProdutosInterface produtosInterface){
        super(context,myResource,produtos);
        this.myContext = context;
        this.myResource = myResource;
        this.produtosInterface=produtosInterface;
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
            Bitmap bitmap = loadImageFromInternalStorage(produto.getImagem());
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery); // Imagem padrão
            }

            nameView.setText(produto.getTitulo());
            descriptionView.setText(produto.getDescricao());
            priceView.setText(String.valueOf(produto.getPreco()));
        }

        return itemView;
    }

    private Bitmap loadImageFromInternalStorage(String path) {
        try {
            File imageFile = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
