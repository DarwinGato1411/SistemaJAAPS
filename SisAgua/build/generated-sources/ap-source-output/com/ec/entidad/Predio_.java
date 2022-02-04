package com.ec.entidad;

import com.ec.entidad.Medidor;
import com.ec.entidad.Propietario;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-03T21:47:50")
@StaticMetamodel(Predio.class)
public class Predio_ { 

    public static volatile SingularAttribute<Predio, BigDecimal> preArea;
    public static volatile SingularAttribute<Predio, Date> preFechaRegistro;
    public static volatile SingularAttribute<Predio, Integer> predNumero;
    public static volatile SingularAttribute<Predio, String> preUbicacion;
    public static volatile SingularAttribute<Predio, Integer> idPredio;
    public static volatile SingularAttribute<Predio, Propietario> idPropietario;
    public static volatile CollectionAttribute<Predio, Medidor> medidorCollection;
    public static volatile SingularAttribute<Predio, String> predDireccion;

}