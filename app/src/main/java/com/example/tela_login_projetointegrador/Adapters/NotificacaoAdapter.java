package com.example.tela_login_projetointegrador.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.HolderNotificacao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotificacaoAdapter extends RecyclerView.Adapter<NotificacaoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HolderNotificacao> notificacoes;

    public NotificacaoAdapter(Context context, ArrayList<HolderNotificacao> notificacoes) {
        this.context = context;
        this.notificacoes = notificacoes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descricao, data;

        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.idTitulo);
            descricao = itemView.findViewById(R.id.idDescricaoNotification);
            data = itemView.findViewById(R.id.notificationData);
        }
    }

    @NonNull
    @Override
    public NotificacaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapternotificacao, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificacaoAdapter.ViewHolder holder, int position) {
        HolderNotificacao notificacao = notificacoes.get(position);

        holder.titulo.setText(notificacao.getTitulo());
        holder.descricao.setText(notificacao.getDescricao());
        holder.data.setText(notificacao.getData_notif() + " " + notificacao.getHora_notif());
    }

    @Override
    public int getItemCount() {
        return notificacoes.size();
    }

    public void removerItem(int position) {
        HolderNotificacao notificacao = notificacoes.get(position);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                .getReference("notificacoes")
                .child(notificacao.getIdUsuario())
                .child(notificacao.getIdNotificacao());

        databaseRef.removeValue().addOnSuccessListener(aVoid -> {
            notificacoes.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Notificação removida!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Erro ao remover notificação!", Toast.LENGTH_SHORT).show();
        });
    }
}
