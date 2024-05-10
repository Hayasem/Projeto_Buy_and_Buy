package com.example.tela_login_projetointegrador.backendactivitys;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.tela_login_projetointegrador.R;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tela_login_projetointegrador.fragment.FragmentCadastrarProdutos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class TelaPerfilUsuario extends Fragment {
   private TextView nomeUsuario, emailUsuario, meusProdutos;
   private ImageView viewProducts;
   private Button bt_deslogar;
   FirebaseFirestore database = FirebaseFirestore.getInstance();
   String usuarioID;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // foi alterado para fragment por questão de melhores praticas, uma vez que não a necessidade de atualizar a tela toda, exemplo o menu e appbar não precisa ficar
        // sendo carregado a toda mudança de tela pois eles não se alteram.
        //https://dev.to/alexandrefreire/qual-a-diferenca-entre-activity-fragmentactivity-e-fragment-216o
        View view = inflater.inflate(R.layout.activity_tela_perfil_usuario, container, false);


        nomeUsuario = view.findViewById(R.id.text_nome_usuario);
        emailUsuario =view.findViewById(R.id.text_email_usuario);
        bt_deslogar = view.findViewById(R.id.bt_deslogar);
        meusProdutos = view.findViewById(R.id.txt_meus_produtos);
        viewProducts = view.findViewById(R.id.imgbtn_products);

        bt_deslogar.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        viewProducts.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentProdutos, new FragmentCadastrarProdutos());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    public static TelaPerfilUsuario newInstance(){
        return new TelaPerfilUsuario();
    }

//    public static TelaPerfilUsuario newInstance(FragmentManager fragmentManager){
//        TelaPerfilUsuario fragment = new TelaPerfilUsuario();
//        fragment.fragmentManager = fragmentManager;
//        return fragment;
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
}