/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Lectura;
import com.ec.entidad.Medidor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author gato
 */
public class ServicioLectura {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Lectura lectura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(lectura);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar lectura " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Lectura lectura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(lectura));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  lectura " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Lectura lectura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(lectura);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar lectura " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Lectura> findIdMedidor(String valor) {

        List<Lectura> listaLecturas = new ArrayList<Lectura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Lectura a WHERE a.idMedidor =:idLectura");
            query.setParameter("estlNombre", valor);

            listaLecturas = (List<Lectura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta lectura findIdMedidor " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLecturas;
    }

    public List<Lectura> findIdMedidorAndLecPagada(String idMedidor, String lecPagada) {

        List<Lectura> listaLecturas = new ArrayList<Lectura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Lectura a WHERE a.idMedidor =:idMedidor AND a.lecPagada=:lecPagada");
            query.setParameter("idMedidor", idMedidor);
            query.setParameter("lecPagada", lecPagada);

            listaLecturas = (List<Lectura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta lectura findIdMedidorAndLecPagada " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLecturas;
    }

    public List<Lectura> findMesAndNumMedidor(String busqueda, Integer mes, Integer lecAnio) {

        List<Lectura> listaLecturas = new ArrayList<Lectura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Lectura a WHERE (a.idMedidor.idPredio.idPropietario.propNombre LIKE :propNombre OR a.idMedidor.idPredio.idPropietario.propApellido LIKE :propApellido OR a.idMedidor.medNumero LIKE :medNumero) AND a.lecMes=:lecMes AND a.lecAnio=:lecAnio ORDER BY CAST(a.idMedidor.medNumero as NUMERIC) ASC");
            query.setParameter("medNumero", "%" + busqueda + "%");
            query.setParameter("propNombre", "%" + busqueda + "%");
            query.setParameter("propApellido", "%" + busqueda + "%");
            query.setParameter("lecMes", mes);
            query.setParameter("lecAnio", lecAnio);

            listaLecturas = (List<Lectura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta lectura findIdMedidor " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLecturas;
    }

    public List<Lectura> findLecturasPorUsuarioNumMedidor(String busqueda, String pagada, Date inicio, Date fin) {

        List<Lectura> listaLecturas = new ArrayList<Lectura>();
        try {

            String SQL = "SELECT  a FROM Lectura a WHERE (a.idMedidor.idPredio.idPropietario.propNombre LIKE :propNombre OR a.idMedidor.idPredio.idPropietario.propApellido LIKE :propApellido OR a.idMedidor.medNumero LIKE :medNumero) ";
            String WHERE = " and a.lecFecha between :inicio and :fin ";
            String WHEREESTADO = " and a.lecPagada=:lecPagada ";
            String ORDERBY = " ORDER BY a.lecFecha ASC";
            //Connection connection = em.unwrap(Connection.class);

            if (pagada.equals("TODO")) {
                SQL = SQL + WHERE + ORDERBY;
            } else {
                SQL = SQL + WHERE + WHEREESTADO + ORDERBY;
            }
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery(SQL);
            query.setParameter("medNumero", "%" + busqueda + "%");
            query.setParameter("propNombre", "%" + busqueda + "%");
            query.setParameter("propApellido", "%" + busqueda + "%");
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            if (pagada.equals("TODO")) {

            } else {
                query.setParameter("lecPagada", pagada);
            }

            listaLecturas = (List<Lectura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta lectura findIdMedidor " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLecturas;
    }

    public void iniciarProximoMes(Integer mes, Date fecha) {
        try {
            em = HelperPersistencia.getEMF();

            em.getTransaction().begin();
//           Query elimina= em.createNativeQuery("delete from model_ruta;");
//            int i=elimina.executeUpdate();
//            System.out.println("VALOR BORRA "+i);
            StoredProcedureQuery queryStore = em.createStoredProcedureQuery("iniciar_proximo_mes_par");
            queryStore.registerStoredProcedureParameter("numeromes", Integer.class, ParameterMode.IN);
            queryStore.registerStoredProcedureParameter("fecharegistro", Date.class, ParameterMode.IN);
            queryStore.setParameter("numeromes", mes);
            queryStore.setParameter("fecharegistro", fecha);
            queryStore.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("error iniciarProximoMes " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Lectura> finbByMedidor(Medidor valor) {

        List<Lectura> listaLecturas = new ArrayList<Lectura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Lectura a WHERE a.idMedidor =:idMedidor  AND a.lecPagada='N' ORDER BY A.lecMes ASC");
            query.setParameter("idMedidor", valor);

            listaLecturas = (List<Lectura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta lectura findIdMedidor " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLecturas;
    }

    /*busar lecturas de medidores activos e inactivos*/
    public List<Lectura> findLecturasMedActivoInactivo(String busqueda, Boolean estado) {

        List<Lectura> listaLecturas = new ArrayList<Lectura>();
        try {

            String SQL = "SELECT  a FROM Lectura a WHERE (a.idMedidor.idPredio.idPropietario.propNombre LIKE :propNombre OR a.idMedidor.idPredio.idPropietario.propApellido LIKE :propApellido OR a.idMedidor.medNumero LIKE :medNumero) ";
            String WHERE = "  ";
//            String WHERE = " and a.lecFecha between :inicio and :fin ";
            String WHEREESTADO = " and a.idMedidor.medActivo=:medActivo ";
            String ORDERBY = " ORDER BY a.idMedidor.idPredio.idPropietario.propNombre,a.idMedidor.idPredio.idPropietario.propApellido ASC";
            //Connection connection = em.unwrap(Connection.class);

            SQL = SQL + WHERE + WHEREESTADO + ORDERBY;

            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery(SQL);
            query.setParameter("medNumero", "%" + busqueda + "%");
            query.setParameter("propNombre", "%" + busqueda + "%");
            query.setParameter("propApellido", "%" + busqueda + "%");
//            query.setParameter("inicio", inicio);
//            query.setParameter("fin", fin);
            query.setParameter("medActivo", estado);

            listaLecturas = (List<Lectura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta lectura findIdMedidor " + e.getMessage());
        } finally {
            em.close();
        }

        return listaLecturas;
    }

}
