package com.example.tela_login_projetointegrador.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import android.util.TypedValue;
import com.google.firebase.database.FirebaseDatabase;

public class UserFragment extends Fragment {
   private TextView nomeUsuario, aguardePag, aguardEnv, enviado, addComentario, devolucoes, historico, cupons, favoritos;
   private ImageView aguardePagIcon, aguardeEnvIcon, enviadoIcon, addComentarioIcon, devolucoesIcon, historicoIcon, cupomIcon, favIcon, userIcon, notificationIcon, suporteIcon, configIcon;
   private Button bt_deslogar, btn_cadastrar_produtos;
   private FirebaseAuth auth;
   private DatabaseReference usuariosRef;

    private FloatingActionButton fabMainAccessibility;
    private FloatingActionButton fabFontSize;
    private FloatingActionButton fabDarkMode;
    private boolean isFabMenuOpen = false;

    private static final String PREFS_NAME = "AccessibilityPrefs";
    private static final String KEY_FONT_SCALE = "font_scale";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";

    private static final float FONT_SCALE_SMALL = 0.8f;
    private static final float FONT_SCALE_NORMAL = 1.0f;
    private static final float FONT_SCALE_LARGE = 1.2f;
    private float currentFontScale;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Carrega as preferências antes de inflar o layout para que a fonte já venha ajustada
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentFontScale = prefs.getFloat(KEY_FONT_SCALE, FONT_SCALE_NORMAL);
        boolean isDarkModeEnabled = prefs.getBoolean(KEY_DARK_MODE, false);

        // Aplica o modo escuro salvo ao iniciar o fragmento
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

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

        fabMainAccessibility = view.findViewById(R.id.fab_accessibility_main);
        fabFontSize = view.findViewById(R.id.fab_font_size);
        fabDarkMode = view.findViewById(R.id.fab_dark_mode);

        applyFontScale(view);

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

        fabMainAccessibility.setOnClickListener(v -> toggleFabMenu());

        fabFontSize.setOnClickListener(v -> {
            toggleFabMenu(); // Fecha o menu flutuante após a seleção
            changeFontSize();
        });

        fabDarkMode.setOnClickListener(v -> {
            toggleFabMenu(); // Fecha o menu flutuante após a seleção
            toggleDarkMode();
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
    private void toggleFabMenu() {
        if (isFabMenuOpen) {
            // Fecha o menu
            fabFontSize.hide();
            fabDarkMode.hide();
            // Para adicionar animações de fechar:
            fabFontSize.animate().translationY(0).alpha(0f).setDuration(200).withEndAction(() -> fabFontSize.setVisibility(View.GONE));
            fabDarkMode.animate().translationY(0).alpha(0f).setDuration(200).withEndAction(() -> fabDarkMode.setVisibility(View.GONE));
        } else {
            // Abre o menu
            fabFontSize.show();
            fabDarkMode.show();
            // Para adicionar animações de abrir (ex: subindo e aparecendo):
            fabFontSize.setAlpha(0f);
            fabFontSize.setTranslationY(fabMainAccessibility.getHeight() + 16); // 16dp margin
            fabFontSize.animate().translationY(0).alpha(1f).setDuration(200).setStartDelay(50).start();
            fabDarkMode.setAlpha(0f);
            fabDarkMode.setTranslationY(fabFontSize.getHeight() + 16 + fabMainAccessibility.getHeight() + 16);
            fabDarkMode.animate().translationY(0).alpha(1f).setDuration(200).setStartDelay(100).start();
        }
        isFabMenuOpen = !isFabMenuOpen;
    }

    private void changeFontSize() {
        if (currentFontScale == FONT_SCALE_NORMAL) {
            currentFontScale = FONT_SCALE_LARGE;
            Toast.makeText(getContext(), "Fonte: Grande", Toast.LENGTH_SHORT).show();
        } else if (currentFontScale == FONT_SCALE_LARGE) {
            currentFontScale = FONT_SCALE_SMALL;
            Toast.makeText(getContext(), "Fonte: Pequena", Toast.LENGTH_SHORT).show();
        } else { // currentFontScale == FONT_SCALE_SMALL
            currentFontScale = FONT_SCALE_NORMAL;
            Toast.makeText(getContext(), "Fonte: Normal", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_FONT_SCALE, currentFontScale);
        editor.apply();
        applyFontScale(getView());
    }

    private void applyFontScale(View view) {
        if (view == null) return;

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            // Obtém o tamanho da fonte original definido no XML (em SP)
            float originalSpSize = textView.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            // Calcula o novo tamanho com base na escala
            float newSpSize = originalSpSize * currentFontScale;
            // Define o novo tamanho da fonte
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSpSize);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyFontScale(viewGroup.getChildAt(i));
            }
        }
    }
    private void toggleDarkMode() {
        boolean isDarkModeCurrentlyEnabled = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        if (isDarkModeCurrentlyEnabled) {
            // Mudar para Modo Claro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(getContext(), "Modo Claro Ativado", Toast.LENGTH_SHORT).show();
        } else {
            // Mudar para Modo Escuro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(getContext(), "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
        }

        // Salva a preferência do modo escuro
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_DARK_MODE, !isDarkModeCurrentlyEnabled);
        editor.apply();
    }
}
