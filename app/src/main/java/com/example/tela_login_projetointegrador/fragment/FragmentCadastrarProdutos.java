package com.example.tela_login_projetointegrador.fragment;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.ProductManager;
import com.example.tela_login_projetointegrador.model.CategoriaProduto;
import com.example.tela_login_projetointegrador.model.Produto;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentCadastrarProdutos extends Fragment {


    private EditText edit_nome_produto;
    private EditText edit_preco_produto;
    private Spinner spinner_categoria_produto;
    private EditText editDescricaoProduto;
    private EditText editQuantidadeProduto;
    private Button btn_confirmar_cadastro_produto;
    private ImageView imageView;
    private String imageString;
    private ProductManager productManager;
    private DatabaseReference productsDataRef;

    String[] mensagens = {"Preencha todos os campos", "Produto Cadastrado com sucesso"};
    private static final int PICK_IMAGE_REQUEST = 1;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_de_produtos, container, false);
        validaPermissao();
        edit_nome_produto = view.findViewById(R.id.edit_nome_produto);
        edit_preco_produto = view.findViewById(R.id.edit_preco_produto);
        spinner_categoria_produto = view.findViewById(R.id.spinner_categoria_produto);
        editDescricaoProduto = view.findViewById(R.id.edit_descricao_produto);
        editQuantidadeProduto = view.findViewById(R.id.edit_quantidade_produto);
        btn_confirmar_cadastro_produto = view.findViewById(R.id.btn_confirmar_cadastro_produto);
        imageView = view.findViewById(R.id.imageView2);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productsDataRef = database.getReference("produtos");

        ArrayAdapter<CategoriaProduto> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,carregaCategorias());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categoria_produto.setAdapter(adapter);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Perfil");
        }
        final int[] categoriaId = {0};
        spinner_categoria_produto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CategoriaProduto categoriaSelecionada = (CategoriaProduto) parentView.getItemAtPosition(position);
                categoriaId[0] = categoriaSelecionada.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nenhuma imagem selecionada
            }
        });

        btn_confirmar_cadastro_produto.setOnClickListener(v -> {
            String titulo = edit_nome_produto.getText().toString();
            String descricao = editDescricaoProduto.getText().toString();
            String preco = edit_preco_produto.getText().toString();
            int quantidadeProd = Integer.parseInt(editQuantidadeProduto.getText().toString());

            if (titulo.isEmpty() || descricao.isEmpty() || preco.isEmpty() || categoriaId[0] == 0) {
                exibirSnackbar(mensagens[0], v);
            }else{
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userId = auth.getCurrentUser().getUid();
                Produto produto = new Produto();
                produto.setIdUsuario(userId);
                produto.setTitulo(titulo);
                produto.setDescricao(descricao);
                produto.setPreco(Float.parseFloat(preco));
                produto.setIdCategoria(categoriaId[0]);
                produto.setImagem(imageString);
                produto.setQuantidadeDisponivel(quantidadeProd);
                salvarProdutosFirebase(produto, v);
                limparCampos();
            }
        });
        imageView.setOnClickListener(v -> openImageChooser());
        return view;
    }

    private void salvarProdutosFirebase(Produto produto, View view){
        String productId = productsDataRef.push().getKey();
        produto.setIdProduto(productId);
        if (productId != null){
            productsDataRef.child(productId).setValue(produto.toMap())
                    .addOnSuccessListener(aVoid -> exibirSnackbar(mensagens[1],view))
                    .addOnFailureListener(e -> exibirSnackbar("Ocorreu um erro ao salvar o produto: " + e.getMessage(), view));
        }
    }

    private void validaPermissao(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permissão do usuario para acessar as pastas do android
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    1);
        }
    }

    private void limparCampos(){
        edit_nome_produto.setText("");
        editDescricaoProduto.setText("");
        edit_preco_produto.setText("");
        editQuantidadeProduto.setText("");
        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        imageString= null;
    }


    private List<CategoriaProduto> carregaCategorias(){
        List<CategoriaProduto> categorias = new ArrayList<>();
        categorias.add(new CategoriaProduto(1, "Eletronicos"));
        categorias.add(new CategoriaProduto(2, "Brinquedos"));
        categorias.add(new CategoriaProduto(3, "Vestuários"));
        categorias.add(new CategoriaProduto(4, "Cama/Mesa/Banho"));
        return categorias;
    }

    private void openImageChooser() {
        imageString = null;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private Bitmap redimensionarBitmap(Bitmap bitmap, int width, int height){
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
            } else {
                // Permissão negada
                Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == requireActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                Bitmap bitmapRedimensionado = redimensionarBitmap(bitmap, 950, 900);

                // Exibir a imagem no ImageView
                imageView.setImageBitmap(bitmapRedimensionado);

                // Salvar a imagem no dispositivo e obter o caminho
                 imageString = saveImageToInternalStorage(bitmapRedimensionado
                 );

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(requireContext());
        // Diretório para salvar a imagem
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Cria um arquivo com nome único
        File imageFile = new File(directory, "image_" + System.currentTimeMillis() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            // Usa o método compress para salvar a imagem no formato JPEG com qualidade 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retorna o caminho absoluto do arquivo salvo
        return imageFile.getAbsolutePath();
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private void saveImageToDatabase(String imageString) {
        // Aqui você deve implementar o código para salvar a string 'imageString' no banco de dados
        Toast.makeText(getActivity(), "Image saved to database!", Toast.LENGTH_SHORT).show();
    }
    private void exibirSnackbar(String erro, View view) {
        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.GREEN);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }
}
