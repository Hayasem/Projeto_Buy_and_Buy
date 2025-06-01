package com.example.tela_login_projetointegrador.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.TypedValue; // dep adicionada
import android.content.res.Configuration;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.fragments.FragmentCadastrarProdutos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // dep adicionada


public class UserActivity extends Fragment {
    private TextView nomeUsuario, aguardePag, aguardEnv, enviado, addComentario, devolucoes, historico, cupons, favoritos;
    private ImageView aguardePagIcon, aguardeEnvIcon, enviadoIcon, addComentarioIcon, devolucoesIcon, historicoIcon, cupomIcon, favIcon, userIcon, notificationIcon, suporteIcon, configIcon;
    private Button bt_deslogar, btn_cadastrar_produtos;
    private FirebaseAuth auth;
    private SQLiteDatabase db;
    private DatabaseReference usuariosRef;
    private FloatingActionButton fab;

    private float fontScaleMultiplier = 1.0f;
    private static final float MAX_SCALE = 4.0f;
    private static final float MIN_SCALE = 2.0f;
    private static final float SCALE_STEP = 0.2f;
    private static final float DEFAULT_SCALE = 2.0f;

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        applyFontScaling((ViewGroup) getView()); // Atualiza ao girar tela/mudar configurações
    }

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

        // Botões de fonte
        Button btnAumentar = view.findViewById(R.id.btn_aumentar_fonte); // ID com typo
        Button btnDiminuir = view.findViewById(R.id.btn_diminuir_fonte);

        // Carregar multiplicador salvo
        fontScaleMultiplier = getSavedFontMultiplier();
        applyFontScaling((ViewGroup) view); // Aplicar ao abrir

        // Listeners
        btnAumentar.setOnClickListener(v -> increaseFontSize());
        btnDiminuir.setOnClickListener(v -> decreaseFontSize());


        return view;
    }

    public static UserActivity newInstance(){
        return new UserActivity();
    }


    //Recuperando o ID:
    @Override
    public void onStart() {
        super.onStart();

    }

    private void saveFontMultiplier(float multiplier) {
        requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .putFloat("font_scale_multiplier", multiplier)
                .apply();
    }

    private float getSavedFontMultiplier() {
        return requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getFloat("font_scale_multiplier", DEFAULT_SCALE);
    }

    private void increaseFontSize() {
        if (fontScaleMultiplier + SCALE_STEP <= MAX_SCALE) {
            fontScaleMultiplier += SCALE_STEP;
            saveFontMultiplier(fontScaleMultiplier);
            applyFontScaling((ViewGroup) getView());
            Toast.makeText(getContext(), "Fonte aumentada!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Tamanho máximo atingido", Toast.LENGTH_SHORT).show();
        }
    }

    // Novo método para diminuir
    private void decreaseFontSize() {
        if (fontScaleMultiplier - SCALE_STEP >= MIN_SCALE) {
            fontScaleMultiplier -= SCALE_STEP;
            saveFontMultiplier(fontScaleMultiplier);
            applyFontScaling((ViewGroup) getView());
            Toast.makeText(getContext(), "Fonte diminuída!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Tamanho mínimo atingido", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyFontScaling(ViewGroup root) {
        float systemFontScale = getResources().getConfiguration().fontScale; // Respeita configuração do sistema
        float baseSizeSp = 14; // Tamanho base do seu design (ajuste conforme necessário)

        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                // Calcula tamanho considerando sistema + multiplicador do app
                float scaledSize = baseSizeSp * fontScaleMultiplier * systemFontScale;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, scaledSize);
            } else if (child instanceof ViewGroup) {
                applyFontScaling((ViewGroup) child);
            }
        }
    }

}

