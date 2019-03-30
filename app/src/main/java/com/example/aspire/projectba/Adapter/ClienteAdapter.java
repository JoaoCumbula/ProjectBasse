package com.example.aspire.projectba.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.aspire.projectba.Fragments.FragmentDefinicoes;
import com.example.aspire.projectba.Modelo.Cliente;
import com.example.aspire.projectba.Others.Conexao;
import com.example.aspire.projectba.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by Aspire on 3/17/2019.
 */

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder> {

    private static final String PATH_DATA = "https://projectba-dd74d.firebaseio.com/Cliente";
    FirebaseAuth auth = Conexao.getFireBaseAuth();
    FirebaseUser user = Conexao.getFirebaseUser();
    Firebase mDataRef;
    private ArrayList<Cliente> mData;
    private Cliente cliente;
    private Context context;
    private FragmentDefinicoes mCallBack;
    private AlertDialog.Builder builder;

    public ClienteAdapter(FragmentDefinicoes callback, Context context) {
        mCallBack = callback;
        this.context = context;
        mData = new ArrayList<Cliente>();
        Firebase.setAndroidContext(context);
        mDataRef = new Firebase(PATH_DATA);
        builder = new AlertDialog.Builder(context);

        mDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                cliente = dataSnapshot.getValue(Cliente.class);
                cliente.setKey(dataSnapshot.getKey());

                if (user.getEmail().toString().equals(user.getEmail())) {
                    mData.add(0, cliente);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Cliente newCliente = dataSnapshot.getValue(Cliente.class);

                for (Cliente nC : mData) {
                    if (nC.getKey().equals(key)) {
                        nC.setValues(newCliente);
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
        view = inflater.inflate(R.layout.model_settings, parent, false);
        return new ClienteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (user.getEmail().toString().equals(mData.get(position).getEmail())) {
            holder.nome.setText(mData.get(position).getNome());
            holder.apelido.setText(mData.get(position).getApelido());
            holder.contacto.setText(mData.get(position).getContacto() + "");
        }
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.nome.setEnabled(true);
                holder.apelido.setEnabled(true);
                holder.contacto.setEnabled(true);
            }
        });

        holder.salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getNome, getApelido;
                int getContacto;

                getNome = holder.nome.getText().toString();
                getApelido = holder.apelido.getText().toString();
                getContacto = Integer.parseInt(holder.contacto.getText().toString());

                cliente.setNome(getNome);
                cliente.setApelido(getApelido);
                cliente.setEmail(user.getEmail());
                cliente.setContacto(getContacto);
                mDataRef.child(cliente.getKey()).setValue(cliente);
                mCallBack.mensagem();

                holder.nome.setText(getNome);
                holder.apelido.setText(getApelido);
                holder.contacto.setText(getContacto + "");

                holder.nome.setEnabled(false);
                holder.apelido.setEnabled(false);
                holder.contacto.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        EditText nome, apelido, contacto;
        Button salvar, editar;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome = (EditText) itemView.findViewById(R.id.tvNumber1);
            apelido = (EditText) itemView.findViewById(R.id.tvNumber2);
            contacto = (EditText) itemView.findViewById(R.id.tvNumber4);
            editar = (Button) itemView.findViewById(R.id.editar);
            salvar = (Button) itemView.findViewById(R.id.salvar);
        }
    }
}
