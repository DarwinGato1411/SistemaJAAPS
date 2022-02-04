package com.ec.entidad;

import com.ec.entidad.Medidor;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-03T21:47:50")
@StaticMetamodel(EstadoLectura.class)
public class EstadoLectura_ { 

    public static volatile SingularAttribute<EstadoLectura, String> estlNombre;
    public static volatile SingularAttribute<EstadoLectura, Boolean> estlEstado;
    public static volatile SingularAttribute<EstadoLectura, Integer> idEstadoLectura;
    public static volatile SingularAttribute<EstadoLectura, String> estlSigla;
    public static volatile CollectionAttribute<EstadoLectura, Medidor> medidorCollection;

}