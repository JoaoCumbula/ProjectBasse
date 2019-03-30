package com.example.aspire.projectba.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.Exclude;

/**
 * Created by Aspire on 3/3/2019.
 */

public class Alarme {
    protected String morada, zonas, clienteAssociado;
    int codAutorizacao, contacto;

    @JsonIgnore
    private String key;

    public Alarme(String morada, int contacto, String zonas, int codAutorizacao, String clienteAssociado) {
        this.morada = morada;
        this.contacto = contacto;
        this.zonas = zonas;
        this.codAutorizacao = codAutorizacao;
        this.clienteAssociado = clienteAssociado;
    }

    public Alarme() {
        this("", 0, "", 0, "");
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public String getZonas() {
        return zonas;
    }

    public void setZonas(String zonas) {
        this.zonas = zonas;
    }

    public String getClienteAssociado() {
        return clienteAssociado;
    }

    public void setClienteAssociado(String clienteAssociado) {
        this.clienteAssociado = clienteAssociado;
    }

    public int getCodAutorizacao() {
        return codAutorizacao;
    }

    public void setCodAutorizacao(int codAutorizacao) {
        this.codAutorizacao = codAutorizacao;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Alarme alarme) {
        contacto = alarme.contacto;
        morada = alarme.morada;
        zonas = alarme.zonas;
        codAutorizacao = alarme.codAutorizacao;
        clienteAssociado = alarme.clienteAssociado;
    }
}
