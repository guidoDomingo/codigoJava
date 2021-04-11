
package Modelo.dto;

import Base.BaseDTO;

public class AlarmaDTO extends BaseDTO{

    public AlarmaDTO() {
    }
    
    public AlarmaDTO(Integer id) {
        super(id);
    }
    
    public AlarmaDTO(Integer id, String descrip) {
        super(id,descrip);
    }
   
    
}
