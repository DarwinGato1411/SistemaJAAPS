package com.ec.entidad;

import com.ec.entidad.Factura;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-03T11:51:03")
@StaticMetamodel(RetencionVenta.class)
public class RetencionVenta_ { 

    public static volatile SingularAttribute<RetencionVenta, Double> rveValorRetencionIva30;
    public static volatile SingularAttribute<RetencionVenta, Integer> rveCodigo;
    public static volatile SingularAttribute<RetencionVenta, Date> rveFecha;
    public static volatile SingularAttribute<RetencionVenta, Double> rveValorRetencionIva70;
    public static volatile SingularAttribute<RetencionVenta, Double> rveValorRetencionIva100;
    public static volatile SingularAttribute<RetencionVenta, Integer> rveSecuencial;
    public static volatile SingularAttribute<RetencionVenta, Double> rveValorRetencionRenta;
    public static volatile SingularAttribute<RetencionVenta, Factura> idFactura;
    public static volatile SingularAttribute<RetencionVenta, String> rvePuntoEmision;
    public static volatile SingularAttribute<RetencionVenta, String> rveSerie;
    public static volatile SingularAttribute<RetencionVenta, String> rveAutorizacion;

}