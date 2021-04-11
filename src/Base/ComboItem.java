
package Base;

public class ComboItem {
    private int id;
    private String descripcion;

    public ComboItem(int id, String descripcion) {
      this.id = id;
      this.descripcion = descripcion;
    }

    public int getId() {
      return id;
    }

    public String getDescription() {
      return descripcion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
