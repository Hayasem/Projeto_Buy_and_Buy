package com.example.tela_login_projetointegrador.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.Produto;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PagamentoFragment extends Fragment {
    private LinearLayout containerProdutos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pagamento, container, false); // Troque pelo seu layout

        containerProdutos = view.findViewById(R.id.containerProdutos);
        Button btnFazerPedido = view.findViewById(R.id.btnFazerPedido);



        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference carrinhoRef = FirebaseDatabase.getInstance().getReference("carrinho").child(usuarioID);
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(usuarioID);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nome = dataSnapshot.child("nome").getValue(String.class);
                    String cep = dataSnapshot.child("cep").getValue(String.class);
                    String cpf = dataSnapshot.child("cpf").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    TextView txtEndereco = view.findViewById(R.id.txtEnderecoid);
                    TextView txtEndCompleto = view.findViewById(R.id.txtEndCompleto);

                    txtEndereco.setText(nome);
                    txtEndCompleto.setText("Campo Grande, MS - " + cep + "\n" + cpf + "\n" + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
            }
        });


        carrinhoRef.addValueEventListener(new ValueEventListener() {
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

                TextView total = view.findViewById(R.id.textTotal);
                total.setText(String.format("R$ %.2f", totalCompra));


                for (ProdutosCarrinho produto : listaCarrinho) {
                    View itemView = inflater.inflate(R.layout.item_produto, containerProdutos, false);

                    TextView txtNome = itemView.findViewById(R.id.txtNome);
                    TextView txtDescricao = itemView.findViewById(R.id.txtDescricao);
                    TextView txtPreco = itemView.findViewById(R.id.txtPreco);


                    txtNome.setText(produto.getNomeProduto());
                    txtDescricao.setText(produto.getNomeProduto());
                    txtPreco.setText("R$ "+ produto.getPreco().toString().replaceAll("\\.", ",") +" ");
                    ImageView imgProduto = itemView.findViewById(R.id.imgProduto);
                    Glide.with(requireContext())
                            .load(produto.getimagemUrl())
                            .placeholder(R.drawable.box_icon)
                            .error(R.drawable.user)
                            .into(imgProduto);

                    containerProdutos.addView(itemView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erro ao carregar carrinho!", Toast.LENGTH_SHORT).show();
            }
        });

        btnFazerPedido.setOnClickListener(v -> {
            deletarCarrinho(usuarioID);
            int selectedId = ((RadioGroup) requireView().findViewById(R.id.radioGroupPagamento)).getCheckedRadioButtonId();
            if (selectedId == R.id.radioPix) {
                navegar(FragmentPix.class);
            } else {
                navegar(FragmentPagamentoDebito.class);
            }
        });

        return view;
    }

    private void deletarCarrinho(String usuarioID) {
        DatabaseReference carrinhoRef = FirebaseDatabase.getInstance().getReference("carrinho").child(usuarioID);

        carrinhoRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Toast.makeText(getContext(), "Carrinho apagado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                /// Toast.makeText(getContext(), "Erro ao apagar o carrinho", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navegar(Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance(); // ou .getDeclaredConstructor().newInstance() para Java 9+
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragmentProdutos, fragment); // container = FrameLayout no layout da Activity
            transaction.addToBackStack(null);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao navegar", Toast.LENGTH_SHORT).show();
        }
    }



}