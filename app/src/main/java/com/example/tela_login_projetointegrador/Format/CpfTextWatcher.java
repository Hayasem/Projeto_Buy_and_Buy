package com.example.tela_login_projetointegrador.Format;

import android.text.Editable;
import android.text.TextWatcher;

public class CpfTextWatcher implements TextWatcher {
    private boolean isUpdating;
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
    }
    @Override
    public void afterTextChanged(Editable s) {}
}
