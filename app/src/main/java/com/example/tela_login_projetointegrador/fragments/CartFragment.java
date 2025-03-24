    package com.example.tela_login_projetointegrador.fragments;
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
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import com.example.tela_login_projetointegrador.Adapters.CartAdapter;
    import com.example.tela_login_projetointegrador.R;
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
        private ImageView trashIcon, iv_bag, iv_left_arrow;
        private Button btn_comprar_mais, btn_finalizar_compra;
        private TextView tv_kart_prompt, tvTotalCompra;
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


            rvProductsInCart.setLayoutManager(new LinearLayoutManager(getContext()));
            rvProductsInCart.setHasFixedSize(true);

            cartAdapter = new CartAdapter(produtosCarrinhoList);
            rvProductsInCart.setAdapter(cartAdapter);

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            carregarCarrinho(userID);

            trashIcon.setOnClickListener(v -> removerDoCarrinho());
            return view;
        }

        private void carregarCarrinho(String usuarioID) {
            DatabaseReference carrinhoRef = FirebaseDatabase.getInstance().getReference("carrinho").child(usuarioID);
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
                    cartAdapter.atualizarLista(listaCarrinho);

                    TextView totalView = tvTotalCompra;
                    totalView.setText(String.format("R$ %.2f", totalCompra));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Erro ao carregar carrinho!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void removerDoCarrinho(){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() == null) {
                Log.e("Firebase", "Usuário não autenticado ao remover do carrinho");
                return;
            }
            String userId = auth.getUid();
            if (userId == null) {
                Log.e("Firebase", "ID do usuário é nulo.");
                return;
            }

            DatabaseReference cartRef = FirebaseDatabase.getInstance()
                    .getReference("carrinho")
                    .child(userId);
            cartRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        produtosCarrinhoList.clear();
                        cartAdapter.notifyDataSetChanged();
                        Log.d("Firebase", "Todos os produtos foram removidos com sucesso.");
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Erro ao remover todos os produtos", e));
        }
        public static CartFragment newInstance() {
            return new CartFragment();
        }
    }