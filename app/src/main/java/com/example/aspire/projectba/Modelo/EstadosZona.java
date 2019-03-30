package com.example.aspire.projectba.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EstadosZona {
    protected String nomeZona;
    protected String clienteAssociado;
    protected boolean estadoZona;
    @JsonIgnore
    private String key;

    public EstadosZona(String nomeZona, String clienteAssociado, boolean estadoZona) {
        this.nomeZona = nomeZona;
        this.clienteAssociado = clienteAssociado;
        this.estadoZona = estadoZona;
    }

    public EstadosZona() {
        this("", "", false);
    }

    public String getClienteAssociado() {
        return clienteAssociado;
    }

    public void setClienteAssociado(String clienteAssociado) {
        this.clienteAssociado = clienteAssociado;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNomeZona() {
        return nomeZona;
    }

    public void setNomeZona(String nomeZona) {
        this.nomeZona = nomeZona;
    }

    public boolean isEstadoZona() {
        return estadoZona;
    }

    public void setEstadoZona(boolean estadoZona) {
        this.estadoZona = estadoZona;
    }

    public void setValues(EstadosZona zona) {
        nomeZona = zona.nomeZona;
        clienteAssociado = zona.clienteAssociado;
        estadoZona = zona.estadoZona;
    }
}
