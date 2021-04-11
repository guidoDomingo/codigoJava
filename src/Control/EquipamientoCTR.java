package Control;

import Base.BaseControl;
import Base.ComboItem;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Modelo.dao.EquipamientoDAO;
import Modelo.dao.MarcaDAO;
import Modelo.dao.ProcedenciaDAO;
import Modelo.dto.EquipamientoDTO;
import Modelo.dto.MarcaDTO;
import Modelo.dto.ProcedenciaDTO;
import Reportes.Informes;
import Vista.Equipamiento;
import java.sql.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class EquipamientoCTR implements BaseControl<EquipamientoDTO> {

    private Equipamiento gui;
    private EquipamientoDAO dao;
    private EquipamientoDTO dto;
    private DefaultTableModel m;

    public EquipamientoCTR(Equipamiento gui) {
        this.gui = gui;
        InitDatos();
    }

      public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("Equipamientos.jasper");
        info.setTitulo("LISTADO DE EQUIPAMIENTOS");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.EQUIPAMIENTOS);
        info.procesar();
    }

    @Override
    public void InitDatos() {
        gui.txFechaIngreso.setText(GUI.formatoFechaHora(GUI.getDateServer()).replace("/", "-"));
        //cargar la tabla 
        m = (DefaultTableModel) gui.tabla.getModel();
        m.setRowCount(0);
        dao = new EquipamientoDAO();
        ArrayList<EquipamientoDTO> datos = dao.recuperarRegistros();
        if (datos.size() > 0) {
            for (EquipamientoDTO u : datos) {
                m.addRow(new Object[]{
                    u.getId(),
                    u.getDescrip(),
                    u.getNroSerie(),
                    u.getModelo(),
                    u.getComentario()
                }
                );
            }
        } else {
            GUI.msgAtencion(gui.lbInfo, "No se pudo recuperar los datos");
        }
        cargarMarca();
        cargarProcedencia();
    }

    
    private void cargarMarca(){
        MarcaDAO marcaDao= new MarcaDAO();
        List<MarcaDTO> marcas = marcaDao.recuperarRegistros();
        DefaultComboBoxModel modeloMarca= (DefaultComboBoxModel) gui.cbMarca.getModel();
        modeloMarca.removeAllElements();
        for (MarcaDTO m : marcas) {
            modeloMarca.addElement(new ComboItem(m.getId(), m.getDescrip()));
        }
        gui.cbMarca.setModel(modeloMarca);
    }
    
    private void cargarProcedencia(){
        ProcedenciaDAO procedenciaDao= new ProcedenciaDAO();
        List<ProcedenciaDTO> p = procedenciaDao.recuperarRegistros();
        DefaultComboBoxModel modeloProcedencia= (DefaultComboBoxModel) gui.cbProcedencia.getModel();
        modeloProcedencia.removeAllElements();
        for (ProcedenciaDTO m : p) {
            modeloProcedencia.addElement(new ComboItem(m.getId(), m.getDescrip()));
        }
        gui.cbProcedencia.setModel(modeloProcedencia);
    }
    
    @Override
    public void CargarDatos(int id) {
        if (dao == null) {
            dao = new EquipamientoDAO();
        }
        dto = dao.recuperarRegistro(new EquipamientoDTO(id));
        if (dto != null) {
            gui.txIdMarca.setText(String.valueOf(dto.getId()));
            gui.txDescrip.setText(dto.getDescrip());
            gui.txNroSerie.setText(dto.getNroSerie());
            gui.txModelo.setText(dto.getModelo());
            gui.txFechaIngreso.setText(GUI.formatoDiaMesAnho(dto.getFechaIngreso()));
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
        gui.txDescrip.setText("");
        gui.txNroSerie.setText("");
        gui.txModelo.setText("");
        gui.txComentario.setText("");
        //gui.txBuscar.setText("");
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new EquipamientoDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Equipamiento '" + dto.getDescrip() + "' registrado correctamente.");
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
            dao = new EquipamientoDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Equipamiento '" + dto.getDescrip() + "' actualizado correctamente.");
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
            dao = new EquipamientoDAO();
        }
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Equipamiento '" + dto.getDescrip() + "' eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public EquipamientoDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txDescrip)) {
            GUI.focusAll(gui.txDescrip);
            GUI.msgAtencion(gui.lbInfo, "Campo [Descripción] requerido");
            return null;
        }

        if (agregar == true) {
            dao = new EquipamientoDAO();
            if (dao.existeDescrip(GUI.get(gui.txDescrip)) == true) {
                GUI.focusAll(gui.txDescrip);
                GUI.msgAtencion(gui.lbInfo, "Equipamiento [" + GUI.get(gui.txDescrip) + "] ya existe");
                return null;
            }
        }
        ComboItem cbMarca=(ComboItem) gui.cbMarca.getSelectedItem();
        ComboItem cbProcedencia=(ComboItem) gui.cbProcedencia.getSelectedItem();

        dto = new EquipamientoDTO();
        dto.setId( (agregar == false ? Integer.valueOf(GUI.get(gui.txIdMarca)) : 0));
        dto.setDescrip(GUI.get(gui.txDescrip));
        dto.setNroSerie(GUI.get(gui.txNroSerie));
        dto.setModelo(GUI.get(gui.txModelo));
        dto.setFechaIngreso( Date.valueOf( GUI.formatoAnhoMesDiaDos(GUI.getDate(gui.txFechaIngreso.getText().replace("-", "/")))));
        dto.setMarca(new MarcaDTO(cbMarca.getId()));
        dto.setProcedencia(new ProcedenciaDTO(cbProcedencia.getId()));
        dto.setComentario(GUI.get(gui.txComentario));
        return dto;
    }

    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

}
