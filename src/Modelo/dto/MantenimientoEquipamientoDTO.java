
package Modelo.dto;

import Base.BaseDTO;
import java.sql.Date;
import java.util.List;

public class MantenimientoEquipamientoDTO extends BaseDTO{
    private UsuarioDTO usuario;
    private EquipamientoDTO equipamiento;
    private TipoMantenimientoDTO mantenimiento;
    private Date fechaOperacion;
    private String comentario;
    private List<TipoMantenimientoDTO> mantenimientos;
    
    public MantenimientoEquipamientoDTO() {
    }
    public MantenimientoEquipamientoDTO(Integer id) {
        super(id);
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public EquipamientoDTO getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(EquipamientoDTO equipamiento) {
        this.equipamiento = equipamiento;
    }

    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public List<TipoMantenimientoDTO> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<TipoMantenimientoDTO> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public void setMantenimiento(TipoMantenimientoDTO mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public TipoMantenimientoDTO getMantenimiento() {
        return mantenimiento;
    }
    
    
    
}
