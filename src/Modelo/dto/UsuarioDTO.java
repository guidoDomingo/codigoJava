/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.dto;

import Base.Usuario_baseDTO;

/**
 *
 * @author guido ruiz
 */
public class UsuarioDTO extends Usuario_baseDTO {

    public UsuarioDTO() {
    }
    
    public UsuarioDTO(Integer id) {
         super(id);
    }
    
    public UsuarioDTO(String usuario) {
         super(usuario);
    }
    public UsuarioDTO(String nombres, String usuario) {
         super(nombres, usuario);
    }
    
    public UsuarioDTO(Integer id, String usuario) {
         super(id,usuario);
    }
    
    public UsuarioDTO(Integer id, String usuario, String nombre) {
         super(id,usuario, nombre);
    }
     
}
