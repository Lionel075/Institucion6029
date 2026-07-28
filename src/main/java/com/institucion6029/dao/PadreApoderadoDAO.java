package com.institucion6029.dao;

import com.institucion6029.model.PadreApoderado;

public interface PadreApoderadoDAO {
    
    /**
     * Recupera la información de contacto y residencia del apoderado usando su código 'PAD-00100'.
     * @param idUsuario Código alfanumérico del apoderado.
     * @return Objeto PadreApoderado con los datos de la base de datos; null si no existe.
     */
    public PadreApoderado obtenerPorIdUsuario(String idUsuario);
}

