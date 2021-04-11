package Modelo.dao;

import ConexionDB.Conexion;
import Modelo.dto.EquipamientoDTO;
import Modelo.dto.MantenimientoEquipamientoDTO;
import Modelo.dto.TipoMantenimientoDTO;
import Modelo.dto.UsuarioDTO;
import static Vista.home.nombreUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoEquipamientoDAO implements Base.BaseSQL<MantenimientoEquipamientoDTO> {

    private String msj = null, sql = null , sqlFuncionario = null;
    private Conexion conec , conec1;
    private PreparedStatement ps , ps1;
    private ResultSet rs , rs1;

    public MantenimientoEquipamientoDAO() {
        conec = new Conexion();
    }

    @Override
    public Boolean agregar(MantenimientoEquipamientoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            for (TipoMantenimientoDTO x : dto.getMantenimientos()) {
                sql = "INSERT INTO public.mantenimientos(id_tipo_mant, id_usuario, id_equipamiento, fecha_operacion,comentario) VALUES (?, ?, ?, ?, ?);";
                ps = conec.obtenerConexion().prepareStatement(sql);
                ps.setInt(1, x.getId());
                ps.setInt(2, dto.getUsuario().getId());
                ps.setInt(3, dto.getEquipamiento().getId());
                ps.setDate(4, dto.getFechaOperacion());
                ps.setString(5, x.getComentario());
                if (ps.executeUpdate() < 0) {
                    conec.Transaccion(Conexion.TR.CANCELAR);
                    return false;
                }
            }
            conec.Transaccion(Conexion.TR.CONFIRMAR);
            return true;
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
    public Boolean modificar(MantenimientoEquipamientoDTO dto) {
        if (eliminar(dto) == true) {
            return agregar(dto);
        } else {
            return false;
        }
    }

    @Override
    public Boolean eliminar(MantenimientoEquipamientoDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM public.mantenimientos WHERE id_equipamiento = ? ;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getEquipamiento().getId());
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
    public MantenimientoEquipamientoDTO recuperarRegistro(MantenimientoEquipamientoDTO dto) {
                try {
            sql = "SELECT a.id, a.id_tipo_mant,c.descrip as tipo_mant, a.id_usuario, a.id_equipamiento, b.descrip as equipo, a.fecha_operacion,  a.comentario\n"
                    + "FROM public.mantenimientos a INNER JOIN public.equipamientos b ON a.id_equipamiento=b.id\n"
                    + "			            INNER JOIN public.tipos_mantenimientos c ON a.id_tipo_mant=c.id\n"
                    + "WHERE a.id_equipamiento = ? ;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getEquipamiento().getId());
            rs = ps.executeQuery();
            MantenimientoEquipamientoDTO d = new MantenimientoEquipamientoDTO();
            List<TipoMantenimientoDTO> lista= new ArrayList<>();
            while (rs != null && rs.next()) {
                d.setEquipamiento(new EquipamientoDTO(rs.getInt("id_equipamiento"), rs.getString("equipo")));
                    lista.add(new TipoMantenimientoDTO(rs.getInt("id_tipo_mant"), rs.getString("tipo_mant")));
                d.setMantenimientos(lista);

            }
            return d;
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
    public List<MantenimientoEquipamientoDTO> recuperarRegistros() {
        try {
            List<MantenimientoEquipamientoDTO> lista = null;
            sql = "SELECT a.id, a.id_tipo_mant,c.descrip as tipo_mant, a.id_usuario, a.id_equipamiento, b.descrip as equipo, a.fecha_operacion,  a.comentario\n"
                    + "FROM public.mantenimientos a INNER JOIN public.equipamientos b ON a.id_equipamiento=b.id\n"
                    + "			            INNER JOIN public.tipos_mantenimientos c ON a.id_tipo_mant=c.id;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs != null && rs.next()) {
                MantenimientoEquipamientoDTO dto = new MantenimientoEquipamientoDTO();
                dto.setEquipamiento(new EquipamientoDTO(rs.getInt("id_equipamiento"), rs.getString("equipo")));
                dto.setMantenimiento(new TipoMantenimientoDTO(rs.getInt("id_tipo_mant"), rs.getString("tipo_mant")));
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
    
    public List<MantenimientoEquipamientoDTO> recuperarMantenimientosEquipamiento(Integer idEquipo) {
        try {
            String filtro="";
            if (idEquipo != null ) {
                filtro = " WHERE a.id_equipamiento = "+ idEquipo +";";
            }
            List<MantenimientoEquipamientoDTO> lista = null;
            sql = "SELECT a.id, a.fecha_operacion, a.comentario, a.id_usuario, d.usuario,d.id_funcionario,a.id_equipamiento, b.descrip, e.nombres, a.id_tipo_mant, c.descrip as tipo_mant \n"
                + "FROM mantenimientos a INNER JOIN equipamientos b ON a.id_equipamiento=b.id\n"
                + "			            INNER JOIN tipos_mantenimientos c ON a.id_tipo_mant=c.id\n"
                + "			            INNER JOIN usuarios d ON a.id_usuario=d.id\n" 
                + "                                 INNER JOIN funcionarios e ON d.id_funcionario = e.id  " + (idEquipo != null ? filtro : ";");
            ps = conec.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            
            lista = new ArrayList<>();
            //int id_funcionario = 0;
            
            while (rs != null && rs.next()) {
                MantenimientoEquipamientoDTO dto = new MantenimientoEquipamientoDTO();
                dto.setEquipamiento(new EquipamientoDTO(rs.getInt("id_equipamiento"), rs.getString("descrip")));
                dto.setMantenimiento(new TipoMantenimientoDTO(rs.getInt("id_tipo_mant"), rs.getString("tipo_mant"),(rs.getString("comentario") == null ? "" : rs.getString("comentario"))));
                UsuarioDTO usu= new UsuarioDTO();
                //traemos el nombre del funcionario
//                id_funcionario = rs.getInt("id_funcionario");
//                sqlFuncionario = "SELECT nombres FROM funcionarios WHERE id = ?";
//                ps1 = conec.obtenerConexion().prepareStatement(sqlFuncionario);
//                ps1.setInt(1, id_funcionario);
//                rs1 = ps1.executeQuery();
//                while (rs1 != null && rs1.next()) {
//                    usu.setUsusario(rs1.getString("nombres"));//ponemos el nombre 
//                }
                //fin
                    usu.setUsusario(rs.getString("usuario")+" - "+rs.getString("nombres"));
                    usu.setId(rs.getInt("id_usuario"));
                    
                dto.setUsuario(usu);
                dto.setFechaOperacion(rs.getDate("fecha_operacion"));
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
    public String msj() {
        return msj;
    }
    
      public List<MantenimientoEquipamientoDTO> getMantenimientosHistorico(String desde , String hasta) {
        if ((desde == null) || (hasta == null)) {
            sql = "SELECT a.id,c.descrip as tipo_mant,  \n" +
"	b.descrip as equipo, \n" +
"	a.comentario, u.usuario, f.nombres,a.fecha_operacion\n" +
"	\n" +
"                    FROM public.mantenimientos a \n" +
"\n" +
"                    INNER JOIN public.equipamientos b \n" +
"                    \n" +
"                    ON \n" +
"                    a.id_equipamiento=b.id\n" +
"                    \n" +
"		    INNER JOIN public.tipos_mantenimientos c \n" +
"		    \n" +
"		    ON a.id_tipo_mant=c.id\n" +
"\n" +
"		    INNER JOIN usuarios as u\n" +
"		    ON\n" +
"		    a.id_usuario = u.id\n" +
"\n" +
"		    INNER JOIN funcionarios f\n" +
"		    ON\n" +
"		    u.id_funcionario = f.id\n" +
"                   ORDER BY a.fecha_operacion DESC";                    

        }else{
            sql = "SELECT a.id,c.descrip as tipo_mant,  \n" +
"	b.descrip as equipo, \n" +
"	a.comentario, u.usuario, f.nombres,a.fecha_operacion\n" +
"	\n" +
"                    FROM public.mantenimientos a \n" +
"\n" +
"                    INNER JOIN public.equipamientos b \n" +
"                    \n" +
"                    ON \n" +
"                    a.id_equipamiento=b.id\n" +
"                    \n" +
"		    INNER JOIN public.tipos_mantenimientos c \n" +
"		    \n" +
"		    ON a.id_tipo_mant=c.id\n" +
"\n" +
"		    INNER JOIN usuarios as u\n" +
"		    ON\n" +
"		    a.id_usuario = u.id\n" +
"\n" +
"		    INNER JOIN funcionarios f\n" +
"		    ON\n" +
"		    u.id_funcionario = f.id\n" +
"\n" +
"		    WHERE a.fecha_operacion  BETWEEN '"+desde+"' AND '"+hasta+"' ORDER BY a.id DESC;";
        }

        try {
            ps = conec.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            List<MantenimientoEquipamientoDTO> lista = new ArrayList<>();
            MantenimientoEquipamientoDTO dto = null;
            while (rs != null && rs.next()) {
                dto = new MantenimientoEquipamientoDTO();
                dto.setId(rs.getInt("id"));
                dto.setMantenimiento(new TipoMantenimientoDTO(rs.getString("tipo_mant")));
                dto.setEquipamiento(new EquipamientoDTO(rs.getString("equipo")));
                dto.setComentario(rs.getString("comentario"));
                dto.setUsuario(new UsuarioDTO(rs.getString("usuario")));
                dto.setUsuario(new UsuarioDTO(rs.getString("nombres"), rs.getString("usuario")));
                dto.setFechaOperacion(rs.getDate("fecha_operacion"));
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

}

