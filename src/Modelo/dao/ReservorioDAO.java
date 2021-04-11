package Modelo.dao;

import Base.BaseSQL;
import Base.GUI;
import ConexionDB.Conexion;
import Modelo.dto.ReservorioDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReservorioDAO implements BaseSQL<ReservorioDTO> {

    
    private String msj = null, sql = null;
    private Conexion conec;
    private PreparedStatement ps;
    private ResultSet rs;

    public ReservorioDAO() {
        conec = new Conexion();
    }

    @Override
    public Boolean agregar(ReservorioDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO public.reservorios (descrip,comentario,capacidad) VALUES (?, ?, ?);";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescrip());
            ps.setString(2, dto.getComentario());
            ps.setInt(3, dto.getCapacidad());
            if (ps.executeUpdate() > 0) {
                conec.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conec.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            conec.Transaccion(Conexion.TR.CANCELAR);
            msj = "Error durante la inserción del registro " + ex.getMessage();
            return false;
        } finally {
            try {
                conec.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public Boolean modificar(ReservorioDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.reservorios SET descrip = ?, comentario = ? , capacidad = ? WHERE id = ? ;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescrip());
            ps.setString(2, dto.getComentario());
            ps.setInt(3, dto.getCapacidad());
            ps.setInt(4, dto.getId());
            if (ps.executeUpdate() > 0) {
                conec.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conec.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            conec.Transaccion(Conexion.TR.CANCELAR);
            msj = "Error durante la modificación del registro " + ex.getMessage();
            return false;
        } finally {
            try {
                conec.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public Boolean eliminar(ReservorioDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM public.reservorios WHERE id = ? ;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId());
            if (ps.executeUpdate() > 0) {
                conec.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conec.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            conec.Transaccion(Conexion.TR.CANCELAR);
            msj = "Error durante la eliminación del registro " + ex.getMessage();
            return false;
        } finally {
            try {
                conec.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public ArrayList<ReservorioDTO> recuperarRegistros() {
        try {
            sql = "SELECT id, descrip, comentario, capacidad  FROM public.reservorios;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            ArrayList<ReservorioDTO> lista = new ArrayList<>();
            while (rs != null && rs.next()) {
                ReservorioDTO dto = new ReservorioDTO();
                dto.setId(rs.getInt("id"));
                dto.setDescrip(GUI.NullVacio(rs.getString("descrip")));
                dto.setComentario(GUI.NullVacio(rs.getString("comentario")));
                dto.setCapacidad(rs.getInt("capacidad"));
                lista.add(dto);
            }
            return lista;
        } catch (Exception e) {
            msj = e.getMessage();
            return null;

        } finally {
            try {
                conec.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }

    }

    @Override
    public ReservorioDTO recuperarRegistro(ReservorioDTO dto) {
        try {
            ReservorioDTO reservorioDTO = null;
            sql = "SELECT id, descrip, comentario, capacidad  FROM public.reservorios WHERE id = ?;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                reservorioDTO = new ReservorioDTO();
                reservorioDTO.setId(rs.getInt("id"));
                reservorioDTO.setDescrip(GUI.NullVacio(rs.getString("descrip")));
                reservorioDTO.setComentario(GUI.NullVacio(rs.getString("comentario")));
                reservorioDTO.setCapacidad(rs.getInt("capacidad"));
            }
            return reservorioDTO;
        } catch (Exception e) {
            msj = e.getMessage();
            return null;
        } finally {
            try {
                conec.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    public boolean existeDescrip(String descrip) {
        try {
            sql = "SELECT * FROM public.reservorios WHERE UPPER(TRIM(descrip))= UPPER(TRIM(?))";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setString(1, descrip);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            msj = e.getMessage();
            return false;
        } finally {
            try {
                conec.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public String msj() {
        return msj;
    }

}
