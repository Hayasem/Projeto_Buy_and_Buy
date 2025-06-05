package com.example.tela_login_projetointegrador.utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Aplica o modo escuro/claro antes de chamar super.onCreate() para garantir que o tema seja carregado corretamente
        AccessibilityUtils.applyDarkMode(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aplica a escala da fonte sempre que a Activity é retomada
        // Isso garante que se a fonte for alterada em outra Activity/Fragment,
        // as mudanças sejam refletidas quando esta Activity voltar ao foco.
        // Nota: se você tem um layout muito complexo com muitas TextViews,
        // e a performance for um problema, você pode otimizar isso.
        AccessibilityUtils.applyFontScale(getWindow().getDecorView(), AccessibilityUtils.getCurrentFontScale(this));
    }
}
