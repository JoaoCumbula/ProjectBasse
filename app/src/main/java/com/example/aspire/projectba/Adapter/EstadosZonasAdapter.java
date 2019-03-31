package com.example.aspire.projectba.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aspire.projectba.Modelo.EstadosZona;
import com.example.aspire.projectba.Others.ArmarDesarmar;
import com.example.aspire.projectba.Others.Conexao;
import com.example.aspire.projectba.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.github.angads25.toggle.LabeledSwitch;
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

    public EstadosZonasAdapter(ArmarDesarmar mCallBack2, Context context) {
        mCallBack2 = mCallBack2;
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
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.model_zonas, parent, false);
        return new EstadosZonasAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nomeZona.setText(mDataZona.get(position).getNomeZona());
        holder.switchZona.setOn(mDataZona.get(position).isEstadoZona());
    }

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

        TextView nomeZona;
        LabeledSwitch switchZona;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomeZona = (TextView) itemView.findViewById(R.id.nomeZona);
            switchZona = (LabeledSwitch) itemView.findViewById(R.id.switchZone);
        }
    }
}
