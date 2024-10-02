package com.example.tela_login_projetointegrador.utils;

public class Utils {


    /*Validações Usuários*/

    public static boolean isEmailvalido(String email){
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isCampoVazio(String campo){
        return campo.isEmpty();
    }

    public static boolean isCPFValido(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        return cpf.matches("\\d{11}");
    }
    public static boolean isCepValido(String cep) {
        cep = cep.replaceAll("\\D", "");
        return cep.matches("\\d{8}");
    }

    public static boolean isValidaCelular(String numero) {
        numero = numero.replace("-", "").replace(" ", "");
        return numero.matches("\\d{11}");
    }

    public static String limparTelefone(String numero) {
        return numero.replace("-", "").replace(" ", "");
    }

    public static Bitmap loadImageFromInternalStorage(String path) {
        try {
            File imageFile = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();


        }
        return null;
    }

    public static String obterDataHoraAtual() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formato);
    }
}
