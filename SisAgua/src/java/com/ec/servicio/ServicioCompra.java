/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.CabeceraCompra;
import com.ec.entidad.DetalleCompra;

import com.ec.untilitario.DetalleCompraUtil;
import com.ec.untilitario.Totales;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioCompra {

    ServicioDetalleCompra servicioIngresoProducto = new ServicioDetalleCompra();
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(CabeceraCompra compra) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(compra);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar compra");
        } finally {
            em.close();
        }

    }

    public void eliminar(CabeceraCompra compra) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(compra));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  compra" + e);
        } finally {
            em.close();
        }

    }

    public void modificar(CabeceraCompra compra) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(compra);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar compra");
        } finally {
            em.close();
        }

    }

    public List<CabeceraCompra> FindALlCabeceraCompra() {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("CabeceraCompra.findAll", CabeceraCompra.class);
//           query.setParameter("codigoUsuario", compra);
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra");
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }

    public void guardarCompra(List<DetalleCompraUtil> detalleCompra, CabeceraCompra compra) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(compra);
            em.flush();
            DetalleCompra ingreso = null;
            for (DetalleCompraUtil item : detalleCompra) {
                ingreso = new DetalleCompra(item.getCantidad(),
                        item.getDescripcion(),
                        item.getSubtotal(),
                        item.getTotal(),
                        compra,
                        item.getProducto());
                em.persist(ingreso);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar factura compra " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<CabeceraCompra> findCabProveedor(String valor) {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("CabeceraCompra.findCabProveedor", CabeceraCompra.class);
            query.setParameter("cabProveedor", "%" + valor + "%");
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra");
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }

    public List<CabeceraCompra> findByBetweenFecha(Date incio, Date fin) {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CabeceraCompra c WHERE c.cabFechaEmision BETWEEN :inicio AND :fin ORDER BY c.cabFechaEmision DESC");
            query.setParameter("inicio", incio);
            query.setParameter("fin", fin);
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra");
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }

    public List<CabeceraCompra> findByNumeroFactura(String cabNumFactura) {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CabeceraCompra c WHERE c.cabNumFactura LIKE :cabNumFactura ORDER BY c.cabNumFactura DESC");
            query.setParameter("cabNumFactura", "%" + cabNumFactura + "%");
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }

    public List<Totales> totalCompra(Date incio, Date fin) {

        List<Totales> listaCabeceraCompras = new ArrayList<Totales>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT new com.ec.untilitario.Totales(SUM(c.cabSubTotal)) FROM CabeceraCompra c WHERE c.cabFechaEmision BETWEEN :inicio AND :fin ");
            query.setParameter("inicio", incio);
            query.setParameter("fin", fin);
            listaCabeceraCompras = (List<Totales>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra");
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }

    /*CONSULTA POR FECHAS LAS COMPRAS DECARGADAS DESDE EL SRI*/
    public List<CabeceraCompra> findByBetweenFechad(Date incio, Date fin) {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CabeceraCompra c WHERE c.cabFechaEmision BETWEEN :inicio AND :fin ORDER BY c.cabFechaEmision DESC");
            query.setParameter("inicio", incio);
            query.setParameter("fin", fin);
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra");
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }

    public CabeceraCompra findByAutorizacion(String cabNumFactura) {
        CabeceraCompra cabeceraCompra = null;
        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CabeceraCompra c WHERE c.cabAutorizacion =:cabAutorizacion ORDER BY c.cabNumFactura DESC");
            query.setParameter("cabAutorizacion", cabNumFactura);
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            if (listaCabeceraCompras.size() > 0) {
                cabeceraCompra = listaCabeceraCompras.get(0);

            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra " + e.getMessage());
        } finally {
            em.close();
        }

        return cabeceraCompra;
    }
    
    /*CABECERAS DEL SRI*/
    public List<CabeceraCompra> findCabProveedorSRI(String valor) {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CabeceraCompra c WHERE c.cabProveedor LIKE :cabProveedor AND c.cabHomologado='N' ORDER BY c.cabFechaEmision DESC");
            query.setParameter("cabProveedor", "%" + valor + "%");
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra");
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }
    
    public List<CabeceraCompra> findByBetweenFechaSRI(Date incio, Date fin) {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CabeceraCompra c WHERE c.cabFecha BETWEEN :inicio AND :fin AND c.cabHomologado='N' ORDER BY c.cabFechaEmision DESC");
            query.setParameter("inicio", incio);
            query.setParameter("fin", fin);
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra");
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }
    
    public List<CabeceraCompra> findByNumeroFacturaSRI(String cabNumFactura) {

        List<CabeceraCompra> listaCabeceraCompras = new ArrayList<CabeceraCompra>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CabeceraCompra c WHERE c.cabNumFactura LIKE :cabNumFactura AND c.cabHomologado='N' ORDER BY c.cabNumFactura DESC");
            query.setParameter("cabNumFactura", "%" + cabNumFactura + "%");
            listaCabeceraCompras = (List<CabeceraCompra>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta compra " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCabeceraCompras;
    }
}
