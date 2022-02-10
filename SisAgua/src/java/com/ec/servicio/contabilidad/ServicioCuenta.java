/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.servicio.*;
import com.ec.entidad.contabilidad.CuCuenta;
import com.ec.entidad.contabilidad.CuGrupo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioCuenta {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(CuCuenta cuCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(cuCuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(CuCuenta cuCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(cuCuenta));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  cuCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(CuCuenta cuCuenta) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(cuCuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuCuenta " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<CuCuenta> findByNombre(String valor) {

        List<CuCuenta> listaCuCuentas = new ArrayList<CuCuenta>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM CuCuenta a WHERE a.grupNombre  LIKE :grupNombre ORDER BY a.grupNumero ASC");
            query.setParameter("grupNombre", "%" + valor + "%");

            listaCuCuentas = (List<CuCuenta>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuCuenta findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCuCuentas;
    }

    public List<CuCuenta> findByIdGrupo(CuGrupo valor) {

        List<CuCuenta> listaCuGrupos = new ArrayList<CuCuenta>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM CuCuenta a WHERE a.idGrupo =:idGrupo");
            query.setParameter("idGrupo", valor);
            listaCuGrupos = (List<CuCuenta>) query.getResultList();
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuGrupo findByIdGrupo " + e.getMessage());

        } finally {
            em.close();
        }
        return listaCuGrupos;
    }

}
