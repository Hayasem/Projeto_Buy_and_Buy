package com.example.tela_login_projetointegrador.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.activities.MainActivity;
import com.example.tela_login_projetointegrador.utils.BaseFragment;

public class FragmentPagamentoDebito extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.layout_pagamento_debito_sucesso, container, false);
        TextView btnVoltarHome = view.findViewById(R.id.btnVoltarHome);

        btnVoltarHome.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
