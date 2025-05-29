package com.example.tela_login_projetointegrador.activities;
import android.os.Bundle;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.fragments.CartFragment;
import com.example.tela_login_projetointegrador.fragments.FragmentNotificacao;
import com.example.tela_login_projetointegrador.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

public class SecondActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private NotificationBadge notificationBadge;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        notificationBadge = findViewById(R.id.idBadgeNotification);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_view);
        updateCartBadge(5);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.homeId); //seleciona o menu da primeira tela aberta

        if (savedInstanceState == null) { // Carregar a primeira tela (Fragment)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentProdutos, new HomeActivity())
                    .commit();
        }

    }
    private void updateCartBadge(int count) {
        if (count > 0) {
            notificationBadge.setVisibility(View.VISIBLE);
            notificationBadge.setText(String.valueOf(count)); // Número de itens no carrinho
        } else {
            notificationBadge.setVisibility(View.GONE);
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onNavigationItemSelected( @NotNull MenuItem menuItem) {
        // metodo responsavel por fazer a navegacao entre as telas

        if(menuItem.getItemId()==R.id.homeId){
            Fragment menuScreen = HomeActivity.newInstance();
            openFragment(menuScreen);
            return true;
        }else if(menuItem.getItemId() == R.id.notification_icon){
            Fragment fragmentNotificacao = FragmentNotificacao.newInstance();
            openFragment(fragmentNotificacao);
            return true;
        }else if (menuItem.getItemId() ==R.id.carrinhoId) {
            Fragment cartScreen = CartFragment.newInstance();
            openFragment(cartScreen);
            return true;
        }else if (menuItem.getItemId() ==R.id.profileId) {
            Fragment telaPerfilUsuario = UserFragment.newInstance();
            openFragment(telaPerfilUsuario);
            return true;
        }
        return false;
    }

    private void openFragment(Fragment fragment) { //metodo generico para efetuar a troca de fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentProdutos, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
    