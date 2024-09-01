package com.example.tela_login_projetointegrador.Format;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
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
                helper_text.setHintTextColor(ColorStateList.valueOf(Color.GREEN));
                helper_text.setHelperText("Senha forte. Pode dar prosseguimento");
                helper_text.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                helper_text.setBoxStrokeColor(Color.GREEN);
            }else{
                helper_text.setHintTextColor(ColorStateList.valueOf(Color.YELLOW));
                helper_text.setHelperText("Senha média. Utilize caracteres especiais");
                helper_text.setHelperTextColor(ColorStateList.valueOf(Color.YELLOW));
                helper_text.setBoxStrokeColor(Color.YELLOW);
            }
        }
    }
}
