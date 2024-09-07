package com.example.tela_login_projetointegrador.Format;

import static androidx.core.content.ContextCompat.getColor;

import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class TelefoneTextWatcher implements TextWatcher {
    private final TextInputLayout layout_telefone;
    private boolean isUpdating;

    public TelefoneTextWatcher(TextInputLayout layoutTelefone) {
        layout_telefone = layoutTelefone;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isUpdating){
            isUpdating =false;
            return;
        }
        isUpdating = true;

        String text = s.toString().replaceAll("\\D","");
        StringBuilder format = new StringBuilder();

        if (text.length() > 11){
            text = text.substring(0, 11);
        }

        int size = text.length();

        if (size > 10){
            format.append("(").append(text, 0, 2).append(") ").append(text, 2, 7)
                    .append("-").append(text.substring(7));
        }else if (size > 5){
            format.append("(").append(text, 0, 2).append(") ").append(text.substring(2, Math.min(size, 7)))
                    .append(size > 6 ? "-" + text.substring(7) : "")    ;
        }else if (size > 2){
            format.append("(").append(text, 0, 2).append(") ").append(text.substring(2));
        }else{
            format.append(text);
        }
        layout_telefone.getEditText().setText(format.toString());
        layout_telefone.getEditText().setSelection(format.length());

        if(text.length() == 11){
            layout_telefone.setHelperText("Número de telefone válido");
            layout_telefone.setHelperTextColor(ColorStateList.valueOf(getColor(layout_telefone.getContext(), R.color.verde)));
            layout_telefone.setHintTextColor(ColorStateList.valueOf(getColor(layout_telefone.getContext(), R.color.verde)));
            layout_telefone.setBoxStrokeColor(ContextCompat.getColor(layout_telefone.getContext(), R.color.verde));
            layout_telefone.setDefaultHintTextColor(ColorStateList.valueOf(getColor(layout_telefone.getContext(), R.color.verde)));
        }else{
            layout_telefone.setHelperText("Por favor, digite um número válido");
            layout_telefone.setHelperTextColor(ColorStateList.valueOf(getColor(layout_telefone.getContext(), R.color.vermelho)));
            layout_telefone.setHintTextColor(ColorStateList.valueOf(getColor(layout_telefone.getContext(), R.color.vermelho)));
            layout_telefone.setBoxStrokeColor(ContextCompat.getColor(layout_telefone.getContext(), R.color.vermelho));
            layout_telefone.setDefaultHintTextColor(ColorStateList.valueOf(getColor(layout_telefone.getContext(), R.color.vermelho)));
        }
        isUpdating = false;
    }
    @Override
    public void afterTextChanged(Editable s) {

    }
}
