/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.entidad.EstadisticoMensual;
import com.ec.entidad.EstadisticoMensualHistorico;

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
public class ServicioEstadisticoMensualHistorico {
    
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(EstadisticoMensualHistorico estadisticoMensual) {

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

    public void eliminar(EstadisticoMensualHistorico estadisticoMensual) {

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

    public void modificar(EstadisticoMensualHistorico estadisticoMensual) {

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

    public List<EstadisticoMensualHistorico> findEstadisticoMensual(Date inicio , Date fin ) {

        List<EstadisticoMensualHistorico> listaEstadisticoMensual = new ArrayList<EstadisticoMensualHistorico>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM EstadisticoMensualHistorico a WHERE a.fechaInicio BETWEEN :inicio and :fin  ORDER BY a.rubro ASC");
            query.setParameter("inicio",inicio);
            query.setParameter("fin",fin);

            listaEstadisticoMensual = (List<EstadisticoMensualHistorico>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadisticoMensual findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadisticoMensual;
    }
    public List<EstadisticoMensualHistorico> findEstadisticoAnual(Date inicio , Date fin ) {

        List<EstadisticoMensualHistorico> listaEstadisticoMensual = new ArrayList<EstadisticoMensualHistorico>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  new com.ec.entidad.EstadisticoMensualHistorico(a.rubro,max(a.fechaInicio),max(a.fechaFin), sum(a.saldoAnterior),sum(a.totalIngreso),sum(a.recaudo),sum(a.saldoActual)) FROM EstadisticoMensualHistorico a WHERE a.fechaInicio BETWEEN :inicio and :fin GROUP BY a.rubro  ORDER BY a.rubro ASC");
            query.setParameter("inicio",inicio);
            query.setParameter("fin",fin);

            listaEstadisticoMensual = (List<EstadisticoMensualHistorico>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadisticoMensual findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadisticoMensual;
    }

   
}
