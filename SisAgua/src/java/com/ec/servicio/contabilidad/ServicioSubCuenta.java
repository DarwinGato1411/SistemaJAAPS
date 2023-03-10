/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.servicio.*;
import com.ec.entidad.contabilidad.CuSubCuenta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioSubCuenta {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(CuSubCuenta cuSubCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(cuSubCuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(CuSubCuenta cuSubCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(cuSubCuenta));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  cuSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(CuSubCuenta cuSubCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(cuSubCuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<CuSubCuenta> findByNombre(String valor) {

        List<CuSubCuenta> listaCuSubCuentas = new ArrayList<CuSubCuenta>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM CuSubCuenta a WHERE a.subcNombre  LIKE :subcNombre ORDER BY a.subcNumero ASC");
            query.setParameter("grupNombre", "%" + valor + "%");

            listaCuSubCuentas = (List<CuSubCuenta>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuSubCuenta findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCuSubCuentas;
    }

    public List<CuSubCuenta> findByIdCuenta(CuCuenta valor) {

        List<CuSubCuenta> listaCuSubCuentas = new ArrayList<CuSubCuenta>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM CuSubCuenta a WHERE a.idCuenta =:idCuenta");
            query.setParameter("idCuenta", valor);
            listaCuSubCuentas = (List<CuSubCuenta>) query.getResultList();
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuGrupo findByIdCuenta " + e.getMessage());

        } finally {
            em.close();
        }
        return listaCuSubCuentas;
    }
public CuSubCuenta findById(Integer idSubcuenta) {

        List<CuSubCuenta> listaLisItems = new ArrayList<CuSubCuenta>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            //  Query query = em.createQuery("SELECT new com.ec.vistas.RotacionProducto(max(a.prodNombre),SUM(a.cantidadVenta),SUM(a.valorVentaProducto)) FROM RotacionProducto a WHERE a.facFecha BETWEEN :inicio and :fin  GROUP BY a.idProducto" );
            Query query = em.createQuery("SELECT  a FROM CuSubCuenta a WHERE a.idSubCuenta=:idSubCuenta ");
            query.setParameter("idSubCuenta", idSubcuenta);
            listaLisItems = (List<CuSubCuenta>) query.getResultList();

            em.getTransaction().commit();
            return listaLisItems.isEmpty() ? null : listaLisItems.get(0);
        } catch (Exception e) {
            System.out.println("Error en lsa consulta CuSubCuenta " + e.getMessage());
        } finally {
            em.close();
        }

        return null;
    }

}
