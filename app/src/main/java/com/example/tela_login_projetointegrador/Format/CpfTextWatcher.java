package com.example.tela_login_projetointegrador.Format;

import static androidx.core.content.ContextCompat.getColor;

import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class CpfTextWatcher implements TextWatcher {
    private final TextInputLayout layout_cpf;
    private boolean isUpdating;

    public CpfTextWatcher(TextInputLayout layoutCpf) {
        layout_cpf = layoutCpf;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
    public void onTextChanged(CharSequence s, int start, int before,  int count){
        if (isUpdating){
            isUpdating = false;
            return;
        }
        isUpdating = true;

        String text = s.toString().replaceAll("[^\\d]","");
        StringBuilder format = new StringBuilder();

        if (text.length() > 11){
            text = text.substring(0, 11);
        }

        int size = text.length();

        if (size > 9){
            format.append(text, 0, 3).append(".");
            format.append(text, 3, 6).append(".");
            format.append(text, 6, 9).append("-");
            format.append(text.substring(9));
        }else if (size > 6){
            format.append(text, 0, 3).append(".");
            format.append(text, 3, 6).append(".");
            format.append(text.substring(6));
        }else if (size > 3){
            format.append(text, 0, 3).append(".");
            format.append(text.substring(3));
        }else{
            format.append(text);
        }
        ((Editable) s).replace(0, s.length(), format);
        if(text.length() == 11){
            layout_cpf.setHelperText("CPF válido");
            layout_cpf.setHelperTextColor(ColorStateList.valueOf(getColor(layout_cpf.getContext(), R.color.verde)));
            layout_cpf.setHintTextColor(ColorStateList.valueOf(getColor(layout_cpf.getContext(), R.color.verde)));
            layout_cpf.setBoxStrokeColor(ContextCompat.getColor(layout_cpf.getContext(), R.color.verde));
            layout_cpf.setDefaultHintTextColor(ColorStateList.valueOf(getColor(layout_cpf.getContext(), R.color.verde)));
        }else{
            layout_cpf.setHelperText("Por favor, digite um CPF válido");
            layout_cpf.setHelperTextColor(ColorStateList.valueOf(getColor(layout_cpf.getContext(), R.color.vermelho)));
            layout_cpf.setHintTextColor(ColorStateList.valueOf(getColor(layout_cpf.getContext(), R.color.vermelho)));
            layout_cpf.setBoxStrokeColor(ContextCompat.getColor(layout_cpf.getContext(), R.color.vermelho));
            layout_cpf.setDefaultHintTextColor(ColorStateList.valueOf(getColor(layout_cpf.getContext(), R.color.vermelho)));
        }
        isUpdating = false;
    }
    @Override
    public void afterTextChanged(Editable s) {}
}
