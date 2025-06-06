package com.example.tela_login_projetointegrador.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tela_login_projetointegrador.Adapters.ProdutosAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.listeners.ProductsListener;
import com.example.tela_login_projetointegrador.models.CategoriaProduto;
import com.example.tela_login_projetointegrador.models.Produto;
import com.example.tela_login_projetointegrador.utils.BaseFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements ProductsListener {
    private GridView gvProdutos;
    private EditText searchBar;
    private Parcelable gridViewState;
    private LinearLayout linearLayoutCategorias;
    private final List<Produto> listProdutos = new ArrayList<>();
    private final List<Produto> listProdutosFiltrados = new ArrayList<>();
    private ProdutosAdapter produtosAdapter;
    private DatabaseReference produtosGlobaisRef;
    public static final String ARG_FOCUS_SEARCH = "focus_search_bar";
    private int currentCategoryId = -1; // -1 significa "Todos" os produtos
    private List<CategoriaProduto> categoriasDisponiveis; // Lista de categorias


    public HomeFragment() {
        // Construtor vazio público é necessário
    }
    public static HomeFragment newInstance(boolean focusSearchBar) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FOCUS_SEARCH, focusSearchBar);
        fragment.setArguments(args);
        return fragment;
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_screen, container, false);

        gvProdutos = view.findViewById(R.id.gridView_products);
        searchBar = view.findViewById(R.id.search_bar);
        linearLayoutCategorias = view.findViewById(R.id.linearLayoutCategorias);

        produtosGlobaisRef = FirebaseDatabase.getInstance().getReference("produtos_globais");

        produtosAdapter = new ProdutosAdapter(getContext(), R.layout.products_itens, listProdutosFiltrados,
                this, requireActivity().getSupportFragmentManager());
        gvProdutos.setAdapter(produtosAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        carregarCategorias();
        carregarProdutos();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            gridViewState = savedInstanceState.getParcelable("gridState");
            if (gridViewState != null){
                gvProdutos.onRestoreInstanceState(gridViewState);
            }
        }
        if (getArguments() != null) {
            boolean focusSearchBar = getArguments().getBoolean(ARG_FOCUS_SEARCH, false);
            if (focusSearchBar) {
                // É importante dar um pequeno delay para garantir que o layout esteja pronto
                // e o teclado virtual tenha tempo de aparecer sem "flicker".
                searchBar.postDelayed(() -> {
                    searchBar.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 100); // Pequeno delay de 100ms
            }
        }

        getParentFragmentManager().setFragmentResultListener("carrinho_atualizado", this, (requestKey, result) -> {
            FragmentTransaction transaction = requireFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentProdutos, new CartFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        if (categoriasDisponiveis != null && !categoriasDisponiveis.isEmpty()) {
            atualizarEstiloCategorias(currentCategoryId);
        }
    }

    private void carregarProdutos() {
        produtosGlobaisRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProdutos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Produto produto = dataSnapshot.getValue(Produto.class);
                    if (produto != null) {
                        listProdutos.add(produto);
                    }
                }
                aplicarFiltros();
                Log.d("HomeFragment", "Produtos globais carregados: " + listProdutos.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Erro ao carregar produtos globais: " + error.getMessage());
                Toast.makeText(getContext(), "Erro ao carregar produtos.", Toast.LENGTH_SHORT).show();
                error.toException().printStackTrace();
            }
        });
    }
    private void carregarCategorias() {
        categoriasDisponiveis = new ArrayList<>();
        // Adiciona a opção "Todos" ou "Remover Filtro"
        categoriasDisponiveis.add(new CategoriaProduto(-1, "Todos")); // ID -1 para "Todos"
        categoriasDisponiveis.add(new CategoriaProduto(1, "Eletronicos"));
        categoriasDisponiveis.add(new CategoriaProduto(2, "Brinquedos"));
        categoriasDisponiveis.add(new CategoriaProduto(3, "Vestuários"));
        categoriasDisponiveis.add(new CategoriaProduto(4, "Cama/Mesa/Banho"));

        exibirCategoriasNoLayout();
    }
    private void exibirCategoriasNoLayout() {
        if (linearLayoutCategorias != null) { // Adicionado um check defensivo extra
            linearLayoutCategorias.removeAllViews();
        }
        for (CategoriaProduto categoria : categoriasDisponiveis) {
            TextView textViewCategoria = new TextView(getContext());
            textViewCategoria.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textViewCategoria.setText(categoria.getDescricao());
            textViewCategoria.setPadding(24, 12, 24, 12); // Aumentei o padding para melhor toque
            textViewCategoria.setBackgroundResource(R.drawable.img_backpack);
            textViewCategoria.setTextColor(Color.BLACK);
            textViewCategoria.setTextSize(14); // Ajustei o tamanho da fonte
            textViewCategoria.setClickable(true);
            textViewCategoria.setFocusable(true);
            textViewCategoria.setTag(categoria.getId()); // Armazena o ID da categoria na tag

            // Define o listener de clique
            textViewCategoria.setOnClickListener(v -> {
                int selectedId = (int) v.getTag();
                if (currentCategoryId == selectedId) {
                    // Clicou na mesma categoria, desativar filtro (voltar para "Todos")
                    currentCategoryId = -1;
                } else {
                    currentCategoryId = selectedId;
                }
                aplicarFiltros(); // Aplica o filtro da categoria e da busca
                atualizarEstiloCategorias(currentCategoryId); // Atualiza o estilo visual dos botões
            });

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textViewCategoria.getLayoutParams();
            params.setMarginEnd(16); // Adiciona margem entre os itens
            textViewCategoria.setLayoutParams(params);

            if (linearLayoutCategorias != null) { // Adicionado um check defensivo extra
                linearLayoutCategorias.addView(textViewCategoria);
            }
        }

            atualizarEstiloCategorias(currentCategoryId);
    }
    private void atualizarEstiloCategorias(int selectedCategoryId) {
        for (int i = 0; i < linearLayoutCategorias.getChildCount(); i++) {
            View child = linearLayoutCategorias.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                int categoryId = (int) textView.getTag();
                if (categoryId == selectedCategoryId) {
                    textView.setBackgroundResource(R.drawable.bg_category_selected);
                    textView.setTextColor(Color.WHITE);
                } else {
                    textView.setBackgroundResource(R.drawable.bg_category_unselected);
                    textView.setTextColor(Color.BLACK);
                }
            }
        }
    }
    private void aplicarFiltros() {
        listProdutosFiltrados.clear();
        String textoBusca = searchBar.getText().toString().toLowerCase().trim();

        for (Produto produto : listProdutos) {
            boolean matchesSearch = (produto.getNomeProduto() != null && produto.getNomeProduto().toLowerCase().contains(textoBusca));
            boolean matchesCategory = (currentCategoryId == -1 || produto.getIdCategoria() == currentCategoryId);

            if (matchesSearch && matchesCategory) {
                listProdutosFiltrados.add(produto);
            }
        }
        produtosAdapter.notifyDataSetChanged();
        Log.d("HomeFragment", "Produtos filtrados (busca + categoria): " + listProdutosFiltrados.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gvProdutos != null) {
            gridViewState = gvProdutos.onSaveInstanceState();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (gvProdutos != null) {
            outState.putParcelable("gridState", gvProdutos.onSaveInstanceState());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (gvProdutos != null && gridViewState != null) {
            gvProdutos.onRestoreInstanceState(gridViewState);
        }
    }

    @Override
    public void getProdutos(List<Produto> produtos) {
    }

    @Override
    public void onProdutoSelected(Produto produto) {
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
