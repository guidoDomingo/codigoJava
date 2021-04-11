
package Base;

public class BaseDTO {
    private Integer id;
    private String  descrip;
    private String  comentario;

    public BaseDTO() {
    }

    public BaseDTO(Integer id) {
        this.id = id;
    }
    public BaseDTO(String desc) {
        this.descrip = desc;
    }
    
    public BaseDTO(Integer id, String descrip) {
        this.id = id;
        this.descrip = descrip;
    }
    
    public BaseDTO(Integer id, String descrip,String  comentario) {
        this.id = id;
        this.descrip = descrip;
        this.comentario = comentario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
