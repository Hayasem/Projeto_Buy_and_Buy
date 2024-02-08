package com.example.tela_login_projetointegrador.backendactivitys;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.Adapter.MyCartAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.CartProducts;



import java.util.ArrayList;
import java.util.List;

public class CartScreen extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_screen, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_CartProducts);
        List<CartProducts> cartProducts = carregaDados();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyCartAdapter(getContext(), cartProducts));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    public static CartScreen newInstance() {
        return new CartScreen();
    }
    private List<CartProducts> carregaDados(){

        List<CartProducts> cartProducts = new ArrayList<>();
        CartProducts product1 = new CartProducts(R.drawable.img_backpack,
                "Mochila militar", R.drawable.plus_icon, R.drawable.minus_icon,
                "Quantidade", "R$ 149,99");
        cartProducts.add(product1);

        List<CartProducts> cartProducts2 = new ArrayList<>();
        CartProducts product2 = new CartProducts(R.drawable.camisa_one_piece,
                "Camiseta One Piece", R.drawable.plus_icon, R.drawable.minus_icon,
                "Quantidade", "R$ 59,99");
        cartProducts.add(product2);

        List<CartProducts> cartProducts3 = new ArrayList<>();
        CartProducts product3 = new CartProducts(R.drawable.calca_jeans,
                "Calça Jeans", R.drawable.plus_icon, R.drawable.minus_icon,
                "Quantidade", "R$ 80,00");
        cartProducts.add(product3);

        List<CartProducts> cartProducts4 = new ArrayList<>();
        CartProducts product4 = new CartProducts(R.drawable.bolsa_couro,
                "Bolsa de couro", R.drawable.plus_icon, R.drawable.minus_icon,
                "Quantidade", "R$ 100,00");
        cartProducts.add(product4);

        List<CartProducts> cartProducts5 = new ArrayList<>();
        CartProducts product5 = new CartProducts(R.drawable.img_headset,
                "Headset Gamer", R.drawable.plus_icon, R.drawable.minus_icon,
                "Quantidade", "R$ 300,00");
        cartProducts.add(product5);
        return cartProducts;

    }
}