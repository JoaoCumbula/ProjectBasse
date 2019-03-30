package com.example.aspire.projectba.Modelo;


/**
 * Created by Aspire on 5/17/2018.
 */

public class Cliente {
    protected String nome, apelido, email;
    protected int contacto;


    private String key;


    public Cliente(String nome, String apelido, String email, int contacto) {
        this.nome = nome;
        this.apelido = apelido;
        this.email = email;
        this.contacto = contacto;
    }

    public Cliente() {
        this("", "", "", 0);
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Cliente cliente) {
        nome = cliente.nome;
        apelido = cliente.apelido;
        email = cliente.email;
        contacto = cliente.contacto;
    }
}
