package com.example.aspire.projectba.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aspire.projectba.Fragments.FragmentPrincipal;
import com.example.aspire.projectba.Modelo.Alarme;
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
 * Created by Aspire on 3/3/2019.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private static final String PATH_DATA = "https://projectba-dd74d.firebaseio.com/Alarme";
    FirebaseAuth auth = Conexao.getFireBaseAuth();
    FirebaseUser user = Conexao.getFirebaseUser();
    Firebase mDataRef;
    private Alarme alarme;
    private List<Alarme> mData;
    private Context context;
    private FragmentPrincipal mCallBack;
    private AlertDialog.Builder builder;

    public RecyclerViewAdapter(FragmentPrincipal callback, Context context) {
        mCallBack = callback;
        this.context = context;
        mData = new ArrayList<Alarme>();
        Firebase.setAndroidContext(context);
        mDataRef = new Firebase(PATH_DATA);
        builder = new AlertDialog.Builder(context);

        mDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                alarme = dataSnapshot.getValue(Alarme.class);
                alarme.setKey(dataSnapshot.getKey());

                if (user.getEmail().toString().equals(alarme.getClienteAssociado())) {
                    mData.add(0, alarme);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Alarme alarme = dataSnapshot.getValue(Alarme.class);

                for (Alarme as : mData) {

                    if (as.getKey().equals(key)) {
                        as.setValues(alarme);
                        break;
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                for (Alarme sa : mData) {
                    if (key.equals(sa.getKey())) {
                        mData.remove(sa);
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
                Log.e("MQ", "Foi Cancelado" + firebaseError.getMessage());
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.alarme.setText("Alarme: " + (position + 1) + " " + mData.get(position).getMorada());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArmarDesarmar.class);

                // passing data to the activity
                intent.putExtra("Morada", mData.get(position).getMorada());
                intent.putExtra("Contacto", mData.get(position).getContacto());
                intent.putExtra("CodAutorizacao", mData.get(position).getCodAutorizacao());
                intent.putExtra("Zonas", mData.get(position).getZonas());
                // start the activity
                context.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final boolean[] real = new boolean[1];
                builder.setCancelable(true);
                builder.setTitle("Remocao do Salao");
                builder.setMessage("Deseja Remover?");
                builder.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove(mData.get(position));
                                //mCallBack.messagem();
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
    }

    public boolean verificarCodAuto(int codAuto) {
        for (Alarme alarme : mData) {
            if (codAuto == alarme.getCodAutorizacao())
                return true;
        }
        return false;
    }

    public void add(Alarme alarme) {
        mDataRef.push().setValue(alarme);
    }

    public void remove(Alarme alarme) {
        mDataRef.child(alarme.getKey()).removeValue();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView alarme;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            alarme = (TextView) itemView.findViewById(R.id.alarme);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
