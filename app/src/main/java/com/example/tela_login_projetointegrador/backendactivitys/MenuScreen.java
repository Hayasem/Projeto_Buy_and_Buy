package com.example.tela_login_projetointegrador.backendactivitys;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.R;

public class MenuScreen extends Fragment {

    private ImageButton bt_perfil;
    private ImageButton bt_notificacao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_screen, container, false);

        // foi alterado para fragment por questão de melhores praticas, uma vez que não há necessidade de atualizar a tela toda, exemplo o menu e appbar não precisa ficar
        // sendo carregado a toda mudança de tela pois eles não se alteram.

        //https://dev.to/alexandrefreire/qual-a-diferenca-entre-activity-fragmentactivity-e-fragment-216o
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    public static MenuScreen newInstance(){
        return new MenuScreen();
    }

}