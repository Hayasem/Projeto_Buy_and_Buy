package com.example.tela_login_projetointegrador.Formats;

import static androidx.core.content.ContextCompat.*;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class SenhaTextWatcher implements TextWatcher {

    private final TextInputLayout helper_text;

    public SenhaTextWatcher(TextInputLayout helperText) {
        helper_text = helperText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatenNivelSenhaText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void updatenNivelSenhaText(String senha){
        if(senha.length() < 8){
            helper_text.setHintTextColor(ColorStateList.valueOf(Color.RED));
            helper_text.setHelperText("Senha fraca. Utilize letras maiúsculas.");
            helper_text.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            helper_text.setBoxStrokeColor(Color.RED);
        }else{
            boolean hasUppercase = !senha.equals(senha.toLowerCase());
            boolean hasSpecialChar = senha.matches(".*[!@#$%^&*()\\-_=+{};:,<.>].*");


            if (hasUppercase && hasSpecialChar) {
                helper_text.setHelperText("Senha forte. Pode dar prosseguimento");
                helper_text.setHelperTextColor(ColorStateList.valueOf(getColor(helper_text.getContext(), R.color.verde)));
                helper_text.setHintTextColor(ColorStateList.valueOf(getColor(helper_text.getContext(), R.color.verde)));
                helper_text.setBoxStrokeColor(ContextCompat.getColor(helper_text.getContext(), R.color.verde));
                helper_text.setDefaultHintTextColor(ColorStateList.valueOf(getColor(helper_text.getContext(), R.color.verde)));
            }else{
                helper_text.setHelperText("Senha média. Utilize caracteres especiais");
                helper_text.setHintTextColor(ColorStateList.valueOf(getColor(helper_text.getContext(), R.color.alaranjado)));
                helper_text.setDefaultHintTextColor(ColorStateList.valueOf(getColor(helper_text.getContext(), R.color.alaranjado)));
                helper_text.setHelperTextColor(ColorStateList.valueOf(getColor(helper_text.getContext(), R.color.alaranjado)));
                helper_text.setBoxStrokeColor(ContextCompat.getColor(helper_text.getContext(), R.color.alaranjado));
            }
        }
    }
}
