package Base;

import ConexionDB.Conexion;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultCaret;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;
import javax.swing.text.MaskFormatter;

public class GUI {

    private static final int SUPER_ADMIN = 1;
    private static BufferedImage imgBg = null;
    private static final SimpleDateFormat fanho = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat famd = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat famdGUION = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat ffh = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat sfh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat fsh = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat fma = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
    private static final SimpleDateFormat fdma = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
    private static final SimpleDateFormat fdmaGUION = new SimpleDateFormat("dd-MM-yyyy", new Locale("es", "ES"));

    private static final String maskCobol = "####.###.##.#";
    private static final String maskCta = "##.####.##.###.#";
    private static final String maskVersion = "#.##";
    private static final String maskLibro = "###.#";
    private static final DecimalFormat maskSaldo = new DecimalFormat("#,##0");
    private static final DecimalFormat separadorMiles = new DecimalFormat("#,###.##");
    private static MaskFormatter mf;

    public static String comilla(String s) {
        return s.replace("'", "''");
    }

    public static String DateNull(Date fecha) {
        return (fecha == null ? "NULL" : "'" + fecha + "'");
    }

    public static String NegativoNull(int valor) {
        return (valor < 0 ? "NULL" : String.valueOf(valor));
    }

    public static String NullVacio(String valor) {
        return (valor == null ? "" : valor);
    }

    public static InputStream logoEssap() {
        return GUI.class.getResourceAsStream("/recursos/logos/essap100.png");
    }

    public static InputStream logoGobierno() {
        return GUI.class.getResourceAsStream("/recursos/logos/gobierno100.png");
    }

    public static InputStream logoSlogan() {
        return GUI.class.getResourceAsStream("/recursos/logos/slogan100.png");
    }

    public static InputStream logoMarcaAguaOriginal() {
        return GUI.class.getResourceAsStream("/reportes/marca_agua_original.png");
    }

    public static InputStream logoEssap2() {
        return GUI.class.getResourceAsStream("/reportes/EssapSA-logo.png");
    }

    public static String validarTexto(String text) {
        return text.replace("'", "''").trim();
    }

