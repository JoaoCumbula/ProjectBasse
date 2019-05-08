package com.example.aspire.projectba.Others;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aspire.projectba.Adapter.EstadosZonasAdapter;
import com.example.aspire.projectba.Modelo.EstadosZona;
import com.example.aspire.projectba.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ArmarDesarmar extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_REQUEST_SMS = 1;
    SmsManager manager;
    FirebaseUser user;
    String guardarZona, getStatus;
    int getContacto, getCodAuto;
    private EditText campoZonas;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private EstadosZonasAdapter adapter;
    private AlertDialog.Builder d;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armar_desarmar);
        user = Conexao.getFirebaseUser();
        manager = SmsManager.getDefault();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabZonas);

        Intent intent = getIntent();
        getStatus = intent.getExtras().getString("Status");
        getContacto = intent.getExtras().getInt("Contacto");
        getCodAuto = intent.getExtras().getInt("CodAutorizacao");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new AlertDialog.Builder(ArmarDesarmar.this);
                view = getLayoutInflater().inflate(R.layout.dialog_zonas, null);
                d.setTitle("ADICIONAR ZONA");
                campoZonas = (EditText) view.findViewById(R.id.campoZonas);


                d.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        guardarZona = String.valueOf(campoZonas.getText());
                        boolean find = adapter.zonaNomeUnico(guardarZona);

                        if (find == true) {
                            mensagem();
                            return;
                        }

                        //if (estadosZona.getIdentificador()==getCodAutorizacao())
                        EstadosZona es = new EstadosZona(guardarZona, getCodAuto, user.getEmail(), false);
                        adapter.add(es);
                    }
                });
                d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new EstadosZonasAdapter(this, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void actualizarNomeZona(final EstadosZona estadosZona) {
        d = new AlertDialog.Builder(ArmarDesarmar.this);
        view = getLayoutInflater().inflate(R.layout.dialog_zonas, null);
        d.setTitle("ACTUALIZAR ZONA");
        campoZonas = (EditText) view.findViewById(R.id.campoZonas);
        campoZonas.setText(estadosZona.getNomeZona());

        d.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String guardarZona = String.valueOf(campoZonas.getText());
                boolean find = adapter.zonaNomeUnico(guardarZona);

                if (find == true) {
                    campoZonas.setError("Zona Existente");
                    campoZonas.requestFocus();
                    return;
                }
                adapter.update(estadosZona, guardarZona);
            }
        });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        d.setView(view);
        AlertDialog mDialog = d.create();
        mDialog.show();
    }

    public void messagem() {
        Toast.makeText(ArmarDesarmar.this, "Zona Removida Com Sucesso", Toast.LENGTH_LONG).show();
    }

    public void mensagem() {
        Toast.makeText(this, "Zona Existente! Tente Denovo.", Toast.LENGTH_SHORT).show();
    }

    public int getContacto() {
        return getContacto;
    }

    public int getCodAutorizacao() {
        return getCodAuto;
    }

    public String getStatus() {
        return getStatus;
    }

    public void armar(int getContacto, int getCodAuto, String getZona, int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_REQUEST_SMS);
        } else {
            // manager.sendTextMessage(String.valueOf(getContacto), null, getCodAuto + " arm " + getZona, null, null);
            //    Toast.makeText(this, "Armado" + position, Toast.LENGTH_SHORT).show();
        }
    }

    public void desarmar(int getContacto, int getCodAuto, String getZona, int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_REQUEST_SMS);
        } else {
            //manager.sendTextMessage(String.valueOf(getContacto), null, getCodAuto + " disarm " + getZona, null, null);
            //  Toast.makeText(this, "Desarmado" + position, Toast.LENGTH_SHORT).show();
        }
    }

}



