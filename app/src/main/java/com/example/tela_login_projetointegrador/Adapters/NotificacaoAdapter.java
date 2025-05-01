package com.example.tela_login_projetointegrador.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.ModelNotification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NotificacaoAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private List<ModelNotification> listaNotificacao;
    private Set<String> selectedIds = new HashSet<>();
    public NotificacaoAdapter(List<ModelNotification> list) {
        this.listaNotificacao = list;
    }
    public Set<String> getSelectedIds() {
        return selectedIds;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void clearSelections() {
        selectedIds.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapternotificacao, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        ModelNotification item = listaNotificacao.get(position);
        holder.title.setText(item.getTitulo());
        holder.message.setText(item.getMensagem());
        holder.timestamp.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(item.getTimestamp())));

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedIds.contains(item.getIdNotificacao()));
        holder.checkBox.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) selectedIds.add(item.getIdNotificacao());
            else selectedIds.remove(item.getIdNotificacao());
        });
    }

    @Override
    public int getItemCount() {
        return listaNotificacao.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeByIds(Set<String> idsToRemove) {
        listaNotificacao.removeIf(n -> idsToRemove.contains(n.getIdNotificacao()));
        notifyDataSetChanged();
    }
}
