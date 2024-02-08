package com.example.tela_login_projetointegrador.backendactivitys;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.Adapter.MyAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends Fragment {

    private ImageButton bt_perfil;
    private ImageButton bt_notificacao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_screen, container, false);
        // foi alterado para fragment por questão de melhores praticas, uma vez que não a necessidade de atualizar a tela toda, exemplo o menu e appbar não precisa ficar
        // sendo carregado a toda mudança de tela pois eles não se alteram.
        //https://dev.to/alexandrefreire/qual-a-diferenca-entre-activity-fragmentactivity-e-fragment-216o
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_products);
        List<Product> productList = carregaDados();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter(getContext(), productList));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    public static MenuScreen newInstance(){
        return new MenuScreen();
    }

    private List<Product> carregaDados(){
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product(R.drawable.img_backpack,
                "Mochila militar",
                "Uma mochila perfeita para aqueles que gostam de se aventurar. Consta com 6 diferentes bolsos a fim de armazenar tudo de essêncial, a fim de atender até aqueles com mais preparo. Anti-impermeável, e fácil de se ajustar, uma dos top modelos da loja.",
                "R$ 149,99");
        productList.add(product1);

        Product product2 = new Product(R.drawable.img_racket,
                "Raquete elétrica de alta tensão",
                "Não aguenta mais aqueles mosquitos atrapalhando seu sono durante a madrugada? Acabe com todos eles utilizando nosso mais novo modelo de raquete elétrica de alta tensão. Você merece ter uma boa noite de sono depois de um longo dia de trabalho, não deixe mais esses pequenos encreiqueiros te incomodarem.",
                "R$ 79,99");
        productList.add(product2);

        Product product3 = new Product(R.drawable.img_sneakers,
                "Tênis Yonex",
                "Tênis perfeitamente equilibrado entre adrenalina e conforto, seus pés vão se sentir no paraíso ao calçar. Fácil de limpar, e adaptável a circunferência de seus pés.",
                "R$ 200,00");
        productList.add(product3);

        Product product4 = new Product(R.drawable.img_headset,
                "Headset logitech",
                "Para os gamers de plantão que costumam virar a noite pra subir de ranked, todos sabem que escutar os passos do asversário é sempre importante para se ter vantagem no game, levando isso em conta, o Headset Logitec é incrivelmente perfeito para você.",
                "R$ 300,00");
        productList.add(product4);

        Product product5 = new Product(R.drawable.bolsa_couro,
                "Bolsa de couro",
                "Para todo o lugar que vamos, sempre há algo que levamos junto pois sabemos que será necessário, mas, e se precisarmos levar mais coisas? Para isso, tenha certeza de sempre levar sua bolsa contigo.",
                "R$ 100,00");
        productList.add(product5);

        Product product6 = new Product(R.drawable.oculos_de_sol,
                "Óculos de sol",
                "Cansado de, mesmo em um dia tão lindo não poder olhar para o sol? Pois seus problemas acabaram, venha confirir nosso mais novo modelo de óculos de sol testado e aprovado por 327 de 328 cientistas.",
                "R$ 20,00");
        productList.add(product6);

        Product product7 = new Product(R.drawable.calca_jeans,
                "Calça Jeans",
                "Calça perfeita para aqueles que desejam estar na moda, e se sentirem confortáveis ao mesmo tempo. Por que não uma calça Jeans para ficar com o look de arrasar em?",
                "R$ 80,00");
        productList.add(product7);

        Product product8 = new Product(R.drawable.camisa_one_piece,
                "Camiseta One Piece",
                "Você é aquele tipo de fã que ama tanto a obra, que coleciona todo tipo de coisa sobre? Então sinta-se a vontade para experimentar nossas estampas ÉPICAS de animes e mangás.",
                "R$ 59,99");
        productList.add(product8);

        Product product9 = new Product(R.drawable.relogio_pulso,
                "Relógio de pulso cor ouro",
                "Relógio de pulso feito especialmente para aqueles que perdem a noção do tempo devido a monotoniedade de suas vidas, exercitem-se mais...sério",
                "R$ 89,99");
        productList.add(product9);
        return productList;
    }
}