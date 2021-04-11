
package Base;

import java.util.List;

public interface BaseSQL<T>{
    public Boolean agregar(T dto);
    public Boolean modificar(T dto);
    public Boolean eliminar(T dto);
    public T recuperarRegistro(T dto);
    public List<T> recuperarRegistros();
    public String msj();
}
