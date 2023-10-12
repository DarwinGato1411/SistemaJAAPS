/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.entidad.EstadisticoDiarioHistorico;

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
public class ServicioEstadisticoDiarioHistorico {
    
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(EstadisticoDiarioHistorico estadisticoDiarioHistorico) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(estadisticoDiarioHistorico);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadisticoDiarioHistorico " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(EstadisticoDiarioHistorico estadisticoDiarioHistorico) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(estadisticoDiarioHistorico));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  estadisticoDiarioHistorico " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(EstadisticoDiarioHistorico estadisticoDiarioHistorico) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(estadisticoDiarioHistorico);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadisticoDiarioHistorico " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<EstadisticoDiarioHistorico> findEstadisticoDairio(Date inicio , Date fin ) {

        List<EstadisticoDiarioHistorico> listaEstadisticoMensual = new ArrayList<EstadisticoDiarioHistorico>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM EstadisticoDiarioHistorico a WHERE a.fechaInicio BETWEEN :inicio and :fin  ORDER BY a.rubro ASC");
            query.setParameter("inicio",inicio);
            query.setParameter("fin",fin);

            listaEstadisticoMensual = (List<EstadisticoDiarioHistorico>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadisticoDiarioHistorico findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadisticoMensual;
    }

   
}
