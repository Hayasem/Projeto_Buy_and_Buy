package com.example.tela_login_projetointegrador.fragment;
import androidx.fragment.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.tela_login_projetointegrador.StatusPedido;
import com.example.tela_login_projetointegrador.backendactivitys.MenuScreen;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.PedidoManager;
import com.example.tela_login_projetointegrador.database.PedidosItensManager;
import com.example.tela_login_projetointegrador.model.Pedido;
import com.example.tela_login_projetointegrador.model.Pedido_itens;
import com.example.tela_login_projetointegrador.model.Produto;
import com.example.tela_login_projetointegrador.utils.Utils;

import java.util.List;

public class FragmentProdutoDetalhe extends Fragment {

    private ConstraintLayout headerDescricao, headerEspecificacoes;
    private boolean isDescriptionExpanded;
    private boolean isEspecificacoesExpanded;
    private TextView textPreco, descricao, descricaoExpandida, categoriaExpandida, quantidadeExpandida;
    private ImageView iconCart, iconShare, iconComeBack, image, descricaoToggleIcon, especificacoesToggleIcon;
    private Produto produto;
    private LinearLayout descricaoContent, especificacoesContent;
    private PedidoManager pedidoManager;
    private PedidosItensManager pedidosItensManager;

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
        iconShare = view.findViewById(R.id.icon_share);
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

        DatabaseConnection databaseConnection = new DatabaseConnection(getContext());
        SQLiteDatabase db = databaseConnection.getWritableDatabase();
        pedidoManager = new PedidoManager(db);
        pedidosItensManager = new PedidosItensManager(db);

        assert getArguments() != null;
        produto = (Produto) getArguments().getSerializable("produto");
        if (produto != null) {
            image.setImageBitmap(Utils.loadImageFromInternalStorage(produto.getImagem()));
            textPreco.setText(String.valueOf("R$ " + produto.getPreco()));
            descricao.setText("Descrição");

            descricaoExpandida.setText(produto.getDescricao());
            categoriaExpandida.setText(String.valueOf(produto.getIdCategoria()));
            quantidadeExpandida.setText(String.valueOf(produto.getQuantidadeDisponivel()) + " peças ainda em estoque");
        }

        acoes();
        return view;
    }

    private void acoes() {
        iconCart.setOnClickListener(v -> {
            List<Pedido> pedidosList = pedidoManager.getPedidoByStatus(StatusPedido.ABERTO); // se não tiver pedido com status aberto eu tenho que criar um novo pedido, caso ao contrato eu uso o pedido em aberto
            if (pedidosList.isEmpty()) {
                Pedido pedido = new Pedido(Utils.obterDataHoraAtual(), 1, StatusPedido.ABERTO.toString());
                String pedidoID = String.valueOf(pedidoManager.cadastrarPedido(pedido));
                Pedido_itens pedidoItens = new Pedido_itens((String) pedidoID, produto.getPreco(), produto.getIdProduto(), 1);
                pedidosItensManager.cadastrarItensPedido(pedidoItens);

            } else {
                Pedido pedidoAberto = pedidosList.get(0);
                Pedido_itens pedidoItens = new Pedido_itens(String.valueOf(pedidoAberto.getIdPedido()), produto.getPreco(), produto.getIdProduto(), 1);
                pedidosItensManager.cadastrarItensPedido(pedidoItens);
            }
            Toast.makeText(getContext(), "Produto adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
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
}