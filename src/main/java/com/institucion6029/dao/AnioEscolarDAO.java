package com.institucion6029.dao;

import com.institucion6029.model.AnioEscolar;

public interface AnioEscolarDAO {

    /**
     * Retorna el único año escolar con estado_ano = 'Activo'.
     * null si no hay ninguno activo configurado.
     */
    public AnioEscolar obtenerAnioActivo();
}