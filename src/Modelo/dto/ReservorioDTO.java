
package Modelo.dto;

import Base.BaseDTO;

public class ReservorioDTO extends BaseDTO{

    private Integer capacidad;
    
    public ReservorioDTO() {
    }
    
    public ReservorioDTO(Integer id) {
        super(id);
    }
    
    public ReservorioDTO(Integer id, String descrip) {
        super(id,descrip);
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getCapacidad() {
        return capacidad;
    }
}
