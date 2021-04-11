package Modelo.dao;

import ConexionDB.Conexion;
import Modelo.dto.AlarmaDTO;
import Modelo.dto.AlarmaEmitidaDTO;
import Modelo.dto.CentralOperativoDTO;
import Modelo.dto.MonitoreoAlarmaDTO;
import Modelo.dto.MonitoreoCaudalDTO;
import Modelo.dto.MonitoreoDTO;
import Modelo.dto.MonitoreoEquipoDTO;
import Modelo.dto.ReservorioDTO;
import Modelo.dto.UsuarioDTO;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonitoreoDAO {

    private Conexion conexion;
    private String msj = null, sql = null;
    private PreparedStatement ps;
    private ResultSet rs;

    public MonitoreoDAO() {
        conexion = new Conexion();
    }

    public Integer getIdMonitoreo() {
        Integer valor = null;
        try {
            sql = "SELECT id FROM monitoreo  WHERE fecha = CURRENT_DATE ORDER BY id DESC";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                valor = rs.getInt("id");
            }
            return valor;
        } catch (Exception e) {
            msj = e.getMessage();
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    public Boolean registrarMonitoreo(MonitoreoDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO monitoreo(id_usuario, id_central_operativo, fecha) VALUES ( ?, ?, ?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getUsuario().getId());
            ps.setInt(2, dto.getCentralOperativo().getId());
            ps.setDate(3, dto.getFecha());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            conexion.Transaccion(Conexion.TR.CANCELAR);
            msj = "Error durante la inserción del registro " + ex.getMessage();
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    public Boolean registrarMonitoreoAlarma(MonitoreoAlarmaDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO monitoreo_alarmas( id_monitoreo, id_alarma, horario, id_reservorio )VALUES ( ?, ?, ?,?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getMonitoreo().getId());
            ps.setInt(2, dto.getAlarma().getId());
            //ps.setTime(3, dto.getHorario());
            ps.setString(3, dto.getHora());
            ps.setInt(4, dto.getReservorio().getId());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            conexion.Transaccion(Conexion.TR.CANCELAR);
            msj = "Error durante la inserción del registro " + ex.getMessage();
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    public List<MonitoreoAlarmaDTO> getAlarmasEmitidas(String desde , String hasta) {
        if ((desde == null) || (hasta == null)) {
            sql = "select a.id, b.id_usuario,e.usuario,fun.nombres, b.fecha,  b.id_central_operativo, f.descrip as planta, a.id_reservorio ,\n" +
"d.descrip as reservorio, a.id_alarma , c.descrip as alarma, a.horario \n" +
"                     from  monitoreo_alarmas a INNER JOIN monitoreo b ON a.id_monitoreo=b.id\n" +
"                    			INNER JOIN usuarios e ON b.id_usuario=e.id\n" +
"                    			INNER join funcionarios fun ON e.id_funcionario = fun.id\n" +
"                    			INNER JOIN central_operativo f ON b.id_central_operativo=f.id\n" +
"                    		   INNER JOIN alarmas c ON  a.id_alarma=c.id\n" +
"                    		   INNER JOIN reservorios d ON  a.id_reservorio=d.id\n" +
"                    ORDER BY a.id DESC  ";

        }else{
            sql = "select a.id, b.id_usuario,e.usuario,fun.nombres , b.fecha,  b.id_central_operativo, f.descrip as planta, a.id_reservorio ,d.descrip as reservorio, a.id_alarma , c.descrip as alarma, a.horario \n" +
            " from  monitoreo_alarmas a INNER JOIN monitoreo b ON a.id_monitoreo=b.id\n" +
            "				INNER JOIN usuarios e ON b.id_usuario=e.id\n" +
            "                           INNER join funcionarios fun ON e.id_funcionario = fun.id\n"+                            
            "				INNER JOIN central_operativo f ON b.id_central_operativo=f.id\n" +
            "			   INNER JOIN alarmas c ON  a.id_alarma=c.id\n" +
            "			   INNER JOIN reservorios d ON  a.id_reservorio=d.id\n" +
            "WHERE b.fecha  BETWEEN '"+desde+"' AND '"+hasta+"' ORDER BY a.id DESC;";
        }

        try {
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            List<MonitoreoAlarmaDTO> lista = new ArrayList<>();
            MonitoreoAlarmaDTO dto = null;
            while (rs != null && rs.next()) {
                dto = new MonitoreoAlarmaDTO();
                dto.setId(rs.getInt("id"));
                    MonitoreoDTO monitoreo = new MonitoreoDTO();
                        monitoreo.setFecha(rs.getDate("fecha"));
                        monitoreo.setUsuario(new UsuarioDTO(rs.getInt("id_usuario"), rs.getString("usuario"),  rs.getString("nombres")));
                        monitoreo.setCentralOperativo(new CentralOperativoDTO(rs.getInt("id_central_operativo"), rs.getString("planta")));
                dto.setMonitoreo(monitoreo);
                dto.setReservorio(new ReservorioDTO(rs.getInt("id_reservorio"), rs.getString("reservorio")));
                dto.setAlarma(new AlarmaDTO(rs.getInt("id_alarma"), rs.getString("alarma")));
                dto.setHorario(rs.getTime("horario"));
                lista.add(dto);
            }
            return lista;
        } catch (Exception e) {
            msj = e.getMessage();
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    public Boolean registrarMonitoreoCaudal(MonitoreoCaudalDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO monitoreo_caudal(id_monitoreo, horario, volumen, id_reservorio) VALUES (?, ?, ?, ?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, getIdMonitoreo());
            ps.setTime(2, dto.getHorario());
            ps.setInt(3, dto.getVolumen());
            ps.setInt(4, dto.getReservorio().getId());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            conexion.Transaccion(Conexion.TR.CANCELAR);
            msj = "Error durante la inserción del registro " + ex.getMessage();
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    
     public List<MonitoreoCaudalDTO> getReporteCaudal() {
        try {
            sql = "SELECT id, id_monitoreo, horario, volumen, id_reservorio,hora_corta FROM monitoreo_caudal;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            List<MonitoreoCaudalDTO> lista = new ArrayList<>();
            MonitoreoCaudalDTO dto = null;
            Integer valor= 2;
            while (rs != null && rs.next()) {
                dto = new MonitoreoCaudalDTO();
                dto.setId(rs.getInt("id"));
                   // MonitoreoDTO monitoreo = new MonitoreoDTO();
                     //   monitoreo.setFecha(rs.getDate("fecha"));
                        //monitoreo.setUsuario(new UsuarioDTO(rs.getInt("id_usuario"), rs.getString("usuario")));
                        //monitoreo.setCentralOperativo(new CentralOperativoDTO(rs.getInt("id_central_operativo"), rs.getString("planta")));
                dto.setMonitoreo(new MonitoreoDTO(rs.getInt("id_monitoreo")));
                dto.setReservorio(new ReservorioDTO(rs.getInt("id_reservorio")));
                dto.setVolumen(rs.getInt("volumen"));
                dto.setHorario(rs.getTime("horario"));
                dto.setHoraCorta(rs.getInt("hora_corta"));
                lista.add(dto);
            }
            return lista;
        } catch (Exception e) {
            msj = e.getMessage();
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }
      public List<AlarmaEmitidaDTO> getAlarmaEmitida() {
        try {
            sql = "select a.id, b.id_usuario,e.usuario, b.fecha,  b.id_central_operativo, f.descrip as planta, a.id_reservorio\n" +
" ,d.descrip as reservorio, a.id_alarma , c.descrip as alarma, a.horario \n" +
" \n" +
"                 from  monitoreo_alarmas a \n" +
"                 \n" +
"                 INNER JOIN monitoreo b \n" +
"                 \n" +
"                 ON a.id_monitoreo=b.id\n" +
"\n" +
"                 INNER JOIN usuarios e \n" +
"\n" +
"                 ON b.id_usuario=e.id\n" +
"                 \n" +
"                 INNER JOIN central_operativo f \n" +
"                 \n" +
"                 ON b.id_central_operativo=f.id\n" +
"                 \n" +
"                 INNER JOIN alarmas c \n" +
"\n" +
"                 ON  a.id_alarma=c.id\n" +
"                 \n" +
"                 INNER JOIN reservorios d \n" +
"\n" +
"                 ON  a.id_reservorio=d.id\n" +
"                 \n" +
"                 ORDER BY b.fecha DESC;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            List<AlarmaEmitidaDTO> lista = new ArrayList<>();
            AlarmaEmitidaDTO dto = null;
            Integer valor= 2;
            while (rs != null && rs.next()) {
                dto = new AlarmaEmitidaDTO();
                dto.setId(rs.getInt("id"));
                   // MonitoreoDTO monitoreo = new MonitoreoDTO();
                     //   monitoreo.setFecha(rs.getDate("fecha"));
                        //monitoreo.setUsuario(new UsuarioDTO(rs.getInt("id_usuario"), rs.getString("usuario")));
                        //monitoreo.setCentralOperativo(new CentralOperativoDTO(rs.getInt("id_central_operativo"), rs.getString("planta")));
                dto.setUsuario(rs.getString("usuario"));
                dto.setFecha(rs.getDate("fecha"));
                dto.setAlarma(rs.getString("alarma"));
                dto.setHorario(rs.getTime("horario"));
                dto.setReservorio(rs.getString("reservorio"));
                lista.add(dto);
            }
            return lista;
        } catch (Exception e) {
            msj = e.getMessage();
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }
    
    public Boolean registrarMonitoreoEquipo(MonitoreoEquipoDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO monitoreo_equipos(id_monitoreo, id_equipo, minutos_trabajo) VALUES ( ?, ?, ?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, getIdMonitoreo());
            ps.setInt(2, dto.getEquipamiento().getId());
            ps.setInt(3, dto.getMinutosTrabajo());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            conexion.Transaccion(Conexion.TR.CANCELAR);
            msj = "Error durante la inserción del registro " + ex.getMessage();
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

}
