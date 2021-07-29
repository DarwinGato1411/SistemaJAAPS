/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.UbicacionMedidor;
import com.ec.entidad.UbicacionMedidor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioUbicacionMedidor {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(UbicacionMedidor ubicacionMedidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(ubicacionMedidor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar ubicacionMedidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(UbicacionMedidor ubicacionMedidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(ubicacionMedidor));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  ubicacionMedidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(UbicacionMedidor ubicacionMedidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(ubicacionMedidor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar ubicacionMedidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<UbicacionMedidor> findAll() {

        List<UbicacionMedidor> listaUbicacionMedidors = new ArrayList<UbicacionMedidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM UbicacionMedidor a ");
//           query.setParameter("codigoUsuario", ubicacionMedidor);
            listaUbicacionMedidors = (List<UbicacionMedidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta ubicacionMedidor " + e.getMessage());
        } finally {
            em.close();
        }

        return listaUbicacionMedidors;
    }

    public List<UbicacionMedidor> findByNombre(String buscar) {

        List<UbicacionMedidor> listaUbicacionMedidors = new ArrayList<UbicacionMedidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM UbicacionMedidor a where a.ubimNombre LIKE :ubimNombre ORDER BY a.ubimNombre DESC  ");
            query.setParameter("ubimNombre", "%" + buscar + "%");
            listaUbicacionMedidors = (List<UbicacionMedidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en la consulta ubicacionMedidor " + e.getMessage());
        } finally {
            em.close();
        }

        return listaUbicacionMedidors;
    }
}
