package com.example.tela_login_projetointegrador.database;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.tela_login_projetointegrador.model.Telefone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TelefoneManager {
    private DatabaseReference databaseReference;

    public TelefoneManager(DatabaseReference databaseReference) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("telefones");
    }

    public void cadastrarTelefone(String userId, String numeroTelefone, View view ) {
       String telefoneId = databaseReference.push().getKey();
       Telefone telefone = new Telefone(telefoneId, userId, numeroTelefone);
        databaseReference.child(telefoneId).setValue(telefone).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "Telefone cadastrado com sucesso!");
            } else {
                Log.i(TAG, "Erro ao cadastrar telefone: " + task.getException().getMessage());
            }
        }).addOnFailureListener(e -> {
            Log.i(TAG, "Erro ao cadastrar telefone: " + e.getMessage());
        });
    }
    public static ValueEventListener getTelefoneListener() {
        return new
                ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Telefone telefone = snapshot.getValue(Telefone.class);
                    if (telefone != null) {
                        System.out.println("Telefone encontrado: " + telefone.getContato());
                    } else {
                        System.out.println("Telefone não encontrado.");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Erro ao consultar telefone: " + error.getMessage());
            }
        };
    }
}
