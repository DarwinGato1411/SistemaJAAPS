package com.ec.entidad;

import com.ec.entidad.CabeceraCompra;
import com.ec.entidad.CierreCaja;
import com.ec.entidad.Factura;
import com.ec.entidad.sri.CabeceraCompraSri;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-03T11:51:03")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> usuCorreo;
    public static volatile CollectionAttribute<Usuario, CabeceraCompra> cabeceraCompraCollection;
    public static volatile SingularAttribute<Usuario, String> usuNombre;
    public static volatile SingularAttribute<Usuario, String> usuLogin;
    public static volatile SingularAttribute<Usuario, Integer> usuNivel;
    public static volatile SingularAttribute<Usuario, byte[]> usuFoto;
    public static volatile CollectionAttribute<Usuario, CierreCaja> cierreCajaCollection;
    public static volatile SingularAttribute<Usuario, Integer> idUsuario;
    public static volatile SingularAttribute<Usuario, String> usuTipoUsuario;
    public static volatile CollectionAttribute<Usuario, CabeceraCompraSri> cabeceraCompraSriCollection;
    public static volatile SingularAttribute<Usuario, String> usuPassword;
    public static volatile CollectionAttribute<Usuario, Factura> facturaCollection;

}