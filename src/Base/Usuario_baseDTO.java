/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

/**
 *
 * @author guido ruiz
 */
public class Usuario_baseDTO {
    private int id;
    private String ususario;
    private String clave;
    private boolean activo;
    private String nombre;
    private String apellido;
    private String cedula;
    private String direccion;
    private String telefono;
    private int id_cargo;
    private String descripcion;
    private String comentario;
    private String correo;
    private String cargo;

    public Usuario_baseDTO() {
    }
    
    public Usuario_baseDTO(Integer id) {
        this.id=id;
    }
    
    public Usuario_baseDTO(String usuario) {
        this.ususario=usuario;
    }
     public Usuario_baseDTO(String nombre, String usuario) {
        this.nombre=nombre;
        this.ususario = usuario;
    }
    
    public Usuario_baseDTO(Integer id,String usuario) {
        this.id=id;
        this.ususario= usuario;
    }
    
    public Usuario_baseDTO(Integer id,String usuario, String nombre) {
        this.id=id;
        this.ususario= usuario;
        this.nombre = nombre;
    }
    
    
            
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsusario() {
        return ususario;
    }

    public void setUsusario(String ususario) {
        this.ususario = ususario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(int id_cargo) {
        this.id_cargo = id_cargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
   
    
    
    
}
