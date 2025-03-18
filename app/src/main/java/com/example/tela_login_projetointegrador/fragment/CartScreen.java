    package com.example.tela_login_projetointegrador.fragment;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import android.os.Bundle;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import com.example.tela_login_projetointegrador.Adapter.MyCartAdapter;
    import com.example.tela_login_projetointegrador.R;
    import com.example.tela_login_projetointegrador.model.CartProducts;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
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

            myCartAdapter = new MyCartAdapter(requireContext(), cartProductsList);
            recyclerView.setAdapter(myCartAdapter);
            carregarProdutosCarrinho();
            return view;
        }

        private void carregarProdutosCarrinho() {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getUid() == null){
                Log.e("Firebase", "Usuário não autenticado");
                return;
            }
            String userID = auth.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carrinho").child(userID);

            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cartProductsList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String idProduto = dataSnapshot.getKey();
                        String titulo = dataSnapshot.child("nome").getValue(String.class);
                        Double preco = dataSnapshot.child("preco").getValue(Double.class);
                        Long quantidade = dataSnapshot.child("quantidade").getValue(Long.class);

                        if (titulo != null && preco != null && quantidade != null) {
                            cartProductsList.add(new CartProducts(idProduto, titulo, preco.floatValue(), quantidade.intValue()));
                        }
                    }
                    if (myCartAdapter != null) {
                        myCartAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Erro ao carregar carrinho", error.toException());
                }
            });
        }
        public static CartScreen newInstance() {
            return new CartScreen();
        }
    }