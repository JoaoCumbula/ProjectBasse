package com.example.aspire.projectba.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

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
    private EstadosZona estadosZona, es2;
    private List<EstadosZona> mDataZona;
    private ArmarDesarmar mCallBack2;
    private Context context;

    public EstadosZonasAdapter(final ArmarDesarmar mCallBack2, Context context) {
        this.mCallBack2 = mCallBack2;
        this.context = context;
        mDataZona = new ArrayList<EstadosZona>();
        Firebase.setAndroidContext(context);
        mDataRef = new Firebase(PATH_ZONAS);

        mDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                estadosZona = dataSnapshot.getValue(EstadosZona.class);
                estadosZona.setKey(dataSnapshot.getKey());

                if (user.getEmail().toString().equals(estadosZona.getClienteAssociado())) {
                    if (estadosZona.getIdentificador() == mCallBack2.getCodAutorizacao())
                        mDataZona.add(0, estadosZona);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                EstadosZona estadosZona = dataSnapshot.getValue(EstadosZona.class);

                for (EstadosZona as : mDataZona) {

                    if (as.getKey().equals(key)) {
                        as.setValues(estadosZona);
                        break;
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
        holder.nomeZona.setText(mDataZona.get(position).getNomeZona());
        holder.switchZone.setChecked(mDataZona.get(position).isEstadoZona());
        if (mDataZona.get(position).isEstadoZona() == true)
            holder.stateZoneText.setText("ON");
        else
            holder.stateZoneText.setText("OFF");

        holder.switchZone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EstadosZona estadosZona = mDataZona.get(position);
                if (isChecked) {
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
            if (nomeZona.equals(es.getNomeZona()))
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
