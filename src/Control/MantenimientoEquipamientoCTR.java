package Control;

import Base.BaseControl;
import Base.BuscadorGUI;
import Modelo.dao.MantenimientoEquipamientoDAO;
import Modelo.dto.MantenimientoEquipamientoDTO;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Modelo.dao.EquipamientoDAO;
import Modelo.dao.TipoMantenimientoDAO;
import Modelo.dto.EquipamientoDTO;
import Modelo.dto.TipoMantenimientoDTO;
import Modelo.dto.UsuarioDTO;
import Reportes.Informes;
import Vista.HistorialMantenimientos;
import Vista.MantenimientoEquipamiento;
import java.awt.Frame;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MantenimientoEquipamientoCTR implements BaseControl<MantenimientoEquipamientoDTO> {

    private MantenimientoEquipamiento gui;
    private HistorialMantenimientos gui1;
    private MantenimientoEquipamientoDAO dao;
    private MantenimientoEquipamientoDTO dto;
    private DefaultTableModel m;
    private List<MantenimientoEquipamientoDTO> lista;

    public MantenimientoEquipamientoCTR(MantenimientoEquipamiento gui) {
        this.gui = gui;
        //InitDatos();
    }
    public MantenimientoEquipamientoCTR(HistorialMantenimientos gui1) {
        this.gui1 = gui1;
        //InitDatos();
        cargarMantenimientos(null,null);
    }

    @Override
    public void InitDatos() {
        //cargar la tabla 

    }

    public void cargarEquipamientos() {
        EquipamientoDAO equiDAO = new EquipamientoDAO();
        List<EquipamientoDTO> datos = equiDAO.recuperarRegistros();
        BuscadorGUI buscador = new BuscadorGUI(new Frame(), true, datos);
        buscador.setTitle("Buscador de Equipamiento");
        buscador.setVisible(true);
        //buscador.setModal(true);

        if (buscador.getID() != null) {
            gui.txIdEquipamiento.setText(buscador.getID().toString());
            gui.txEquipamiento.setText(buscador.getDescrip());
            GUI.focusAll(gui.txIdMantenimiento);
        }

    }

    public void cargarMantenimientos() {
        TipoMantenimientoDAO equiDAO = new TipoMantenimientoDAO();
        List<TipoMantenimientoDTO> datos = equiDAO.recuperarRegistros();

        BuscadorGUI buscador = new BuscadorGUI(new Frame(), true, datos);
        buscador.setTitle("Buscador de Equipamiento");
        buscador.setVisible(true);

        if (buscador.getID() != null) {
            gui.txIdMantenimiento.setText(buscador.getID().toString());
            gui.txMantenimiento.setText(buscador.getDescrip());
            GUI.focusAll(gui.txComentario);
        }
    }

    @Override
    public void CargarDatos(int id) {
        if (dao == null) {
            dao = new MantenimientoEquipamientoDAO();
        }
        dto = dao.recuperarRegistro(new MantenimientoEquipamientoDTO(id));
        if (dto != null) {
            gui.txIdMantenimientoEquip.setText(String.valueOf(dto.getId()));
            // gui.txMarca.setText(dto.getDescrip());
            // gui.txComentario.setText(dto.getComentario());
        } else {
            GUI.msgError(gui.lbInfo, "Error al cargar datos");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }
    
    public void cargarMantenimientos(String fecha1, String fecha2){
        dao = new MantenimientoEquipamientoDAO();
        
        lista = dao.getMantenimientosHistorico(fecha1 , fecha2);
        DefaultTableModel tabla = (DefaultTableModel) gui1.tabla.getModel();
        tabla.setRowCount(0);
//"Id", "Tipo Mantenimiento", "Equipo", "Comentario", "Usuario","Nombres","Fecha"
        for (MantenimientoEquipamientoDTO m : lista) {
            tabla.addRow(new Object[]{ m.getId(),
                //GUI.formatoDiaMesAnho(m.getMonitoreo().getFecha()),
                m.getMantenimiento().getDescrip(),
                m.getEquipamiento().getDescrip(),
                m.getComentario(),
                m.getUsuario().getUsusario(),
                m.getUsuario().getNombre(),
                GUI.formatoDiaMesAnho(m.getFechaOperacion())
                
            });
        }
    }

    public void editar() {
        BotonesAgregar(false);
        gui.txIdMantenimientoEquip.setEnabled(true);
        gui.txIdMantenimientoEquip.setEditable(true);
        GUI.focusAll(gui.txIdMantenimientoEquip);
//        int f = gui.tabla.getSelectedRow();
//        if (f >= 0) {
//            BotonesAgregar(false);
//            int id = Integer.valueOf(gui.tabla.getValueAt(f, 0).toString());
//            CargarDatos(id);
//        } else {
//            GUI.msgAtencion(gui.lbInfo, "Seleccionar una marca de la lista.");
//        }
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

        gui.txIdMantenimientoEquip.setText("");
        gui.txIdEquipamiento.setText("");
        gui.txEquipamiento.setText("");
        gui.txIdMantenimiento.setText("");
        gui.txMantenimiento.setText("");
        gui.txComentario.setText("");
        GUI.limpiarTabla(Arrays.asList(gui.tabla));
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new MantenimientoEquipamientoDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Mantemientos  registrado correctamente.");
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
            dao = new MantenimientoEquipamientoDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Mantemientos  actualizado correctamente.");
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
            dao = new MantenimientoEquipamientoDAO();
        }
        dto = new MantenimientoEquipamientoDTO();
        dto.setEquipamiento(new EquipamientoDTO(GUI.getInt(gui.txIdEquipamiento)));
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Mantemientos  eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public MantenimientoEquipamientoDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txIdEquipamiento)) {
            GUI.focusAll(gui.txIdEquipamiento);
            GUI.msgAtencion(gui.lbInfo, "Campo [Equipamiento] requerido");
            return null;
        }

        dto = new MantenimientoEquipamientoDTO();
        dto.setEquipamiento(new EquipamientoDTO(GUI.getInt(gui.txIdEquipamiento)));
        dto.setFechaOperacion(GUI.getDateSQL());
        int id = Integer.parseInt(gui.txtIdUsuario.getText());
        dto.setUsuario(new UsuarioDTO(id));
        List<TipoMantenimientoDTO> lista = new ArrayList<>();
        System.out.println("**********************");
        System.out.println(id);
        TipoMantenimientoDTO d = null;
        for (int i = 0; i < gui.tabla.getRowCount(); i++) {
            d = new TipoMantenimientoDTO();
            d.setId(Integer.parseInt(gui.tabla.getValueAt(i, 0).toString()));
            d.setComentario((gui.tabla.getValueAt(i, 2) == null ? "" : gui.tabla.getValueAt(i, 2).toString())
            );
            lista.add(d);
        }
        dto.setMantenimientos(lista);
        return dto;
    }

    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

    public void agregarFila() {
        DefaultTableModel modeloMantenimiento = (DefaultTableModel) gui.tabla.getModel();
        Integer idMant = GUI.getInt(gui.txIdMantenimiento);
        String descripMant = GUI.get(gui.txMantenimiento);
        String comentario = GUI.get(gui.txComentario);

        modeloMantenimiento.addRow(new Object[]{idMant, descripMant, comentario});
        gui.txIdMantenimiento.setText("");
        gui.txMantenimiento.setText("");
        gui.txComentario.setText("");
        GUI.focusAll(gui.txIdMantenimiento);

    }

    public void eliminarFila() {
        if (gui.tabla.getSelectedRow() >= 0) {
            DefaultTableModel modeloMantenimiento = (DefaultTableModel) gui.tabla.getModel();
            modeloMantenimiento.removeRow(gui.tabla.getSelectedRow());
        } else {
            GUI.msgAtencion(gui.lbInfo, "Debe seleccionar una fila");
        }

    }

    public void actualizarFila() {
        if (gui.tabla.getSelectedRow() >= 0) {
            DefaultTableModel modeloMantenimiento = (DefaultTableModel) gui.tabla.getModel();
            Integer idMant = Integer.parseInt(modeloMantenimiento.getValueAt(gui.tabla.getSelectedRow(), 0).toString());
            String descripMant = modeloMantenimiento.getValueAt(gui.tabla.getSelectedRow(), 1).toString();
            String comentario = (modeloMantenimiento.getValueAt(gui.tabla.getSelectedRow(), 2) == null ? "" : modeloMantenimiento.getValueAt(gui.tabla.getSelectedRow(), 2).toString());

            gui.txIdMantenimiento.setText(idMant.toString());
            gui.txMantenimiento.setText(descripMant);
            gui.txComentario.setText(comentario);
            GUI.focusAll(gui.txIdMantenimiento);
        } else {
            GUI.msgAtencion(gui.lbInfo, "Debe seleccionar una fila");
        }

    }

    public void recuperarMantenimiento() {
        HashMap<Integer, EquipamientoDTO> map = new HashMap<>();

        MantenimientoEquipamientoDAO dao = new MantenimientoEquipamientoDAO();
        List<MantenimientoEquipamientoDTO> l = dao.recuperarRegistros();
        for (MantenimientoEquipamientoDTO m : l) {
            map.put(m.getEquipamiento().getId(), m.getEquipamiento());
        }
        List<EquipamientoDTO> listaBuscador = new ArrayList<>();
        for (Map.Entry<Integer, EquipamientoDTO> entry : map.entrySet()) {
            EquipamientoDTO value = entry.getValue();
            listaBuscador.add(value);
        }

        BuscadorGUI buscador = new BuscadorGUI(new Frame(), true, listaBuscador);
        buscador.setTitle("Buscador de Equipamiento");
        buscador.setVisible(true);

        if (buscador.getID() != null) {
            MantenimientoEquipamientoDTO x = new MantenimientoEquipamientoDTO();
            x.setEquipamiento(new EquipamientoDTO(Integer.parseInt(buscador.getID())));
            MantenimientoEquipamientoDTO d = new MantenimientoEquipamientoDAO().recuperarRegistro(x);

            gui.txIdEquipamiento.setText(buscador.getID().toString());
            gui.txEquipamiento.setText(buscador.getDescrip());
            DefaultTableModel m = (DefaultTableModel) gui.tabla.getModel();
            m.setRowCount(0);
            for (TipoMantenimientoDTO a : d.getMantenimientos()) {
                m.addRow(new Object[]{a.getId(), a.getDescrip(), a.getComentario()});
            }
            gui.tabla.setEnabled(true);
        }
    }

    public void verMantenimientos() {
        Integer idEquipo = GUI.getInt(gui.txIdEquipamiento);
        Informes info = new Informes();
        info.setArchivo("MantenimientoEquipamiento.jasper");
        info.setTitulo("LISTADO DE MANTENIMIENTOS DE EQUIPAMIENTOS");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.MANTENIMIENTO_EQUIPAMIENTO);
        if ((idEquipo != null) || (!GUI.get(gui.txIdEquipamiento).isEmpty())) {
            info.setIdEquipamiento(idEquipo);
            info.procesar();
        }else{
            GUI.msgAtencion(gui.lbInfo, "Debe seleccionar un equipamiento - Utilice el buscador <Campo Id>");
        }
    }

}
