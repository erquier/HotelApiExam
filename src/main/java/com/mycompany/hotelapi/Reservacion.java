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
@Table(name="reservacion")

public class Reservacion {
    
    public int IDreserva;
    public int IDcliente;
    public String fechallegada;
    public String fechasalida;
    public float costo;
    
    public Reservacion(){
    
    }

    public Reservacion(int IDcliente, String fechallegada, String fechasalida, float costo) {
        super();
        
        this.IDcliente = IDcliente;
        this.fechallegada = fechallegada;
        this.fechasalida = fechasalida;
        this.costo = costo;
    }
    
    
    @Id
    @Column(name = "IDreserva")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIDreserva() {
        return IDreserva;
    }

    public void setIDreserva(int IDreserva) {
        this.IDreserva = IDreserva;
    }

    public int getIDcliente() {
        return IDcliente;
    }

    public void setIDcliente(int IDcliente) {
        this.IDcliente = IDcliente;
    }

    public String getFechallegada() {
        return fechallegada;
    }

    public void setFechallegada(String fechallegada) {
        this.fechallegada = fechallegada;
    }

    public String getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(String fechasalida) {
        this.fechasalida = fechasalida;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
    
    
}
