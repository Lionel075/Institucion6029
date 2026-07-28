package com.institucion6029.dao;

import com.institucion6029.model.InstitucionWeb;

public interface LandingPageDAO {
    
    /**
     * Recupera los datos institucionales y de configuración del colegio para la web pública.
     * @return Un objeto InstitucionWeb cargado con los datos del registro principal (ID = 1); 
     *         null si ocurre un error o no se encuentra la información.
     */
    public InstitucionWeb obtenerDatosInstitucion();
}

