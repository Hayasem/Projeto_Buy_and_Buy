package com.example.tela_login_projetointegrador.Format;

import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class NomeTextWatcher implements TextWatcher {
    private final TextInputLayout layout_nome;

    public NomeTextWatcher(TextInputLayout layoutNome) {
        layout_nome = layoutNome;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String nomeUsuario = s.toString();
        if (nomeUsuario.length() > 3){
            layout_nome.setBoxStrokeColor(ContextCompat.getColor(layout_nome.getContext(), R.color.verde));
            layout_nome.setHelperText("Nome válido");
            layout_nome.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.verde)));
            layout_nome.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.verde)));
            layout_nome.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.verde)));
            layout_nome.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.verde)));
        }else{
            layout_nome.setBoxStrokeColor(ContextCompat.getColor(layout_nome.getContext(), R.color.vermelho));
            layout_nome.setHelperText("Por favor, insira um nome válido");
            layout_nome.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.vermelho)));
            layout_nome.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.vermelho)));
            layout_nome.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.vermelho)));
            layout_nome.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_nome.getContext(), R.color.vermelho)));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
