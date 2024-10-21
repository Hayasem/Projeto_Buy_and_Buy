package com.example.tela_login_projetointegrador.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.tela_login_projetointegrador.model.Telefone;
import com.example.tela_login_projetointegrador.model.Usuario;
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

    public void cadastrarTelefone(Telefone telefone) {
        String telefoneId = databaseReference.push().getKey();
        telefone.setIdTelefone(telefoneId);
        assert telefoneId != null;
        databaseReference.child(telefoneId).setValue(telefone).addOnCompleteListener(aVoid -> {
            System.out.println("Telefone cadastrado com sucesso!");
        }).addOnFailureListener(e ->{
            System.out.println("Erro ao cadastrar o telefone: " + e.getMessage());
        });
    }
    public void consultarTelefone(String telefoneId, ValueEventListener listener) {
        databaseReference.child(telefoneId).addListenerForSingleValueEvent(listener);
    }
    public static ValueEventListener getTelefoneListener() {
        return new
                ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Telefone telefone = snapshot.getValue(Telefone.class);
                    if (telefone != null) {
                        System.out.println("Telefone encontrado: " + telefone.getNumero());
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
