/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Darwin
 */
public class ListadoMeses {

    private static List<ModeloMeses> listaMeses;
    private static ModeloMeses mesActual;

    static {
        listaMeses = new ArrayList<ModeloMeses>();
        listaMeses.add(new ModeloMeses(1, "ENERO"));
        listaMeses.add(new ModeloMeses(2, "FEBRERO"));
        listaMeses.add(new ModeloMeses(3, "MARZO"));
        listaMeses.add(new ModeloMeses(4, "ABRIL"));
        listaMeses.add(new ModeloMeses(5, "MAYO"));
        listaMeses.add(new ModeloMeses(6, "JUNIO"));
        listaMeses.add(new ModeloMeses(7, "JULIO"));
        listaMeses.add(new ModeloMeses(8, "AGOSTO"));
        listaMeses.add(new ModeloMeses(9, "SEPTIEMBRE"));
        listaMeses.add(new ModeloMeses(10, "OCTUBRE"));
        listaMeses.add(new ModeloMeses(11, "NOVIEMBRE"));
        listaMeses.add(new ModeloMeses(12, "DICIEMBRE"));

    }

    public static List<ModeloMeses> getListaMeses() {
        return listaMeses;
    }

    public static ModeloMeses getMesActual() {
        Integer numeroMes = new Date().getMonth() + 1;
        for (ModeloMeses listaMese : listaMeses) {
            if (listaMese.getNumero() == numeroMes) {
                mesActual = listaMese;
                return mesActual;
            }
        }
        return mesActual;
    }

}
