package com.example.tela_login_projetointegrador.utils;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Aplica a escala da fonte a todas as views do fragmento quando ele é criado/recriado
        // getContext() ou requireContext() pode ser usado aqui
        AccessibilityUtils.applyFontScale(view, AccessibilityUtils.getCurrentFontScale(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Opcional: Reaplicar a fonte no onResume.
        // Isso é útil se a fonte for alterada enquanto este fragmento está em background
        // e ele precisa ser atualizado quando voltar ao foreground sem recriar a Activity.
        // Se sua Activity já recria no changeFontSize, pode não ser estritamente necessário aqui,
        // mas é uma boa prática para robustez.
        if (getView() != null) {
            AccessibilityUtils.applyFontScale(getView(), AccessibilityUtils.getCurrentFontScale(requireContext()));
        }
    }
}
