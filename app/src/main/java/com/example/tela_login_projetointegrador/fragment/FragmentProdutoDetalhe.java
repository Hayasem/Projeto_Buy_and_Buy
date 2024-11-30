package com.example.tela_login_projetointegrador.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.StatusPedido;
import com.example.tela_login_projetointegrador.backendactivitys.MenuScreen;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.PedidoManager;
import com.example.tela_login_projetointegrador.database.PedidosItensManager;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.example.tela_login_projetointegrador.model.Pedido;
import com.example.tela_login_projetointegrador.model.Pedido_itens;
import com.example.tela_login_projetointegrador.model.Produto;
import com.example.tela_login_projetointegrador.utils.Utils;

import java.util.List;

public class FragmentProdutoDetalhe extends Fragment {

    private ImageView iconCart, iconShare, iconComeBack;
    private Produto produto;
    private PedidoManager pedidoManager;
    private PedidosItensManager pedidosItensManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produto_detalhe, container, false);

        ImageView image = view.findViewById(R.id.product_image);
        iconCart = view.findViewById(R.id.icon_cart);
        iconComeBack = view.findViewById(R.id.icon_comeBack);
        iconShare = view.findViewById(R.id.icon_share);
        TextView textPreco = view.findViewById(R.id.product_price);
        TextView descricao = view.findViewById(R.id.txt_descricao);


        DatabaseConnection databaseConnection = new DatabaseConnection(getContext());
        SQLiteDatabase db = databaseConnection.getWritableDatabase();
        pedidoManager = new PedidoManager(db);
        pedidosItensManager = new PedidosItensManager(db);



        assert getArguments() != null;
        produto = (Produto) getArguments().getSerializable("produto");
        if (produto != null) {
            image.setImageBitmap(Utils.loadImageFromInternalStorage(produto.getImagem()));
            textPreco.setText(String.valueOf(produto.getPreco()));
            descricao.setText(produto.getDescricao());
        }

        acoes();
        return view;
    }

    private void acoes() {
        iconCart.setOnClickListener(v -> {
            List<Pedido> pedidosList = pedidoManager.getPedidoByStatus(StatusPedido.ABERTO); // se não tiver pedido com status aberto eu tenho que criar um novo pedido, caso ao contrato eu uso o pedido em aberto
            if(pedidosList.isEmpty()){
                Pedido pedido = new  Pedido(Utils.obterDataHoraAtual(),1,StatusPedido.ABERTO.toString());
                String pedidoID = String.valueOf(pedidoManager.cadastrarPedido(pedido));
                Pedido_itens pedidoItens = new Pedido_itens( (String)pedidoID, produto.getPreco(), produto.getIdProduto(),1);
                pedidosItensManager.cadastrarItensPedido(pedidoItens);

            }else{
                Pedido pedidoAberto = pedidosList.get(0);
                Pedido_itens pedidoItens = new Pedido_itens(String.valueOf(pedidoAberto.getIdPedido()), produto.getPreco(), produto.getIdProduto(), 1);
                pedidosItensManager.cadastrarItensPedido(pedidoItens);
            }
            Toast.makeText(getContext(), "Produto adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
        });
        iconComeBack.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentProdutos, new MenuScreen())
                    .addToBackStack(null)
                    .commit();
        });
    }
}