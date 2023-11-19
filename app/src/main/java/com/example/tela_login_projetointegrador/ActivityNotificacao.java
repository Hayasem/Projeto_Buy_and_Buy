package com.example.tela_login_projetointegrador;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.tela_login_projetointegrador.Adapter.NotificacaoAdapter;
import com.example.tela_login_projetointegrador.model.HolderNotificacao;


import java.util.ArrayList;
import java.util.List;

public class ActivityNotificacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);

        // Referência para a ListView no layout
        ListView list =  findViewById(R.id.lvNotificacoes);

        // Carrega os dados de exemplo e configura o ArrayAdapter
        List<HolderNotificacao> lista = carregarDados();
        ArrayAdapter<HolderNotificacao> listaAdapter = new NotificacaoAdapter( this, R.layout.adapternotificacao, lista);

        // Define o adapter para a ListView
        list.setAdapter(listaAdapter);
    }


        // Função para carregar as notificações ficticias
        private List<HolderNotificacao> carregarDados (){
        List<HolderNotificacao> listaNotificacao =new ArrayList<>();

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
    }
}