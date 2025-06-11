package com.example.tela_login_projetointegrador.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
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
import com.bumptech.glide.util.Util;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;
import com.example.tela_login_projetointegrador.services.MercadoPagoService;
import com.example.tela_login_projetointegrador.utils.BaseFragment;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class PagamentoFragment extends BaseFragment {
    private LinearLayout containerProdutos;
    private final List<ProdutosCarrinho> listaCarrinho = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pagamento, container, false); // Troque pelo seu layout

        containerProdutos = view.findViewById(R.id.containerProdutos);
        Button btnFazerPedido = view.findViewById(R.id.btnFazerPedido);



        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(usuarioID);



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
                    txtEndCompleto.setText("Campo Grande, MS - " + cep + "\n"+cpf+ "\n" + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
            }
        });



        DatabaseReference carrinhoRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(usuarioID)
                .child("carrinho");

        carrinhoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalCompra = 0.0;


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
                    txtPreco.setText(String.format("R$ %.2f", produto.getPreco()));
//                    txtPreco.setText("R$ "+ produto.getPreco().toString().replaceAll("\\.", ",") +" ");
                    ImageView imgProduto = itemView.findViewById(R.id.imgProduto);
                    Glide.with(requireContext())
                            .load(produto.getImageUrl())
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
            //String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            carregarCarrinhoDoUsuario(usuarioID, () -> {
                MercadoPagoService mercadoPago = new MercadoPagoService(listaCarrinho,getContext() );
                mercadoPago.criarPreferencia(initPoint -> {
                    // Redireciona o usuário para o navegador
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(initPoint));
                    startActivity(browserIntent);

                    iniciarLoopVerificacao(initPoint);
                });
            });

            // Remove navegação imediata! Agora só deve acontecer após pagamento
        });

        return view;
    }

    private void iniciarLoopVerificacao(String preferenceId) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable loop = new Runnable() {
            @Override
            public void run() {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    String json = MercadoPagoService.verificarStatusPagamento(preferenceId);

                    if (json != null && json.contains("\"status\":\"approved\"")) {
                        handler.post(() -> {


                            deletarCarrinho();
                            navegar(FragmentPagamentoDebito.class); // ou outro fragment
                        });
                    } else {
                        // Se não aprovado, verifica novamente em 5 segundos
                        handler.postDelayed(this, 5000);
                    }
                });
            }
        };
        handler.postDelayed(loop, 5000); // inicia após 5 segundos
    }


    private void carregarCarrinhoDoUsuario(String usuarioID, Runnable callback) {
        DatabaseReference carrinhoRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(usuarioID)
                .child("carrinho");

        listaCarrinho.clear();

        carrinhoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ProdutosCarrinho item = itemSnapshot.getValue(ProdutosCarrinho.class);
                    if (item != null) {
                        listaCarrinho.add(item);
                    }
                }

                if (callback != null) callback.run(); // executa algo após carregar (ex: chamada para MercadoPago)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erro ao carregar o carrinho do usuário.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletarCarrinho() {
        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference carrinhoRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(usuarioID)
                .child("carrinho");

        carrinhoRef.removeValue().addOnCompleteListener(task -> {
        });
    }

    private void navegar(Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.getDeclaredConstructor().newInstance();

            if (isAdded() && !isDetached() && !isRemoving()) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentProdutos, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss(); // <= usa método seguro para pós onSaveInstanceState
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao navegar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}