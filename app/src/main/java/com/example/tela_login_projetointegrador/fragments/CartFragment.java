package com.example.tela_login_projetointegrador.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.Adapters.CartAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.activities.HomeActivity;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private ImageView trashIcon, iv_bag, iv_left_arrow, iv_empty_state;
    private Button btn_comprar_mais, btn_finalizar_compra;
    private TextView tv_kart_prompt, tvTotalCompra, tv_empty_state_msg;
    private CartAdapter cartAdapter;
    private RecyclerView rvProductsInCart;
    private double totalCompra = 0.0;
    private final List<ProdutosCarrinho> produtosCarrinhoList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_screen, container, false);
        tvTotalCompra = view.findViewById(R.id.tv_total);
        trashIcon = view.findViewById(R.id.trash_icon);
        iv_bag = view.findViewById(R.id.iv_bag);
        rvProductsInCart = view.findViewById(R.id.recyclerView_CartProducts);
        iv_left_arrow = view.findViewById(R.id.iv_left_arrow);
        btn_comprar_mais = view.findViewById(R.id.btn_comprar_mais);
        btn_finalizar_compra = view.findViewById(R.id.btn_finalizar_compra);
        tv_kart_prompt = view.findViewById(R.id.tv_kart_prompt);
        iv_empty_state = view.findViewById(R.id.iv_empty_state);
        tv_empty_state_msg = view.findViewById(R.id.tv_empty_state_msg);



        rvProductsInCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductsInCart.setHasFixedSize(true);
        cartAdapter = new CartAdapter(produtosCarrinhoList, tvTotalCompra);
        rvProductsInCart.setAdapter(cartAdapter);

        trashIcon.setOnClickListener(v ->{
            removerDoCarrinho();
        });

        btn_finalizar_compra.setOnClickListener(v ->{
            navegarFinalizarCompra();
        });
        btn_comprar_mais.setOnClickListener(v ->{
            navegarParaProdutos();
        });

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        carregarCarrinho(userID);
        return view;
    }
    private void carregarCarrinho(String usuarioID) {
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference carrinhoRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(usuarioID)
                .child("carrinho");

        carrinhoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalCompra = 0.0;
                List<ProdutosCarrinho> listaCarrinho = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProdutosCarrinho item = dataSnapshot.getValue(ProdutosCarrinho.class);
                    if (item != null){
                        listaCarrinho.add(item);
                        totalCompra += item.getPreco() * item.getQuantidade();
                    }
                }
                if (cartAdapter == null) {
                    cartAdapter = new CartAdapter(listaCarrinho, tvTotalCompra);
                    rvProductsInCart.setAdapter(cartAdapter);
                } else {
                    cartAdapter.atualizarLista(listaCarrinho);
                }

                tvTotalCompra.setText(String.format("R$ %.2f", totalCompra));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erro ao carregar carrinho!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarEmptyState() {
        rvProductsInCart.setVisibility(View.GONE);
        tv_kart_prompt.setVisibility(View.GONE);
        tvTotalCompra.setVisibility(View.GONE);
        trashIcon.setVisibility(View.GONE);
        btn_finalizar_compra.setVisibility(View.GONE);
        iv_bag.setVisibility(View.GONE);

        iv_empty_state.setVisibility(View.VISIBLE);
        tv_empty_state_msg.setVisibility(View.VISIBLE);
    }
    private void navegarFinalizarCompra(){
        Fragment fragment = new PagamentoFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
        );
        transaction.replace(R.id.fragmentProdutos, fragment); // container = FrameLayout no layout da Activity
        transaction.addToBackStack(null); // permite voltar com o botão "voltar"
        transaction.commit();
    }
    private void removerDoCarrinho(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();

        if (auth.getCurrentUser() == null) {
            Log.e("Firebase", "Usuário não autenticado ao remover do carrinho");
            return;
        }

        assert userId != null;
        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(userId)
                .child("carrinho");

        cartRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    produtosCarrinhoList.clear();
                    cartAdapter.atualizarLista(produtosCarrinhoList);
                    rvProductsInCart.postDelayed(() -> mostrarEmptyState(), 300);
                    Toast.makeText(getContext(), "Carrinho esvaziado", Toast.LENGTH_SHORT).show();
                    Log.d("Firebase", "Todos os produtos foram removidos com sucesso.");
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Erro ao remover todos os produtos", e));
    }
    private void navegarParaProdutos() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }



}