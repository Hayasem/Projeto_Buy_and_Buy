package com.example.tela_login_projetointegrador.backendactivitys;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.PedidoManager;
import com.example.tela_login_projetointegrador.database.PedidosItensManager;
import com.example.tela_login_projetointegrador.model.CartProducts;
import com.example.tela_login_projetointegrador.model.Pedido;
import com.example.tela_login_projetointegrador.model.Pedido_itens;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

public class CartScreen extends Fragment {

    private RecyclerView recyclerView;
    private MyCartAdapter myCartAdapter;
    private final List<CartProducts> cartProductsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_screen, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_CartProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        carregarProdutosCarrinho();

        return view;
    }

    private void carregarProdutosCarrinho() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carrinho")
                .child(auth.getUid());

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartProductsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String idProduto = dataSnapshot.getKey();
                    String titulo = dataSnapshot.child("nome").getValue(String.class);
                    float preco = dataSnapshot.child("preco").getValue(Float.class);
                    int quantidade = dataSnapshot.child("quantidade").getValue(Integer.class);

                    cartProductsList.add(new CartProducts(idProduto, titulo, preco, quantidade));
                }
                myCartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Erro ao carregar carrinho", error.toException());
            }
        });
    }
}