package com.example.tela_login_projetointegrador.fragment;
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
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.CartProducts;
import com.example.tela_login_projetointegrador.model.Produto;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FragmentProdutoDetalhe extends Fragment {

    private ConstraintLayout headerDescricao, headerEspecificacoes;
    private boolean isDescriptionExpanded;
    private boolean isEspecificacoesExpanded;
    private TextView textPreco, descricao, descricaoExpandida, categoriaExpandida, quantidadeExpandida;
    private ImageView iconCart, iconComeBack, image, descricaoToggleIcon, especificacoesToggleIcon;
    private Produto produto;
    private LinearLayout descricaoContent, especificacoesContent;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produto_detalhe, container, false);

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

        headerDescricao.setOnClickListener(v -> {
            isDescriptionExpanded = !isDescriptionExpanded;
            toggleSection(descricaoContent, descricaoToggleIcon, isDescriptionExpanded);
        });

        headerEspecificacoes.setOnClickListener(v -> {
            isEspecificacoesExpanded = !isEspecificacoesExpanded;
            toggleSection(especificacoesContent, especificacoesToggleIcon, isEspecificacoesExpanded);
        });

        assert getArguments() != null;
        produto = (Produto) getArguments().getSerializable("produto");
        if (produto != null) {
            image.setImageBitmap(Utils.loadImageFromInternalStorage(produto.getImagem()));
            textPreco.setText(String.valueOf("R$ " + produto.getPreco()));
            descricao.setText("Descrição");

            descricaoExpandida.setText(produto.getDescricao());
            categoriaExpandida.setText(String.valueOf(produto.getIdCategoria()));
            quantidadeExpandida.setText(produto.getQuantidadeDisponivel() + " peças ainda em estoque");
        }
        acoes();
        return view;
    }
    private void acoes() {
        iconCart.setOnClickListener(v -> {
            iconCart.setEnabled(false);
            adicionarProdutoCarrinho(new CartProducts(
                    produto.getIdProduto(),
                    produto.getTitulo(),
                    produto.getPreco(),
                    1
            ));
            requireActivity().getSupportFragmentManager().setFragmentResult("carrinho_atualizado", new Bundle());
        });

        iconComeBack.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });
    }
    private void toggleSection (LinearLayout content, ImageView toggleIcon,boolean isExpanded){
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
    public void adicionarProdutoCarrinho(CartProducts produto){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getUid() == null) {
            Log.e("Firebase", "Usuário não autenticado");
            return;
        }

        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("carrinho")
                .child(auth.getUid())
                .child(produto.getIdProduto());

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int novaQuantidade = 1;
                if (snapshot.exists()) {
                    Integer quantidadeAtual = snapshot.child("quantidade").getValue(Integer.class);
                    if (quantidadeAtual != null) {
                        novaQuantidade = quantidadeAtual + 1;
                    }
                }
                Map<String, Object> cartItem = new HashMap<>();
                cartItem.put("nome", produto.getNomeProduto());
                cartItem.put("preco", produto.getPreco_uni());
                cartItem.put("quantidade", novaQuantidade);

                cartRef.setValue(cartItem)
                        .addOnSuccessListener(aVoid -> {
                            iconCart.setEnabled(true);
                            Log.d("Firebase", "Produto adicionado/atualizado no carrinho com sucesso!");
                            // Atualiza a tela ou notifica o usuário
                            Toast.makeText(getContext(), "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                            // Notifica o Fragment que o carrinho foi atualizado
                            requireActivity().getSupportFragmentManager().setFragmentResult("carrinho_atualizado", new Bundle());
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firebase", "Erro ao adicionar produto ao carrinho", e);
                            Toast.makeText(getContext(), "Erro ao adicionar produto", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iconCart.setEnabled(true);
                Log.e("Firebase", "Erro ao acessar o carrinho", error.toException());
                Toast.makeText(getContext(), "Erro ao acessar o carrinho", Toast.LENGTH_SHORT).show();
            }
        });
    }
}