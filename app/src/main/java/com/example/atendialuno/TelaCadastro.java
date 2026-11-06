package com.example.atendialuno;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class TelaCadastro extends AppCompatActivity {
    EditText nomeProfessor,emailProfessor,senhaProfessor;
    Button BtCadastro;
    String resposta = "";
    boolean cadastrou = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nomeProfessor = findViewById(R.id.edNomeProfessor);
        emailProfessor = findViewById(R.id.edNomeProfessor);
        senhaProfessor = findViewById(R.id.edSenhaProfesssor);



        BtCadastro = findViewById(R.id.btCadastrarProfessor);

        BtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nomeProfessor.getText().toString().isEmpty()  ||
                    emailProfessor.getText().toString().isEmpty() ||
                    senhaProfessor.getText().toString().isEmpty()
                ){
                    Toast.makeText(TelaCadastro.this, "Campos vazios", Toast.LENGTH_SHORT).show();
                }else {
                    cadastrar(nomeProfessor.getText().toString(),emailProfessor.getText().toString()
                            ,senhaProfessor.getText().toString());

                }
            }
        });



    }

    public void cadastrar(String nome, String email,String senha){

        String req = montarequisição(nome,email,senha);



        new Thread(() -> {
            manipulahttp http = new manipulahttp();
            resposta = http.requisitaservico(req);

            //tempo


                Intent i = new Intent(TelaCadastro.this, MainActivity.class);
                try {
                    JSONObject jb = new JSONObject(resposta);
                    String status = jb.getString("status");

                    if (status.equals("sucesso"))
                         cadastrou = true;


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (cadastrou) {
                        Toast.makeText(TelaCadastro.this, "Usuario criad", Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }else{
                        Toast.makeText(TelaCadastro.this, "Erro", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }).start();





    }

    public String montarequisição(String name, String email,String password){

        return "http://10.0.2.2/bancoapi/TrabalhoProgAndroidBancoAPI/cadastro.php?" +
                "senha="+password+"&email=" +email + "&nome=" + name;

    }
}
