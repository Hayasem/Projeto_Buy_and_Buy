package com.example.tela_login_projetointegrador.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.tela_login_projetointegrador.R;

import java.util.Objects;

public class FragmentCadastrarProdutos extends Fragment {
    private Button btnCadastrarProdutos;
    private TextView tvNenhumProduto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cadastrar_produtos, container, false);
        btnCadastrarProdutos = view.findViewById(R.id.btn_cadastrar_produtos);
        tvNenhumProduto = view.findViewById(R.id.tv_nenhum_produto);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Perfil");
        }
        return view;
    }


}
