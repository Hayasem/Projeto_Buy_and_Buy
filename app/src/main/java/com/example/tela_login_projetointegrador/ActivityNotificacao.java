package com.example.tela_login_projetointegrador;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.tela_login_projetointegrador.Adapter.NotificacaoAdapter;

import java.util.ArrayList;

public class ActivityNotificacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);
        ListView list = (ListView) findViewById(R.id.lvNotificacoes);
        ArrayList<String> lista = carregarDados();
        ArrayAdapter<String> listaAdapter = new NotificacaoAdapter( this, R.layout.adapternotificacao, lista);
        list.setAdapter(listaAdapter);
    }

    private ArrayList<String> carregarDados (){
        ArrayList<String> listaNotificacao = new ArrayList<>();
        listaNotificacao.add("Seu pedido 12345 foi Reservado.");
        listaNotificacao.add("Seu pedido 24578 foi Reservado.");
        listaNotificacao.add("Seu pedido 95874 foi Reservado.");
        listaNotificacao.add("Seu pedido 12378 foi Cancelado.");
        listaNotificacao.add("Seu pedido 00198 foi Cancelado.");
        return listaNotificacao;
    }
}