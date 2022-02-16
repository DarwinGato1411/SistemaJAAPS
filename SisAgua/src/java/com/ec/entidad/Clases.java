/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author HC
 */

    
    @Entity
@Table(name = "cu_clase")
public class Clases implements Serializable {

  private static final long serialVersionUID = 1L;
   @Column(name = "id_clase")
    @Id private BigInteger idClase;
   
   @Column(name = "clas_numero")
   private String clasnumero;
  
@Column(name = "clas_nombre")
private String clasnombre;
        
@Column(name = "clas_total")
private BigDecimal clastotal;

@Column(name = "clas_saldo")
private BigDecimal classaldo;

@Column(name = "clas_otro")
private BigDecimal clasotro;

    public Clases() {
    }


    public BigInteger getIdClase() {
        return idClase;
    }

    public void setIdClase(BigInteger idClase) {
        this.idClase = idClase;
    }

    public String getClasnumero() {
        return clasnumero;
    }

    public void setClasnumero(String clasnumero) {
        this.clasnumero = clasnumero;
    }

    public String getClasnombre() {
        return clasnombre;
    }

    public void setClasnombre(String clasnombre) {
        this.clasnombre = clasnombre;
    }

    public BigDecimal getClastotal() {
        return clastotal;
    }

    public void setClastotal(BigDecimal clastotal) {
        this.clastotal = clastotal;
    }

    public BigDecimal getClassaldo() {
        return classaldo;
    }

    public void setClassaldo(BigDecimal classaldo) {
        this.classaldo = classaldo;
    }

    public BigDecimal getClasotro() {
        return clasotro;
    }

    public void setClasotro(BigDecimal clasotro) {
        this.clasotro = clasotro;
    }



}

