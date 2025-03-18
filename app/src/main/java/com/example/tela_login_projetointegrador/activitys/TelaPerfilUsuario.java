package com.example.tela_login_projetointegrador.activitys;

import android.app.AlertDialog;
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
import com.example.tela_login_projetointegrador.database.ProductManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TelaPerfilUsuario extends Fragment {
   private TextView nomeUsuario, aguardePag, aguardEnv, enviado, addComentario, devolucoes, historico, cupons, favoritos;
   private ImageView aguardePagIcon, aguardeEnvIcon, enviadoIcon, addComentarioIcon, devolucoesIcon, historicoIcon, cupomIcon, favIcon, userIcon, notificationIcon, suporteIcon, configIcon;
   private Button bt_deslogar, btn_limparProdutos;
   private FirebaseAuth auth;
   private DatabaseReference usuariosRef;
   ProductManager productManager;
   String idUsuario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tela_perfil_usuario, container, false);

        nomeUsuario = view.findViewById(R.id.user_name);
        aguardePag = view.findViewById(R.id.txt_aguarde_pag);
        aguardEnv = view.findViewById(R.id.txt_aguarde_env);
        enviado = view.findViewById(R.id.txt_enviado);
        addComentario = view.findViewById(R.id.txt_add_comentario);
        devolucoes = view.findViewById(R.id.txt_devolucoes);
        historico = view.findViewById(R.id.txt_historico);
        cupons = view.findViewById(R.id.txt_cupons);
        favoritos = view.findViewById(R.id.txt_favoritos);
        aguardePagIcon = view.findViewById(R.id.pending_payment_icon);
        aguardeEnvIcon = view.findViewById(R.id.box_icon);
        enviadoIcon = view.findViewById(R.id.post_truck_icon);
        addComentarioIcon = view.findViewById(R.id.comment_icon);
        devolucoesIcon = view.findViewById(R.id.return_icon);
        historicoIcon = view.findViewById(R.id.history_icon);
        cupomIcon = view.findViewById(R.id.coupons_icon);
        favIcon = view.findViewById(R.id.heart_icon);
        userIcon = view.findViewById(R.id.user_icon);
        notificationIcon = view.findViewById(R.id.notification_icon);
        suporteIcon = view.findViewById(R.id.suporte_icon);
        configIcon = view.findViewById(R.id.settings_icon);
        bt_deslogar = view.findViewById(R.id.btn_deslogar);
        btn_limparProdutos = view.findViewById(R.id.btn_limparProdutos);
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

        btn_limparProdutos.setOnClickListener(view1 -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmação")
                    .setMessage("Tem certeza que deseja excluir todos os produtos?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        ProductManager productManager = new ProductManager(requireContext());
                        productManager.limparTodosProdutos();
                        Toast.makeText(requireContext(), "Todos os produtos foram removidos.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
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
