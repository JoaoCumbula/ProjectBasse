package com.example.aspire.projectba.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aspire.projectba.Modelo.EstadosZona;
import com.example.aspire.projectba.Others.ArmarDesarmar;
import com.example.aspire.projectba.Others.Conexao;
import com.example.aspire.projectba.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspire on 3/28/2019.
 */

public class EstadosZonasAdapter extends RecyclerView.Adapter<EstadosZonasAdapter.MyViewHolder> {

    private static final String PATH_ZONAS = "https://projectba-dd74d.firebaseio.com/EstadosAlarme";
    FirebaseAuth auth = Conexao.getFireBaseAuth();
    FirebaseUser user = Conexao.getFirebaseUser();
    Firebase mDataRef;
    private EstadosZona estadosZona;
    private List<EstadosZona> mDataZona;
    private ArmarDesarmar mCallBack2;
    private Context context;
    private AlertDialog.Builder builder;

    public EstadosZonasAdapter(final ArmarDesarmar mCallBack2, Context context) {
        this.mCallBack2 = mCallBack2;
        this.context = context;
        mDataZona = new ArrayList<EstadosZona>();
        Firebase.setAndroidContext(context);
        mDataRef = new Firebase(PATH_ZONAS);
        builder = new AlertDialog.Builder(context);

        mDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                estadosZona = dataSnapshot.getValue(EstadosZona.class);
                estadosZona.setKey(dataSnapshot.getKey());

                if (user.getEmail().toString().equals(estadosZona.getClienteAssociado())) {
                    if (estadosZona.getIdentificador() == mCallBack2.getCodAutorizacao())
                        mDataZona.add(0, estadosZona);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                EstadosZona newestadosZona = dataSnapshot.getValue(EstadosZona.class);

                for (EstadosZona as : mDataZona) {

                    if (as.getKey().equals(key)) {
                        as.setValues(newestadosZona);
                        break;
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                for (EstadosZona sa : mDataZona) {
                    if (key.equals(sa.getKey())) {
                        mDataZona.remove(sa);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_zonas, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final EstadosZona estadosZona = mDataZona.get(position);
        Toast.makeText(context, estadosZona.getNomeZona(), Toast.LENGTH_SHORT).show();
        holder.nomeZona.setText(estadosZona.getNomeZona());

        holder.switchZone.setChecked(estadosZona.isEstadoZona());


        if (estadosZona.isEstadoZona() == true)
            holder.stateZoneText.setText("ON");
        else
            holder.stateZoneText.setText("OFF");

        holder.switchZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchCompat s = (SwitchCompat) v;
                if (s.isChecked()) {
                    Toast.makeText(context, estadosZona.getNomeZona() + " click" + position, Toast.LENGTH_SHORT).show();

                    mCallBack2.armar(mCallBack2.getContacto(), estadosZona.getIdentificador(), estadosZona.getNomeZona(), position);
                    estadosZona.setEstadoZona(true);
                    mDataRef.child(estadosZona.getKey()).setValue(estadosZona);
                } else {

                    mCallBack2.desarmar(mCallBack2.getContacto(), estadosZona.getIdentificador(), estadosZona.getNomeZona(), position);
                    estadosZona.setEstadoZona(false);
                    mDataRef.child(estadosZona.getKey()).setValue(estadosZona);
                }
            }
        });
// {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//
//            }
//        });

        holder.nomeZona.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final boolean[] real = new boolean[1];
                builder.setCancelable(true);
                builder.setTitle("Remocao da Zona");
                builder.setMessage("Deseja Remover?");
                builder.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove(mDataZona.get(position));
                                //mCallBack
                                real[0] = true;
                            }
                        });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        real[0] = false;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return real[0];
            }
        });

        holder.nomeZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack2.actualizarNomeZona(estadosZona);
            }
        });
    }

    public void remove(EstadosZona estadosZona) {
        mDataRef.child(estadosZona.getKey()).removeValue();
    }

    public void update(EstadosZona estadosZona, String nomeZona) {
        estadosZona.setNomeZona(nomeZona);
        mDataRef.child(estadosZona.getKey()).setValue(estadosZona);
    }

  /*  public void armaTudo(){
            es2.setEstadoZona(true);
            mDataRef.child(estadosZona.getKey()).setValue(estadosZona);

    }
    public void desarmaTudo(){
            es2.setEstadoZona(false);
            mDataRef.child(estadosZona.getKey()).setValue(estadosZona);
    }*/

    public boolean zonaNomeUnico(String nomeZona) {
        for (EstadosZona es : mDataZona) {
            if (nomeZona.equalsIgnoreCase(es.getNomeZona()))
                return true;
        }
        return false;
    }

    public void add(EstadosZona estadosZona) {
        mDataRef.push().setValue(estadosZona);
    }

    @Override
    public int getItemCount() {
        return mDataZona.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeZona, stateZoneText;
        SwitchCompat switchZone;

        public MyViewHolder(View itemView) {
            super(itemView);
            stateZoneText = (TextView) itemView.findViewById(R.id.statezone);
            nomeZona = (TextView) itemView.findViewById(R.id.nomeZona);
            switchZone = (SwitchCompat) itemView.findViewById(R.id.switchZone);
        }
    }
}
