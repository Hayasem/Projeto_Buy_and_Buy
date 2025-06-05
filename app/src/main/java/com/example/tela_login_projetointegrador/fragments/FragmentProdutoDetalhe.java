package com.example.tela_login_projetointegrador.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;
import com.example.tela_login_projetointegrador.models.Produto;
import com.example.tela_login_projetointegrador.utils.BaseFragment;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProdutoDetalhe extends BaseFragment {

    private ConstraintLayout headerDescricao, headerEspecificacoes;
    private boolean isDescriptionExpanded, isEspecificacoesExpanded;
    private TextView textPreco, descricao, descricaoExpandida, categoriaExpandida, quantidadeExpandida, nomeVendedorTextView;
    private ImageView iconCart, iconComeBack, image, descricaoToggleIcon, especificacoesToggleIcon;
    private String nomeVendedorCache = "Desconhecido";
    private Produto produto;
    private LinearLayout descricaoContent, especificacoesContent;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produto_detalhe, container, false);
        inicializarComponentes(view);
        configurarExpandirSecoes();

        if (getArguments() != null) {
            produto = (Produto) getArguments().getSerializable("produto");
            if (produto != null) {
                preencherDadosProduto(produto);
                carregarNomeVendedor(produto.getIdVendedor());
            }
        }
        configurarAcoes();
        return view;
    }

    private void configurarAcoes() {
        iconCart.setOnClickListener(v -> adicionarAoCarrinho(produto));

        iconComeBack.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());
    }

    @SuppressLint("SetTextI18n")
    private void preencherDadosProduto(Produto produto) {
        if (produto.getImagem() != null) {
            Glide.with(this).load(produto.getImagem()).into(image);
        } else {
            image.setImageResource(R.drawable.img_backpack);
        }
        textPreco.setText("R$ " + produto.getPreco());
        descricao.setText(produto.getDescricao());

        descricaoExpandida.setText(produto.getDescricao());
        categoriaExpandida.setText("Categoria: " + String.valueOf(produto.getIdCategoria()));
        quantidadeExpandida.setText("Em estoque: " + String.valueOf(produto.getQuantidade()));
        nomeVendedorTextView.setText("Vendedor: Carregando...");

    }

    private void carregarNomeVendedor(String idVendedor) {
        if (idVendedor == null || idVendedor.isEmpty()) {
            nomeVendedorTextView.setText("Vendedor: Indisponível");
            nomeVendedorCache = "Indisponível";
            return;
        }
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(idVendedor)
                .child("nome"); // Assumindo que o nome do usuário está em 'usuarios/UID/nome'

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    nomeVendedorCache = snapshot.getValue(String.class);
                    nomeVendedorTextView.setText("Vendedor: " + nomeVendedorCache);
                } else {
                    nomeVendedorTextView.setText("Vendedor: Desconhecido");
                    nomeVendedorCache = "Desconhecido";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FragmentProdutoDetalhe", "Erro ao carregar nome do vendedor: " + error.getMessage());
                nomeVendedorTextView.setText("Vendedor: Erro ao carregar");
                nomeVendedorCache = "Erro ao carregar";
            }
        });
    }

    private void configurarExpandirSecoes() {
        headerDescricao.setOnClickListener(v -> {
            isDescriptionExpanded = !isDescriptionExpanded;
            toggleSection(descricaoContent, descricaoToggleIcon, isDescriptionExpanded);
        });

        headerEspecificacoes.setOnClickListener(v -> {
            isEspecificacoesExpanded = !isEspecificacoesExpanded;
            toggleSection(especificacoesContent, especificacoesToggleIcon, isEspecificacoesExpanded);
        });
    }

    private void inicializarComponentes(View view) {
        categoriaExpandida = view.findViewById(R.id.txt_categoria_expandida);
        quantidadeExpandida = view.findViewById(R.id.txt_quantidade_expandida);
        descricaoExpandida = view.findViewById(R.id.txt_descricao_expandida);
        descricaoContent = view.findViewById(R.id.content_descricao);
        especificacoesContent = view.findViewById(R.id.content_especificacoes);
        descricaoToggleIcon = view.findViewById(R.id.descricaoToggleIcon);
        especificacoesToggleIcon = view.findViewById(R.id.especificacoesToggleIcon);
        headerDescricao = view.findViewById(R.id.header_descricao);
        headerEspecificacoes = view.findViewById(R.id.header_especificacoes);
        image = view.findViewById(R.id.product_image);
        iconCart = view.findViewById(R.id.icon_cart);
        iconComeBack = view.findViewById(R.id.icon_comeBack);
        textPreco = view.findViewById(R.id.product_price);
        descricao = view.findViewById(R.id.txt_descricao);
        nomeVendedorTextView = view.findViewById(R.id.txt_nome_vendedor);
    }

    private void toggleSection(LinearLayout content, ImageView toggleIcon, boolean isExpanded) {
        // Alternar visibilidade
        content.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Animação do ícone (rotação)
        float fromDegree = isExpanded ? 0f : 180f;
        float toDegree = isExpanded ? 180f : 0f;
        RotateAnimation rotate = new RotateAnimation(fromDegree, toDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        toggleIcon.startAnimation(rotate);
    }

    public void adicionarAoCarrinho(Produto produto) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getContext(), "Você precisa estar logado para adicionar produtos ao carrinho.", Toast.LENGTH_SHORT).show();
            return;
        }
        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference carrinhoUsuarioRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(usuarioID)
                .child("carrinho");

        DatabaseReference produtoGlobalRef = FirebaseDatabase.getInstance()
                .getReference("produtos_globais")
                .child(produto.getIdProduto());

        produtoGlobalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Produto produtoOriginal = snapshot.getValue(Produto.class);
                if (produtoOriginal == null || !produtoOriginal.isEmEstoque() || produtoOriginal.getQuantidade() <= 0) {
                    Toast.makeText(getContext(), "Produto esgotado ou indisponível.", Toast.LENGTH_SHORT).show();
                    return;
                }
                carrinhoUsuarioRef.orderByChild("idProduto").equalTo(produto.getIdProduto())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ProdutosCarrinho itemExistente = null;
                                String idCarrinhoExistente = null;

                                if (snapshot.exists()) {
                                    // Se o produto já está no carrinho, obtemos o primeiro (e único) item
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        itemExistente = childSnapshot.getValue(ProdutosCarrinho.class);
                                        idCarrinhoExistente = childSnapshot.getKey(); // Obtém a chave real do item no carrinho
                                        break; // Deve haver apenas um item por idProduto se você estiver usando idProduto como ID
                                    }
                                }

                                int novaQuantidade;
                                if (itemExistente != null) {
                                    // Produto já existe, incrementa a quantidade
                                    novaQuantidade = itemExistente.getQuantidade() + 1;
                                    if (novaQuantidade > produtoOriginal.getQuantidade()) {
                                        Toast.makeText(getContext(), "Quantidade máxima em estoque atingida para este produto.", Toast.LENGTH_SHORT).show();
                                        return; // Não permite adicionar mais do que o estoque disponível
                                    }
                                } else {
                                    // Produto não existe, adiciona com quantidade 1
                                    novaQuantidade = 1;
                                    idCarrinhoExistente = carrinhoUsuarioRef.push().getKey(); // Gera um NOVO ID ÚNICO para o item do carrinho
                                }

                                // Cria ou atualiza o objeto ProdutosCarrinho
                                ProdutosCarrinho itemParaSalvar = new ProdutosCarrinho(
                                        idCarrinhoExistente, // Use o ID do carrinho existente ou o novo gerado
                                        produto.getIdProduto(),
                                        produto.getNomeProduto(),
                                        produto.getImagem(),
                                        produto.getPreco(),
                                        novaQuantidade,
                                        usuarioID,
                                        nomeVendedorCache
                                        // Você pode adicionar nomeVendedor aqui se tiver acesso a ele
                                );

                                // Salva/Atualiza o item no Firebase usando o idCarrinhoExistente como chave
                                carrinhoUsuarioRef.child(idCarrinhoExistente).setValue(itemParaSalvar)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Produto adicionado ao carrinho!", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getContext(), "Erro ao adicionar ao carrinho: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("AdicionarCarrinho", "Erro ao adicionar/atualizar no Firebase: " + e.getMessage(), e);
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Erro ao verificar carrinho: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("AdicionarCarrinho", "Erro de banco de dados: " + error.getMessage(), error.toException());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erro ao verificar estoque do produto: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AdicionarCarrinho", "Erro de banco de dados ao verificar estoque: " + error.getMessage(), error.toException());
            }
        });
    }
}
