package com.example.tela_login_projetointegrador.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.StatusPedido;
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



    private Button buttonAddToCart;
    private Produto produto;
    private PedidoManager pedidoManager;
    private PedidosItensManager pedidosItensManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produto_detalhe, container, false);

        ImageView image = view.findViewById(R.id.product_image);
        TextView textPreco = view.findViewById(R.id.product_price);
        TextView descricao = view.findViewById(R.id.product_description);
        buttonAddToCart = view.findViewById(R.id.button_add_to_cart);

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
        buttonAddToCart.setOnClickListener(v -> {
            List<Pedido> pedidosList = pedidoManager.getPedidoByStatus(StatusPedido.ABERTO); // se não tiver pedido com status aberto eu tenho que criar um novo pedido, caso ao contrato eu uso o pedido em aberto
            if(pedidosList.isEmpty()){
                Pedido pedido = new  Pedido(Utils.obterDataHoraAtual(),1,StatusPedido.ABERTO.toString());
                long pedidoID = pedidoManager.cadastrarPedido(pedido);
                Pedido_itens pedidoItens = new Pedido_itens( (int)pedidoID, produto.getPreco(), produto.getIdProduto(),1);
                pedidosItensManager.cadastrarItensPedido(pedidoItens);


            }
        });
    }
}