package Base;

public interface BaseControl<T> {
    
    void InitDatos();
    void CargarDatos(int i);
    void editar();
    void BotonesAgregar(boolean b);
    void Cancelar();
    void Agregar();
    void Modificar();
    void Eliminar();
    T ValidarDatos(boolean agregar);
    void CerrarVentana();
    
}
