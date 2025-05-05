package com.example.tela_login_projetointegrador.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.activities.MainActivity;
import com.example.tela_login_projetointegrador.activities.SecondActivity;

public class FragmentPix extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pagamento_pix, container, false); // Troque pelo seu l

        TextView btnVoltarHome = view.findViewById(R.id.btnVoltarHome);

        btnVoltarHome.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), MainActivity.class); // Substitua pela sua Activity real
            startActivity(intent);
            requireActivity().finish(); // Encerra a Activity atual, se for o caso
        });

        return view;
    }
}