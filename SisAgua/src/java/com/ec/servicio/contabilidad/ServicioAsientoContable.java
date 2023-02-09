/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;


import com.ec.entidad.contabilidad.AsientoContable;
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
public class ServicioAsientoContable {
        private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(AsientoContable asientoContable) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(asientoContable);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar asientoCOntable " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(AsientoContable asientoContable) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(asientoContable));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  asientoContable " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(AsientoContable asientoContable) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(asientoContable);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar AsientoContable " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<AsientoContable> findByNombre(String valor) {

        List<AsientoContable> listaAsientoContable = new ArrayList<AsientoContable>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM AsientoContable a WHERE a.referenciaDc  LIKE :referenciaDc ORDER BY a.referenciaDc ASC");
            query.setParameter("referenciaDc", "%" + valor + "%");

            listaAsientoContable = (List<AsientoContable>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta AsientoContable findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaAsientoContable;
    }

    public List<AsientoContable> findByMes(Date inicioAC, Date finAC) {

        List<AsientoContable> listaAsientoContable2 = new ArrayList<AsientoContable>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
          //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT new com.ec.entidad.contabilidad.AsientoContable(  a.fechaDc, a.referenciaDc, a.documentoDc, a.observacionDc, a.totalDebeDc, a.totalHaberDc) FROM AsientoContable a  WHERE a.fechaDc  BETWEEN :inicioAC and :finAC " );
            query.setParameter("inicioAC", inicioAC);
            query.setParameter("finDC", finAC);
            listaAsientoContable2 = (List<AsientoContable>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta AsientoContable " + e.getMessage());
        } finally {
            em.close();
        }

        return listaAsientoContable2;
    }
    
}
