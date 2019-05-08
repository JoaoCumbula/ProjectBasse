package com.example.aspire.projectba.Others;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspire.projectba.R;
import com.google.firebase.auth.FirebaseUser;

public class ArmarTudo extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_REQUEST_SMS = 1;
    SmsManager manager;
    FirebaseUser user;
    int getContacto, getCodAuto;
    String getStatus;
    private Button armar, desarmar, bypass;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armar_tudo);

        manager = SmsManager.getDefault();
        user = Conexao.getFirebaseUser();

        armar = (Button) findViewById(R.id.buttonArmar);
        desarmar = (Button) findViewById(R.id.buttonDesarmar);
        bypass = (Button) findViewById(R.id.bypass);
        status = (TextView) findViewById(R.id.status);

        Intent intent = getIntent();
        final String getMorada = intent.getExtras().getString("Morada");
        getContacto = intent.getExtras().getInt("Contacto");
        getCodAuto = intent.getExtras().getInt("CodAutorizacao");

        armarDesarmarTudo();
    }

    public void armarDesarmarTudo() {
        armar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                armar(getContacto, getCodAuto);
                status.setText("Armado");
                getStatus = status.getText().toString().trim();
            }
        });
        desarmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desarmar(getContacto, getCodAuto);
                status.setText("Desarmado");
                getStatus = status.getText().toString().trim();
            }
        });
        bypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArmarTudo.this, ArmarDesarmar.class);
                intent.putExtra("Contacto", getContacto);
                intent.putExtra("CodAutorizacao", getCodAuto);
                intent.putExtra("Status", getStatus);
                // start the activity
                startActivity(intent);
            }
        });
    }


    public void armar(int getContacto, int getCodAuto) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_REQUEST_SMS);
        } else {
            // manager.sendTextMessage(String.valueOf(getContacto), null, getCodAuto + " arm " + getZona, null, null);
            Toast.makeText(this, "Armado", Toast.LENGTH_SHORT).show();
        }
    }

    public void desarmar(int getContacto, int getCodAuto) {
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
