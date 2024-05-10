package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.example.tela_login_projetointegrador.fragment.FragmentCadastrarProdutos;
import com.example.tela_login_projetointegrador.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaPerfilUsuario extends Fragment {
   private TextView nomeUsuario, emailUsuario, meusProdutos;
   private ImageView viewProducts;
   private Button bt_deslogar;
    private DatabaseConnection db;
   private UserManager userManager;
   int idUsuario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // foi alterado para fragment por questão de melhores praticas, uma vez que não a necessidade de atualizar a tela toda, exemplo o menu e appbar não precisa ficar
        // sendo carregado a toda mudança de tela pois eles não se alteram.
        //https://dev.to/alexandrefreire/qual-a-diferenca-entre-activity-fragmentactivity-e-fragment-216o
        View view = inflater.inflate(R.layout.activity_tela_perfil_usuario, container, false);

        nomeUsuario = view.findViewById(R.id.text_nome_usuario);
        emailUsuario =view.findViewById(R.id.text_email_usuario);
        bt_deslogar = view.findViewById(R.id.bt_deslogar);
        meusProdutos = view.findViewById(R.id.txt_meus_produtos);
        viewProducts = view.findViewById(R.id.imgbtn_products);

        DatabaseConnection databaseConnection = new DatabaseConnection(requireContext());
        SQLiteDatabase db = databaseConnection.getReadableDatabase();
        userManager = new UserManager(db);

        bt_deslogar.setOnClickListener(view1 -> {
            userManager.deslogarUsuario();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        viewProducts.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentProdutos, new FragmentCadastrarProdutos());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    public static TelaPerfilUsuario newInstance(){
        return new TelaPerfilUsuario();
    }


    //Recuperando o ID:
    @Override
    public void onStart() {
        super.onStart();

        Usuario usuarioAtual = userManager.consultarUsuario(idUsuario);
        if (usuarioAtual != null){
            nomeUsuario.setText(usuarioAtual.getNome());
            emailUsuario.setText(usuarioAtual.getEmail());
        }else{
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }
}
