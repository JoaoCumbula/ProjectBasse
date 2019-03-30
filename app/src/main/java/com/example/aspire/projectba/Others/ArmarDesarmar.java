package com.example.aspire.projectba.Others;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspire.projectba.R;
import com.github.angads25.toggle.LabeledSwitch;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ArmarDesarmar extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_REQUEST_SMS = 1;
    SmsManager manager = SmsManager.getDefault();
    private TextView armarTudo;
    private LabeledSwitch switchArmarTudo;
    private DatabaseReference databaseReference;
    private ArrayList<String> zonasSep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armar_desarmar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        armarTudo = (TextView) findViewById(R.id.armarTudo);
        switchArmarTudo = (LabeledSwitch) findViewById(R.id.switchArmarTudo);

        Intent intent = getIntent();
        final String getMorada = intent.getExtras().getString("Morada");
        final int getContacto = intent.getExtras().getInt("Contacto");
        final int getCodAuto = intent.getExtras().getInt("CodAutorizacao");
        final String getZonas = intent.getExtras().getString("Zonas");

        String getCozinha = "", getEscritorio = "", getEscritorio2 = "", getSala = "";

        StringTokenizer st = new StringTokenizer(getZonas, ",");


        final String semZona = "";

    }

    /*private void armarDesarmarTudo(final int getContacto, final int getCodAuto, final String semZona) {
        sLigarTudo.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isChecked) {
                if (isChecked)
                    armar(getContacto, getCodAuto, semZona);
                 else
                    desarmar(getContacto, getCodAuto, semZona);
            }
        });
    }*/

    public void armar(int getContacto, int getCodAuto, String getZona) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_REQUEST_SMS);
        } else {
            manager.sendTextMessage(String.valueOf(getContacto), null, getCodAuto + " arm " + getZona, null, null);
            Toast.makeText(this, "Armado", Toast.LENGTH_SHORT).show();
        }
    }

    public void desarmar(int getContacto, int getCodAuto, String getZona) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_REQUEST_SMS);
        } else {
            manager.sendTextMessage(String.valueOf(getContacto), null, getCodAuto + " disarm " + getZona, null, null);
            Toast.makeText(this, "Armado", Toast.LENGTH_SHORT).show();
        }
    }

}



