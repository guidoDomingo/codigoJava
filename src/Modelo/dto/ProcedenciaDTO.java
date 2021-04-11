
package Modelo.dto;

import Base.BaseDTO;

public class ProcedenciaDTO extends BaseDTO{
    
    public ProcedenciaDTO() {
    }
    
    public ProcedenciaDTO(Integer id) {
        super(id);
    }
    
    public ProcedenciaDTO(Integer id, String descrip) {
        super(id,descrip);
    }
   
}
