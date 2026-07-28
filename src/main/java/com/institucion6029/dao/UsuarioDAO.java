package com.institucion6029.dao;

import com.institucion6029.model.Usuario;

public interface UsuarioDAO {
    
    /**
     * Valida el acceso del usuario en la base de datos institucion6029.
     * Puede recibir tanto el correo electrónico como el ID alfanumérico.
     */
    public Usuario validarAcceso(String usuario, String clave);
}

