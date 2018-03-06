package com.example.julolopop.tarearss.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Julolopop on 06/03/2018.
 */

public class Tarea implements Serializable {
private int id;
    private String nombre;
    private String descripcion;
    private int importancia;
    private Date fecha;
    private String enlace;
    private String imagen;

    public Tarea(int id, String nombre, String descripcion, int importancia, Date fecha, String enlace, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.importancia = importancia;
        this.fecha = fecha;
        this.enlace = enlace;
        this.imagen = imagen;
    }

    public Tarea(String nombre, String descripción, int importancia, Date fecha, String enlace, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripción;
        this.importancia = importancia;
        this.fecha = fecha;
        this.enlace = enlace;
        this.imagen = imagen;
    }

    public Tarea() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripción) {
        this.descripcion = descripción;
    }

    public String getImportancia() {
        return String.valueOf(importancia);
    }

    public void setImportancia(int importancia) {
        this.importancia = importancia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return nombre;
    }
}


