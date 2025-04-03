package com.example.tela_login_projetointegrador.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.HolderNotificacao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotificacaoAdapter extends ArrayAdapter<HolderNotificacao> {
    private Context context;
    private int resource;
    private ArrayList<HolderNotificacao> notificacoes;
    private DatabaseReference databaseRef;

    //Contrutor para receber a lista de notificações
    public NotificacaoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HolderNotificacao> notificacoes) {
        super(context, resource, notificacoes);
        this.context = context;
        this.resource = resource;
        this.notificacoes = notificacoes;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        HolderNotificacao notificacao = notificacoes.get(position);

        TextView titulo = convertView.findViewById(R.id.idTitulo);
        TextView descricao = convertView.findViewById(R.id.idDescricaoNotification);
        TextView data = convertView.findViewById(R.id.notificationData);
        ImageView imagem = convertView.findViewById(R.id.notificationLogo);
        ImageView btnRemover = convertView.findViewById(R.id.cleanIcon);

        titulo.setText(notificacao.getTitulo());
        descricao.setText(notificacao.getDescricao());
        data.setText(notificacao.getData_notif() + " " + notificacao.getHora_notif());

        btnRemover.setOnClickListener(v -> {
            databaseRef = FirebaseDatabase.getInstance().getReference("notificacoes")
                    .child(String.valueOf(notificacao.getIdUsuario()))
                    .child(String.valueOf(notificacao.getIdNotificacao()));

            databaseRef.removeValue().addOnSuccessListener(aVoid -> {
                notificacoes.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Notificação removida!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Erro ao remover!", Toast.LENGTH_SHORT).show();
            });
        });
        return convertView;
    }
}
