/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vistas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author HC
 */
@Entity
@Table(name = "listado_items")
public class ListadoItems implements Serializable {

  private static final long serialVersionUID = 1L;
   @Column(name = "id")
    @Id private BigInteger idItems;
  
@Column(name = "det_descripcion")
private String detDescripcion;
        
@Column(name = "sum")
private BigDecimal sumaTotal;

    public ListadoItems(BigInteger idItems, String detDescripcion, BigDecimal sumaTotal) {
        this.idItems = idItems;
        this.detDescripcion = detDescripcion;
        this.sumaTotal = sumaTotal;
    }

    public ListadoItems() {
    }

    public BigInteger getIdItems() {
        return idItems;
    }

    public void setIdItems(BigInteger idItems) {
        this.idItems = idItems;
    }

    public String getDetDescripcion() {
        return detDescripcion;
    }

    public void setDetDescripcion(String detDescripcion) {
        this.detDescripcion = detDescripcion;
    }

    public BigDecimal getSumaTotal() {
        return sumaTotal;
    }

    public void setSumaTotal(BigDecimal sumaTotal) {
        this.sumaTotal = sumaTotal;
    }

  


    
}
