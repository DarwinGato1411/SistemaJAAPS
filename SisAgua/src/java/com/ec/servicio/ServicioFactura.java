/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.dao.DetalleFacturaDAO;
import com.ec.entidad.Cliente;
import com.ec.entidad.DetalleFactura;
import com.ec.entidad.Factura;
import com.ec.entidad.Lectura;
import com.ec.entidad.Tipoambiente;
import com.ec.untilitario.Totales;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioFactura {

    ServicioDetalleFactura servicioDetalleFactura = new ServicioDetalleFactura();
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Factura factura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(factura);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar factura simple " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void guardarFactura(List<DetalleFacturaDAO> detalleFacturaDAOs, Factura factura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();

            DetalleFacturaDAO recuAO = detalleFacturaDAOs.get(0);
            if (recuAO.getLectura() != null) {
                factura.setFacLecAnterior(recuAO.getLectura() != null ? recuAO.getLectura().getLecAnterior() : BigDecimal.ZERO);
                factura.setFacLecActual(recuAO.getLectura() != null ? recuAO.getLectura().getLecActual() : BigDecimal.ZERO);
                factura.setFacMetrosCubicos(recuAO.getLectura() != null ? recuAO.getLectura().getLecMetrosCubicos() : BigDecimal.ZERO);
                factura.setFacLecMes(recuAO.getLectura() != null ? recuAO.getLectura().getLecMes() : 0);
                factura.setFacMedidor(recuAO.getLectura() != null ? recuAO.getLectura().getIdMedidor().getMedNumero() : "S/N");
                factura.setFacDirMedidor(recuAO.getLectura() != null ? recuAO.getLectura().getIdMedidor().getMedDireccion() : "S/N");
            }

            factura.setIdNotaEntrega(detalleFacturaDAOs.size() > 0 ? detalleFacturaDAOs.get(0).getFactura() != null ? detalleFacturaDAOs.get(0).getFactura().getIdFactura() : 0 : 0);
            
            em.persist(factura);
            em.flush();
            DetalleFactura detalleFactura = null;
            for (DetalleFacturaDAO item : detalleFacturaDAOs) {
                detalleFactura = new DetalleFactura(item.getCantidad(),
                            item.getDescripcion(),
                            item.getSubTotal(),
                            item.getTotal(),
                            item.getProducto(),
                            factura, item.getTipoVenta());
                detalleFactura.setDetIva(item.getDetIva());
                detalleFactura.setDetTotalconiva(item.getDetTotalconiva());

                detalleFactura.setDetSubtotaldescuento(item.getSubTotalDescuento());
                detalleFactura.setDetTotaldescuento(item.getDetTotaldescuento());
                detalleFactura.setDetPordescuento(item.getDetPordescuento());
                detalleFactura.setDetValdescuento(item.getDetValdescuento());
                detalleFactura.setDetTotaldescuentoiva(item.getDetTotalconivadescuento());
                detalleFactura.setDetCantpordescuento(item.getDetCantpordescuento());
                detalleFactura.setDetSubtotaldescuentoporcantidad(item.getDetSubtotaldescuentoporcantidad());
                detalleFactura.setDetCodTipoVenta(item.getCodTipoVenta());
                detalleFactura.setDetCodIva("2");
                detalleFactura.setDetCodPorcentaje(item.getProducto().getProdGrabaIva() ? "2" : "0");
                detalleFactura.setDetTarifa(item.getProducto().getProdIva());

                /*para las motocicletas o vehiculos*/
                detalleFactura.setTipoIdentificacionPropietario(item.getTipoIdentificacionPropietario());
                detalleFactura.setDetCamvcpn(item.getDetCamvcpn());
                detalleFactura.setDetSerialvin(item.getDetSerialvin());
                detalleFactura.setDetAtributo(item.getDetAtributo());
                detalleFactura.setTipodir(item.getTipodir());
                detalleFactura.setCalle(item.getCalle());
                detalleFactura.setNumero(item.getNumero());
                detalleFactura.setInterseccion(item.getInterseccion());
                detalleFactura.setTipotel(item.getTipotel());
                detalleFactura.setProvincia(item.getProvincia());
                detalleFactura.setNumerotel(item.getNumerotel());
                detalleFactura.setCodigoCantonMatriculacion(item.getCodigoCantonMatriculacion());
                detalleFactura.setDetLecActual(item.getLectura() != null ? item.getLectura().getLecActual() : BigDecimal.ZERO);
                detalleFactura.setDetLecAnterior(item.getLectura() != null ? item.getLectura().getLecAnterior() : BigDecimal.ZERO);
                detalleFactura.setDetMetrosCubicos(item.getLectura() != null ? item.getLectura().getLecMetrosCubicos() : BigDecimal.ZERO);
                detalleFactura.setDetLecMes(item.getLectura() != null ? item.getLectura().getLecMes() : 0);
                detalleFactura.setDetMedidor(item.getLectura() != null ? item.getLectura().getIdMedidor().getMedNumero() : "");
                detalleFactura.setDetDirMedidor(item.getLectura() != null ? item.getLectura().getIdMedidor().getMedBarrio() : "");
                detalleFactura.setIdLectura(item.getLectura());

                if (item.getLectura() != null) {
                    Lectura actual = item.getLectura();
                    actual.setLecPagada("S");
                    em.merge(actual);
                    detalleFactura.setIdLectura(actual);
                }
                em.persist(detalleFactura);
                em.flush();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar factura GUARDAR CON DETALLE " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificarFactura(List<DetalleFacturaDAO> detalleFacturaDAOs, Factura factura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            //em.remove(factura);
            em.persist(factura);
            em.flush();
            DetalleFactura detalleFactura = null;
            for (DetalleFacturaDAO item : detalleFacturaDAOs) {
                detalleFactura = new DetalleFactura(item.getCantidad(),
                            item.getDescripcion(),
                            item.getSubTotal(),
                            item.getTotal(),
                            item.getProducto(),
                            factura, item.getTipoVenta());
                detalleFactura.setDetIva(item.getDetIva());
                detalleFactura.setDetTotalconiva(item.getDetTotalconiva());
//                servicioDetalleFactura.modificar(detalleFactura);
                em.persist(detalleFactura);
                em.flush();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar factura GUARDAR CON DETALLE " + e.getMessage());
        } finally {
            em.close();
        }

    }

    //guarda para la venta diaria desde nota venta
    public void guardarFacturaVentaDiaria(List<DetalleFacturaDAO> detalleFacturaDAOs, Factura factura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(factura);
//            em.flush();
            DetalleFactura detalleFactura = null;
            for (DetalleFacturaDAO item : detalleFacturaDAOs) {
                detalleFactura = new DetalleFactura(item.getCantidad(),
                            item.getDescripcion(),
                            item.getSubTotal(),
                            item.getTotal(),
                            item.getProducto(),
                            factura, item.getTipoVenta());
                detalleFactura.setDetSubtotaldescuento(item.getSubTotalDescuento());
                detalleFactura.setDetTotaldescuento(item.getDetTotaldescuento());
                detalleFactura.setDetPordescuento(item.getDetPordescuento());
                detalleFactura.setDetValdescuento(item.getDetValdescuento());
                detalleFactura.setDetTotaldescuentoiva(item.getDetTotalconivadescuento());
                detalleFactura.setDetIva(item.getDetIva());
                detalleFactura.setDetTotalconiva(item.getDetTotalconiva());
                detalleFactura.setDetCodTipoVenta(item.getCodTipoVenta());
                em.persist(detalleFactura);
                em.flush();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar factura Venta diaria " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Factura factura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(factura));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  factura" + e);
        } finally {
            em.close();
        }

    }

    public void modificar(Factura factura) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(factura);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar factura " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Factura> FindALlFactura() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findAll", Factura.class);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public Factura FindUltimaFactura() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facTipo='FACT' AND f.facNumero IS NOT NULL ORDER BY f.facNumero DESC");
            query.setMaxResults(1);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return facturas;
    }

    public Factura FindUltimaProforma() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facTipo='PROF' AND f.facNumProforma IS NOT NULL ORDER BY f.idFactura DESC");
            query.setMaxResults(1);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    public Factura findFirIdFact(Integer idFactura) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findByIdFactura", Factura.class);
            query.setMaxResults(2);
            query.setParameter("idFactura", idFactura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    public Factura findByIdCotizacion(Integer idFactura) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findByIdCotizacion", Factura.class);
            query.setMaxResults(2);
            query.setParameter("idFactura", idFactura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    public Factura findFirIdFactDiaria(Integer idFactura) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findByIdFacturaDiaria", Factura.class);
            query.setMaxResults(2);
            query.setParameter("idFactura", idFactura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    public List<Factura> FindLikeCliente(String cliente) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findLikeCliente", Factura.class);
            query.setMaxResults(400);
            query.setParameter("cliente", "%" + cliente + "%");
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> FindLikeNumeroFacturaText(String numfac) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facNumeroText LIKE :facNumeroText AND f.facNumero > 0 AND f.facTipo='FACT' ORDER BY f.idFactura DESC");
            query.setMaxResults(400);
            query.setParameter("facNumeroText", "%" + numfac + "%");
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findLikeProformaCliente(String cliente) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findLikeProformaCliente", Factura.class);
            query.setMaxResults(400);
            query.setParameter("cliente", "%" + cliente + "%");
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    //consulta las 20 primeras notas de venta
    public List<Factura> FindALlFacturaMaxVeinte() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findAllMaxUltimoVeinte", Factura.class);
            query.setMaxResults(400);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findAllProformas() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findAllProformas", Factura.class);
            query.setMaxResults(400);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    //buscar por estado
    public List<Factura> FindEstado(String estado) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findAllEstado", Factura.class);
//            query.setMaxResults(2);
            query.setParameter("facEstado", estado);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    //buscar por estado
    public List<Factura> findEstadoFactura(String estado) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query;
            if (!estado.equals("TODO")) {
                query = em.createQuery("SELECT f FROM Factura f WHERE  f.facEstado=:facEstado AND f.facTipo='FACT'");
                query.setParameter("facEstado", estado);
            } else {
                query = em.createQuery("SELECT f FROM Factura f");
                query.setMaxResults(2000);
            }

            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findEstadoCliente(String estado, Cliente cliente) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE  f.facEstado=:facEstado AND f.idCliente=:idCliente AND f.facNumero > 0 AND f.facTipo='FACT' ORDER BY f.facNumero DESC");
//            query.setMaxResults(2);
            query.setParameter("facEstado", estado);
            query.setParameter("idCliente", cliente);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    //ultima venta diaria
    public Factura ultimaVentaDiaria(Date fecha) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findUltimaVentaDiaria", Factura.class
            );
            query.setMaxResults(2);
            query.setParameter("facFecha", fecha);
            listaFacturas = (List<Factura>) query.getResultList();
            System.out.println("lista de venta diaria " + listaFacturas.size());
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    /*CONSULTAS DEL PORTAL*/
    //consulta las 20 primeras notas de venta
    public List<Factura> findAllMaxUltimoVeinteForCliente(Cliente idCliente) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findAllMaxUltimoVeinteForCliente", Factura.class
            );
            query.setParameter("idCliente", idCliente);
            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findBetweenFacFecha(Date inicio, Date fin, Cliente idCliente) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findBetweenFacFecha", Factura.class
            );
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            query.setParameter("idCliente", idCliente);
            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findFacFecha(Date inicio, Date fin, String estado) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            Query query;

