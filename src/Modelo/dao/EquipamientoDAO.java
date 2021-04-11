
package Modelo.dao;

import Base.GUI;
import ConexionDB.Conexion;
import Modelo.dto.EquipamientoDTO;
import Modelo.dto.MarcaDTO;
import Modelo.dto.ProcedenciaDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EquipamientoDAO implements Base.BaseSQL<EquipamientoDTO>{
    private String msj = null, sql = null;
    private Conexion conec;
    private PreparedStatement ps;
    private ResultSet rs;

    public EquipamientoDAO() {
        conec = new Conexion();
    }
    
    @Override
    public Boolean agregar(EquipamientoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO public.equipamientos( descrip, nro_serie, fecha_ingreso, modelo, comentario, id_marca, id_procedencia)\n" +
                  "VALUES ( ?, ?, ?, ?, ?, ?, ?);";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescrip());
            ps.setString(2, dto.getNroSerie());
            ps.setDate(3, dto.getFechaIngreso());
            ps.setString(4, dto.getModelo());
            ps.setString(5, dto.getComentario());
            ps.setInt(6, dto.getMarca().getId());
            ps.setInt(7, dto.getProcedencia().getId());
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
    public Boolean modificar(EquipamientoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.equipamientos SET descrip=?, nro_serie=?, fecha_ingreso=?, modelo=?, comentario=?, id_marca=?, id_procedencia=?  WHERE id=?;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescrip());
            ps.setString(2, dto.getNroSerie());
            ps.setDate(3, dto.getFechaIngreso());
            ps.setString(4, dto.getModelo());
            ps.setString(5, dto.getComentario());
            ps.setInt(6, dto.getMarca().getId());
            ps.setInt(7, dto.getProcedencia().getId());
            ps.setInt(8, dto.getId());
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
    public Boolean eliminar(EquipamientoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM public.equipamientos WHERE id = ? ;";
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
    public ArrayList<EquipamientoDTO> recuperarRegistros() {
        try {
            sql = "SELECT a.id, a.descrip, a.nro_serie, a.fecha_ingreso, a.modelo, a.comentario, a.id_marca, b.descrip as marca,  \n" +
                  "       a.id_procedencia, c.descrip as procedencia\n" +
                  "FROM public.equipamientos a INNER JOIN public.marcas b ON a.id_marca=b.id\n" +
                  "	                       INNER JOIN public.procedencias c ON   a.id_procedencia=c.id;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            ArrayList<EquipamientoDTO> lista = new ArrayList<>();
            while (rs != null && rs.next()) {
                EquipamientoDTO dto = new EquipamientoDTO();
                dto.setId(rs.getInt("id"));
                dto.setDescrip(GUI.NullVacio(rs.getString("descrip")));
                dto.setNroSerie(GUI.NullVacio(rs.getString("nro_serie")));
                dto.setFechaIngreso(rs.getDate("fecha_ingreso"));
                dto.setModelo(GUI.NullVacio(rs.getString("modelo")));
                dto.setComentario(GUI.NullVacio(rs.getString("comentario")));
                dto.setMarca(new MarcaDTO(rs.getInt("id_marca"), GUI.NullVacio(rs.getString("marca"))));
                dto.setProcedencia(new ProcedenciaDTO(rs.getInt("id_procedencia"), GUI.NullVacio(rs.getString("procedencia"))));
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
    public EquipamientoDTO recuperarRegistro(EquipamientoDTO dto) {
        try {
            EquipamientoDTO equipamiento = null;
            sql = "SELECT a.id, a.descrip, a.nro_serie, a.fecha_ingreso, a.modelo, a.comentario, a.id_marca, b.descrip as marca,  \n" +
                  "       a.id_procedencia, c.descrip as procedencia\n" +
                  "FROM public.equipamientos a INNER JOIN public.marcas b ON a.id_marca=b.id\n" +
                  "	                       INNER JOIN public.procedencias c ON   a.id_procedencia=c.id " +
                  " WHERE a.id = ?;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                equipamiento = new EquipamientoDTO();
                equipamiento.setId(rs.getInt("id"));
                equipamiento.setDescrip(GUI.NullVacio(rs.getString("descrip")));
                equipamiento.setNroSerie(GUI.NullVacio(rs.getString("nro_serie")));
                equipamiento.setFechaIngreso(rs.getDate("fecha_ingreso"));
                equipamiento.setModelo(GUI.NullVacio(rs.getString("modelo")));
                equipamiento.setComentario(GUI.NullVacio(rs.getString("comentario")));
                equipamiento.setMarca(new MarcaDTO(rs.getInt("id_marca"), GUI.NullVacio(rs.getString("marca"))));
                equipamiento.setProcedencia(new ProcedenciaDTO(rs.getInt("id_procedencia"), GUI.NullVacio(rs.getString("procedencia"))));
            }
            return equipamiento;
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
            sql = "SELECT * FROM public.equipamientos WHERE UPPER(TRIM(descrip))= UPPER(TRIM(?))";
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
