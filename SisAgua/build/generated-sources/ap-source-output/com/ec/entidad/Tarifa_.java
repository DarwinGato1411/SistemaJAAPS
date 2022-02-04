package com.ec.entidad;

import com.ec.entidad.DetalleTarifa;
import com.ec.entidad.Medidor;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-03T21:47:50")
@StaticMetamodel(Tarifa.class)
public class Tarifa_ { 

    public static volatile SingularAttribute<Tarifa, Integer> idTarifa;
    public static volatile SingularAttribute<Tarifa, Date> tariFecha;
    public static volatile SingularAttribute<Tarifa, String> tariSigla;
    public static volatile SingularAttribute<Tarifa, Boolean> tariEstado;
    public static volatile CollectionAttribute<Tarifa, Medidor> medidorCollection;
    public static volatile CollectionAttribute<Tarifa, DetalleTarifa> detalleTarifaCollection;
    public static volatile SingularAttribute<Tarifa, String> tariDescripcion;
    public static volatile SingularAttribute<Tarifa, BigDecimal> tariMetrosBase;

}