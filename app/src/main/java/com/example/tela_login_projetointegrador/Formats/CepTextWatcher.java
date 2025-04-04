package com.example.tela_login_projetointegrador.Formats;

import static androidx.core.content.ContextCompat.getColor;

import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class CepTextWatcher implements TextWatcher {
    private final TextInputLayout layout_cep;
    private boolean isUpdating;

    public CepTextWatcher(TextInputLayout layoutCep) {
        layout_cep = layoutCep;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isUpdating) {
            return;
        }
        isUpdating = true;

        String text = s.toString().replaceAll("[^\\d]", "");
        StringBuilder format = new StringBuilder();

        if (text.length() > 8) {
            text = text.substring(0, 8);
        }

        int size = text.length();

        if (size > 5) {
            format.append(text, 0, 5).append("-");
            format.append(text.substring(5));
        } else {
            format.append(text);
        }
        ((Editable) s).replace(0, s.length(), format);

        if(text.length() == 8){
            layout_cep.setHelperText("CEP válido");
            layout_cep.setHelperTextColor(ColorStateList.valueOf(getColor(layout_cep.getContext(), R.color.verde)));
            layout_cep.setHintTextColor(ColorStateList.valueOf(getColor(layout_cep.getContext(), R.color.verde)));
            layout_cep.setBoxStrokeColor(ContextCompat.getColor(layout_cep.getContext(), R.color.verde));
            layout_cep.setDefaultHintTextColor(ColorStateList.valueOf(getColor(layout_cep.getContext(), R.color.verde)));
        }else{
            layout_cep.setHelperText("Por favor, digite um CEP válido");
            layout_cep.setHelperTextColor(ColorStateList.valueOf(getColor(layout_cep.getContext(), R.color.vermelho)));
            layout_cep.setHintTextColor(ColorStateList.valueOf(getColor(layout_cep.getContext(), R.color.vermelho)));
            layout_cep.setBoxStrokeColor(ContextCompat.getColor(layout_cep.getContext(), R.color.vermelho));
            layout_cep.setDefaultHintTextColor(ColorStateList.valueOf(getColor(layout_cep.getContext(), R.color.vermelho)));
        }
        isUpdating = false;
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
