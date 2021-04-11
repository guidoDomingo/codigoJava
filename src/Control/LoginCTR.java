/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Base.GUI;
import Modelo.dao.AlarmaDAO;
import Modelo.dao.Hash;
import Modelo.dao.UsuarioDao;
import Modelo.dto.AlarmaDTO;
import Modelo.dto.UsuarioDTO;
import Vista.Alarma;
import Vista.home;
import Vista.login;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author guido ruiz
 */
public class LoginCTR {
       
    private login gui;
    private UsuarioDao dao;
    private UsuarioDTO dto;
    private DefaultTableModel m;

    public LoginCTR(login gui) {
        this.gui = gui;
    }

     public boolean Login() throws SQLException {
        dto = ValidarDatos();
        if (dto != null) {
            //convertir el password a un String
            String pass = new String(gui.txtPassword.getPassword());
            //encriptamos el password
            String nuevoPass = Hash.sha1(pass);
            
            //seteamos la clave del dto
            
            dto.setClave(nuevoPass);
            
            //inicializamos el dao
            
            dao = new UsuarioDao();
            if (dao.login(dto) == true) {
                GUI.msgInfo(gui.label7, "Disfrute del Sistema.");
                
                home frmHome = new home(dto);
               
                
                frmHome.setVisible(true);
                
                return true;
                
            } else {
                GUI.msgAtencion(gui.label7, "Error al Loguearse.");
                if (GUI.esAdmin()) {
//                    GUI.adminMsg(dao.msj());
                }
                limpiar1();
                return false;
            
            }
        } 
        
        limpiar1();
        return false;
        
    }
   

    
    public UsuarioDTO ValidarDatos() {
        if (GUI.isEmpty(gui.txtUsuario)) {
            GUI.isEmpty(gui.txtPassword);
            GUI.msgAtencion(gui.label7, "Campo [login] requerido");
            return null;
        }


        dto = new UsuarioDTO();
        dto.setNombre(GUI.get(gui.txtUsuario));
        dto.setClave(GUI.get(gui.txtPassword));
        return dto;
    }
    
    public void limpiar1(){
        gui.txtUsuario.setText("");
        gui.txtPassword.setText("");
    }

}
