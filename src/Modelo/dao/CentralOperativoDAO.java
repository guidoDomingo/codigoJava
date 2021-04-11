package Modelo.dao;

import Base.BaseSQL;
import Base.GUI;
import ConexionDB.Conexion;
import Modelo.dto.CentralOperativoDTO;
import Modelo.dto.CentralOperativoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CentralOperativoDAO implements BaseSQL<CentralOperativoDTO> {

    
    private String msj = null, sql = null;
    private Conexion conec;
    private PreparedStatement ps;
    private ResultSet rs;

    public CentralOperativoDAO() {
        conec = new Conexion();
    }

    @Override
    public Boolean agregar(CentralOperativoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO public.central_operativo (descrip,comentario) VALUES (?,?);";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescrip());
            ps.setString(2, dto.getComentario());
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
    public Boolean modificar(CentralOperativoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.central_operativo SET descrip = ?, comentario = ? WHERE id = ? ;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescrip());
            ps.setString(2, dto.getComentario());
            ps.setInt(3, dto.getId());
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
    public Boolean eliminar(CentralOperativoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM public.central_operativo WHERE id = ? ;";
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
    public ArrayList<CentralOperativoDTO> recuperarRegistros() {
        try {
            sql = "SELECT id, descrip, comentario FROM public.central_operativo;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            ArrayList<CentralOperativoDTO> lista = new ArrayList<>();
            while (rs != null && rs.next()) {
                CentralOperativoDTO dto = new CentralOperativoDTO();
                dto.setId(rs.getInt("id"));
                dto.setDescrip(GUI.NullVacio(rs.getString("descrip")));
                dto.setComentario(GUI.NullVacio(rs.getString("comentario")));
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
    public CentralOperativoDTO recuperarRegistro(CentralOperativoDTO dto) {
        try {
            CentralOperativoDTO unidMedDto = null;
            sql = "SELECT id, descrip, comentario FROM public.central_operativo WHERE id = ?;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                unidMedDto = new CentralOperativoDTO();
                unidMedDto.setId(rs.getInt("id"));
                unidMedDto.setDescrip(GUI.NullVacio(rs.getString("descrip")));
                unidMedDto.setComentario(GUI.NullVacio(rs.getString("comentario")));
            }
            return unidMedDto;
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
            sql = "SELECT * FROM public.central_operativo WHERE UPPER(TRIM(descrip))= UPPER(TRIM(?))";
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
