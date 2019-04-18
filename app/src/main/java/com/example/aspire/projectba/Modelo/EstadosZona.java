package com.example.aspire.projectba.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EstadosZona {
    protected String nomeZona;
    protected int identificador;
    protected boolean estadoZonab;
    protected String clienteAssociado;

    @JsonIgnore
    private String key;

    public EstadosZona(String nomeZona, int identificador, String clienteAssociado, boolean estadoZona) {
        this.nomeZona = nomeZona;
        this.identificador = identificador;
        this.clienteAssociado = clienteAssociado;
        this.estadoZonab = estadoZona;
    }

    public EstadosZona() {
        this("", 0, "", false);
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
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
        return estadoZonab;
    }

    public void setEstadoZona(boolean estadoZona) {
        this.estadoZonab = estadoZona;
    }

    public void setValues(EstadosZona zona) {
        estadoZonab = zona.estadoZonab;
    }
}
