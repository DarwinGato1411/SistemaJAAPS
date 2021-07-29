/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;

import com.ec.entidad.DetalleTarifa;
import com.ec.entidad.Tarifa;

/**
 *
 * @author Darwin
 */
public class ParamTarifaDetalle {

    private Tarifa tarifa;
    private DetalleTarifa detalleTarifa;

    public ParamTarifaDetalle() {
    }

    public ParamTarifaDetalle(Tarifa tarifa, DetalleTarifa detalleTarifa) {
        this.tarifa = tarifa;
        this.detalleTarifa = detalleTarifa;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public DetalleTarifa getDetalleTarifa() {
        return detalleTarifa;
    }

    public void setDetalleTarifa(DetalleTarifa detalleTarifa) {
        this.detalleTarifa = detalleTarifa;
    }

}
