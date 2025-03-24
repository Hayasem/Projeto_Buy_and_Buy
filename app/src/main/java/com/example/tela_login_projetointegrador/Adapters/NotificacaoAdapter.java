package com.example.tela_login_projetointegrador.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.HolderNotificacao;
import org.jetbrains.annotations.NotNull;

public class NotificacaoAdapter extends ArrayAdapter<HolderNotificacao> {

    private Context context;
    private int resource;

    //Contrutor para receber a lista de notificações
    public NotificacaoAdapter(@NonNull @NotNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView txtTitulo = convertView.findViewById(R.id.idTitulo);
        TextView txtSubtitulo = convertView.findViewById(R.id.idDescricaoNotification);

        // Exiba o item nas TextViews
        txtTitulo.setText(getItem(position).getTitulo());
        txtSubtitulo.setText(getItem(position).getDescricao());

        return convertView;
    }
}
