package com.example.tela_login_projetointegrador.backendactivitys;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tela_login_projetointegrador.Adapter.ProdutosAdapter;
import com.example.tela_login_projetointegrador.ProdutosInterface;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.ProductManager;
import com.example.tela_login_projetointegrador.fragment.FragmentProdutoDetalhe;
import com.example.tela_login_projetointegrador.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends Fragment implements ProdutosInterface {

    private ImageButton bt_perfil;
    private ImageButton bt_notificacao;
    private ListView lvProdutos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_screen, container, false);

        lvProdutos = view.findViewById(R.id.listView_products);
        final List<Produto> listProdutos = new ArrayList<>();
        ProdutosAdapter produtosAdapter = new ProdutosAdapter(getContext(), R.layout.products_itens, listProdutos,
                this, requireActivity().getSupportFragmentManager());
        lvProdutos.setAdapter(produtosAdapter);

            DatabaseReference productsDataRef = FirebaseDatabase.getInstance().getReference("produtos");
            productsDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

        return view;
    }

    public static MenuScreen newInstance(){
        return new MenuScreen();
    }

    @Override
    public void getProdutos(List<Produto> produtos) {
        ProdutosAdapter produtosAdapter = new ProdutosAdapter(getContext(),R.layout.products_itens,produtos,this,requireActivity().getSupportFragmentManager());
        lvProdutos.setAdapter(produtosAdapter);
    }

    @Override
    public void onProdutoSelected(Produto produto) {
        Fragment produtoDetalheFragment = new FragmentProdutoDetalhe();
        Bundle args = new Bundle();
        args.putSerializable("produto", produto);
        produtoDetalheFragment.setArguments(args);

        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentProdutos, produtoDetalheFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}