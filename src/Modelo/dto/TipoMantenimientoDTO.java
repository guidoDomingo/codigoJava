
package Modelo.dto;

import Base.BaseDTO;

public class TipoMantenimientoDTO extends BaseDTO{
   
    public TipoMantenimientoDTO() {
    }
    
    public TipoMantenimientoDTO(Integer id) {
        super(id);
    }
    public TipoMantenimientoDTO(String desc) {
        super(desc);
    }
    
    public TipoMantenimientoDTO(Integer id, String descrip) {
        super(id, descrip);
    }
    public TipoMantenimientoDTO(Integer id, String descrip, String comentario) {
        super(id, descrip, comentario);
    }
}
