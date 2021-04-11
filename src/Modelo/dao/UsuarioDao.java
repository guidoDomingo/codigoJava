
package Modelo.dao;

import Base.GUI;
import ConexionDB.Conexion;
import Modelo.dto.AlarmaDTO;
import Modelo.dto.UsuarioDTO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author guido ruiz
 */
public class UsuarioDao {
    private String msj = null, sql = null;
    private Conexion conec;
    private PreparedStatement ps;
    private ResultSet rs;

    public UsuarioDao() {
        conec = new Conexion();
    }
    
    
    public boolean registrar(UsuarioDTO usr) throws SQLException{
        
        try{
             conec.Transaccion(Conexion.TR.INICIAR);
        
             sql = " INSERT INTO public.funcionarios(\n" +
"	 nro_cedula, nombres, apellidos, direccion, nro_telefono, id_cargo, correo)\n" +
"	VALUES ( ?, ?, ?, ?, ?, ? , ?); ";
             
            
            ps = conec.obtenerConexion().prepareStatement(sql);
        
            ps.setString(1,usr.getCedula());
            ps.setString(2,usr.getNombre());
            ps.setString(3,usr.getApellido()); 
            ps.setString(4,usr.getDireccion());
            ps.setString(5,usr.getTelefono());
            ps.setInt(6, usr.getId_cargo());
            ps.setString(7,usr.getCorreo());
            
            if (ps.executeUpdate() > 0) { 
              conec.Transaccion(Conexion.TR.CONFIRMAR);
              String consulta = "SELECT id\n" +
                                "  FROM public.funcionarios"
                                + " order by id desc;   ";
              ps = conec.obtenerConexion().prepareStatement(consulta);
              rs = ps.executeQuery();
              if(rs.next()){
                  System.out.print("existe el id");
              }else{
                  System.out.print("No existe el id");
              }
              String sqlUsuario = "INSERT INTO public.usuarios(\n" +
"             usuario, clave, id_funcionario)\n" +
"             VALUES (?, ?, ?);";
              ps = conec.obtenerConexion().prepareStatement(sqlUsuario);
              ps.setString(1,usr.getUsusario());
              ps.setString(2,usr.getClave());
              ps.setInt(3, rs.getInt("id"));
              ps.execute();
              
              
                return true;
            } else {
                conec.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
         
           
            
        }catch(SQLException ex){
             System.out.print(ex);
             conec.Transaccion(Conexion.TR.CANCELAR);
             return false;
        }finally {
            try {
                conec.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
            }
        }
       
    }
    public boolean existeUsuario(String cedula) throws SQLException{
        
        try{
            conec.Transaccion(Conexion.TR.INICIAR);
        
            String sql = "SELECT * FROM public.funcionarios WHERE nro_cedula = ? ";
             ps = conec.obtenerConexion().prepareStatement(sql);
        
             ps.setString(1,cedula);
            
             rs = ps.executeQuery();
             
             if(rs.next()){
                 return true;
             }
            return false;
            
        }catch(SQLException ex){
             System.out.print(ex);
             
             return false;
        }
       
    }
    public boolean validaEmail(String correo){
        // Patrón para validar el email
		Pattern pattern = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

		Matcher mather = pattern.matcher(correo);
                
                return mather.find();
    }
    public boolean login(UsuarioDTO user) throws SQLException{
        
        
               
        
        
        try{
              conec.Transaccion(Conexion.TR.INICIAR);
        
             sql = "select f.nombres,u.usuario,u.clave,f.correo, f.nro_telefono, u.id from public.funcionarios as f\n" +
                    "inner join public.usuarios as u\n" +
                    "on f.id = u.id_funcionario\n" +
                    "where f.nombres = ? and u.clave = ? ;" ;
             ps = conec.obtenerConexion().prepareStatement(sql);
        
             ps.setString(1,user.getNombre());
             ps.setString(2,user.getClave());
             
             rs = ps.executeQuery();
           
             if(rs.next()){
                 if(user.getClave().equals(rs.getString(3))){

                     user.setNombre(rs.getString(1));
                     user.setUsusario(rs.getString(2));
                     user.setCorreo(rs.getString(4));
                     user.setTelefono(rs.getString(5));
                     user.setId(rs.getInt(6));
                     return true;
                 }else{
                     return false;
                 }
                 
             }
            return false;
            
        }catch(SQLException ex){
             System.out.print(ex);
             
             return false;
        }
       
    }
    
    public ArrayList<UsuarioDTO> TraerUsuario(String llamada){
        
        try{
            conec.Transaccion(Conexion.TR.INICIAR);
             if(llamada == "getAll"){
                sql = "select u.id , f.nombres,u.usuario,f.nro_cedula from public.funcionarios as f\n" +
                    "inner join public.usuarios as u\n" +
                    "on f.id = u.id_funcionario\n" +
                    " ORDER BY id DESC ;" ;
             }else if(llamada == "getAllUser"){
                sql = "select u.id , f.nombres,u.usuario,f.nro_cedula from public.funcionarios as f\n" +
                    "inner join public.usuarios as u\n" +
                    "on f.id = u.id_funcionario\n" +
                    "where u.usuario = 'Operador'\n" +   
                    " ORDER BY id DESC ;" ;    
             }else if(llamada != null){
                sql = "select u.id , f.nombres,u.usuario,f.nro_cedula from public.funcionarios as f\n" +
                    "inner join public.usuarios as u\n" +
                    "on f.id = u.id_funcionario\n" +
                    "where f.nombres LIKE '%"+llamada+"%' OR u.usuario LIKE '%"+llamada+"%' OR f.nro_cedula LIKE '%"+llamada+"%' \n" +   
                    " ORDER BY id DESC ;" ;      
             }
              ps = conec.obtenerConexion().prepareStatement(sql);
              rs = ps.executeQuery();
              ArrayList<UsuarioDTO> lista = new ArrayList<>();
            while (rs != null && rs.next()) {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setId(rs.getInt("id"));
                dto.setNombre(GUI.NullVacio(rs.getString("nombres")));
                dto.setUsusario(GUI.NullVacio(rs.getString("usuario")));
                dto.setCedula(GUI.NullVacio(rs.getString("nro_cedula")));
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
    
     public UsuarioDTO recuperarRegistro(UsuarioDTO dto) {
        try {
            UsuarioDTO unidMedDto = null;
            sql = "SELECT  u.id, f.nro_cedula, f.nombres, f.apellidos, f.direccion, f.nro_telefono, f.correo, u.usuario , c.descrip\n" +
                    "  FROM funcionarios as f\n" +
                    "  inner join usuarios as u\n" +
                    "  on\n" +
                    "  f.id = u.id_funcionario\n" +
                    "  inner join cargos as c\n" +
                    "  on\n" +
                    "  f.id_cargo = c.id\n" +
                    "  \n" +
                    "  where u.id = ?;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                unidMedDto = new UsuarioDTO();
                unidMedDto.setCedula(GUI.NullVacio(rs.getString("nro_cedula")));
                unidMedDto.setNombre(GUI.NullVacio(rs.getString("nombres")));
                unidMedDto.setApellido(GUI.NullVacio(rs.getString("apellidos")));
                unidMedDto.setDireccion(GUI.NullVacio(rs.getString("direccion")));
                unidMedDto.setTelefono(GUI.NullVacio(rs.getString("nro_telefono")));
                unidMedDto.setCorreo(GUI.NullVacio(rs.getString("correo")));
                unidMedDto.setUsusario(GUI.NullVacio(rs.getString("usuario")));
                unidMedDto.setCargo(GUI.NullVacio(rs.getString("descrip")));
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
     
      public boolean actualizar(UsuarioDTO usr) throws SQLException{
         
        try{
             int id_funcionario = 0;
             conec.Transaccion(Conexion.TR.INICIAR);
             
              String consulta = "SELECT id_funcionario " +
                                "  FROM usuarios " +
                                "  WHERE id = ?;";
              ps = conec.obtenerConexion().prepareStatement(consulta);
              ps.setInt(1, usr.getId());
              rs = ps.executeQuery();
              if (rs != null && rs.next()) {
                 id_funcionario = rs.getInt("id_funcionario");
              }
              
        
             sql = "UPDATE funcionarios " +
                    " SET  nro_cedula=?, nombres=?, apellidos=?, direccion=?, nro_telefono=?, " +
                    "  id_cargo=?, correo=? " +
                    " WHERE id = ? ;";
             
            
            ps = conec.obtenerConexion().prepareStatement(sql);
        
            ps.setString(1,usr.getCedula());
            ps.setString(2,usr.getNombre());
            ps.setString(3,usr.getApellido()); 
            ps.setString(4,usr.getDireccion());
            ps.setString(5,usr.getTelefono());
            ps.setInt(6, usr.getId_cargo());
            ps.setString(7,usr.getCorreo());
            ps.setInt(8,id_funcionario);
            
            if (ps.executeUpdate() > 0) { 
              conec.Transaccion(Conexion.TR.CONFIRMAR);
             
              String sqlUsuario = "UPDATE usuarios" +
                                "   SET  usuario=?, clave=?, id_funcionario=?" +
                                " WHERE id = ? ;";
              ps = conec.obtenerConexion().prepareStatement(sqlUsuario);
              ps.setString(1,usr.getUsusario());
              ps.setString(2,usr.getClave());
              ps.setInt(3, id_funcionario);
              ps.setInt(4 , usr.getId());
              ps.execute();
              
              
                return true;
            } else {
                conec.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
         
           
            
        }catch(SQLException ex){
             System.out.print(ex);
             conec.Transaccion(Conexion.TR.CANCELAR);
             return false;
        }finally {
            try {
                conec.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
            }
        }
       
    }
      
    public Boolean eliminar(UsuarioDTO dto) {
        try {
            conec.Transaccion(Conexion.TR.INICIAR);
            int id_funcionario = 0;
             String consulta = "SELECT id_funcionario " +
                                "  FROM usuarios " +
                                "  WHERE id = ?;";
              ps = conec.obtenerConexion().prepareStatement(consulta);
              ps.setInt(1, dto.getId());
              rs = ps.executeQuery();
              if (rs != null && rs.next()) {
                 id_funcionario = rs.getInt("id_funcionario");
              }
              
            sql = "DELETE FROM public.usuarios WHERE id = ? ;";
            ps = conec.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId());
            if (ps.executeUpdate() > 0) {
                consulta = "DELETE FROM public.funcionarios WHERE id = ? ;";
                ps = conec.obtenerConexion().prepareStatement(consulta);
                ps.setInt(1, id_funcionario);
                ps.execute();
                
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
  
}

