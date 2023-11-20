package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.tela_login_projetointegrador.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class TelaPerfilUsuario extends AppCompatActivity {
   private TextView nomeUsuario, emailUsuario;
   private Button bt_deslogar;

   FirebaseFirestore database = FirebaseFirestore.getInstance();
   String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_tela_perfil_usuario);
        IniciarComponentes();


        //Ao clicar em deslogar, o usuário será deslogado, e retornará a tela inicial:
        bt_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TelaPerfilUsuario.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //Recuperando o ID:
    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = database.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshot, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    nomeUsuario.setText(documentSnapshot.getString("nome"));
                    emailUsuario.setText(email);
                }
            }
        });
    }

    private void IniciarComponentes(){
        nomeUsuario = findViewById(com.example.tela_login_projetointegrador.R.id.text_nome_usuario);
        emailUsuario = findViewById(com.example.tela_login_projetointegrador.R.id.text_email_usuario);
        bt_deslogar = findViewById(R.id.bt_deslogar);
    }
}