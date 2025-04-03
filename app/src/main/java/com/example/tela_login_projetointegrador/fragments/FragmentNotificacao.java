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
import android.widget.Toast;

import com.example.tela_login_projetointegrador.Adapters.NotificacaoAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.HolderNotificacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentNotificacao extends Fragment {
    private ListView listView;
    private NotificacaoAdapter adapter;
    private ArrayList<HolderNotificacao> listaNotificacoes;
    private DatabaseReference databaseRef;
    private String idUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notificacao, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            idUsuario = auth.getCurrentUser().getUid();
            if (!idUsuario.isEmpty()) { // Garante que o ID não seja nulo ou vazio
                databaseRef = FirebaseDatabase.getInstance().getReference("notificacoes").child(idUsuario);
            } else {
                Toast.makeText(getContext(), "Erro ao obter ID do usuário", Toast.LENGTH_SHORT).show();
                return view;
            }
        } else {
            Toast.makeText(getContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return view; // Sai da função se o usuário não estiver logado
        }
        listView = view.findViewById(R.id.lvNotificacoes);
        listaNotificacoes = new ArrayList<>();
        adapter = new NotificacaoAdapter(getContext(), R.layout.adapternotificacao, listaNotificacoes);
        listView.setAdapter(adapter);
        carregarNotificacoes();
        return view;
    }
    private void carregarNotificacoes(){
        if (databaseRef == null) {
            Toast.makeText(getContext(), "Erro ao acessar o Firebase", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaNotificacoes.clear();
                for (DataSnapshot notificacaoSnapshot : snapshot.getChildren()){
                    HolderNotificacao notificacao = notificacaoSnapshot.getValue(HolderNotificacao.class);
                    if (notificacao != null){
                        listaNotificacoes.add(notificacao);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erro ao carregar notificações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static FragmentNotificacao newInstance() {
        return new FragmentNotificacao();
    }

}