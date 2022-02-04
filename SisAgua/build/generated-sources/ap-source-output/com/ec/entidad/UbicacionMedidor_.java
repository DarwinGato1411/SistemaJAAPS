package com.ec.entidad;

import com.ec.entidad.Medidor;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-03T11:51:03")
@StaticMetamodel(UbicacionMedidor.class)
public class UbicacionMedidor_ { 

    public static volatile SingularAttribute<UbicacionMedidor, String> ubimNombre;
    public static volatile SingularAttribute<UbicacionMedidor, Boolean> ubimEstado;
    public static volatile CollectionAttribute<UbicacionMedidor, Medidor> medidorCollection;
    public static volatile SingularAttribute<UbicacionMedidor, Integer> idUbicacionMedidor;
    public static volatile SingularAttribute<UbicacionMedidor, String> ubimSigla;

}