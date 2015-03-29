package com.example.kronos.practica1;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kronos on 17/03/2015.
 */
public class XmlIO {


    private ArrayList<Datos> ListaDatos = new ArrayList<Datos>();
    private ArrayList<Grupo> ListaGrupos;
    private Grupo grupo;
    private int posicion;
    private UtilIO util;

    public XmlIO(ArrayList<Datos> listaDatos, int posicion) {
        this.ListaDatos = listaDatos;
        this.posicion = posicion;
    }

    public int getPosicion() {
        return this.posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public ArrayList<Datos> getListaDatos() {
        return this.ListaDatos;
    }

    public void setListaDatos(ArrayList<Datos> listaDatos) {
        this.ListaDatos = listaDatos;
    }

    public void escribir(File f) {
        try {
            FileOutputStream fosxml;
            fosxml = new FileOutputStream(f);
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "curso");
            for (int i = 0; i < this.ListaDatos.size(); i++) {
                docxml.startTag(null, "nivel");
                docxml.attribute(null, "nombre", this.ListaDatos.get(i).getNivel());
                for (int e = 0; e < this.ListaDatos.get(i).getGrupo().size(); e++) {
                    docxml.startTag(null,"grupo");
                    docxml.startTag(null, "numero");
                    docxml.text(this.ListaDatos.get(i).getGrupo().get(e).getNumero());
                    docxml.endTag(null, "numero");
                    docxml.startTag(null, "acronimo");
                    docxml.text(this.ListaDatos.get(i).getGrupo().get(e).getAcronimo());
                    docxml.endTag(null, "acronimo");
                    docxml.startTag(null, "letra");
                    docxml.text(this.ListaDatos.get(i).getGrupo().get(e).getLetra());
                    docxml.endTag(null, "letra");
                    docxml.endTag(null, "grupo");
                }
                docxml.endTag(null, "nivel");
            }
            docxml.endTag(null, "curso");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leer(File f) {
        try {
            XmlPullParser lectorxml = Xml.newPullParser();
            lectorxml.setInput(new FileInputStream(f), "utf-8");
            int evento = lectorxml.getEventType();
            Datos d = new Datos();
            ListaGrupos = new ArrayList<Grupo>();
            Listgrup grupo = new Listgrup();
            String a = "";
            while (evento != XmlPullParser.END_DOCUMENT) {
                if (evento == XmlPullParser.START_TAG) {
                    String etiqueta = lectorxml.getName();
                    if (etiqueta.compareTo("nivel") == 0) {
                        d.setNivel(lectorxml.getAttributeValue(null, "nombre"));
                    }
                    if (etiqueta.compareTo("grupo")==0){
                        Log.v("1","grupo");
                    }
                    if (etiqueta.compareTo("numero") == 0) {
                        a = lectorxml.nextText().toString();
                        Log.v("1","grupo "+a);
                        grupo.setNumero(a);
                    }
                    if (etiqueta.compareTo("acronimo") == 0) {
                        grupo.setAcronimo(lectorxml.nextText().toString());
                    }
                    if (etiqueta.compareTo("letra") == 0) {
                        grupo.setLetra(lectorxml.nextText().toString());
                    }
                }
                if (evento == XmlPullParser.END_TAG) {
                    String etiqueta = lectorxml.getName();
                    if (etiqueta.compareTo("nivel") == 0) {
                        d.setGrupo(grupo.getListaGrup());
                        this.ListaDatos.add(d);
                        ListaGrupos = new ArrayList<Grupo>();
                        grupo = new Listgrup();
                        d = new Datos();
                    }
                }
                evento = lectorxml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Listgrup extends ArrayList<Grupo> {

        private ArrayList<Grupo> ListaGrup = new ArrayList<Grupo>();
        private ArrayList lnumeros = new ArrayList();
        private ArrayList lletras = new ArrayList();
        private ArrayList lacronimos = new ArrayList();

        public void setNumero(String numero) {
            this.lnumeros.add(numero);
        }

        public void setAcronimo(String acronimo) {
            this.lacronimos.add(acronimo);
        }

        public void setLetra(String letra) {
            this.lletras.add(letra);
        }

        public ArrayList<Grupo> getListaGrup() {
            Grupo g = new Grupo();
            for (int i = 0; i < lnumeros.size(); i++) {
                g.setNumero(String.valueOf(lnumeros.get(i)));
                g.setAcronimo(String.valueOf(lacronimos.get(i)));
                g.setLetra(String.valueOf(lletras.get(i)));
                ListaGrup.add(g);
                g = new Grupo();
            }
            return ListaGrup;
        }
    }

}
