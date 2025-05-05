package com.example.tela_login_projetointegrador.Adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tela_login_projetointegrador.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public CheckBox checkBox;
    public TextView title, message, timestamp;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkbox_select);
        title = itemView.findViewById(R.id.text_title);
        message = itemView.findViewById(R.id.text_message);
        timestamp = itemView.findViewById(R.id.text_timestamp);
    }
}
