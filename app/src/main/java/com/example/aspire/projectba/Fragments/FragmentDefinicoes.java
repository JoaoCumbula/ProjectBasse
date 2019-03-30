package com.example.aspire.projectba.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aspire.projectba.Adapter.ClienteAdapter;
import com.example.aspire.projectba.Others.Conexao;
import com.example.aspire.projectba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentDefinicoes extends android.support.v4.app.Fragment {

    private View v;
    private RecyclerView recyclerView;
    private ClienteAdapter adapter;
    private FirebaseUser user;
    private FirebaseAuth auth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_fragment_definicoes, container, false);

        // inicializarComponentes();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new ClienteAdapter(this, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return v;
    }

    public void mensagem() {
        Toast.makeText(getActivity(), "Perfil Actualizado Com Sucesso", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFireBaseAuth();
        user = Conexao.getFirebaseUser();
    }

}
