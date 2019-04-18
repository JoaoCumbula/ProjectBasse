package com.example.aspire.projectba.Others;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspire.projectba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email, senha;
    private TextView manual, content;
    private Button btnEntrar, cadastrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializaComponentes();
        eventClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFireBaseAuth();
        if (auth.getCurrentUser() != null) ;
    }


    private void inicializaComponentes() {
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        btnEntrar = (Button) findViewById(R.id.entrar);
        cadastrar = (Button) findViewById(R.id.cadastrar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        manual = (TextView) findViewById(R.id.guia);
        progressBar.setVisibility(View.GONE);
    }

    private void eventClick() {
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.GONE);
                final String getEmail = email.getText().toString().trim();
                final String getSenha = senha.getText().toString().trim();

                if (getEmail.isEmpty()) {
                    email.setError("Necessario Preencher este Campo");
                    email.requestFocus();
                    return;
                }

                if (getSenha.isEmpty()) {
                    senha.setError("Necessario Preencher este Campo");
                    senha.requestFocus();
                    return;
                }

                if (getSenha.length() < 6) {
                    senha.setError("Password deve conter pelo menos 6 caracteres");
                    senha.requestFocus();
                    return;
                }

                    /*if (getSenha.equals("1234@Basse")) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                        senha.setText("");
                        alert("Bem Vindo");
                    }

                    if(!getSenha.equals("1234@Basse"))
                        alert("Senha Invalida");*/
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(getEmail, getSenha).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                            startActivity(new Intent(Login.this, MainActivity.class));
                        else
                            alert("Email ou Senha Incorrecta");
                    }
                });
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Cadastro.class));
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder d = new AlertDialog.Builder(Login.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_manual, null);
                content = (TextView) view.findViewById(R.id.content);
                content.setText("A Aplicação Basse tem como objectivo facilitar/Automatizar do Alarme equipado na Residência. " +
                        "\n\nCOMO UTILIZAR??" +
                        "\nÉ necessário cadastrar a conta com dados pessoais, a mesma conta deve estar de preferência associada" +
                        "a um endereço e-mail (hotmail, gmail, yahoo)" +
                        "Após a criação esta habilitado/a a iniciar a sessão" +
                        "\n\nCONFIGURAÇÃO" +
                        "\nDando Inicio a página principal, e necessário que adicione o alarme correspondente a residência com dados" +
                        "que serão fornecidos pela BASSE (O contacto e código de Autorização)." +
                        "Será necessário selecionar zonas que correspondem a cada compartimento (cômodos) da residência" +
                        "\n\nCOMO UTILIZAR" +
                        "\nDe seguida, o Alarme estará pronto para ser utilizado, clicando no Alarme tem as opções de armar" +
                        "e desarmar em cada compartimento (cozinha, sala, escritorio, etc.)" +
                        "\n\n\t2019 BASSE. Todos os Direitos Reservados.\n\n\n\n");
                d.setView(view);
                AlertDialog dialog = d.create();
                dialog.show();
            }
        });

    }

    private void alert(String s) {
        Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
    }
}
