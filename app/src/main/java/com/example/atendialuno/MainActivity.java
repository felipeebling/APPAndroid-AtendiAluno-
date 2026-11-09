package com.example.atendialuno;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    Button btCadastro, btLogin;
    EditText edNome, edSenha;
    String resposta, nome;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btCadastro = findViewById(R.id.btCadastro);
        btLogin = findViewById(R.id.btlogin);
        edSenha = findViewById(R.id.edSenha);
        edNome = findViewById(R.id.edNome);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerLogin();
                // teste de login


            }
        });

        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TelaCadastro.class);
                startActivity(i);

            }
        });

    }

    public void logar() {

        if (edNome.getText().toString().isEmpty() || edSenha.getText().toString().isEmpty()) {
            Toast.makeText(this, "Campos vazios", Toast.LENGTH_SHORT).show();
        } else {

            String req = "http://10.0.2.2/bancoapi/TrabalhoProgAndroidBancoAPI/login.php?senha="
                    + edSenha.getText().toString() + "&email=" + edNome.getText().toString();
            new Thread(() -> {

                System.out.println("thread");

                manipulahttp http = new manipulahttp();
                resposta += http.requisitaservico(req);

                //tempo

                if (resposta.contains("sucesso")) {
                    Intent i = new Intent(MainActivity.this, TelaPrincipal.class);
                    try {
                        JSONObject jb = new JSONObject(resposta);
                        i.putExtra("id", jb.get("id")+"");
                        i.putExtra("nome", jb.get("nome")+"");
                        startActivity(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                } else {
                    Toast.makeText(this, "Usuario ou senha incorretos", Toast.LENGTH_LONG).show();
                }

            }).start();
        }


    }


    private void fazerLogin() {
        Intent i = new Intent(MainActivity.this, TelaPrincipal.class);
        System.out.println("cliclou ");

        if (edNome.getText().toString().isEmpty() || edSenha.getText().toString().isEmpty()) {
            Toast.makeText(this, "Campos vazios", Toast.LENGTH_SHORT).show();
        } else {

            String req = "http://10.0.2.2/bancoapi/TrabalhoProgAndroidBancoAPI/login.php?senha="
                    + edSenha.getText().toString() + "&email=" + edNome.getText().toString();

            new Thread(() -> {
                try {

                    System.out.println("entrou na thred");
                    manipulahttp http = new manipulahttp();
                    String resposta = http.requisitaservico(req);

                    if (!resposta.isEmpty()) {
                        JSONObject json = new JSONObject(resposta);
                        String status = json.getString("status");


                        if (status.equals("sucesso")) {

                            id = json.getInt("id");
                            System.out.println(id +" id pego pelo json");
                            nome = json.getString("nome");
                            System.out.println("id= " +id +"\n" + "nome= " + nome);



                        runOnUiThread(() -> {


                            i.putExtra("id", id);
                            i.putExtra("nome", nome);
                            startActivity(i);


                        });

                        } else if (status.equals("erro")) {

                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Erro Usuario invalido", Toast.LENGTH_SHORT).show();
                            });


                        }

                    } else {
                        //erro
                        System.out.println("ERRo requisi~ção");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

}