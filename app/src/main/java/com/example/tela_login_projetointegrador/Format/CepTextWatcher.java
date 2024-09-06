package com.example.tela_login_projetointegrador.Format;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class CepTextWatcher implements TextWatcher {
    private boolean isUpdating;


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

    }

    @Override
    public void afterTextChanged(Editable s) {}
}
