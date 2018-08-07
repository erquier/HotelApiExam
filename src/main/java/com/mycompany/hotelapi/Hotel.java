/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hotelapi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author ernestosantana
 */
@Entity
@Table(name="hotel")
public class Hotel {
    public int idhotel;
    public String nombre;
    public String telefono;
    public String direccion;

    public Hotel() {
    }
    
    
    public Hotel(String nombre, String telefono, String direccion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    @Id
    @Column(name = "idhotel")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdhotel() {
        return idhotel;
    }

    public void setIdhotel(int idhotel) {
        this.idhotel = idhotel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    
}
