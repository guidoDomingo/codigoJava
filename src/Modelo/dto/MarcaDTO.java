
package Modelo.dto;

import Base.BaseDTO;

public class MarcaDTO extends BaseDTO{

    public MarcaDTO() {
    }
    
    public MarcaDTO(Integer id) {
        super(id);
    }
    
    public MarcaDTO(Integer id, String descrip) {
        super(id,descrip);
    }
   
    
}
