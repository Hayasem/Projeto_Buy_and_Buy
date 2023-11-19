package com.example.tela_login_projetointegrador;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.Adapter.MyAdapter;
import com.example.tela_login_projetointegrador.model.Product;
import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends AppCompatActivity {

    private ImageButton bt_perfil;
    private ImageButton bt_notificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        IniciarComponentes();

        bt_perfil.setOnClickListener(v -> {
            Intent intent = new Intent(MenuScreen.this, TelaPerfilUsuario.class);
            startActivity(intent);
        });

        bt_notificacao.setOnClickListener(v -> {
            Intent intent = new Intent(MenuScreen.this, ActivityNotificacao.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView_products);
        List<Product> productList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), productList));
        recyclerView.setHasFixedSize(true);

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

    }
    private void IniciarComponentes(){
        bt_perfil = findViewById(R.id.bt_perfil);
        bt_notificacao = findViewById(R.id.bt_notification);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_products);

    }


}
