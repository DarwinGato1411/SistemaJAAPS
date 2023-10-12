/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.AcSubCuenta;
import com.ec.servicio.HelperPersistencia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author HC
 */
public class ServicioAcSubcuenta {
    
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(AcSubCuenta acSubCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(acSubCuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar acSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(AcSubCuenta acSubCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(acSubCuenta));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  acSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(AcSubCuenta acSubCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(acSubCuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar acSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<AcSubCuenta> findByNombre(String valor) {

        List<AcSubCuenta> listaAcSubCuenta = new ArrayList<AcSubCuenta>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM AcSubCuenta a WHERE a.subcNombre  LIKE :subcNombre ORDER BY a.subcNumero ASC");
            query.setParameter("subcNombre", "%" + valor + "%");

            listaAcSubCuenta = (List<AcSubCuenta>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta acSubCuenta findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaAcSubCuenta;
    }

    public List<AcSubCuenta> findByIdCuenta(CuCuenta valor) {

        List<AcSubCuenta> listaAcSubCuenta = new ArrayList<AcSubCuenta>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM AcSubCuenta a WHERE a.idCuenta =:idCuenta");
            query.setParameter("idCuenta", valor);
            listaAcSubCuenta = (List<AcSubCuenta>) query.getResultList();
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuGrupo findByIdCuenta " + e.getMessage());

        } finally {
            em.close();
        }
        return listaAcSubCuenta;
    }

    public List<AcSubCuenta> listadoTotal() {

        List<AcSubCuenta> listaLisItems = new ArrayList<AcSubCuenta>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT a FROM AcSubCuenta a ");

            listaLisItems = (List<AcSubCuenta>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta AcSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLisItems;
    }
    
        public List<AcSubCuenta> listadoComprobante() {

        List<AcSubCuenta> listaLisItems = new ArrayList<AcSubCuenta>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT a.subcNombre, a.subcTotal FROM AcSubCuenta a ");

            listaLisItems = (List<AcSubCuenta>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta AcSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLisItems;
    }

    public AcSubCuenta findById(Integer idSubcuenta) {

        List<AcSubCuenta> listaLisItems = new ArrayList<AcSubCuenta>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT  a FROM AcSubCuenta a WHERE a.idSubCuenta=:idSubCuenta ");
            query.setParameter("idSubCuenta", idSubcuenta);
            listaLisItems = (List<AcSubCuenta>) query.getResultList();

            em.getTransaction().commit();
            return listaLisItems.isEmpty() ? null : listaLisItems.get(0);
        } catch (Exception e) {
            System.out.println("Error en lsa consulta AcSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

        return null;
    }
}
