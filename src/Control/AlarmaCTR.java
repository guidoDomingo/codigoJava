package Control;

import Base.BaseControl;
import Modelo.dao.MarcaDAO;
import Modelo.dto.AlarmaDTO;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Modelo.dao.AlarmaDAO;
import Modelo.dto.AlarmaDTO;
import Reportes.Informes;
import Vista.Alarma;
import Vista.Marca;

public class AlarmaCTR implements BaseControl<AlarmaDTO> {

    
    private Alarma gui;
    private AlarmaDAO dao;
    private AlarmaDTO dto;
    private DefaultTableModel m;

    public AlarmaCTR(Alarma gui) {
        this.gui = gui;
        InitDatos();
    }

    @Override
    public void InitDatos() {
        //cargar la tabla 
        m = (DefaultTableModel) gui.tabla.getModel();
        m.setRowCount(0);
        dao = new AlarmaDAO();
        ArrayList<AlarmaDTO> datos = dao.recuperarRegistros();
        if (datos.size() > 0) {
            for (AlarmaDTO u : datos) {
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
            dao = new AlarmaDAO();
        }
        dto = dao.recuperarRegistro(new AlarmaDTO(id));
        if (dto != null) {
            gui.txIdAlarma.setText(String.valueOf(dto.getId()));
            gui.txAlarma.setText(dto.getDescrip());
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
            GUI.msgAtencion(gui.lbInfo, "Seleccionar un registro de la lista.");
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
        
        gui.txIdAlarma.setText("");
        
        gui.txAlarma.setText("");
        gui.txComentario.setText("");
        //gui.txBuscar.setText("");
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new AlarmaDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Alarma '" + dto.getDescrip() + "' registrado correctamente.");
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
            dao = new AlarmaDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Alarma '" + dto.getDescrip() + "' actualizado correctamente.");
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
            dao = new AlarmaDAO();
        }
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Alarma '" + dto.getDescrip() + "' eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public AlarmaDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txAlarma)) {
            GUI.focusAll(gui.txAlarma);
            GUI.msgAtencion(gui.lbInfo, "Campo [Alarma] requerido");
            return null;
        }

        if (agregar == true) {
            dao = new AlarmaDAO();
            if (dao.existeDescrip(GUI.get(gui.txAlarma)) == true) {
                GUI.focusAll(gui.txAlarma);
                GUI.msgAtencion(gui.lbInfo, "Alarma [" + GUI.get(gui.txAlarma) + "] ya existe");
                return null;
            }
        }

        dto = new AlarmaDTO();
        dto.setId( (agregar == false ? Integer.valueOf(GUI.get(gui.txIdAlarma)) : 0));
        dto.setDescrip(GUI.get(gui.txAlarma));
        dto.setComentario(GUI.get(gui.txComentario));
        return dto;
    }

    public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("Alarmas.jasper");
        info.setTitulo("LISTADO DE ALARMAS");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.ALARMAS);
        info.procesar();
    }
    
    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

}
