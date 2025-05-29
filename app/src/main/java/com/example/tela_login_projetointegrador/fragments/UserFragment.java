package com.example.tela_login_projetointegrador.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFragment extends Fragment {
   private TextView nomeUsuario, aguardePag, aguardEnv, enviado, addComentario, devolucoes, historico, cupons, favoritos;
   private ImageView aguardePagIcon, aguardeEnvIcon, enviadoIcon, addComentarioIcon, devolucoesIcon, historicoIcon, cupomIcon, favIcon, userIcon, notificationIcon, suporteIcon, configIcon;
   private Button bt_deslogar, btn_cadastrar_produtos;
   private FirebaseAuth auth;
   private DatabaseReference usuariosRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tela_perfil_usuario, container, false);

        nomeUsuario = view.findViewById(R.id.user_name);
        aguardePag = view.findViewById(R.id.txt_aguarde_pag);
        aguardEnv = view.findViewById(R.id.txt_aguarde_env);
        enviado = view.findViewById(R.id.txt_enviado);
        devolucoes = view.findViewById(R.id.txt_devolucoes);
        historico = view.findViewById(R.id.txt_historico);
        cupons = view.findViewById(R.id.txt_cupons);
        favoritos = view.findViewById(R.id.txt_favoritos);
        aguardePagIcon = view.findViewById(R.id.pending_payment_icon);
        aguardeEnvIcon = view.findViewById(R.id.box_icon);
        enviadoIcon = view.findViewById(R.id.post_truck_icon);
        devolucoesIcon = view.findViewById(R.id.return_icon);
        historicoIcon = view.findViewById(R.id.history_icon);
        cupomIcon = view.findViewById(R.id.coupons_icon);
        favIcon = view.findViewById(R.id.heart_icon);
        userIcon = view.findViewById(R.id.user_icon);
        notificationIcon = view.findViewById(R.id.notification_icon);
        suporteIcon = view.findViewById(R.id.suporte_icon);
        bt_deslogar = view.findViewById(R.id.btn_deslogar);
        btn_cadastrar_produtos = view.findViewById(R.id.btn_cadastrar_produto);
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

        btn_cadastrar_produtos.setOnClickListener(view1 -> {
            FragmentCadastrarProdutos fragmentCadastrarProdutos = new FragmentCadastrarProdutos();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentProdutos, fragmentCadastrarProdutos)
                    .addToBackStack(null)
                    .commit();
        });

        notificationIcon.setOnClickListener(view1 -> {
            FragmentNotificacao fragmentNotificacoes = new FragmentNotificacao();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentProdutos, fragmentNotificacoes) // Usa o ID correto do container
                    .addToBackStack(null)
                    .commit();
        });

        suporteIcon.setOnClickListener(view1 -> {
            FragmentoSuporteAoUsuario fragmentoSuporteAoUsuario = new FragmentoSuporteAoUsuario();
            fragmentoSuporteAoUsuario.show(getParentFragmentManager(), "suporte_dialog");
        });

        return view;
    }

    public static UserFragment newInstance(){
        return new UserFragment();
    }


    //Recuperando o ID:
    @Override
    public void onStart() {
        super.onStart();
        String userId = auth.getCurrentUser().getUid();
        usuariosRef.child(userId).child("nome").get().addOnSuccessListener(dataSnapshot -> {
            String nome = dataSnapshot.getValue(String.class);
            nomeUsuario.setText(nome != null ? nome : "Usuário");
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Erro ao carregar nome do usuário", Toast.LENGTH_SHORT).show();
        });
    }
}
