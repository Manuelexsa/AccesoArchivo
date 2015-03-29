package com.example.kronos.practica1;

/**
 * Created by kronos on 21/03/2015.
 */
public class Grupo {

    private String numero,acronimo,letra;

    public Grupo() {
    }

    public Grupo(String numero, String acronimo, String letra) {
        this.numero = numero;
        this.acronimo = acronimo;
        this.letra = letra;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grupo)) return false;

        Grupo grupo = (Grupo) o;

        if (acronimo != null ? !acronimo.equals(grupo.acronimo) : grupo.acronimo != null)
            return false;
        if (letra != null ? !letra.equals(grupo.letra) : grupo.letra != null) return false;
        if (numero != null ? !numero.equals(grupo.numero) : grupo.numero != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = numero != null ? numero.hashCode() : 0;
        result = 31 * result + (acronimo != null ? acronimo.hashCode() : 0);
        result = 31 * result + (letra != null ? letra.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ""+numero+" "+acronimo+" "+letra;
    }
}
