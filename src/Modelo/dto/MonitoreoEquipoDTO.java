
package Modelo.dto;

import Base.BaseDTO;


public class MonitoreoEquipoDTO extends BaseDTO{
    private Integer id;
    private MonitoreoDTO monitoreo;
    private EquipamientoDTO equipamiento;
    private Integer minutosTrabajo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MonitoreoDTO getMonitoreo() {
        return monitoreo;
    }

    public void setMonitoreo(MonitoreoDTO monitoreo) {
        this.monitoreo = monitoreo;
    }

    public EquipamientoDTO getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(EquipamientoDTO equipamiento) {
        this.equipamiento = equipamiento;
    }

    public Integer getMinutosTrabajo() {
        return minutosTrabajo;
    }

    public void setMinutosTrabajo(Integer minutosTrabajo) {
        this.minutosTrabajo = minutosTrabajo;
    }
    
    
    
}
