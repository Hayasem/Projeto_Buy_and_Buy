package com.example.tela_login_projetointegrador.fragments;

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

import com.example.tela_login_projetointegrador.Adapters.NotificacaoAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.HolderNotificacao;

public class FragmentNotificacao extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_notificacao, container, false);
        ListView list = view.findViewById(R.id.lvNotificacoes);

        // Carrega os dados de exemplo e configura o ArrayAdapter
        ArrayAdapter<HolderNotificacao> listaAdapter = new NotificacaoAdapter( getContext(), R.layout.adapternotificacao);


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

}