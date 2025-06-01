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

import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.Locale;

public class CartFragment extends Fragment {
    ConstraintLayout prompt_container,container_valor_total;
    private ImageView trashIcon, iv_bag, iv_left_arrow, iv_empty_state;
    private Button btn_comprar_mais, btn_finalizar_compra;
    private TextView tv_kart_prompt, tvTotalCompra, tv_empty_state_msg;
    private CartAdapter cartAdapter;
    private RecyclerView rvProductsInCart;
    private double totalCompra = 0.0;
    private final List<ProdutosCarrinho> produtosCarrinhoList = new ArrayList<>();
    private DatabaseReference carrinhoRef;
    private ValueEventListener carrinhoEventListener; // Listener para ser removido


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_screen, container, false);
        inicializarComponentes(view);
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        carrinhoRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(userID)
                .child("carrinho");

        rvProductsInCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductsInCart.setHasFixedSize(true);

        Log.d("CartFragment", "New CartAdapter instance created: " + cartAdapter); // Log a instância do adapter
        Log.d("CartFragment", "CartAdapter set to RecyclerView: " + rvProductsInCart.getAdapter()); // Log a instância que o RV tem

        configurarListeners(view);
        return view;
    }
    private void inicializarComponentes(View view) {
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
        prompt_container = view.findViewById(R.id.prompt_container);
        container_valor_total = view.findViewById(R.id.container_valor_total);
    }
    private void configurarListeners(View view) {
        trashIcon.setOnClickListener(v -> removerTodosDoCarrinho());
        btn_finalizar_compra.setOnClickListener(v -> navegarFinalizarCompra());
        btn_comprar_mais.setOnClickListener(v -> navegarParaProdutos());
    }
    @Override
    public void onResume() {
        super.onResume();
        // Anexa o listener de dados quando o fragmento se torna visível
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Garante que carrinhoRef está inicializado antes de anexar o listener
            if (carrinhoRef == null) {
                carrinhoRef = FirebaseDatabase.getInstance()
                        .getReference("usuarios")
                        .child(userID)
                        .child("carrinho");
            }
            anexarCarrinhoListener();
        } else {
            Toast.makeText(getContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            // Opcional: Redirecionar para tela de login
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        // Remove o listener de dados quando o fragmento não está mais visível para evitar vazamentos de memória
        desanexarCarrinhoListener();
    }
    private void anexarCarrinhoListener() {
        if (carrinhoRef != null && carrinhoEventListener == null) { // Evita anexar múltiplas vezes
            carrinhoEventListener = new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    produtosCarrinhoList.clear();
                    double currentTotal = 0.0;

                    Log.d("CartFragment", "onDataChange - Snapshot exists: " + snapshot.exists());
                    Log.d("CartFragment", "onDataChange - Snapshot children count: " + snapshot.getChildrenCount());

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("CartFragment", "Processing child: " + dataSnapshot.getKey());
                        ProdutosCarrinho item = dataSnapshot.getValue(ProdutosCarrinho.class);
                        if (item != null) {
                            produtosCarrinhoList.add(item);
                            currentTotal += item.getPreco() * item.getQuantidade();
                            Log.d("CartFragment", "Added item: " + item.getNomeProduto() + ", Qtd: " + item.getQuantidade() + ", Preco: " + item.getPreco());
                        }else{
                            Log.e("CartFragment", "Failed to parse item for key: " + dataSnapshot.getKey());
                        }
                    }
                    Log.d("CartFragment", "Final productsCarrinhoList size: " + produtosCarrinhoList.size());
                    Log.d("CartFragment", "Final currentTotal: " + currentTotal);

                    if (cartAdapter == null) {
                        // Primeira vez que os dados chegam, inicializa o adapter
                        cartAdapter = new CartAdapter(produtosCarrinhoList, tvTotalCompra);
                        rvProductsInCart.setAdapter(cartAdapter);
                        Log.d("CartFragment", "CartAdapter initialized and set for the first time.");
                    } else {
                        // Dados subsequentes (alterações), apenas atualiza o adapter existente
                        cartAdapter.atualizarLista(produtosCarrinhoList);
                        Log.d("CartFragment", "CartAdapter updated with new data.");
                    }
                    tvTotalCompra.setText(String.format(Locale.getDefault(), "R$ %.2f", currentTotal));

                    rvProductsInCart.post(() -> { // Executa no próximo ciclo de UI
                        verificarEstadoCarrinho();
                        Log.d("CartFragment", "Dados do carrinho atualizados em tempo real (após post).");
                        // Adicione logs para as dimensões do RecyclerView e contagem do Adapter AQUI, dentro do post()
                        Log.d("CartFragment", "RecyclerView width: " + rvProductsInCart.getWidth() + ", height: " + rvProductsInCart.getHeight());
                        if (rvProductsInCart.getAdapter() != null) {
                            Log.d("CartFragment", "Adapter item count (inside post): " + rvProductsInCart.getAdapter().getItemCount());
                        }
                    });

                    Log.d("CartFragment", "Dados do carrinho atualizados em tempo real.");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Erro ao carregar carrinho: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("CartFragment", "Erro ao carregar carrinho do Firebase", error.toException());
                }
            };
            carrinhoRef.addValueEventListener(carrinhoEventListener);
        }
    }
    private void desanexarCarrinhoListener() {
        if (carrinhoRef != null && carrinhoEventListener != null) {
            carrinhoRef.removeEventListener(carrinhoEventListener);
            carrinhoEventListener = null; // Limpa a referência
            Log.d("CartFragment", "Listener do carrinho desanexado.");
        }
    }

    private void verificarEstadoCarrinho() { // Manter o nome para compatibilidade
        if (cartAdapter != null) {
            int itemCount = cartAdapter.getItemCount(); // Obtém a contagem de itens do adapter
            Log.d("CartFragment", "verificarEstadoCarrinho - Adapter item count: " + itemCount);
            if (itemCount == 0) {
                Log.d("CartFragment", "Calling mostrarEmptyState() - Adapter reports EMPTY.");
                mostrarEmptyState();
            } else {
                Log.d("CartFragment", "Calling mostrarCarrinhoCheio() - Adapter reports NOT EMPTY.");
                mostrarCarrinhoCheio();
            }
        } else {
            Log.e("CartFragment", "verificarEstadoCarrinho: cartAdapter is null!");
            // Se o adapter for nulo (acontece em onCreate), assuma carrinho vazio temporariamente
            mostrarEmptyState();
        }
    }

    private void mostrarEmptyState() {
        rvProductsInCart.setVisibility(View.GONE);
        tv_kart_prompt.setVisibility(View.GONE);
        tvTotalCompra.setVisibility(View.GONE);
        trashIcon.setVisibility(View.GONE);
        btn_finalizar_compra.setVisibility(View.GONE);
        iv_bag.setVisibility(View.GONE);
        prompt_container.setVisibility(View.GONE);
        container_valor_total.setVisibility(View.GONE);

        iv_empty_state.setVisibility(View.VISIBLE);
        tv_empty_state_msg.setVisibility(View.VISIBLE);
        btn_comprar_mais.setVisibility(View.VISIBLE);
    }
    private void mostrarCarrinhoCheio() {
        rvProductsInCart.setVisibility(View.VISIBLE);
        tv_kart_prompt.setVisibility(View.VISIBLE);
        tvTotalCompra.setVisibility(View.VISIBLE);
        btn_finalizar_compra.setVisibility(View.VISIBLE);
        trashIcon.setVisibility(View.VISIBLE);
        prompt_container.setVisibility(View.VISIBLE);
        btn_finalizar_compra.setVisibility(View.VISIBLE);
        iv_bag.setVisibility(View.VISIBLE);

        iv_empty_state.setVisibility(View.GONE);
        tv_empty_state_msg.setVisibility(View.GONE);
        btn_comprar_mais.setVisibility(View.VISIBLE);
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
    private void removerTodosDoCarrinho(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (carrinhoRef != null) {
            carrinhoRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Carrinho esvaziado", Toast.LENGTH_SHORT).show();
                        Log.d("Firebase", "Todos os produtos foram removidos com sucesso.");
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Erro ao remover todos os produtos", e));
        } else {
            Log.e("Firebase", "carrinhoRef é nulo ao tentar remover todos os produtos.");
        }
    }
    private void navegarParaProdutos() {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragmentProdutos, new HomeActivity());
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public static CartFragment newInstance() {
        return new CartFragment();
    }

}