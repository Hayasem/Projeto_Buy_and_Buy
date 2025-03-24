package com.example.tela_login_projetointegrador.activities;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tela_login_projetointegrador.Adapters.ProdutosAdapter;
import com.example.tela_login_projetointegrador.listeners.CartListener;
import com.example.tela_login_projetointegrador.listeners.ProductsListener;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.fragments.CartFragment;
import com.example.tela_login_projetointegrador.fragments.FragmentProdutoDetalhe;
import com.example.tela_login_projetointegrador.models.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment implements ProductsListener {
    private GridView gvProdutos;
    private Parcelable gridViewState;
    private final List<Produto> listProdutos = new ArrayList<>();
    private ProdutosAdapter produtosAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_screen, container, false);

        gvProdutos = view.findViewById(R.id.gridView_products);
        produtosAdapter = new ProdutosAdapter(getContext(), R.layout.products_itens, listProdutos,
                this, requireActivity().getSupportFragmentManager());
        gvProdutos.setAdapter(produtosAdapter);

        carregarProdutos();
        return view;
    }
        private void carregarProdutos() {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String userId = auth.getCurrentUser().getUid();
            DatabaseReference productsDataRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId).child("produtos");
            productsDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listProdutos.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Produto produto = dataSnapshot.getValue(Produto.class);
                        if (produto != null) {
                            listProdutos.add(produto);
                        }
                    }
                    produtosAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });
        }

        public void onPause () {
            super.onPause();
            if (gvProdutos != null) {
                gridViewState = gvProdutos.onSaveInstanceState();
            }
        }
        @Override
        public void onSaveInstanceState (@NonNull Bundle outState){
            super.onSaveInstanceState(outState);
            if (gvProdutos != null) {
                outState.putParcelable("gridState", gvProdutos.onSaveInstanceState());
            }
        }
        @Override
        public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            if (savedInstanceState != null) {
                gridViewState = savedInstanceState.getParcelable("gridState");
            }
            getParentFragmentManager().setFragmentResultListener("carrinho_atualizado", this, (requestKey, result) -> {
                // Substituir a tela do carrinho por uma nova versão atualizada
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentProdutos, new CartFragment());
                transaction.commit();
            });
        }

        public void onResume () {
            super.onResume();
            GridView gridView = getView().findViewById(R.id.gridView_products);
            if (gridView != null && gridViewState != null) {
                gridView.onRestoreInstanceState(gridViewState);
            }
        }
        public static HomeActivity newInstance () {
            return new HomeActivity();
        }

        @Override
        public void getProdutos (List <Produto> produtos) {
        listProdutos.clear();
        listProdutos.addAll(produtos);
        produtosAdapter.notifyDataSetChanged();
        }

        @Override
        public void onProdutoSelected (Produto produto){
            Fragment produtoDetalheFragment = new FragmentProdutoDetalhe();
            Bundle args = new Bundle();
            args.putSerializable("produto", produto);
            produtoDetalheFragment.setArguments(args);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentProdutos, produtoDetalheFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }