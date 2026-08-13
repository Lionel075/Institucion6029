package com.institucion6029.dao;

import com.institucion6029.model.Usuario;

public interface UsuarioDAO {
    
    public Usuario validarAcceso(String usuario, String clave);
}

