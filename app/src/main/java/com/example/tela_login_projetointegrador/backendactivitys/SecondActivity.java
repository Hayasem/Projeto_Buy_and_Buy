package com.example.tela_login_projetointegrador.backendactivitys;
import android.os.Bundle;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.fragment.FragmentNotificacao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

public class SecondActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.homeId); //seleciona o menu da primeira tela aberta

        if (savedInstanceState == null) { // Carregar a primeira tela (Fragment)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuScreen())
                    .commit();
        }

    }
    @Override
    public boolean onNavigationItemSelected( @NotNull MenuItem menuItem) {// metodo responsal por fazer a navegacao entre as telas

        if(menuItem.getItemId()==R.id.homeId){
            Fragment menuScreen = MenuScreen.newInstance();
            openFragment(menuScreen);
            return true;
        }else if(menuItem.getItemId() == R.id.notificacaoId){
            Fragment fragmentNotificacao = FragmentNotificacao.newInstance();
            openFragment(fragmentNotificacao);
            return true;
        }else if (menuItem.getItemId() ==R.id.carrinhoId) {
            return true;
        }else if (menuItem.getItemId() ==R.id.profileId) {
            Fragment telaPerfilUsuario = TelaPerfilUsuario.newInstance();
            openFragment(telaPerfilUsuario);
            return true;
        }
        return false;
    }

    private void openFragment(Fragment fragment) { //metodo generico para efetuar a troca de fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
