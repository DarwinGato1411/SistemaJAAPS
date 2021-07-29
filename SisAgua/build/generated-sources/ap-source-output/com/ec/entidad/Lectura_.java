package com.ec.entidad;

import com.ec.entidad.DetalleFactura;
import com.ec.entidad.Medidor;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-07-29T07:21:47")
@StaticMetamodel(Lectura.class)
public class Lectura_ { 

    public static volatile CollectionAttribute<Lectura, DetalleFactura> detalleFacturaCollection;
    public static volatile SingularAttribute<Lectura, Integer> lecMes;
    public static volatile SingularAttribute<Lectura, BigDecimal> lecActual;
    public static volatile SingularAttribute<Lectura, String> lecPagada;
    public static volatile SingularAttribute<Lectura, BigDecimal> lecMetrosCubicos;
    public static volatile SingularAttribute<Lectura, Integer> idLectura;
    public static volatile SingularAttribute<Lectura, Medidor> idMedidor;
    public static volatile SingularAttribute<Lectura, String> lecDescripcion;
    public static volatile SingularAttribute<Lectura, BigDecimal> lecAnterior;
    public static volatile SingularAttribute<Lectura, Date> lecFecha;

}