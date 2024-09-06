package com.example.tela_login_projetointegrador.Format;

import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class EmailTextWatcher implements TextWatcher {

    private final TextInputLayout layout_email;

    public EmailTextWatcher(TextInputLayout layoutEmail) {
        layout_email = layoutEmail;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String email = s.toString();
        if(email.contains("@") && email.contains(".com")){
            layout_email.setBoxStrokeColor(ContextCompat.getColor(layout_email.getContext(), R.color.verde));
            layout_email.setHelperText("Email válido");
            layout_email.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.verde)));
            layout_email.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.verde)));
            layout_email.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.verde)));
            layout_email.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.verde)));
        }else{
            layout_email.setBoxStrokeColor(ContextCompat.getColor(layout_email.getContext(), R.color.vermelho));
            layout_email.setHelperText("Email inválido");
            layout_email.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.vermelho)));
            layout_email.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.vermelho)));
            layout_email.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.vermelho)));
            layout_email.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_email.getContext(), R.color.vermelho)));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
