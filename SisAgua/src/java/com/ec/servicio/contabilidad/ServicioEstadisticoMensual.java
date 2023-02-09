/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.entidad.EstadisticoMensual;
import com.ec.entidad.contabilidad.CuCuenta;

import com.ec.servicio.HelperPersistencia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author HC
 */
public class ServicioEstadisticoMensual {
    
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(EstadisticoMensual estadisticoMensual) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(estadisticoMensual);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadisticoMensual " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(EstadisticoMensual estadisticoMensual) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(estadisticoMensual));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  estadisticoMensual " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(EstadisticoMensual estadisticoMensual) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(estadisticoMensual);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadisticoMensual " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<EstadisticoMensual> findEstadisticoMensual() {

        List<EstadisticoMensual> listaEstadisticoMensual = new ArrayList<EstadisticoMensual>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM EstadisticoMensual a WHERE a.saldoAnterior>0 OR a.recaudo>0 OR a.saldoActual>0 OR a.totalIngreso>0  ORDER BY a.rubro ASC");
//            query.setParameter("subcNombre", "%" + valor + "%");

            listaEstadisticoMensual = (List<EstadisticoMensual>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadisticoMensual findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadisticoMensual;
    }

   
}
