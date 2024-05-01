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
import com.example.tela_login_projetointegrador.Adapter.MyViewCartHolder;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.CartProducts;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

public class CartScreen extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_screen, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_CartProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    public static CartScreen newInstance() {
        return new CartScreen();
    }
}