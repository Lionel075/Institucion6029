package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.institucion6029.dao.HorarioDAO;
import com.institucion6029.model.HorarioClase;
import com.institucion6029.utility.Conexion;

public class HorarioDAOImpl implements HorarioDAO {

    @Override
    public List<HorarioClase> obtenerHorarioPorSeccion(int idSeccion) {
        List<HorarioClase> listaHorario = new ArrayList<>();
        
        // Consulta estructurada ordenando los bloques por la secuencia de días laborables y hora de entrada
        String sql = "SELECT id_horario, id_seccion, id_curso, id_docente, dia_semana, hora_inicio, hora_fin "
                   + "FROM sch_horarios_clases WHERE id_seccion = ? "
                   + "ORDER BY FIELD(dia_semana, 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes'), hora_inicio ASC";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSeccion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HorarioClase bloque = new HorarioClase();
                    bloque.setIdHorario(rs.getInt("id_horario"));
                    bloque.setIdSeccion(rs.getInt("id_seccion"));
                    bloque.setIdCurso(rs.getInt("id_curso"));
                    bloque.setIdDocente(rs.getString("id_docente")); // Mapeado correctamente como String
                    bloque.setDiaSemana(rs.getString("dia_semana"));
                    bloque.setHoraInicio(rs.getTime("hora_inicio"));
                    bloque.setHoraFin(rs.getTime("hora_fin"));
                    
                    listaHorario.add(bloque);
                }
            }
        } catch (SQLException e) {
            System.err.println("[HorarioDAOImpl] Error al obtener el horario de la sección: " + e.getMessage());
        }
        return listaHorario;
    }
}

