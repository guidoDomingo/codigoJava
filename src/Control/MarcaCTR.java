package Control;

import Base.BaseControl;
import Modelo.dao.MarcaDAO;
import Modelo.dto.MarcaDTO;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Reportes.Informes;
import Vista.Marca;

public class MarcaCTR implements BaseControl<MarcaDTO> {

    private Marca gui;
    private MarcaDAO dao;
    private MarcaDTO dto;
    private DefaultTableModel m;

    public MarcaCTR(Marca gui) {
        this.gui = gui;
        InitDatos();
    }

    @Override
    public void InitDatos() {
        //cargar la tabla 
        m = (DefaultTableModel) gui.tabla.getModel();
        m.setRowCount(0);
        dao = new MarcaDAO();
        ArrayList<MarcaDTO> datos = dao.recuperarRegistros();
        if (datos.size() > 0) {
            for (MarcaDTO u : datos) {
                m.addRow(new Object[]{
                    u.getId(),
                    u.getDescrip(),
                    u.getComentario()
                }
                );
            }
        } else {
            GUI.msgAtencion(gui.lbInfo, "No se pudo recuperar los datos");
        }
    }

    @Override
    public void CargarDatos(int id) {
        if (dao == null) {
            dao = new MarcaDAO();
        }
        dto = dao.recuperarRegistro(new MarcaDTO(id));
        if (dto != null) {
            gui.txIdMarca.setText(String.valueOf(dto.getId()));
            gui.txMarca.setText(dto.getDescrip());
            gui.txComentario.setText(dto.getComentario());
        } else {
            GUI.msgError(gui.lbInfo, "Error al cargar datos");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    public void editar() {
        int f = gui.tabla.getSelectedRow();
        if (f >= 0) {
            BotonesAgregar(false);
            int id = Integer.valueOf(gui.tabla.getValueAt(f, 0).toString());
            CargarDatos(id);
        } else {
            GUI.msgAtencion(gui.lbInfo, "Seleccionar una marca de la lista.");
        }
    }

    @Override
    public void BotonesAgregar(boolean b) {
        gui.btAgregar.setVisible(b);
        gui.btGuardar.setVisible(!b);
        gui.btEditar.setVisible(b);

        if (GUI.esAdmin()) {
            gui.btEliminar.setVisible(!b);
        }

        gui.tabla.setEnabled(b);
        if (b) {
            gui.tabla.setBackground(Color.white);
        } else {
            gui.tabla.setBackground(new Color(240, 240, 240, 255));
        }

        //gui.txBuscar.setEnabled(b);
        //gui.txIdMarca.setEnabled(b);
    }

    @Override
    public void Cancelar() {
        BotonesAgregar(true);
        dto = null;
        dao = null;
        
        gui.txIdMarca.setText("");
        
        gui.txMarca.setText("");
        gui.txComentario.setText("");
        //gui.txBuscar.setText("");
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new MarcaDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Marca '" + dto.getDescrip() + "' registrado correctamente.");
                Cancelar();
            } else {
                GUI.msgAtencion(gui.lbInfo, "Error al insertar datos.");
                if (GUI.esAdmin()) {
                    GUI.adminMsg(dao.msj());
                }
            }
        } else {

        }
    }

    @Override
    public void Modificar() {
        if (dao == null) {
            dao = new MarcaDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Marca '" + dto.getDescrip() + "' actualizado correctamente.");
                Cancelar();
            } else {
                GUI.msgAtencion(gui.lbInfo, "Error al modificar datos.");
                if (GUI.esAdmin()) {
                    GUI.adminMsg(dao.msj());
                }
            }
        }

    }

    @Override
    public void Eliminar() {
        if (dao == null) {
            dao = new MarcaDAO();
        }
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Marca '" + dto.getDescrip() + "' eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public MarcaDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txMarca)) {
            GUI.focusAll(gui.txMarca);
            GUI.msgAtencion(gui.lbInfo, "Campo [Marca] requerido");
            return null;
        }

        if (agregar == true) {
            dao = new MarcaDAO();
            if (dao.existeDescrip(GUI.get(gui.txMarca)) == true) {
                GUI.focusAll(gui.txMarca);
                GUI.msgAtencion(gui.lbInfo, "Marca [" + GUI.get(gui.txMarca) + "] ya existe");
                return null;
            }
        }

        dto = new MarcaDTO();
        dto.setId( (agregar == false ? Integer.valueOf(GUI.get(gui.txIdMarca)) : 0));
        dto.setDescrip(GUI.get(gui.txMarca));
        dto.setComentario(GUI.get(gui.txComentario));
        return dto;
    }

    public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("Marcas.jasper");
        info.setTitulo("LISTADO DE MARCAS");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.MARCA);
        info.procesar();
    }
    
    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

}
