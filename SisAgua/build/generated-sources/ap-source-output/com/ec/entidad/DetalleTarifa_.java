package com.ec.entidad;

import com.ec.entidad.Tarifa;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-07-29T07:21:47")
@StaticMetamodel(DetalleTarifa.class)
public class DetalleTarifa_ { 

    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettAmbiente;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettIndustria;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettDescuento;
    public static volatile SingularAttribute<DetalleTarifa, Boolean> dettValidadesecho;
    public static volatile SingularAttribute<DetalleTarifa, Integer> idDetalleTar;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettGestion;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettMetroFinal;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettPrecioBase;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettDesechos;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettMetroInicial;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettAlcantarillado;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettPorcentajeDesechos;
    public static volatile SingularAttribute<DetalleTarifa, Tarifa> idTarifa;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettPorcentajeExcedente;
    public static volatile SingularAttribute<DetalleTarifa, Date> dettFecha;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettPrecioExcedente;
    public static volatile SingularAttribute<DetalleTarifa, BigDecimal> dettPorcentajeAlcantarillado;

}