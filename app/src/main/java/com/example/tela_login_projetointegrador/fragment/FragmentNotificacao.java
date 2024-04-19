package com.example.tela_login_projetointegrador.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.tela_login_projetointegrador.Adapter.NotificacaoAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.HolderNotificacao;


import java.util.ArrayList;
import java.util.List;

public class FragmentNotificacao extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_notificacao, container, false);
        ListView list = view.findViewById(R.id.lvNotificacoes);

        // Carrega os dados de exemplo e configura o ArrayAdapter
        List<HolderNotificacao> lista = carregarDados();
        ArrayAdapter<HolderNotificacao> listaAdapter = new NotificacaoAdapter( getContext(), R.layout.adapternotificacao, lista);


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Notificações");
        }
        // Define o adapter para a ListView
        list.setAdapter(listaAdapter);
        return view;
    }

    public static FragmentNotificacao newInstance() {
        return new FragmentNotificacao();
    }

        // Função para carregar as notificações ficticias
        private List<HolderNotificacao> carregarDados (){
        List<HolderNotificacao> listaNotificacao =new ArrayList<>();
/*
        listaNotificacao.add(new HolderNotificacao("Seu pedido 12378 foi Cancelado!",
                "O prazo para que fosse realizado a compra expirou."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 12345 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 78996 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 45863 foi Cancelado!",
                "O prazo para que fosse realizado a compra expirou."));

        listaNotificacao.add(new HolderNotificacao("Aguardado confirmação de pedido!",
                "Aguardado confirmação da loja."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 12345 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 01234 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 78996 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 78996 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 78996 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 78996 foi Reservado!",
                "Sua compra foi realizada com sucesso."));


        listaNotificacao.add(new HolderNotificacao("Seu pedido 78996 foi Reservado!",
                "Sua compra foi realizada com sucesso."));

        return listaNotificacao;
 */return null;
    }
}