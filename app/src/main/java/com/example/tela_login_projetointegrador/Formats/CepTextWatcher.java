package com.example.tela_login_projetointegrador.Formats;

import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText; // Importe EditText

import androidx.core.content.ContextCompat;

import com.example.tela_login_projetointegrador.R;
import com.google.android.material.textfield.TextInputLayout;

public class CepTextWatcher implements TextWatcher {
    private final TextInputLayout layout_cep;
    private final EditText editTextCep; // Adicione uma referência ao EditText
    private boolean isUpdating;
    private String current = "";

    public CepTextWatcher(TextInputLayout layoutCep, EditText editTextCep) {
        layout_cep = layoutCep;
        this.editTextCep = editTextCep;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        current = s.toString(); // Salva o estado atual do texto
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Nada a fazer aqui se a lógica principal estiver em afterTextChanged
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        String text = s.toString().replaceAll("[^\\d]", ""); // Remove não dígitos
        String formattedText = "";

        if (text.length() > 8) { // Limita a 8 dígitos para o CEP numérico
            text = text.substring(0, 8);
        }

        if (text.length() > 5) {
            formattedText = text.substring(0, 5) + "-" + text.substring(5);
        } else {
            formattedText = text;
        }

        if (!current.equals(formattedText)) {
            isUpdating = true;
            editTextCep.setText(formattedText);
            editTextCep.setSelection(formattedText.length()); // Posiciona o cursor no final
        }

        if (text.length() == 8) {
            layout_cep.setHelperText("CEP válido");
            layout_cep.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_cep.getContext(), R.color.verde)));
            layout_cep.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_cep.getContext(), R.color.verde)));
            layout_cep.setBoxStrokeColor(ContextCompat.getColor(layout_cep.getContext(), R.color.verde));
            // layout_cep.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_cep.getContext(), R.color.verde))); // Isso pode não ser necessário ou pode sobrepor o hintTextColor
        } else {
            layout_cep.setHelperText("Por favor, digite um CEP válido");
            layout_cep.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_cep.getContext(), R.color.vermelho)));
            layout_cep.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_cep.getContext(), R.color.vermelho)));
            layout_cep.setBoxStrokeColor(ContextCompat.getColor(layout_cep.getContext(), R.color.vermelho));
            // layout_cep.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(layout_cep.getContext(), R.color.vermelho))); // Isso pode não ser necessário ou pode sobrepor o hintTextColor
        }
    }
}