package com.example.aspire.projectba.Others;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aspire.projectba.Modelo.Cliente;
import com.example.aspire.projectba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Cadastro extends AppCompatActivity {

    private EditText nome, apelido, senha, confirmarSenha, contacto, email;
    private Button criarConta, cancelar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        auth = FirebaseAuth.getInstance();
        inicializaComponentes();
        eventClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFireBaseAuth();
        if (auth.getCurrentUser() != null) {
        }
    }

    public void inicializaComponentes() {
        nome = (EditText) findViewById(R.id.nome);
        apelido = (EditText) findViewById(R.id.apelido);
        senha = (EditText) findViewById(R.id.senha);
        confirmarSenha = (EditText) findViewById(R.id.confirmSenha);
        contacto = (EditText) findViewById(R.id.contacto);
        criarConta = (Button) findViewById(R.id.criarConta);
        cancelar = (Button) findViewById(R.id.cancelar);
        email = (EditText) findViewById(R.id.email);
    }

    private void eventClick() {
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String getNome = nome.getText().toString().trim();
                    final String getApelido = apelido.getText().toString().trim();
                    final String getSenha = senha.getText().toString().trim();
                    final String getConfirmSenha = confirmarSenha.getText().toString().trim();
                    final String getEmail = email.getText().toString().trim();
                    final int getContacto = Integer.parseInt(contacto.getText().toString());

                    if (getNome.isEmpty()) {
                        nome.setError("Necessario Preencher este Campo");
                        nome.requestFocus();
                        return;
                    }

                    if (getApelido.isEmpty()) {
                        apelido.setError("Necessario Preencher este Campo");
                        apelido.requestFocus();
                        return;
                    }

                    if (getSenha.length() < 6) {
                        senha.setError("Password deve conter pelo menos 6 caracteres");
                        senha.requestFocus();
                        return;
                    }
                    if (!getConfirmSenha.equals(getSenha)) {
                        confirmarSenha.setError("Senhas nao correspondem");
                        confirmarSenha.requestFocus();
                        return;
                    }

                    if (getEmail.isEmpty()) {
                        email.setError("Necessario Preencher este Campo");
                        email.requestFocus();
                        return;
                    }

                    if (getContacto < 000000000 && getContacto > 999999999) {
                        contacto.setError("Numero Invalido!");
                        contacto.requestFocus();
                        return;
                    }
                    auth.createUserWithEmailAndPassword(getEmail, getSenha).
                            addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Cliente cliente = new Cliente(
                                                getNome,
                                                getApelido,
                                                getEmail,
                                                getContacto
                                        );
                                        FirebaseDatabase.getInstance().getReference("Cliente")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(cliente)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            nome.setText("");
                                                            apelido.setText("");
                                                            email.setText("");
                                                            senha.setText("");
                                                            confirmarSenha.setText("");
                                                            contacto.setText("");
                                                        } else {
                                                        }
                                                    }
                                                });
                                    } else
                                        Toast.makeText(Cadastro.this, "O E-mail ja foi utilizado para outra conta", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (NumberFormatException e) {
                    contacto.setError("Contacto Invalido!");
                    contacto.requestFocus();
                }
            }
        });
    }
}
