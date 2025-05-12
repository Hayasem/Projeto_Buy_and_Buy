package com.example.tela_login_projetointegrador.fragments;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tela_login_projetointegrador.Adapters.NotificacaoAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.ModelNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FragmentNotificacao extends Fragment {

    private RecyclerView recyclerView;
    private NotificacaoAdapter adapter;
    private List<ModelNotification> listaNotificacoes;
    private DatabaseReference notiRef;

    public static FragmentNotificacao newInstance() {
        return new FragmentNotificacao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        recyclerView = v.findViewById(R.id.recycler_notifications);
        Button deleteButton = v.findViewById(R.id.btn_delete_selected);

        listaNotificacoes = new ArrayList<>();
        adapter = new NotificacaoAdapter(listaNotificacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        notiRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid).child("notificacoes");

        notiRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaNotificacoes.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ModelNotification item = snap.getValue(ModelNotification.class);
                    if (item != null){
                        item.setIdNotificacao(snap.getKey());
                        listaNotificacoes.add(item);
                    }
                }
                Collections.reverse(listaNotificacoes);
                adapter.notifyDataSetChanged();
            }

            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        deleteButton.setOnClickListener(v1 -> {
            Set<String> toDelete = new HashSet<>(adapter.getSelectedIds()); // Cópia segura
            if (!toDelete.isEmpty()) {
                for (String id : toDelete) {
                    notiRef.child(id).removeValue();
                }
                adapter.removeByIds(toDelete);
                adapter.clearSelections();
            } else {
                Toast.makeText(getContext(), "Nenhuma notificação selecionada.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}