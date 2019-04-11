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
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspire.projectba.Adapter.EstadosZonasAdapter;
import com.example.aspire.projectba.Modelo.EstadosZona;
import com.example.aspire.projectba.R;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ArmarDesarmar extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_REQUEST_SMS = 1;
    SmsManager manager = SmsManager.getDefault();
    FirebaseUser user = Conexao.getFirebaseUser();
    String guardarZona;
    int getContacto, getCodAuto;
    private TextView armarTudo;
    private EditText campoZonas;
    private LabeledSwitch switchArmarTudo;
    private DatabaseReference databaseReference;
    private ArrayList<EstadosZona> zonasSep;
    private RecyclerView recyclerView;
    private EstadosZonasAdapter adapter;
    private AlertDialog.Builder d;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armar_desarmar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        armarTudo = (TextView) findViewById(R.id.armarTudo);
        switchArmarTudo = (LabeledSwitch) findViewById(R.id.switchArmarTudo);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabZonas);

        Intent intent = getIntent();
        final String getMorada = intent.getExtras().getString("Morada");
        getContacto = intent.getExtras().getInt("Contacto");
        getCodAuto = intent.getExtras().getInt("CodAutorizacao");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new AlertDialog.Builder(ArmarDesarmar.this);
                view = getLayoutInflater().inflate(R.layout.dialog_zonas, null);
                d.setTitle("Adicionar Zonas");
                campoZonas = (EditText) view.findViewById(R.id.campoZonas);


                d.setPositiveButton("Adicionar Zona", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        guardarZona = String.valueOf(campoZonas.getText());
                        boolean find = adapter.zonaNomeUnico(guardarZona);

                        if (find == true) {
                            campoZonas.setError("Zona Existente");
                            campoZonas.requestFocus();
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
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new EstadosZonasAdapter(this, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        switchArmarTudo.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                String semZona = "";
                if (isOn)
                    armar(getContacto, getCodAuto, semZona);
                else
                    desarmar(getContacto, getCodAuto, semZona);
            }
        });

    }


    public int getContacto() {
        return getContacto;
    }

    public int getCodAutorizacao() {
        return getCodAuto;
    }

    public void armar(int getContacto, int getCodAuto, String getZona) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_REQUEST_SMS);
        } else {
            // manager.sendTextMessage(String.valueOf(getContacto), null, getCodAuto + " arm " + getZona, null, null);
            Toast.makeText(this, "Armado", Toast.LENGTH_SHORT).show();
        }
    }

    public void desarmar(int getContacto, int getCodAuto, String getZona) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_REQUEST_SMS);
        } else {
            //manager.sendTextMessage(String.valueOf(getContacto), null, getCodAuto + " disarm " + getZona, null, null);
            Toast.makeText(this, "Desarmado", Toast.LENGTH_SHORT).show();
        }
    }

}