    public static void adminMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Admin msg", JOptionPane.ERROR_MESSAGE);
    }

    public static BigDecimal toMB(long bytes) {
        double b = bytes;
        BigDecimal mb = BigDecimal.valueOf(b / 1024 / 1024).setScale(2, BigDecimal.ROUND_HALF_UP);
        return mb;
    }

    public static Object setNull(int id) {
        if (id == 0) {
            return null;
        }
        return id;
    }

    public static boolean isEmpty(JTextField tx) {
        return tx.getText().trim().isEmpty();
    }

    public static boolean F1(KeyEvent evt) {
        return evt.getKeyCode() == KeyEvent.VK_F1;
    }

    public static boolean F5(KeyEvent evt) {
        return evt.getKeyCode() == KeyEvent.VK_F5;
    }

    public static Integer getNumeroAleatorio() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(Integer.MAX_VALUE);
    }

    public static boolean validarTxtRequerido(JTextField tx, JLabel lbl) {
        if (!tx.getText().trim().isEmpty()) {
            return true;
        } else {
            msgAtencion(lbl, "Campo " + tx.getAccessibleContext().getAccessibleName() + " requerido");
            focusAll(tx);
            return false;
        }
    }

    public static boolean validarTxtRequeridos(List<JTextField> lTxt, JLabel lbl) {
        if (lTxt != null) {
            for (JTextField tx : lTxt) {
                if (tx.getText().trim().isEmpty()) {
                    msgAtencion(lbl, "Campo " + tx.getAccessibleContext().getAccessibleName() + " requerido");
                    focusAll(tx);
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static void limpiarTxt(List<JComponent> jPanel){
        for (JComponent cont : jPanel) {
            if (cont instanceof JPanel) {
                for (Object i : ((JPanel) cont).getComponents()) {
                    if (i instanceof JTextField) {
                        ((JTextField) i).setText("");
                    }
                }
            }
        }
    }
   
    public static void habilitarBotones(List<JComponent> jPanel, boolean habilitar){
        for (JComponent cont : jPanel) {
            if (cont instanceof JPanel) {
                for (Object i : ((JPanel) cont).getComponents()) {
                    if (i instanceof JButton) {
                        ((JButton) i).setEnabled(habilitar);
                    }
                }
            }
        }
    }
  
    public static void editarTxt(List<JComponent> jPanel,boolean editar){
        for (JComponent cont : jPanel) {
            if (cont instanceof JPanel) {
                for (Object i : ((JPanel) cont).getComponents()) {
                    if (i instanceof JTextField) {
                        ((JTextField) i).setEditable(editar);
                    }
                }
            }
        }
    }
    
    public static void limpiarTabla(List<JTable> jTable){
        for (JTable tb : jTable) {
            DefaultTableModel tbLote = (DefaultTableModel) tb.getModel();
            tbLote.getDataVector().removeAllElements();
            tbLote.fireTableDataChanged();
        }
    }

    public static Object set(JTextField txtUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static enum tipoVentana {
        estandard, buscadores, cabecera_detalle, mediana, ancha
    };

    public static void setFiltroTabla(JTable tabla, String tx, int cols[]) {
        TableRowSorter sorter = new TableRowSorter(tabla.getModel());

        try { // bandera para CASE INSENSITIVE --> "(?i)"
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + tx, cols));
            tabla.setRowSorter(sorter);
        } catch (java.util.regex.PatternSyntaxException e) {
            // mal
        }
    }

    public static void actualizarFiltro(JTable tabla, String tx, int cols[]) {
        TableRowSorter t = (TableRowSorter) tabla.getRowSorter();
        t.setRowFilter(RowFilter.regexFilter("(?i)" + tx, cols));
    }

    public static void setFiltroTablaNumerico(JTable tabla, int num, int cols[]) {
        TableRowSorter sorter = new TableRowSorter(tabla.getModel());

        try { // bandera para CASE INSENSITIVE --> "(?i)"
            //sorter.setRowFilter( RowFilter.regexFilter("(?i)" + tx, cols) );
            sorter.setRowFilter(RowFilter.numberFilter(ComparisonType.BEFORE, num + 1, cols));
            tabla.setRowSorter(sorter);
        } catch (java.util.regex.PatternSyntaxException e) {
            // mal
        }
    }

    public static void actualizarFiltroNumerico(JTable tabla, int num, int cols[]) {
        TableRowSorter t = (TableRowSorter) tabla.getRowSorter();
        t.setRowFilter(RowFilter.numberFilter(ComparisonType.BEFORE, num + 1, cols));
    }

    public static boolean esAdmin() {
        return true;
       // return PrincipalGUI.usuario.getPerfil().getId() == SUPER_ADMIN;
    }

  

    public static void msgDebug(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Debug", JOptionPane.ERROR_MESSAGE);
    }

    public static String CeroNull(int valor) {
        return (valor == 0 ? "NULL" : String.valueOf(valor));
    }

    public static Object intNull(int valor) {
        return (valor == 0 ? null : valor);
    }

    public static void msgError(JLabel lb, String m) {
        lb.setBackground(new Color(150, 0, 0, 255));
        lb.setForeground(Color.WHITE);
        formatoMsg(lb, m, "t16/Warning16");
    }

    public static void msgInfo(JLabel lb, String m) {
        lb.setBackground(new Color(0, 100, 150, 255));
        lb.setForeground(Color.WHITE);
        formatoMsg(lb, m, "t16/Information16");
    }

    public static void msgInfoB(JLabel lb, String m) {
        lb.setBackground(new Color(255, 255, 255, 255));
        lb.setForeground(Color.BLACK);
        formatoMsg(lb, m, "t16/Information16");
    }

    public static void msgB(JLabel lb, String m) {
        lb.setBackground(new Color(255, 255, 255, 255));
        lb.setForeground(Color.DARK_GRAY);
        formatoMsg(lb, m, null);
    }

    public static void msgB(JLabel lb, String m, String icono) {
        lb.setBackground(new Color(255, 255, 255, 255));
        lb.setForeground(Color.DARK_GRAY);
        formatoMsg(lb, m, icono);
    }

    public static void msgTip(JLabel lb, String m) {
        lb.setBackground(new Color(255, 255, 175, 255));
        lb.setForeground(new Color(45, 45, 45, 255));
        formatoMsg(lb, m, null);
    }

    public static void msg(JLabel lb, String m) {
        lb.setBackground(new Color(240, 240, 240, 255));
        lb.setForeground(new Color(0, 0, 0, 255));
        formatoMsg(lb, m, null);
    }

    public static void msgAtencion(JLabel lb, String m) {
        lb.setBackground(new Color(255, 204, 51, 255));
        lb.setForeground(Color.BLACK);
        formatoMsg(lb, m, "t16/Warning16");
    }

    private static void formatoMsg(JLabel lb, String m, String icono) {
        lb.setOpaque(true);
        lb.setText("<html><div style=\"padding:5px\">" + m + "</div></html>");
        lb.setIcon(getIcono(icono));
    }

    public static Image getImagen(String img) {
        ImageIcon icono = getIcono(img);
        if (icono != null) {
            return icono.getImage();
        } else {
            return null;
        }
    }

    public static ImageIcon getIcono(String icono) {
        try {
            ImageIcon ico = new ImageIcon(GUI.class.getResource("/recursos/iconos/" + icono + ".png"));
            return ico;
        } catch (Exception e) {
            return null;
        }
    }

    public static void setMaxLength(KeyEvent evt, int max) {
        JTextField tx = (JTextField) evt.getSource();
        if (tx.getText().length() >= max) {
            evt.consume();
        }
    }

    

    public static void FormatoVentanas(JInternalFrame ventana, tipoVentana tVentana) {
        switch (tVentana) {
            case estandard:
                //ventana.getContentPane().setBackground(Color.white);
                ventana.setSize(new Dimension(800, 600));
                break;
            case cabecera_detalle:
                // ventana.getContentPane().setBackground(new Color(125, 85, 25));
                ventana.setSize(new Dimension(1020, 675));
                break;
            case mediana:
                // ventana.getContentPane().setBackground(new Color(125, 85, 25));
                ventana.setSize(new Dimension(700, 550));
                break;
            case buscadores:
                // ventana.getContentPane().setBackground(Color.WHITE);
                ventana.setSize(new Dimension(500, 550));
                break;
            case ancha:
                // ventana.getContentPane().setBackground(new Color(125, 85, 25));
                ventana.setSize(new Dimension(900, 550));
                break;
        }
        ventana.setResizable(false);
    }

    public static void focusAll(JTextField tx) {
        tx.requestFocus();
        tx.selectAll();
    }

    public static String formatoAnhoMesDia(Date fecha) {
        return famd.format(fecha);
    }
    public static String formatoAnhoMesDiaDos(Date fecha) {
        return famdGUION.format(fecha);
    }

    public static String formatoDiaMesAnho(Date fecha) {
        return fdma.format(fecha);
    }

    public static int getAnho(Timestamp fecha) {
        return Integer.parseInt(fanho.format(fecha));
    }

    public static boolean validarFecha(int anho, int mes, int dia) {
        if (anho < 1900) {
            return false;
        }

        try {
            Calendar cal = Calendar.getInstance();
            cal.setLenient(false);
            cal.set(Calendar.YEAR, anho);
            cal.set(Calendar.MONTH, mes - 1); // [0,...,11]
            cal.set(Calendar.DAY_OF_MONTH, dia);
            Date fecha = cal.getTime();
            String x = famd.format(fecha);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static String formatoFechaHora(Date fecha) {
        return ffh.format(fecha);
    }

    public static Timestamp getTimestamp(String fecha) {
        try {
            Date d = ffh.parse(fecha);
            Timestamp ts = new Timestamp(d.getTime());
            return ts;
        } catch (ParseException ex) {
            System.err.println("Error " + ex.getMessage());
            return null;
        }
    }
       public static Date getDateServer() {
        try {
            ConexionDB.Conexion c=new Conexion();
            PreparedStatement p;
            String s = "select current_date as fecha";
            p=c.obtenerConexion().prepareStatement(s);
            ResultSet rs = p.executeQuery();
            rs.next();
            return rs.getDate("fecha");
        } catch (SQLException e) {
            return null;
        }
    }
       
        public static Time getTimeServer() {
        try {
            Conexion c = new Conexion();
            String s = "select current_time as hora";
            PreparedStatement p;
            p= c.obtenerConexion().prepareStatement(s);
            ResultSet rs = p.executeQuery();
            rs.next();
            return rs.getTime("hora");
        } catch (SQLException e) {
            return null;
        }
    }
      
       
       
    public static String formatoSoloHora(Date fecha) {
        return fsh.format(fecha);
    }
    
    public static String formatoSoloHora(Time hora) {
        return fsh.format(hora);
    }

    public static String formatoMesAnho(Date fecha) {
        return fma.format(fecha);
    }

    public static Date getDate(String fecha) {
        try {
            // fdma >>> "dd/MM/yyyy"
            Date date = fdma.parse(fecha);
            return date;
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Timestamp getTimestampServer() {
//        try {
//            ResultSet rs = ConexionDB.consultar("select current_timestamp as fecha_hora");
//            rs.next();
//            return rs.getTimestamp("fecha_hora");
//        } catch (SQLException e) {
//            return null;
//        }
        return null;
    }



    public static java.sql.Date getDateSQL() {
         try {
            ConexionDB.Conexion c=new Conexion();
            PreparedStatement p;
            String s = "select current_date as fecha";
            p=c.obtenerConexion().prepareStatement(s);
            ResultSet rs = p.executeQuery();
            rs.next();
            return rs.getDate("fecha");
        } catch (SQLException e) {
            return null;
        }
        
        
        /*
        try {
            //java.util.Date date = fdma.parse(fecha);
            java.sql.Date res = new java.sql.Date(fecha.getTime());
            return res;
        } catch (Exception ex) {
            return null;
        }
        */
    }

    public static java.sql.Date getDateSQL(Date fecha) {
        try {
            //java.util.Date date = fdma.parse(fecha);
            java.sql.Date res = new java.sql.Date(fecha.getTime());
            return res;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String formatoMesAnho(String fecha) {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return formatoMesAnho(df.parse(fecha));
        } catch (ParseException ex) {
            return "-";
        }
    }

    public static String formatoCobol(long cobol) {
        try {
            if (String.valueOf(cobol).length() == 10) {
                mf = new MaskFormatter(maskCobol);
                mf.setValueContainsLiteralCharacters(false);
                return mf.valueToString(cobol);
            } else {
                return String.valueOf(cobol);
            }
        } catch (ParseException ex) {
            return "[error]";
        }
    }

    public static String formatoSaldo(long saldo) {
        return maskSaldo.format(saldo);
    }

    public static String formatoVersion(long version) {
        try {
            if (String.valueOf(version).length() == 3 || String.valueOf(version).length() == 4) {
                mf = new MaskFormatter(maskVersion);
                mf.setValueContainsLiteralCharacters(false);
                return mf.valueToString(version);
            } else {
                return String.valueOf(version);
            }
        } catch (ParseException ex) {
            return "[error]";
        }
    }

    public static String formatoLibro(int libro) {
        try {
            mf = new MaskFormatter(maskLibro);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(pad4(libro));
        } catch (ParseException ex) {
            return "[error]";
        }
    }

    public static String formatoCta(long cta) {
        try {
            if (String.valueOf(cta).length() == 12) {
                mf = new MaskFormatter(maskCta);
                mf.setValueContainsLiteralCharacters(false);
                return mf.valueToString(cta);
            } else {
                return String.valueOf(cta);
            }
        } catch (ParseException ex) {
            return "[error]";
        }
    }

    public static boolean verificarCrearDir(String dir) {
        File a;
        boolean ok = true;
        String arr[] = dir.split("/");
        dir = "";
        int i = 0;
        while (i < arr.length && ok == true) {
            dir += arr[i++] + "/";
            a = new File(dir);
            if (a.exists()) {
                if (a.isFile()) {
                    ok = false;
                }
            } else {
                if (a.mkdir() == false) {
                    ok = false;
                }
            }
        }

        return ok;
    }

    public static String formatoCiclo(int mes) {
        return String.format("%02d", mes);
    }

    public static String log(String m, boolean b) {
        Date hora = new Date();
        if (b) {
            m = "[" + GUI.formatoSoloHora(hora) + "] <span style=\"color:green\">" + m + "</span><br>";
        } else {
            m = "[" + GUI.formatoSoloHora(hora) + "] <span style=\"color:red\">" + m + "</span><br>";
        }
        return m;
    }

    public static InputStream logoEssapEscalaGrises() {
        return GUI.class.getResourceAsStream("/reportes/logo_essap_neutro.jpg");
    }

    public static void setLogScroll(JTextArea txLog) {
        DefaultCaret caret = (DefaultCaret) txLog.getCaret();
        caret.setUpdatePolicy(ALWAYS_UPDATE);
    }

    public static String pad1(int n) {
        String p = "" + n;
        return ("0" + p).substring(p.length());
    }

    public static String pad2(int n) {
        String p = "" + n;
        return ("00" + p).substring(p.length());
    }

    public static String pad4(int n) {
        String p = "" + n;
        return ("0000" + p).substring(p.length());
    }

    public static String pad10(long n) {
        String p = "" + n;
        return ("0000000000" + p).substring(p.length());
    }

    public static String pad12(long n) {
        String p = "" + n;
        return ("000000000000" + p).substring(p.length());
    }

    public static String pad32(String s) {
        return (s + "                                ").substring(0, 32);
    }

    public static String pad19(String s) {
        return (s + "                   ").substring(0, 19);
    }

    public static String sinTildes(String cadena) {
        return cadena.replace("�", "A")
                .replace("�", "E")
                .replace("�", "I")
                .replace("�", "O")
                .replace("�", "U")
                .replace("�", "a")
                .replace("�", "e")
                .replace("�", "i")
                .replace("�", "o")
                .replace("�", "u");
    }

    public static String strim(String s) {
        return s.trim().replaceAll(" +", " ");
    }

    public static void soloNumeros(KeyEvent e) {
        char c = e.getKeyChar();
        //if(Character.isLetter(c)) { 
        if ((c < '0') || (c > '9')) {
            e.consume();
        }
    }

    public static boolean esNumerico(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException e) {
            resultado = false;
        }
        return resultado;
    }

    public static int getIndexCombo(JComboBox cb, String item) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).toString().equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public static int getIDSegunItemCombo(JComboBox cb, String valor) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).toString().equals(valor)) {
                ComboItem combo = (ComboItem) cb.getItemAt(i);
                return combo.getId();
            }
        }
        return -1;
    }

    public static enum ALINEACION {
        CENTRO, IZQUIERDA, DERECHA
    };

    public static void alinerColumnaTabla(JTable tabla, int indexCol, ALINEACION alineacion) {
        DefaultTableCellRenderer modelo = new DefaultTableCellRenderer();
        switch (alineacion) {
            case CENTRO:
                modelo.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            case IZQUIERDA:
                modelo.setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case DERECHA:
                modelo.setHorizontalAlignment(SwingConstants.RIGHT);
                break;
        }
        tabla.getColumnModel().getColumn(indexCol).setCellRenderer(modelo);
    }

    public static void ocultarColumnaTabla(JTable tabla, int indexCol) {
        tabla.getColumnModel().getColumn(indexCol).setMinWidth(0);
        tabla.getColumnModel().getColumn(indexCol).setMaxWidth(0);
        tabla.getColumnModel().getColumn(indexCol).setWidth(0);
        tabla.getColumnModel().getColumn(indexCol).setPreferredWidth(0);
    }

    public static String getPasswordString(char[] p) {
        String v = "";
        for (char i : p) {
            v += i;
        }
        return v.trim();
    }


    public static long getDays(Date fecha1, Date fecha2) {
        try {
            long n;
            if (fecha1 == null || fecha2 == null) {
                n = 99999;
            } else {
                LocalDate i = toLocalDate(fecha1);
                LocalDate f = toLocalDate(fecha2);
                n = ChronoUnit.DAYS.between(i, f);
            }
            return n;
        } catch (Exception e) {
            // para detectar error (99.999 dias = 274 a�os aprox
            return 99999;
        }
    }

    private static LocalDate toLocalDate(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return LocalDate.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String cleanStr(String s) {
        return s.replaceAll("[<>=\\[\\]\\(\\)\\.\\-{}~#�?'/!+\\*:$%]", "").replace("\\", "");
    }
    
    public static boolean intValido(Integer valor){
        return (valor > 0);
    }
    
    public static boolean longValido(Long valor){
        return (valor > 0);
    }

    public static boolean enterTab(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            return true;
        } 
        else {
            if (evt.getKeyCode() == KeyEvent.VK_TAB) {
                return true;
            }
        }
        return false;
    }

    public static String get(JTextField txt) {
        return txt.getText().trim();
    }

    public static Integer getInt(JTextField txt) {
        return   ( NullVacio(txt.getText()).trim().isEmpty() ? null :  Integer.parseInt(get(txt)));
    }

    public static String geteparadorMiles(Long v) {
        return separadorMiles.format(v);
    }

    public static void setFormatoFecha(JFormattedTextField fecha) {
        try {
            fecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
        } catch (ParseException ex) {
        }
    }
    public static JFormattedTextField setFormatoFechaVacia(JFormattedTextField fecha) {
        fecha.setFormatterFactory(null);
        return fecha;
    }

    public static void validaFecha(JLabel lbl, KeyEvent evt, Runnable run) {
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            //System.out.println("asdadasdada");
            JFormattedTextField asdfecha = (JFormattedTextField) evt.getComponent();
            asdfecha.setValue(null);
            asdfecha.setText("");
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField asdfecha = (JTextField) evt.getComponent();
            String texto = asdfecha.getText();
            String[] partes = texto.split("-");
            texto = "";
            for (int x = 0; x < partes.length; x++) {
                String parte = partes[x].trim();
                if (parte.length() != 0) {
                    if (x == 0 || x == 1) {
                        parte = rellenoInt(Integer.valueOf(parte).toString(), 2);
                    } else {
                        parte = rellenoInt(Integer.valueOf(parte).toString(), 4);
                    }
                }
                texto += parte;
            }
            asdfecha.setText(texto);
            boolean valida = false;
            if (texto.length() == 8) {
                SimpleDateFormat asd = new SimpleDateFormat("ddMMyyy");
                try {
                    Date newasd = asd.parse(texto);
                    String parseasd = asd.format(newasd);
                    valida = texto.equals(parseasd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (valida) {
                    asdfecha.setText(texto);
                    run.run();
                } else {
                    GUI.msgAtencion(lbl, "Fecha ingresada invalida");
                    focusAll(asdfecha);
                    //JOptionPane.showMessageDialog(getPrimerFrame(asdfecha), "Ingreso una fecha incorrecta", "    ....A T E N C I O N....    ", 2);
                    //asdfecha.setText("");
                }
            }
        }
    }

    public static String rellenoInt(String ori, int longitudDestino) {
        int length = 0;
        if (ori != null) {
            length = ori.length();
        } else {
            ori = "";
            length = ori.length();
        }

        int resta = longitudDestino - length;
        for (int x = 0; x < resta; x++) {
            ori = "0" + ori;
        }
        return ori;
    }

    public static Component getPrimerFrame(Component asd) {
        while (true) {
            asd = asd.getParent();
            if (asd instanceof JRootPane) {
                break;
            }
        }
        return asd;
    }
    
    public static void foco(final Component compo) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                compo.requestFocus();
            }
        });
    }

    public static void enter(Component compo) {
        foco(compo);
        try {
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_ENTER);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void parseTabEnter() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getSource() instanceof JTextField) {
                        final JTextField tfSrc = (JTextField) e.getSource();
                        if (((KeyEvent) e).getKeyCode() == KeyEvent.VK_TAB) {
                            KeyEvent evt = new KeyEvent(tfSrc, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, '0');
                            tfSrc.dispatchEvent(evt);
                            ((KeyEvent) e).consume();
                        }
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }
    
    public static void salirEdicion(JTable tabla) {
       if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();//cancelCellEditing();
        }
    }

}
