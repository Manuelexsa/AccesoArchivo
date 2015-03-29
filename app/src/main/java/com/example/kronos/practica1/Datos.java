package com.example.kronos.practica1;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kronos on 16/03/2015.
 */
public class Datos {
    private String nivel;
    private ArrayList<Grupo> grupo;

    public Datos() {
    }

    public Datos(String nivel, ArrayList<Grupo> grupo) {
        this.nivel = nivel;
        this.grupo = grupo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public ArrayList<Grupo> getGrupo() {
        return grupo;
    }

    public void setGrupo(ArrayList<Grupo> grupo) {
        this.grupo = grupo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Datos)) return false;

        Datos datos = (Datos) o;

        if (nivel != null ? !nivel.equals(datos.nivel) : datos.nivel != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nivel != null ? nivel.hashCode() : 0;
        result = 31 * result + (grupo != null ? grupo.hashCode() : 0);
        return result;
    }

}
