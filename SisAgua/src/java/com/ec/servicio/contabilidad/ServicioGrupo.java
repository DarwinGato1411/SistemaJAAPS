/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio.contabilidad;

import com.ec.entidad.contabilidad.CuClase;
import com.ec.servicio.*;
import com.ec.entidad.contabilidad.CuGrupo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioGrupo {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(CuGrupo cuGrupo) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(cuGrupo);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuGrupo " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(CuGrupo cuGrupo) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(cuGrupo));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  cuGrupo " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(CuGrupo cuGrupo) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(cuGrupo);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar cuGrupo " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<CuGrupo> findByNombre(String valor) {

        List<CuGrupo> listaCuGrupos = new ArrayList<CuGrupo>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM CuGrupo a WHERE a.grupNombre  LIKE :grupNombre ORDER BY a.grupNumero ASC");
            query.setParameter("grupNombre", "%" + valor + "%");

            listaCuGrupos = (List<CuGrupo>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuGrupo findByNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCuGrupos;
    }

    public List<CuGrupo> findByIdClase(CuClase valor) {

        List<CuGrupo> listaCuGrupos = new ArrayList<CuGrupo>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM CuGrupo a WHERE a.cuClase=:cuClase");
            query.setParameter("cuClase", valor);
            listaCuGrupos = (List<CuGrupo>) query.getResultList();
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en lsa consulta cuGrupo findByIdClase " + e.getMessage());

        } finally {
            em.close();
        }
        return listaCuGrupos;
    }

}
