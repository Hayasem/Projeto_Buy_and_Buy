package com.example.tela_login_projetointegrador.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.tela_login_projetointegrador.listeners.OnCartCountChangeListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.Adapters.CartAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.activities.HomeActivity;
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
import java.util.Locale;

public class CartFragment extends Fragment {
    ConstraintLayout prompt_container,container_valor_total;
    private ImageView trashIcon, iv_bag, iv_left_arrow, iv_empty_state;
    private Button btn_comprar_mais, btn_finalizar_compra;
    private TextView tv_kart_prompt, tvTotalCompra, tv_empty_state_msg;
    private CartAdapter cartAdapter;
    private RecyclerView rvProductsInCart;
    private final List<ProdutosCarrinho> produtosCarrinhoList = new ArrayList<>();
    private DatabaseReference carrinhoRef;
    private ValueEventListener carrinhoEventListener; // Listener para ser removido
    private Map<String, Integer> estoqueProdutosGlobais = new HashMap<>();
    private OnCartCountChangeListener cartCountChangeListener; // <--- ADICIONE ESTA LINHA

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCartCountChangeListener) {
            cartCountChangeListener = (OnCartCountChangeListener) context;
            Log.d("CartFragment_LIFECYCLE", "onAttach: Listener de contagem do carrinho anexado.");
        } else {
            // Se a Activity não implementar a interface, isso pode ser um erro ou uma situação esperada
            Log.e("CartFragment_LIFECYCLE", "onAttach: Activity não implementa OnCartCountChangeListener.");
            // Opcional: throw new RuntimeException(context.toString() + " must implement OnCartCountChangeListener");
        }
    }

    // 2. Implementar onDetach para liberar a referência
    @Override
    public void onDetach() {
        super.onDetach();
        cartCountChangeListener = null; // Limpa a referência para evitar vazamentos de memória
        Log.d("CartFragment_LIFECYCLE", "onDetach: Listener de contagem do carrinho desanexado.");
    }

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

        cartAdapter = new CartAdapter(tvTotalCompra, new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged(ProdutosCarrinho item, int newQuantity) {
                // Callback do adaptador quando a quantidade muda
                Log.d("CartFragment", "onQuantityChanged: " + item.getNomeProduto() + " para " + newQuantity);
                Log.d("CartFragment_DEBUG", "onQuantityChanged: " + item.getNomeProduto() + " para " + newQuantity);
                atualizarQuantidadeNoFirebase(item, newQuantity);
            }

            @Override
            public void onShowAlertDialog(ProdutosCarrinho item, Context context) {
                Log.d("CartFragment_DEBUG", "onShowAlertDialog: Exibindo alerta para " + item.getNomeProduto());
                exibirAlertaRemocaoCarrinho(item, context);
            }
        });
        rvProductsInCart.setAdapter(cartAdapter);
        Log.d("CartFragment_LIFECYCLE", "onCreateView: Fragment View Criada e Adapter Setado.");
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
        Log.d("CartFragment_LIFECYCLE", "configurarListeners: Listeners configurados.");
        trashIcon.setOnClickListener(v -> removerTodosDoCarrinho());
        btn_finalizar_compra.setOnClickListener(v -> navegarFinalizarCompra());
        btn_comprar_mais.setOnClickListener(v -> navegarParaProdutos());
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("CartFragment_LIFECYCLE", "onResume: Fragment Resumido.");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Garante que carrinhoRef está inicializado antes de anexar o listener
            if (carrinhoRef == null) {
                carrinhoRef = FirebaseDatabase.getInstance()
                        .getReference("usuarios")
                        .child(userID)
                        .child("carrinho");
                Log.d("CartFragment_LIFECYCLE", "onResume: carrinhoRef inicializado (era nulo).");
            }
            Log.d("CartFragment_LIFECYCLE", "onResume: Chamando carregarEstoqueProdutosGlobais().");
            carregarEstoqueProdutosGlobais();
        } else {
            Toast.makeText(getContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            Log.w("CartFragment_LIFECYCLE", "onResume: Usuário não autenticado.");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("CartFragment_LIFECYCLE", "onPause: Fragment Pausado. Chamando desanexarCarrinhoListener().");
        desanexarCarrinhoListener();
    }
    private void carregarEstoqueProdutosGlobais() {
        Log.d("CartFragment_FIREBASE", "carregarEstoqueProdutosGlobais: Iniciando...");
        DatabaseReference produtosGlobaisRef = FirebaseDatabase.getInstance().getReference("produtos_globais");
        produtosGlobaisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                estoqueProdutosGlobais.clear();
                Log.d("CartFragment_FIREBASE", "carregarEstoqueProdutosGlobais.onDataChange: Snapshot de produtos globais recebido. Count: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Produto produto = dataSnapshot.getValue(Produto.class);
                    if (produto != null && produto.getIdProduto() != null) {
                        estoqueProdutosGlobais.put(produto.getIdProduto(), produto.getQuantidade());
                    }else{
                        Log.w("CartFragment_FIREBASE", "Produto global nulo ou sem ID na leitura: " + (produto != null ? produto.getNomeProduto() : "Nulo"));
                    }
                }
                Log.d("CartFragment", "Estoque de produtos globais carregado: " + estoqueProdutosGlobais.size() + " itens.");
                Log.d("CartFragment_FIREBASE", "Estoque de produtos globais carregado: " + estoqueProdutosGlobais.size() + " itens.");
                Log.d("CartFragment_FIREBASE", "Chamando anexarCarrinhoListener() e configurarListeners().");
                anexarCarrinhoListener();
                configurarListeners(getView());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CartFragment", "Erro ao carregar estoque de produtos globais: " + error.getMessage());
                Log.e("CartFragment_FIREBASE", "Erro ao carregar estoque de produtos globais: " + error.getMessage(), error.toException());
                Toast.makeText(getContext(), "Erro ao carregar estoque: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                // Em caso de erro, ainda tente carregar o carrinho, mas os itens terão estoque 0.
                Log.d("CartFragment_FIREBASE", "Chamando anexarCarrinhoListener() e configurarListeners() APÓS ERRO NO ESTOQUE.");
                anexarCarrinhoListener();
                configurarListeners(getView());
            }
        });
    }
    private void anexarCarrinhoListener() {
        if (carrinhoRef != null && carrinhoEventListener == null) {
            Log.d("CartFragment_FIREBASE", "anexarCarrinhoListener: Anexando ValueEventListener do carrinho...");
            carrinhoEventListener = new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    produtosCarrinhoList.clear();
                    double currentTotal = 0.0;
                    int itemCount = 0;

                    Log.d("CartFragment_FIREBASE", "carrinhoEventListener.onDataChange: Snapshot do carrinho recebido. Children: " + snapshot.getChildrenCount());
                    if (!snapshot.exists()) {
                        Log.d("CartFragment_FIREBASE", "carrinhoEventListener.onDataChange: Caminho do carrinho não existe ou está vazio no Firebase.");
                    }

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("CartFragment_FIREBASE", "Processing carrinho item: " + dataSnapshot.getKey());
                        ProdutosCarrinho item = dataSnapshot.getValue(ProdutosCarrinho.class);
                        if (item == null) {
                            Log.e("CartFragment_FIREBASE", "Falha ao converter DataSnapshot para ProdutosCarrinho para chave: " + dataSnapshot.getKey() + ". Dados brutos: " + dataSnapshot.getValue());
                            continue; // Pula este item se a conversão falhar
                        }
                        if (item.getIdCarrinho() == null) {
                            item.setIdCarrinho(dataSnapshot.getKey());
                            Log.d("CartFragment_FIREBASE", "ID do carrinho definido para item: " + item.getIdCarrinho());
                        }

                        // Logs detalhados do item
                        Log.d("CartFragment_ITEM_DATA", "Nome: " + item.getNomeProduto());
                        Log.d("CartFragment_ITEM_DATA", "Preço: " + item.getPreco());
                        Log.d("CartFragment_ITEM_DATA", "Quantidade: " + item.getQuantidade());
                        Log.d("CartFragment_ITEM_DATA", "URL Imagem: " + item.getImageUrl());
                        Log.d("CartFragment_ITEM_DATA", "ID Produto (para estoque): " + item.getIdProduto());


                        int estoqueDisponivel = estoqueProdutosGlobais.getOrDefault(item.getIdProduto(), 0);
                        item.setEstoqueDisponivel(estoqueDisponivel);
                        Log.d("CartFragment_ITEM_DATA", "Estoque Disponível para " + item.getNomeProduto() + ": " + estoqueDisponivel);


                        produtosCarrinhoList.add(item);
                        currentTotal += (item.getPreco() != null ? item.getPreco() : 0.0f) * item.getQuantidade();
                        itemCount += item.getQuantidade();
                    }

                    Log.d("CartFragment_FIREBASE", "Lista produtosCarrinhoList após loop: " + produtosCarrinhoList.size() + " itens.");
                    cartAdapter.atualizarLista(produtosCarrinhoList);
                    cartAdapter.notifyDataSetChanged();
                    Log.d("CartFragment_FIREBASE", "CartAdapter atualizado e notificado. Total de itens na lista do adapter: " + produtosCarrinhoList.size());

                    tvTotalCompra.setText(String.format(Locale.getDefault(), "R$ %.2f", currentTotal));
                    Log.d("CartFragment_FIREBASE", "Total da compra atualizado para: " + String.format(Locale.getDefault(), "R$ %.2f", currentTotal));

                    verificarEstadoCarrinho();
                    if (cartCountChangeListener != null) {
                        cartCountChangeListener.onCartCountChanged(itemCount); // <--- ADICIONE ESTA LINHA
                        Log.d("CartFragment_BADGE", "onDataChange: Notificando Activity com " + itemCount + " itens.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Erro ao carregar carrinho: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("CartFragment_FIREBASE", "Erro ao carregar carrinho do Firebase: " + error.getMessage(), error.toException());
                    verificarEstadoCarrinho();
                    if (cartCountChangeListener != null) {
                        cartCountChangeListener.onCartCountChanged(0); // Em caso de erro, assume 0 itens no carrinho
                        Log.d("CartFragment_BADGE", "onCancelled: Notificando Activity com 0 itens devido a erro.");
                    }
                }
            };
            carrinhoRef.addValueEventListener(carrinhoEventListener);
        } else if (carrinhoRef == null) {
            Log.e("CartFragment_FIREBASE", "anexarCarrinhoListener: carrinhoRef é nulo, não é possível anexar listener.");
        } else {
            Log.d("CartFragment_FIREBASE", "anexarCarrinhoListener: Listener do carrinho já anexado.");
        }
    }
    private void desanexarCarrinhoListener() {
        if (carrinhoRef != null && carrinhoEventListener != null) {
            carrinhoRef.removeEventListener(carrinhoEventListener);
            carrinhoEventListener = null;
            Log.d("CartFragment_LIFECYCLE", "desanexarCarrinhoListener: Listener do carrinho desanexado.");
        } else {
            Log.d("CartFragment_LIFECYCLE", "desanexarCarrinhoListener: Listener do carrinho já nulo ou carrinhoRef nulo.");
        }
    }

    private void verificarEstadoCarrinho() { // Manter o nome para compatibilidade
        if (produtosCarrinhoList.isEmpty()) {
            Log.d("CartFragment_UI_STATE", "verificarEstadoCarrinho: produtosCarrinhoList está VAZIA. Mostrando empty state.");
            mostrarEmptyState();
        } else {
            Log.d("CartFragment_UI_STATE", "verificarEstadoCarrinho: produtosCarrinhoList NÃO está vazia (" + produtosCarrinhoList.size() + " itens). Mostrando carrinho cheio.");
            mostrarCarrinhoCheio();
        }
    }
    private void removerItemDoCarrinho(ProdutosCarrinho item) {
        if (carrinhoRef != null && item != null && item.getIdCarrinho() != null) {
            carrinhoRef.child(item.getIdCarrinho()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Produto removido do carrinho.", Toast.LENGTH_SHORT).show();
                        // A atualização da UI será tratada pelo ValueEventListener
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Erro ao remover item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("CartFragment", "Erro ao remover item do Firebase: " + e.getMessage());
                    });
        }
    }

    private void atualizarQuantidadeNoFirebase(ProdutosCarrinho item, int newQuantity) {
        if (carrinhoRef != null && item != null && item.getIdCarrinho() != null) {
            // Verifica o estoque disponível ANTES de atualizar no Firebase
            int estoqueDisponivel = estoqueProdutosGlobais.getOrDefault(item.getIdProduto(), 0);

            if (newQuantity <= 0) { // Se a nova quantidade for zero ou negativa, remove o item
                removerItemDoCarrinho(item);
                return;
            }

            if (newQuantity > estoqueDisponivel) {
                Toast.makeText(getContext(), "A quantidade máxima em estoque é " + estoqueDisponivel + ".", Toast.LENGTH_SHORT).show();
                // O adapter precisará reverter a quantidade para o valor anterior ou máximo disponível
                // A melhor forma de lidar com isso é re-renderizar com os dados do Firebase,
                // que não serão alterados se a condição de estoque não for atendida.
                return;
            }

            carrinhoRef.child(item.getIdCarrinho()).child("quantidade").setValue(newQuantity)
                    .addOnSuccessListener(aVoid -> {
                        // Toast.makeText(getContext(), "Quantidade atualizada.", Toast.LENGTH_SHORT).show(); // Comentar para evitar spam de toast
                        // A UI será automaticamente atualizada pelo ValueEventListener
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Erro ao atualizar quantidade: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("CartFragment", "Erro ao atualizar quantidade no Firebase: " + e.getMessage());
                    });
        }
    }
    public void exibirAlertaRemocaoCarrinho(ProdutosCarrinho produto, Context context){
        new android.app.AlertDialog.Builder(context) // Use android.app.AlertDialog
                .setTitle("Remover item")
                .setMessage("Você deseja remover esse item do carrinho?")
                .setPositiveButton("Sim", (dialog, which) -> removerItemDoCarrinho(produto))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
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
        if (produtosCarrinhoList.isEmpty()) {
            Toast.makeText(getContext(), "Seu carrinho já está vazio.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (carrinhoRef != null) {
            carrinhoRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Carrinho esvaziado", Toast.LENGTH_SHORT).show();
                        Log.d("Firebase", "Todos os produtos foram removidos com sucesso.");
                        // O ValueEventListener já vai cuidar da atualização da UI.
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