//            String SQL = "SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin ORDER BY f.facFecha DESC";
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            if (!estado.equals("TODO")) {
                query = em.createQuery("SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin AND f.facEstado=:facEstado AND f.facTipo='FACT' ORDER BY f.facFecha DESC");
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                query.setParameter("facEstado", estado);
            } else {
                query = em.createQuery("SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin  AND f.facTipo='FACT' ORDER BY f.facFecha DESC");
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
            }

//            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findBetweenFecha(Date inicio, Date fin) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("Factura.findBetweenFecha", Factura.class
            );
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findBetweenPendientesEnviarSRI(Date inicio, Date fin) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Factura a WHERE a.facFecha BETWEEN :inicio AND :fin AND a.estadosri='PENDIENTE' AND a.facTipo='FACT' ORDER BY a.facFecha DESC");
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findBetweenDevueltaPorReenviarSRI(Date inicio, Date fin) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Factura a WHERE a.facFecha BETWEEN :inicio AND :fin AND a.estadosri='DEVUELTA' AND a.mensajesri='CLAVE ACCESO REGISTRADA' AND a.facTipo='FACT' ORDER BY a.facFecha DESC");
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findBetweenDevueltaPorCorregirSRI(Date inicio, Date fin) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Factura a WHERE a.facFecha BETWEEN :inicio AND :fin AND a.estadosri<>'AUTORIZADO' AND a.estadosri<>'PENDIENTE' AND a.mensajesri<>'CLAVE ACCESO REGISTRADA' AND a.facTipo='FACT' ORDER BY a.facFecha DESC");
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Totales> totalVenta(Date inicio, Date fin) {

        List<Totales> listaFacturas = new ArrayList<Totales>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT new com.ec.untilitario.Totales(SUM(f.facTotalBaseGravaba)) FROM Factura f WHERE f.facFecha BETWEEN :inicio AND :fin AND f.facNumero > 0 AND f.facTipo='FACT'");
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            query.setMaxResults(400);
            listaFacturas = (List<Totales>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Totales> acumuladosVentas(Date inicio, Date fin) {

        List<Totales> listaFacturas = new ArrayList<Totales>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT new com.ec.untilitario.Totales(SUM(f.facTotalBaseGravaba)) FROM Factura f WHERE f.facFecha BETWEEN :inicio AND :fin AND f.facNumero > 0 AND f.facTipo='FACT'");
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            query.setMaxResults(400);
            listaFacturas = (List<Totales>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findLikeCedula(String valor) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.idCliente.cliCedula LIKE :cliCedula AND f.facNumero > 0 AND f.facTipo='FACT' ORDER BY f.idFactura DESC");
            query.setMaxResults(400);
            query.setParameter("cliCedula", "%" + valor + "%");
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    /*NOTA DE ENTREGA*/
    public Factura findUltimaNotaEnt() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facTipo='NTE' AND f.facNumNotaEntrega IS NOT NULL ORDER BY f.facNumNotaEntrega DESC");
            query.setMaxResults(2);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    /*NOTA DE ENTREGA POR CLIENTE*/
    public List<Factura> findNotaEntPorCliente(String cedula) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facTipo='NTE' AND f.facNumNotaEntrega IS NOT NULL AND f.idCliente.cliCedula=:cliCedula AND (f.facNotaEntregaProcess IS NOT NULL OR f.facNotaEntregaProcess ='Nx   ') ORDER BY f.facNumNotaEntrega DESC");
            query.setParameter("cliCedula", cedula);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    /*TODAS LASNOTA DE ENTREGA */
    public List<Factura> findAllNotaEnt(Cliente idCliente) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facTipo='NTE' AND f.idCliente=:idCliente AND f.facNumNotaEntrega IS NOT NULL AND f.facNotaEntregaProcess IS NOT NULL AND f.facNotaEntregaProcess ='N' ORDER BY f.facNumNotaEntrega DESC");
            query.setParameter("idCliente", idCliente);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    /*NOTA DE venta numero*/
    public Factura findUltimaNotaVent() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facTipo='NTV' AND f.facNumNotaVenta IS NOT NULL ORDER BY f.facNumNotaVenta DESC");
            query.setMaxResults(2);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    /**
     * BUSQUE DE NOTAS DE VENTA
     */
    public List<Factura> findNVLikeCliente(String cliente) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.idCliente.cliNombre LIKE :cliente AND f.facNumNotaVenta > 0 AND f.facTipo='NTV' ORDER BY f.idFactura DESC");
//            query.setMaxResults(2);
            query.setParameter("cliente", "%" + cliente + "%");
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findNVLikeCedula(String valor) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.idCliente.cliCedula LIKE :cliCedula AND f.facNumNotaVenta > 0 AND f.facTipo='NTV' ORDER BY f.idFactura DESC");
//            query.setMaxResults(2);
            query.setParameter("cliCedula", "%" + valor + "%");
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    public List<Factura> findNVFacFecha(Date inicio, Date fin, String estado) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            Query query;

//            String SQL = "SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin ORDER BY f.facFecha DESC";
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            if (!estado.equals("TODO")) {
                query = em.createQuery("SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin AND f.facEstado=:facEstado AND f.facTipo='NTV' ORDER BY f.facFecha DESC");
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                query.setParameter("facEstado", estado);
            } else {
                query = em.createQuery("SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin  AND f.facTipo='NTV' ORDER BY f.facFecha DESC");
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
            }

//            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    //consulta las 20 primeras notas de venta
    public List<Factura> FindALlNVMaxVeinte() {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.facNumNotaVenta > 0 AND f.facTipo='NTV' ORDER BY f.facNumNotaVenta DESC");
            query.setMaxResults(100);
//           query.setParameter("codigoUsuario", factura);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return listaFacturas;
    }

    //recupera la nota de venta
    public Factura findFirIdFactNTV(Integer idFactura) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        Factura facturas = new Factura();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT f FROM Factura f WHERE f.idFactura =:idFactura  AND f.facTipo='NTV'");
            query.setMaxResults(2);
            query.setParameter("idFactura", idFactura);
            listaFacturas = (List<Factura>) query.getResultList();
            if (listaFacturas.size() > 0) {
                facturas = listaFacturas.get(0);
            } else {
                facturas = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura");
        } finally {
            em.close();
        }

        return facturas;
    }

    
     public List<Factura> findNotaEntregaFecha(Date inicio, Date fin, String estado) {

        List<Factura> listaFacturas = new ArrayList<Factura>();
        try {
            Query query;

//            String SQL = "SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin ORDER BY f.facFecha DESC";
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            if (!estado.equals("TODO")) {
                query = em.createQuery("SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin AND f.facNotaEntregaProcess=:facNotaEntregaProcess AND f.facTipo='NTE' ORDER BY f.facFecha DESC");
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                query.setParameter("facNotaEntregaProcess", estado);
            } else {
                query = em.createQuery("SELECT f FROM Factura f WHERE f.facFecha BETWEEN :inicio and :fin  AND f.facTipo='NTE' ORDER BY f.facFecha DESC");
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
            }

//            query.setMaxResults(400);
            listaFacturas = (List<Factura>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta factura " + e.getMessage());
        } finally {
            em.close();
        }

        return listaFacturas;
    }
    
    
}
