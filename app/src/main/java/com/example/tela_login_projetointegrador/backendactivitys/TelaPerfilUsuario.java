package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.tela_login_projetointegrador.Alerts.DialogUtils;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.example.tela_login_projetointegrador.fragment.FragmentCadastrarProdutos;
import com.example.tela_login_projetointegrador.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TelaPerfilUsuario extends Fragment {
   private TextView nomeUsuario, emailUsuario, meusProdutos;
   private ImageView viewProducts;
   private Button bt_deslogar;
   private FirebaseAuth auth;
   private DatabaseReference usuariosRef;
   String idUsuario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tela_perfil_usuario, container, false);

        nomeUsuario = view.findViewById(R.id.text_nome_usuario);
        emailUsuario =view.findViewById(R.id.text_email_usuario);
        bt_deslogar = view.findViewById(R.id.bt_deslogar);
        meusProdutos = view.findViewById(R.id.txt_meus_produtos);
        viewProducts = view.findViewById(R.id.imgbtn_products);
        auth = FirebaseAuth.getInstance();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");

        bt_deslogar.setOnClickListener(view1 -> {
            requireActivity().getSharedPreferences(MainActivity.PREFS_NAME, getContext().MODE_PRIVATE)
                            .edit().remove(MainActivity.KEY_LAST_LOGIN)
                            .apply();
            auth.signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
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


    //Recuperando o ID:
    @Override
    public void onStart() {
        super.onStart();

    }
}
