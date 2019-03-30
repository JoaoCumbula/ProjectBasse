package com.example.aspire.projectba.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspire.projectba.Adapter.RecyclerViewAdapter;
import com.example.aspire.projectba.Modelo.Alarme;
import com.example.aspire.projectba.Others.Conexao;
import com.example.aspire.projectba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class FragmentPrincipal extends Fragment {

    int MY_PERMISSIONS_REQUEST_REQUEST_SMS = 1;
    String[] listItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private TextView texto;
    private EditText campoZonas;
    private Button add, zonas;
    private EditText morada, contacto, codAutorizacao;
    private AlertDialog.Builder d;
    private View vi, view, view1;
    private boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        vi = inflater.inflate(R.layout.activity_fragment_principal, container, false);

        FloatingActionButton fab = (FloatingActionButton) vi.findViewById(R.id.fab);
        //checkedItems = new boolean[listItems.length];


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new AlertDialog.Builder(getActivity());
                view = getLayoutInflater().inflate(R.layout.dialog_alarme, null);
                d.setTitle("ADICIONA ALARME");
                inicializarComponentes();

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getMorada = morada.getText().toString();
                        int getContacto = Integer.parseInt(contacto.getText().toString());
                        int getCodAutorizacao = Integer.parseInt(codAutorizacao.getText().toString());
                        String getTexto = texto.getText().toString();
                        boolean find = adapter.verificarCodAuto(getCodAutorizacao);

                        if (getMorada.isEmpty()) {
                            morada.setError("Preencha o Campo");
                            morada.requestFocus();
                            return;
                        }

                        if (find == true) {
                            codAutorizacao.setError("Codigo de Autorizacao Existente");
                            codAutorizacao.requestFocus();
                            return;
                        }

                        if (getCodAutorizacao == 0) {
                            codAutorizacao.setError("Preencha o Campo");
                            codAutorizacao.requestFocus();
                            return;
                        }

                        if (getContacto < 000000000 && getContacto > 999999999) {
                            contacto.setError("Numero Invalido!");
                            contacto.requestFocus();
                            return;
                        }

                        Alarme al = new Alarme(getMorada, getContacto, getTexto, getCodAutorizacao, user.getEmail());
                        adapter.add(al);
                        Toast.makeText(getActivity(), "Alarme Adicionado Com Sucesso", Toast.LENGTH_SHORT).show();
                    }
                });
                       /* }else
                            Toast.makeText(getActivity(), "Necessario adicionar Zonas", Toast.LENGTH_SHORT).show();*/

                d.setView(view);
                AlertDialog dialog = d.create();
                dialog.show();

                zonas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d = new AlertDialog.Builder(getActivity());
                        view = getLayoutInflater().inflate(R.layout.dialog_zonas, null);
                        d.setTitle("Adicionar Zonas");
                        // inicializarComponentes();
                        campoZonas = (EditText) view.findViewById(R.id.campoZonas);
                        campoZonas.setText(texto.getText());
                        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                texto.setText(campoZonas.getText());
                            }
                        });
                        d.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        d.setView(view);
                        AlertDialog mDialog = d.create();
                        mDialog.show();
                    }
                });
            }
        });
        recyclerView = (RecyclerView) vi.findViewById(R.id.recyclerrvi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(this, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return vi;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFireBaseAuth();
        user = Conexao.getFirebaseUser();
    }

    public void inicializarComponentes() {
        morada = (EditText) view.findViewById(R.id.idEditText);
        contacto = (EditText) view.findViewById(R.id.contactoEditText);
        codAutorizacao = (EditText) view.findViewById(R.id.codigoAutorizacao);
        add = (Button) view.findViewById(R.id.addicionar);
        zonas = (Button) view.findViewById(R.id.zonas);
        texto = (TextView) view.findViewById(R.id.texto);

    }
}
