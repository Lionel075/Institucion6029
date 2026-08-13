package com.institucion6029.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.institucion6029.dao.AlumnoDAO;
import com.institucion6029.model.Alumno;
import com.institucion6029.utility.Conexion;

public class AlumnoDAOImpl implements AlumnoDAO {

    @Override
    public List<Alumno> listarHijosPorPadre(String idPadre) {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT id_alumno, nombres, apellidos, fecha_nacimiento, id_padre "
                   + "FROM per_alumnos WHERE id_padre = ? ORDER BY id_alumno ASC";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, idPadre);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Alumno alu = new Alumno();
                    alu.setIdAlumno(rs.getInt("id_alumno"));
                    alu.setNombres(rs.getString("nombres"));
                    alu.setApellidos(rs.getString("apellidos"));
                    alu.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                    alu.setIdPadre(rs.getString("id_padre"));
                    lista.add(alu);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AlumnoDAOImpl] Error al listar hijos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Alumno buscarPorId(int idAlumno) {
        Alumno alu = null;
        String sql = "SELECT id_alumno, nombres, apellidos, fecha_nacimiento, id_padre, estado_academico "
                   + "FROM per_alumnos WHERE id_alumno = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAlumno);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    alu = new Alumno();
                    alu.setIdAlumno(rs.getInt("id_alumno"));
                    alu.setNombres(rs.getString("nombres"));
                    alu.setApellidos(rs.getString("apellidos"));
                    alu.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                    alu.setIdPadre(rs.getString("id_padre"));
                    alu.setEstadoAcademico(rs.getString("estado_academico")); // NUEVO
                }
            }
        } catch (SQLException e) {
            System.err.println("[AlumnoDAOImpl] Error al buscar alumno: " + e.getMessage());
        }
        return alu;
    }

}
