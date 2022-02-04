package com.ec.entidad;

import com.ec.entidad.EstadoLectura;
import com.ec.entidad.Lectura;
import com.ec.entidad.Predio;
import com.ec.entidad.Tarifa;
import com.ec.entidad.UbicacionMedidor;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-03T11:51:03")
@StaticMetamodel(Medidor.class)
public class Medidor_ { 

    public static volatile SingularAttribute<Medidor, Date> medFechaRegistro;
    public static volatile SingularAttribute<Medidor, String> medBarrio;
    public static volatile CollectionAttribute<Medidor, Lectura> lecturaCollection;
    public static volatile SingularAttribute<Medidor, Predio> idPredio;
    public static volatile SingularAttribute<Medidor, Date> medFechaInstala;
    public static volatile SingularAttribute<Medidor, String> medNumero;
    public static volatile SingularAttribute<Medidor, String> medMarca;
    public static volatile SingularAttribute<Medidor, EstadoLectura> idEstadoLectura;
    public static volatile SingularAttribute<Medidor, Tarifa> idTarifa;
    public static volatile SingularAttribute<Medidor, String> medDireccion;
    public static volatile SingularAttribute<Medidor, UbicacionMedidor> idUbicacionMedidor;
    public static volatile SingularAttribute<Medidor, Integer> idMedidor;
    public static volatile SingularAttribute<Medidor, String> medDescripcion;
    public static volatile SingularAttribute<Medidor, Integer> medAnio;

}