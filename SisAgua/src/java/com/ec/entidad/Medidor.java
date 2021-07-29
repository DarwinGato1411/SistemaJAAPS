/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "medidor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medidor.findAll", query = "SELECT m FROM Medidor m")
    , @NamedQuery(name = "Medidor.findByIdMedidor", query = "SELECT m FROM Medidor m WHERE m.idMedidor = :idMedidor")
    , @NamedQuery(name = "Medidor.findByMedNumero", query = "SELECT m FROM Medidor m WHERE m.medNumero = :medNumero")
    , @NamedQuery(name = "Medidor.findByMedFechaRegistro", query = "SELECT m FROM Medidor m WHERE m.medFechaRegistro = :medFechaRegistro")
    , @NamedQuery(name = "Medidor.findByMedFechaInstala", query = "SELECT m FROM Medidor m WHERE m.medFechaInstala = :medFechaInstala")
    , @NamedQuery(name = "Medidor.findByMedDescripcion", query = "SELECT m FROM Medidor m WHERE m.medDescripcion = :medDescripcion")
    , @NamedQuery(name = "Medidor.findByMedMarca", query = "SELECT m FROM Medidor m WHERE m.medMarca = :medMarca")
    , @NamedQuery(name = "Medidor.findByMedAnio", query = "SELECT m FROM Medidor m WHERE m.medAnio = :medAnio")})
public class Medidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medidor")
    private Integer idMedidor;
    @Column(name = "med_numero")
    private String medNumero;
    @Column(name = "med_fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date medFechaRegistro;
    @Column(name = "med_fecha_instala")
    @Temporal(TemporalType.DATE)
    private Date medFechaInstala;
    @Column(name = "med_descripcion")
    private String medDescripcion;
    @Column(name = "med_marca")
    private String medMarca;
    @Column(name = "med_barrio")
    private String medBarrio;
    @Column(name = "med_anio")
    private Integer medAnio;
    @Column(name = "med_direccion")
    private String medDireccion;
    @JoinColumn(name = "id_estado_lectura", referencedColumnName = "id_estado_lectura")
    @ManyToOne
    private EstadoLectura idEstadoLectura;
    @JoinColumn(name = "id_predio", referencedColumnName = "id_predio")
    @ManyToOne
    private Predio idPredio;
    @JoinColumn(name = "id_tarifa", referencedColumnName = "id_tarifa")
    @ManyToOne
    private Tarifa idTarifa;
    @OneToMany(mappedBy = "idMedidor")
    private Collection<Lectura> lecturaCollection;

    @JoinColumn(name = "id_ubicacion_medidor", referencedColumnName = "id_ubicacion_medidor")
    @ManyToOne
    private UbicacionMedidor idUbicacionMedidor;

    public Medidor() {
    }

    public Medidor(Integer idMedidor) {
        this.idMedidor = idMedidor;
    }

    public Integer getIdMedidor() {
        return idMedidor;
    }

    public void setIdMedidor(Integer idMedidor) {
        this.idMedidor = idMedidor;
    }

    public String getMedNumero() {
        return medNumero;
    }

    public void setMedNumero(String medNumero) {
        this.medNumero = medNumero;
    }

    public Date getMedFechaRegistro() {
        return medFechaRegistro;
    }

    public void setMedFechaRegistro(Date medFechaRegistro) {
        this.medFechaRegistro = medFechaRegistro;
    }

    public Date getMedFechaInstala() {
        return medFechaInstala;
    }

    public void setMedFechaInstala(Date medFechaInstala) {
        this.medFechaInstala = medFechaInstala;
    }

    public String getMedDescripcion() {
        return medDescripcion;
    }

    public void setMedDescripcion(String medDescripcion) {
        this.medDescripcion = medDescripcion;
    }

    public String getMedMarca() {
        return medMarca;
    }

    public void setMedMarca(String medMarca) {
        this.medMarca = medMarca;
    }

    public Integer getMedAnio() {
        return medAnio;
    }

    public void setMedAnio(Integer medAnio) {
        this.medAnio = medAnio;
    }

    public EstadoLectura getIdEstadoLectura() {
        return idEstadoLectura;
    }

    public void setIdEstadoLectura(EstadoLectura idEstadoLectura) {
        this.idEstadoLectura = idEstadoLectura;
    }

    public Predio getIdPredio() {
        return idPredio;
    }

    public void setIdPredio(Predio idPredio) {
        this.idPredio = idPredio;
    }

    public Tarifa getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Tarifa idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getMedBarrio() {
        return medBarrio;
    }

    public void setMedBarrio(String medBarrio) {
        this.medBarrio = medBarrio;
    }

    public UbicacionMedidor getIdUbicacionMedidor() {
        return idUbicacionMedidor;
    }

    public void setIdUbicacionMedidor(UbicacionMedidor idUbicacionMedidor) {
        this.idUbicacionMedidor = idUbicacionMedidor;
    }

    public String getMedDireccion() {
        return medDireccion;
    }

    public void setMedDireccion(String medDireccion) {
        this.medDireccion = medDireccion;
    }

    @XmlTransient
    public Collection<Lectura> getLecturaCollection() {
        return lecturaCollection;
    }

    public void setLecturaCollection(Collection<Lectura> lecturaCollection) {
        this.lecturaCollection = lecturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedidor != null ? idMedidor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medidor)) {
            return false;
        }
        Medidor other = (Medidor) object;
        if ((this.idMedidor == null && other.idMedidor != null) || (this.idMedidor != null && !this.idMedidor.equals(other.idMedidor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Medidor[ idMedidor=" + idMedidor + " ]";
    }

}
