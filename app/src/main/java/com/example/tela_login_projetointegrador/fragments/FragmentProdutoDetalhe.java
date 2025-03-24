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
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProdutoDetalhe extends Fragment {

    private ConstraintLayout headerDescricao, headerEspecificacoes;
    private boolean isDescriptionExpanded, isEspecificacoesExpanded;
    private TextView textPreco, descricao, descricaoExpandida, categoriaExpandida, quantidadeExpandida;
    private ImageView iconCart, iconComeBack, image, descricaoToggleIcon, especificacoesToggleIcon;
    private ProdutosCarrinho produtosCarrinho;
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
            image.setImageBitmap(Utils.loadImageFromInternalStorage(produto.getImagem()));
            Glide.with(this).load(produto.getImagem()).into(image);
        } else {
            image.setImageResource(R.drawable.img_backpack);
        }
        textPreco.setText("R$ " + produto.getPreco());
        descricao.setText(produto.getDescricao());

        descricaoExpandida.setText(produto.getDescricao());
        categoriaExpandida.setText(String.valueOf(produto.getIdCategoria()));
        quantidadeExpandida.setText(String.valueOf(produto.getQuantidade()));
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
        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference carrinhoRef = FirebaseDatabase.getInstance().getReference("carrinho").child(usuarioID);

        carrinhoRef.orderByChild("idProduto").equalTo(produto.getIdProduto())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int novaQuantidade = 1;
                        String idCarrinhoExistente = null;

                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                ProdutosCarrinho itemExistente = data.getValue(ProdutosCarrinho.class);
                                if (itemExistente != null){
                                    novaQuantidade = itemExistente.getQuantidade() + 1;
                                    idCarrinhoExistente = data.getKey();
                                }
                            }
                        }else{
                            idCarrinhoExistente = carrinhoRef.push().getKey();
                            if (idCarrinhoExistente == null) {
                                Toast.makeText(getContext(), "Erro ao adicionar ao carrinho!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        ProdutosCarrinho novoItem = new ProdutosCarrinho(
                                idCarrinhoExistente,
                                produto.getIdProduto(),
                                produto.getNomeProduto(),
                                produto.getImagem(),
                                produto.getPreco(),
                                novaQuantidade
                        );

                        assert idCarrinhoExistente != null;
                        carrinhoRef.child(idCarrinhoExistente).setValue(novoItem)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Produto adicionado ao carrinho!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Erro ao adicionar ao carrinho!", Toast.LENGTH_SHORT).show());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

