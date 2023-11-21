package com.example.tela_login_projetointegrador;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import org.w3c.dom.Text;

public class TelaPerfilUsuario extends Fragment {
   private TextView nomeUsuario, emailUsuario;
   private Button bt_deslogar;
   FirebaseFirestore database = FirebaseFirestore.getInstance();
   String usuarioID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // foi alterado para fragment por questão de melhores praticas, uma vez que não a necessidade de atualizar a tela toda, exemplo o menu e appbar não precisa ficar
        // sendo carregado a toda mudança de tela pois eles não se alteram.
        //https://dev.to/alexandrefreire/qual-a-diferenca-entre-activity-fragmentactivity-e-fragment-216o
        View view = inflater.inflate(R.layout.activity_tela_perfil_usuario, container, false);
        nomeUsuario = view.findViewById(R.id.text_nome_usuario);
        emailUsuario =view.findViewById(R.id.text_email_usuario);
        bt_deslogar = view.findViewById(R.id.bt_deslogar);

        bt_deslogar.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        return view;
    }
    public static TelaPerfilUsuario newInstance(){
        return new TelaPerfilUsuario();
    }


    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tela_perfil_usuario);
//        IniciarComponentes();
//
//
//        //Ao clicar em deslogar, o usuário será deslogado, e retornará a tela inicial:
//        bt_deslogar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(TelaPerfilUsuario.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//    }

    //Recuperando o ID:
    @Override
    public void onStart() {
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

//    private void IniciarComponentes(){
//        nomeUsuario = findViewById(R.id.text_nome_usuario);
//        emailUsuario = findViewById(R.id.text_email_usuario);
//        bt_deslogar = findViewById(R.id.bt_deslogar);
//    }
}