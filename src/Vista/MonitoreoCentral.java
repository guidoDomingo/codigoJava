package Vista;

import Control.MonitoreoCTR;
import Control.MonitoreoCTR.ALARMAS;
import static Vista.home.Usuario_home;
import static Vista.home.correo;
import static Vista.home.id_usuario_home;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
//twilio
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.awt.Color;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

public class MonitoreoCentral extends javax.swing.JFrame {

    //  Conexion bd=new Conexion();
    //WHATSAP
    public static final String ACCOUNT_SID = "AC7b27484d624d4a073fce424ba571ea8b";
    public static final String AUTH_TOKEN = "03d6177112aab1db4fbce57dbc7c2ead";

    //FIN WHATSAP
    public static int banrepor = 0;
    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    private Timer segundo, segundo2, segundo3, timer1 , timer2 , timer3;
     private Timer t,t1,t2;
    int cont, cont2, cont3, cont4, cont5, cont6, cont7 , cont7mas , contR3,contR3mas ,  m1, m2, conmut, contm1, contm1_2, estacon, pe, codalar;
    public String motivo;
    int coduusuario = 0;
    // public int codu;
    public final static int ONE_SECOND = 200;
    public final static int ONE_SECOND2 = 200;
    public final static int ONE_SECOND3 = 200;
    int h, m, s, banbomb;
    int hora2, minuto2, seg2, banbomb2;
    private int hora1, minuto1, segundo1, cs;
    String op = "";
    private MonitoreoCTR ctr;
    boolean automatico = false;
    
    public MonitoreoCentral() {
        initComponents();
        ctr = new MonitoreoCTR(id_usuario_home);
        
        setIconImage(new ImageIcon(getClass().getResource("/Iconos/icon.png")).getImage());
        banbomb = 0;
        //int  String comp=form_movimientos.acceso.rool;
        // coduusuario=form_movimientos.acceso.codusua;
        // JOptionPane.showMessageDialog(null, coduusuario);

        //bd.conect();
        Date capfecha = new Date();
        //codbombeo.setVisible(false);
        txtfecha.setText(fecha.format(capfecha));
        contmotor1.setText("0" + ":" + "0" + ":" + "0");
        contmotor2.setText("0" + ":" + "0" + ":" + "0");
        contmotor3.setText("0" + ":" + "0" + ":" + "0");
        avisosvisibles_false();
        textalarma.setVisible(false);
        //hora.setVisible(false);
        //txthoraconver.setVisible(false);
        txtfecha.setVisible(false);
        contm1 = 0;
        pe = 0;
        contm1_2 = 0;
        estacon = 1;
        m1 = 1;
        m2 = 1;
        cont4 = -1;
        cont5 = -1;
        //setExtendedState(MonitoreoCentral.MAXIMIZED_BOTH);
        motor1_on.setVisible(false);
        motor2_on.setVisible(false);

        cont = 70;
//        barra.setValue(70);
//        barra.setStringPainted(true);
//        segundo = new Timer(ONE_SECOND, new TimerListener());

        cont2 = 90;
        barra2.setValue(90);
        barra2.setStringPainted(true);

        cont5 = 90;
        barra2.setValue(90);
        barra2.setStringPainted(true);
        barraReservorio3.setValue(90);
        barraReservorio3.setStringPainted(true);
        barraReservorio2.setValue(80);
        barraReservorio2.setStringPainted(true);
        
        //motores
        text_motor1_parada.setVisible(true);
        text_motor1_parada.setForeground(Color.YELLOW);
        text_motor1_parada.setText("Motor 1. Parado");
        text_motor1_marcha.setVisible(true); 
        text_motor1_marcha.setForeground(Color.YELLOW);
        text_motor1_marcha.setText("Motor 2. Parado");
        text_motor2_parada.setVisible(true); 
        text_motor2_parada.setForeground(Color.YELLOW);
        text_motor2_parada.setText("Motor 3. Parado");
        text_motor2_marcha.setVisible(false);        
    }

    class TimerBarraReservorio2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            contR3--;
            barraReservorio3.setValue(contR3);
            if (barraReservorio3.getValue() < 15) {
                luzniv_tanque_critico.setVisible(true);
                text_nveltanque_mini.setVisible(true);
                text_nveltanque_maxi.setVisible(false);
                luz_tanque_ok.setVisible(false);
                text_nveltanque_ok.setVisible(false);

                if (barraReservorio3.getValue() <= 8) {
                    text_alarma.setVisible(true);
                    luz_sondido_alarma.setVisible(true);
                    luz_sistbombeo_ok.setVisible(false);
                    text_sistBombeo.setVisible(false);
                    motor1_on.setVisible(true);
                    motor2_on.setVisible(true);
                    motorReservorio3.setVisible(true);
                    captura_hora();

                    ctr.grabarAlarma(ALARMAS.NIVEL_MINIMO,3);
                    
                    //whasapt
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
                    Message message = Message.creator(new PhoneNumber("+595971868353"),
                            new PhoneNumber("+16109475021"),
                            "Nivel  Minimo de Agua en el tanque, verifique por favor").create();

                    System.out.println(message.getSid());

                    labelR3A.setText("Whatsapp enviado");
                    //fin del whasapt

                    //eviar correo
                    Properties propiedad = new Properties();
                    propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
                    propiedad.setProperty("mail.smtp.starttls.enable", "true");
                    propiedad.setProperty("mail.smtp.port", "587");
                    propiedad.setProperty("mail.smtp.auth", "true");

                    Session sesion = Session.getDefaultInstance(propiedad);

                    String correoEnvia = "monitoreoaviso65@gmail.com";
                    String contrasena = "MonitoreoAviso65";
                    String destinatario = correo;
                    String asunto = "El nivel del agua está muy bajo";
                    String mensaje = "Nivel  Minimo de Agua del Tanque Alcanzado";

                    MimeMessage mail = new MimeMessage(sesion);

                    try {
                        mail.setFrom(new InternetAddress(correoEnvia));
                        mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                        mail.setSubject(asunto);
                        mail.setText(mensaje);

                        Transport transporte = sesion.getTransport("smtp");
                        transporte.connect(correoEnvia, contrasena);
                        transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
                        transporte.close();

                        labelR3B.setText("Correo enviado");

                    } catch (AddressException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MessagingException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //fin del correo
                    if(automatico == false){
                    JOptionPane.showMessageDialog(null, "Nivel  Minimo del reservorio 3", "ATENCION ENCIEDA MOTOR CORRESPONDIENTE",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    timer1.stop();
//                     segundo2 = new Timer(ONE_SECOND2, new TimerListener4());
//                     segundo2.start();
                    if(automatico == true){
                        motor2_on1.setSelected(true);
                        cont6 = 8;
                        cont5 = 90;
                        segundo2 = new Timer(ONE_SECOND2, new TimerListener4());
                        segundo2.start();
                        luz_sondido_alarma.setVisible(false);
                        text_alarma.setVisible(false);
                        luz_sistbombeo_ok.setVisible(true);
                        text_sistBombeo.setVisible(true);
                        
                    }
                     
                }
            }
            
            //activamos reservorio 2
            
        }
    }
    
    class TimerBarraReservorio3 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cont7--;
            barraReservorio2.setValue(cont7);
            if (barraReservorio2.getValue() < 15) {
                luzniv_tanque_critico.setVisible(true);
                text_nveltanque_mini.setVisible(true);
                text_nveltanque_maxi.setVisible(false);
                luz_tanque_ok.setVisible(false);
                text_nveltanque_ok.setVisible(false);

                if (barraReservorio2.getValue() <= 8) {
                    text_alarma.setVisible(true);
                    luz_sondido_alarma.setVisible(true);
                    luz_sistbombeo_ok.setVisible(false);
                    text_sistBombeo.setVisible(false);
                    motor1_on.setVisible(true);
                    motor2_on.setVisible(true);
                    motorReservorio3.setVisible(true);
                    captura_hora();

                    ctr.grabarAlarma(ALARMAS.NIVEL_MINIMO,2);
                    
                    //whasapt
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
                    Message message = Message.creator(new PhoneNumber("+595971868353"),
                            new PhoneNumber("+16109475021"),
                            "Nivel  Minimo de Agua en el tanque, verifique por favor").create();

                    System.out.println(message.getSid());

                    labelR2A.setText("Whatsapp enviado");
                    //fin del whasapt

                    //eviar correo
                    Properties propiedad = new Properties();
                    propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
                    propiedad.setProperty("mail.smtp.starttls.enable", "true");
                    propiedad.setProperty("mail.smtp.port", "587");
                    propiedad.setProperty("mail.smtp.auth", "true");

                    Session sesion = Session.getDefaultInstance(propiedad);

                    String correoEnvia = "monitoreoaviso65@gmail.com";
                    String contrasena = "MonitoreoAviso65";
                    String destinatario = correo;
                    String asunto = "El nivel del agua está muy bajo";
                    String mensaje = "Nivel  Minimo de Agua del Tanque Alcanzado";

                    MimeMessage mail = new MimeMessage(sesion);

                    try {
                        mail.setFrom(new InternetAddress(correoEnvia));
                        mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                        mail.setSubject(asunto);
                        mail.setText(mensaje);

                        Transport transporte = sesion.getTransport("smtp");
                        transporte.connect(correoEnvia, contrasena);
                        transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
                        transporte.close();

                        labelR2B.setText("Correo enviado");

                    } catch (AddressException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MessagingException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //fin del correo
                     if(automatico == false){
                    JOptionPane.showMessageDialog(null, "Nivel  Minimo del reservorio 2", "ATENCION ENCIEDA MOTOR CORRESPONDIENTE",
                            JOptionPane.INFORMATION_MESSAGE);
                     }
                        timer3.stop();
                    
//                     segundo2 = new Timer(ONE_SECOND2, new TimerListener4());
//                     segundo2.start();

                    if(automatico == true){
                        motor2_on.setSelected(true);
                        cont7mas = 8;
                        timer3 = new Timer(ONE_SECOND2, new TimerBarraReservorio3Aumenta());
                        timer3.start();
                        luz_sondido_alarma.setVisible(false);
                        text_alarma.setVisible(false);
                        luz_sistbombeo_ok.setVisible(true);
                        text_sistBombeo.setVisible(true);
                    }
                     
                }
            }
            
            //activamos reservorio 2
            
        }
    }

class TimerBarraReservorio2Aumenta implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (motor2_on1.isSelected() == true) {
            int cont7Nuevo = 100 - cont5;
            cont7Nuevo++;
            contR3mas++;
            barraReservorio3.setValue(cont7Nuevo);
            barraReservorio3.setStringPainted(true);
            
            
                    
            if (barraReservorio3.getValue() >= 75) {
                luzniv_tanque_critico.setVisible(false);
                text_nveltanque_mini.setVisible(false);
                text_nveltanque_maxi.setVisible(false);
                luz_tanque_ok.setVisible(true);
                text_nveltanque_ok.setVisible(false);

                if (barraReservorio3.getValue() == 80) {
                    text_alarma.setVisible(true);
                    luz_sondido_alarma.setVisible(true);
                    luz_sistbombeo_ok.setVisible(true);
                    text_sistBombeo.setVisible(true);
                    motor1_on.setVisible(true);
                    motor2_on.setVisible(true);
                    motorReservorio3.setVisible(true);
                    captura_hora();
                    
                    ctr.grabarAlarma(ALARMAS.NIVEL_MAXIMO,3);
                    
                    
                    
                    //whasapt
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
                    Message message = Message.creator(new PhoneNumber("+595971868353"),
                            new PhoneNumber("+16109475021"),
                            "Nivel  Minimo de Agua en el tanque, verifique por favor").create();

                    System.out.println(message.getSid());

                    labelR3A.setText("Whatsapp enviado");
                    //fin del whasapt

                    //eviar correo
                    Properties propiedad = new Properties();
                    propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
                    propiedad.setProperty("mail.smtp.starttls.enable", "true");
                    propiedad.setProperty("mail.smtp.port", "587");
                    propiedad.setProperty("mail.smtp.auth", "true");

                    Session sesion = Session.getDefaultInstance(propiedad);

                    String correoEnvia = "monitoreoaviso65@gmail.com";
                    String contrasena = "MonitoreoAviso65";
                    String destinatario = correo;
                    String asunto = "El nivel del agua está muy bajo";
                    String mensaje = "Nivel  Minimo de Agua del Tanque Alcanzado";

                    MimeMessage mail = new MimeMessage(sesion);

                    try {
                        mail.setFrom(new InternetAddress(correoEnvia));
                        mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                        mail.setSubject(asunto);
                        mail.setText(mensaje);

                        Transport transporte = sesion.getTransport("smtp");
                        transporte.connect(correoEnvia, contrasena);
                        transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
                        transporte.close();

                        labelR3B.setText("Correo enviado");

                    } catch (AddressException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MessagingException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //fin del correo
                     if(automatico == false){
                    JOptionPane.showMessageDialog(null, "Nivel  Máximo de Agua del Tanque Alcanzado", "ATENCION ENCIEDA MOTOR CORRESPONDIENTE",
                            JOptionPane.INFORMATION_MESSAGE);
                     }
                timer1.stop();
                    
                }
            }
            
            
            
            //activamos reservorio 2
            
            }
            
        }
    }

class TimerBarraReservorio3Aumenta implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            
            
            //int nuevoContador7 = 100 - cont7;
            
            cont7++;
            barraReservorio2.setValue(cont7);
            barraReservorio2.setStringPainted(true);
            
            

               // text_motor1_parada.setVisible(false);
                luz_motor1_marcha.setVisible(true);
                //text_motor1_marcha.setVisible(true);
                animarmotor2_manual();
        
                    
            if (barraReservorio2.getValue() >= 75) {
                luzniv_tanque_critico.setVisible(false);
                text_nveltanque_mini.setVisible(false);
                text_nveltanque_maxi.setVisible(true);
                luz_tanque_ok.setVisible(true);
                text_nveltanque_ok.setVisible(true);

                if (barraReservorio2.getValue() == 80) {
                    text_alarma.setVisible(true);
                    luz_sondido_alarma.setVisible(true);
                    luz_sistbombeo_ok.setVisible(true);
                    text_sistBombeo.setVisible(true);
                    motor1_on.setVisible(true);
                    motor2_on.setVisible(true);
                    motorReservorio3.setVisible(true);
                    captura_hora();
                    
                    ctr.grabarAlarma(ALARMAS.NIVEL_MAXIMO,2);
                    
                    
                    
                    //whasapt
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
                    Message message = Message.creator(new PhoneNumber("+595971868353"),
                            new PhoneNumber("+16109475021"),
                            "Nivel  Minimo de Agua en el tanque, verifique por favor").create();

                    System.out.println(message.getSid());

                    labelR2A.setText("Nivel máximo, Whatsapp enviado");
                    //fin del whasapt

                    //eviar correo
                    Properties propiedad = new Properties();
                    propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
                    propiedad.setProperty("mail.smtp.starttls.enable", "true");
                    propiedad.setProperty("mail.smtp.port", "587");
                    propiedad.setProperty("mail.smtp.auth", "true");

                    Session sesion = Session.getDefaultInstance(propiedad);

                    String correoEnvia = "monitoreoaviso65@gmail.com";
                    String contrasena = "MonitoreoAviso65";
                    String destinatario = correo;
                    String asunto = "El nivel del agua está muy bajo";
                    String mensaje = "Nivel  Minimo de Agua del Tanque Alcanzado";

                    MimeMessage mail = new MimeMessage(sesion);

                    try {
                        mail.setFrom(new InternetAddress(correoEnvia));
                        mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                        mail.setSubject(asunto);
                        mail.setText(mensaje);

                        Transport transporte = sesion.getTransport("smtp");
                        transporte.connect(correoEnvia, contrasena);
                        transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
                        transporte.close();

                        labelR2B.setText("Nivel máximo, Correo enviado");

                    } catch (AddressException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MessagingException ex) {
                        Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //fin del correo
                     if(automatico == false){
                    JOptionPane.showMessageDialog(null, "Nivel  Máximo de Agua del Tanque Alcanzado", "ATENCION ENCIEDA MOTOR CORRESPONDIENTE",
                            JOptionPane.INFORMATION_MESSAGE);
                     }
                    if(automatico == true){
                        
                        motor2_on.setSelected(false);
                        timer3.stop();
                    }
                //
                    
                }
            }
            
            
            
            //activamos reservorio 2
            
                
            
        }
    }

class TimerListener2 implements ActionListener {

    public void actionPerformed(ActionEvent evt) {
        cont2--;
        barra2.setValue(cont2);
        if (barra2.getValue() <= 80) {
            luzniv_tanque_critico.setVisible(false);
            text_nveltanque_maxi.setVisible(false);
            text_nveltanque_mini.setVisible(false);
            luz_tanque_ok.setVisible(true);
            text_nveltanque_ok.setVisible(true);
        }
        if (barra2.getValue() == 36) {
            conmut = estacon;
        }
        if (barra2.getValue() <= 12) {
            luzniv_tanque_critico.setVisible(true);
            text_nveltanque_mini.setVisible(true);
            text_nveltanque_maxi.setVisible(false);
            luz_tanque_ok.setVisible(false);
            text_nveltanque_ok.setVisible(false);

            if (conmut == 1) {
                estacon = 0;
                if (barra2.getValue() == 10) {
                    segundo2.stop();
                    ctr.grabarAlarma(ALARMAS.NIVEL_MINIMO);
                    segundo2 = new Timer(ONE_SECOND2, new TimerListener3());
                    segundo2.start();
                    cont4 = 10;
                    barra2.setValue(cont4);
                }
            }
            if (conmut == 0) {
                estacon = 1;
                if (barra2.getValue() == 10) {
                    segundo2.stop();
                    ctr.grabarAlarma(ALARMAS.NIVEL_MINIMO);
                    segundo2 = new Timer(ONE_SECOND2, new TimerListener3());
                    segundo2.start();
                    cont4 = 10;
                    barra2.setValue(cont4);
                }
            }
        }
    }
}

class TimerListener3 implements ActionListener {

    public void actionPerformed(ActionEvent evt) {
        // ctr.grabarAlarma(ALARMAS.NIVEL_MINIMO);
        cont4++;
        if (cont4 < 85) {
            if (cont4 > 20) {
                luzniv_tanque_critico.setVisible(false);
                text_nveltanque_mini.setVisible(false);
                text_nveltanque_maxi.setVisible(false);
                luz_tanque_ok.setVisible(true);
                text_nveltanque_ok.setVisible(true);
            }
            barra2.setValue(cont4);
            if (conmut == 1) {
                //text_motor1_parada.setVisible(false);
                luz_motor1_marcha.setVisible(true);
                text_motor1_marcha.setVisible(true);
                animar_motor1();

            } else {
                //text_motor2_parada.setVisible(false);
                text_motor2_marcha.setVisible(true);
                luz_motor2_marcha.setVisible(true);
                animar_motor2();
                //JOptionPane.showMessageDialog(null,"Aqui graba motor 2");
            }
        } else {
            barra2.setValue(85);

            if (conmut == 1) {
                text_motor1_parada.setVisible(true);
                //text_motor1_marcha.setVisible(false);
                //luz_motor1_marcha.setVisible(false);
            } else {
                text_motor2_parada.setVisible(true);
                //text_motor2_marcha.setVisible(false);
                //luz_motor2_marcha.setVisible(false);

            }
            h = 0;
            m = 0;
            s = 0;
        }
        if (cont4 > 85) {
            luzniv_tanque_critico.setVisible(true);
            text_nveltanque_maxi.setVisible(true);
            text_nveltanque_mini.setVisible(false);
            luz_tanque_ok.setVisible(false);
            text_nveltanque_ok.setVisible(false);
        }
        if (cont4 == 90) {
            segundo2.stop();
            cont2 = 90;
            barra2.setValue(90);
            barra2.setStringPainted(true);
            ctr.grabarAlarma(ALARMAS.NIVEL_MAXIMO);
            segundo2 = new Timer(ONE_SECOND2, new TimerListener2());
            segundo2.start();
        }
    }
}

class TimerListener4 implements ActionListener {

    public void actionPerformed(ActionEvent evt) {
        //contR3 = cont5;
        //contR3--;
        if (motor2_on1.isSelected() == true) {
        cont5--;
        barra2.setValue(cont5);
        
        

                    //text_motor1_parada.setVisible(false);
                    luz_motor1_marcha.setVisible(true);
                    text_motor1_marcha.setVisible(true);
                    animarmotor3_manual();
        
        //barraReservorio3.setValue(contR3);
//        if(barra2.getValue() < 50){
//            contR3mas = 8;
//            timer2 = new Timer(500, new TimerBarraReservorio2Aumenta());
//            timer2.start();
//        }
        if (barra2.getValue() < 15) {
            luzniv_tanque_critico.setVisible(true);
            text_nveltanque_mini.setVisible(true);
            text_nveltanque_maxi.setVisible(false);
            luz_tanque_ok.setVisible(false);
            text_nveltanque_ok.setVisible(false);
            
            

            if (barra2.getValue() <= 8) {
                text_alarma.setVisible(true);
                luz_sondido_alarma.setVisible(true);
                luz_sistbombeo_ok.setVisible(false);
                text_sistBombeo.setVisible(false);
                motor1_on.setVisible(true);
                motor2_on.setVisible(true);
                motorReservorio3.setVisible(true);
                captura_hora();

                ctr.grabarAlarma(ALARMAS.NIVEL_MINIMO);
                
                //whasapt
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
                Message message = Message.creator(new PhoneNumber("+595971868353"),
                        new PhoneNumber("+16109475021"),
                        "Nivel  Minimo de Agua en el tanque, verifique por favor").create();

                System.out.println(message.getSid());

                labelA.setText("Whatsapp enviado");
                //fin del whasapt

                //eviar correo
                Properties propiedad = new Properties();
                propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
                propiedad.setProperty("mail.smtp.starttls.enable", "true");
                propiedad.setProperty("mail.smtp.port", "587");
                propiedad.setProperty("mail.smtp.auth", "true");

                Session sesion = Session.getDefaultInstance(propiedad);

                String correoEnvia = "monitoreoaviso65@gmail.com";
                String contrasena = "MonitoreoAviso65";
                String destinatario = correo;
                String asunto = "El nivel del agua está muy bajo";
                String mensaje = "Nivel  Minimo de Agua del Tanque Alcanzado";

                MimeMessage mail = new MimeMessage(sesion);

                try {
                    mail.setFrom(new InternetAddress(correoEnvia));
                    mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                    mail.setSubject(asunto);
                    mail.setText(mensaje);

                    Transport transporte = sesion.getTransport("smtp");
                    transporte.connect(correoEnvia, contrasena);
                    transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
                    transporte.close();

                    labelB.setText("Correo enviado");

                } catch (AddressException ex) {
                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MessagingException ex) {
                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                }
                //fin del correo
                 if(automatico == false){
                JOptionPane.showMessageDialog(null, "Nivel  Minimo de Agua del Tanque Alcanzado", "ATENCION ENCIEDA MOTOR CORRESPONDIENTE",
                        JOptionPane.INFORMATION_MESSAGE);
                 }
                segundo2.stop();
                
                contR3mas = 8;
                barraReservorio3.setStringPainted(false);
                timer2 = new Timer(300, new TimerBarraReservorio2Aumenta());
                timer2.start();
                
                if(automatico == true){
                    motor2_on1.setSelected(false);
                    motor1_on.setSelected(true);
                    cont6 = 8;
                    segundo2 = new Timer(ONE_SECOND2, new TimerListener5());
                    segundo2.start();
                    luz_sondido_alarma.setVisible(false);
                    text_alarma.setVisible(false);
                    luz_sistbombeo_ok.setVisible(true);
                    text_sistBombeo.setVisible(true);
                }
               

            }
        }
        
        
        }
    }
}

class TimerListener5 implements ActionListener {

    public void actionPerformed(ActionEvent evt) {
        
        
        if (motor1_on.isSelected() == true) {
        
        cont5++;
        barra2.setValue(cont5);
        barra2.setStringPainted(true);

        

            //text_motor1_parada.setVisible(false);
            luz_motor1_marcha.setVisible(true);
            //text_motor1_marcha.setVisible(true);
            animarmotor1_manual();
           
//            t = new Timer(10, acciones);
//            t.start();
            if (cont5 > 20) {
                luzniv_tanque_critico.setVisible(false);
                text_nveltanque_mini.setVisible(false);
                text_nveltanque_maxi.setVisible(false);
                luz_tanque_ok.setVisible(true);
                text_nveltanque_ok.setVisible(true);
            }

            if (cont5 == 70) {
                luzniv_tanque_critico.setVisible(true);
                text_nveltanque_maxi.setVisible(true);
                text_nveltanque_mini.setVisible(false);
                luz_tanque_ok.setVisible(false);
                text_nveltanque_ok.setVisible(false);
                
                //whasapt
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
                Message message = Message.creator(new PhoneNumber("+595971868353"),
                        new PhoneNumber("+16109475021"),
                        "El tanque llego al limite, verifique por favor").create();

                System.out.println(message.getSid());

                JOptionPane.showMessageDialog(null, "Whatsapp enviado");
                labelA.setText("Whatsapp enviado");

                //fin del whasapt
                //enviar correo
                Properties propiedad = new Properties();
                propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
                propiedad.setProperty("mail.smtp.starttls.enable", "true");
                propiedad.setProperty("mail.smtp.port", "587");
                propiedad.setProperty("mail.smtp.auth", "true");

                Session sesion = Session.getDefaultInstance(propiedad);

                String correoEnvia = "monitoreoaviso65@gmail.com";
                String contrasena = "MonitoreoAviso65";
                String destinatario = correo;
                String asunto = "El nivel del agua alcanzo su limite";
                String mensaje = "Nivel máximo del reservorio 2 . Apague el motor en 10 minutos";

                MimeMessage mail = new MimeMessage(sesion);

                try {
                    mail.setFrom(new InternetAddress(correoEnvia));
                    mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                    mail.setSubject(asunto);
                    mail.setText(mensaje);

                    Transport transporte = sesion.getTransport("smtp");
                    transporte.connect(correoEnvia, contrasena);
                    transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
                    transporte.close();

                    labelB.setText("Correo enviado");

                } catch (AddressException ex) {
                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MessagingException ex) {
                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                }
//                
                labelC.setText("Nivel de agua al limite");
               

            }
            if(cont5 == 80){
                if(automatico == true){
                    
                    motor1_on.setSelected(false);
                    segundo2.stop();
                }
            }
            if (cont5 == 90) {
                int contador = 1;
                luz_sondido_alarma.setVisible(true);
                luz_sistbombeo_ok.setVisible(false);
                text_alarma.setVisible(true);
                text_sistBombeo.setVisible(false);
                captura_hora();
                ctr.grabarAlarma(ALARMAS.NIVEL_MAXIMO);
                segundo2.setDelay(400);
                  //whasapt
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
                Message message = Message.creator(new PhoneNumber("+595971868353"),
                        new PhoneNumber("+16109475021"),
                        "Nivel del agua superó el limite, verifique por favor").create();

                System.out.println(message.getSid());
                
                labelA.setText("Whatsapp enviado");
                


                //fin del whasapt
                //enviar correo
                Properties propiedad = new Properties();
                propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
                propiedad.setProperty("mail.smtp.starttls.enable", "true");
                propiedad.setProperty("mail.smtp.port", "587");
                propiedad.setProperty("mail.smtp.auth", "true");

                Session sesion = Session.getDefaultInstance(propiedad);

                String correoEnvia = "monitoreoaviso65@gmail.com";
                String contrasena = "MonitoreoAviso65";
                String destinatario = correo;
                String asunto = "El nivel del agua alcanzo su limite";
                String mensaje = "Nivel del agua superó el limite de agua";

                MimeMessage mail = new MimeMessage(sesion);

                try {
                    mail.setFrom(new InternetAddress(correoEnvia));
                    mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                    mail.setSubject(asunto);
                    mail.setText(mensaje);

                    Transport transporte = sesion.getTransport("smtp");
                    transporte.connect(correoEnvia, contrasena);
                    transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
                    transporte.close();

                   labelB.setText("correo enviado");

                } catch (AddressException ex) {
                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MessagingException ex) {
                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                  if(automatico == false){
                  JOptionPane.showMessageDialog(null, "Supero el Nivel Máximo del reservorio ", "Atencion Apague Motor",
                        JOptionPane.INFORMATION_MESSAGE);
                  }
               
                 // t.stop();
                
            }
            
        }
       
    }
        

}

class TimerListener6 implements ActionListener {

    public void actionPerformed(ActionEvent evt) {
        cont6++;
        barra2.setValue(cont6);
        barra2.setStringPainted(true);
        if (motor2_on.isSelected() == true) {

            //text_motor2_parada.setVisible(false);
            luz_motor2_marcha.setVisible(true);
            text_motor2_marcha.setVisible(true);
            animarmotor2_manual();

            if (cont6 > 20) {
                luzniv_tanque_critico.setVisible(false);
                text_nveltanque_mini.setVisible(false);
                text_nveltanque_maxi.setVisible(false);
                luz_tanque_ok.setVisible(true);
                text_nveltanque_ok.setVisible(true);
            }
            if (cont6 < 85) {
                luzniv_tanque_critico.setVisible(false);
                text_nveltanque_maxi.setVisible(false);
                text_nveltanque_mini.setVisible(false);
                luz_tanque_ok.setVisible(true);
                text_nveltanque_ok.setVisible(true);
            }
            if (cont6 > 85) {
                luzniv_tanque_critico.setVisible(true);
                text_nveltanque_maxi.setVisible(true);
                text_nveltanque_mini.setVisible(false);
                luz_tanque_ok.setVisible(false);
                text_nveltanque_ok.setVisible(false);
            }

            if (cont6 == 90) {
                int contador = 1;
                luz_sondido_alarma.setVisible(true);
                luz_sistbombeo_ok.setVisible(false);
                text_alarma.setVisible(true);
                text_sistBombeo.setVisible(false);
                autonumerico_alarmas();
                captura_hora();
                ctr.grabarAlarma(ALARMAS.NIVEL_MAXIMO,2);
                //correo motor2
//                Properties propiedad = new Properties();
//                propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
//                propiedad.setProperty("mail.smtp.starttls.enable", "true");
//                propiedad.setProperty("mail.smtp.port", "587");
//                propiedad.setProperty("mail.smtp.auth", "true");
//
//                Session sesion = Session.getDefaultInstance(propiedad);
//
//                String correoEnvia = "monitoreoaviso65@gmail.com";
//                String contrasena = "MonitoreoAviso65";
//                String destinatario = correo;
//                String asunto = "El nivel del agua alcanzo su limite";
//                String mensaje = "Nivel  Maximo de Agua del Tanque Alcanzado";
//
//                MimeMessage mail = new MimeMessage(sesion);
//
//                try {
//                    mail.setFrom(new InternetAddress(correoEnvia));
//                    mail.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
//                    mail.setSubject(asunto);
//                    mail.setText(mensaje);
//
//                    Transport transporte = sesion.getTransport("smtp");
//                    transporte.connect(correoEnvia, contrasena);
//                    transporte.sendMessage(mail, mail.getRecipients(javax.mail.Message.RecipientType.TO));
//                    transporte.close();

                    labelR2B.setText( "Correo enviado");

//                } catch (AddressException ex) {
//                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (MessagingException ex) {
//                    Logger.getLogger(MonitoreoCentral.class.getName()).log(Level.SEVERE, null, ex);
//                }//fin del correo
                 if(automatico == false){
                JOptionPane.showMessageDialog(null, "Nivel Maximo de Agua del Tanque Alcanzado", "Atencion Apague Motor", JOptionPane.INFORMATION_MESSAGE);
                 }
            }
        } else {
            h = 0;
            m = 0;
            s = 0;
        }
    }
}

/**
 * This method is called from within the constructor to initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is always
 * regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        barra2 = new javax.swing.JProgressBar();
        motor1 = new javax.swing.JLabel();
        text_motor1_marcha = new javax.swing.JLabel();
        text_motor1_parada = new javax.swing.JLabel();
        motorR2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        contmotor1 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        contmotor4 = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        contmotor2 = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        luz_motor1_marcha = new javax.swing.JLabel();
        luz_motor1_parada = new javax.swing.JLabel();
        text_motor2_marcha = new javax.swing.JLabel();
        text_motor2_parada = new javax.swing.JLabel();
        luz_motor2_marcha = new javax.swing.JLabel();
        luz_motor2_parada = new javax.swing.JLabel();
        luz_sondido_alarma = new javax.swing.JLabel();
        luz_sistbombeo_ok = new javax.swing.JLabel();
        text_sistBombeo = new javax.swing.JLabel();
        text_alarma = new javax.swing.JLabel();
        luzniv_tanque_critico = new javax.swing.JLabel();
        operador_planta = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        parada_emergencia = new javax.swing.JLabel();
        text_nveltanque_ok = new javax.swing.JLabel();
        luz_tanque_ok = new javax.swing.JLabel();
        text_nveltanque_mini = new javax.swing.JLabel();
        text_nveltanque_maxi = new javax.swing.JLabel();
        text_modalidad = new javax.swing.JLabel();
        luz_modalida = new javax.swing.JLabel();
        luz_aviso_emitido = new javax.swing.JLabel();
        luz_aviso_ok = new javax.swing.JLabel();
        text_aviso_ok = new javax.swing.JLabel();
        text_aviso_encendido = new javax.swing.JLabel();
        opcion_automatico = new javax.swing.JRadioButton();
        opcion_manual = new javax.swing.JRadioButton();
        motor1_on = new javax.swing.JRadioButton();
        motor2_on = new javax.swing.JRadioButton();
        parada_emer = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        desligado = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        textalarma = new javax.swing.JTextField();
        txtfecha = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        motorReservorio3 = new javax.swing.JLabel();
        motor2_on1 = new javax.swing.JRadioButton();
        motor5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        contmotor3 = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        barra3 = new javax.swing.JProgressBar();
        barraReservorio2 = new javax.swing.JProgressBar();
        labelC = new javax.swing.JLabel();
        labelA = new javax.swing.JLabel();
        labelB = new javax.swing.JLabel();
        barraReservorio3 = new javax.swing.JProgressBar();
        jTextField1 = new javax.swing.JTextField();
        txtbarra2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        labelR2A = new javax.swing.JLabel();
        labelR2B = new javax.swing.JLabel();
        labelR2C = new javax.swing.JLabel();
        labelR3A = new javax.swing.JLabel();
        labelR3C = new javax.swing.JLabel();
        labelR3B = new javax.swing.JLabel();
        text_motor2_marcha1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        text_motor1_parada1 = new javax.swing.JLabel();

        jLabel7.setText("jLabel7");

        jLabel11.setText("jLabel11");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 960, 100, 100);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(1967, 960, 100, 100);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(131, 1320, 100, 100);

        barra2.setBackground(new java.awt.Color(0, 0, 0));
        barra2.setForeground(new java.awt.Color(0, 102, 255));
        barra2.setMaximum(110);
        barra2.setMinimum(1);
        barra2.setOrientation(1);
        barra2.setBorderPainted(false);
        barra2.setString("");
        barra2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                barra2StateChanged(evt);
            }
        });
        getContentPane().add(barra2);
        barra2.setBounds(590, 120, 80, 110);

        motor1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/motor1_encendido.png"))); // NOI18N
        getContentPane().add(motor1);
        motor1.setBounds(510, 210, 50, 70);

        text_motor1_marcha.setBackground(java.awt.Color.red);
        text_motor1_marcha.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_motor1_marcha.setForeground(new java.awt.Color(51, 255, 0));
        text_motor1_marcha.setText("Motor Nº 1 Encendido");
        getContentPane().add(text_motor1_marcha);
        text_motor1_marcha.setBounds(70, 400, 169, 18);

        text_motor1_parada.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_motor1_parada.setForeground(java.awt.Color.yellow);
        text_motor1_parada.setText("Motor Nº 1 Parado");
        getContentPane().add(text_motor1_parada);
        text_motor1_parada.setBounds(70, 370, 155, 18);

        motorR2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/motor2_marcha.png"))); // NOI18N
        getContentPane().add(motorR2);
        motorR2.setBounds(520, 440, 44, 70);

        jPanel5.setLayout(null);

        contmotor1.setEditable(false);
        contmotor1.setBackground(new java.awt.Color(204, 204, 204));
        contmotor1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        contmotor1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contmotor1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel5.add(contmotor1);
        contmotor1.setBounds(0, 25, 119, 25);
        jPanel5.add(jSeparator2);
        jSeparator2.setBounds(10, 20, 100, 2);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setText("Ultimo Trabajo M1 ");
        jPanel5.add(jLabel4);
        jLabel4.setBounds(10, 0, 110, 22);

        jPanel8.setLayout(null);

        contmotor4.setEditable(false);
        contmotor4.setBackground(new java.awt.Color(204, 204, 204));
        contmotor4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        contmotor4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contmotor4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel8.add(contmotor4);
        contmotor4.setBounds(0, 25, 119, 25);
        jPanel8.add(jSeparator6);
        jSeparator6.setBounds(10, 20, 100, 2);

        jLabel10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel10.setText("Ultimo Trabajo M1 ");
        jPanel8.add(jLabel10);
        jLabel10.setBounds(10, 0, 110, 22);

        jPanel5.add(jPanel8);
        jPanel8.setBounds(430, 90, 120, 50);

        getContentPane().add(jPanel5);
        jPanel5.setBounds(430, 90, 120, 50);

        jPanel6.setLayout(null);

        contmotor2.setEditable(false);
        contmotor2.setBackground(new java.awt.Color(153, 153, 153));
        contmotor2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        contmotor2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contmotor2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel6.add(contmotor2);
        contmotor2.setBounds(0, 25, 120, 25);
        jPanel6.add(jSeparator3);
        jSeparator3.setBounds(10, 20, 100, 2);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setText("Ultimo Trabajo M2 ");
        jPanel6.add(jLabel5);
        jLabel5.setBounds(10, 0, 110, 22);

        getContentPane().add(jPanel6);
        jPanel6.setBounds(490, 560, 120, 50);

        luz_motor1_marcha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_marcha.png"))); // NOI18N
        getContentPane().add(luz_motor1_marcha);
        luz_motor1_marcha.setBounds(280, 440, 44, 40);

        luz_motor1_parada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_parada.png"))); // NOI18N
        getContentPane().add(luz_motor1_parada);
        luz_motor1_parada.setBounds(280, 440, 40, 40);

        text_motor2_marcha.setBackground(java.awt.Color.red);
        text_motor2_marcha.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_motor2_marcha.setForeground(new java.awt.Color(51, 255, 0));
        text_motor2_marcha.setText("Motor Nº 2 Encendido");
        getContentPane().add(text_motor2_marcha);
        text_motor2_marcha.setBounds(60, 470, 169, 20);

        text_motor2_parada.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_motor2_parada.setForeground(java.awt.Color.yellow);
        text_motor2_parada.setText("Motor Nº 2 Parado");
        getContentPane().add(text_motor2_parada);
        text_motor2_parada.setBounds(70, 440, 155, 18);

        luz_motor2_marcha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_marcha.png"))); // NOI18N
        getContentPane().add(luz_motor2_marcha);
        luz_motor2_marcha.setBounds(280, 530, 44, 40);

        luz_motor2_parada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_parada.png"))); // NOI18N
        getContentPane().add(luz_motor2_parada);
        luz_motor2_parada.setBounds(280, 530, 40, 40);

        luz_sondido_alarma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/sonido4.png"))); // NOI18N
        getContentPane().add(luz_sondido_alarma);
        luz_sondido_alarma.setBounds(260, 170, 71, 64);

        luz_sistbombeo_ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_parada.png"))); // NOI18N
        getContentPane().add(luz_sistbombeo_ok);
        luz_sistbombeo_ok.setBounds(280, 190, 40, 40);

        text_sistBombeo.setBackground(java.awt.Color.yellow);
        text_sistBombeo.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_sistBombeo.setForeground(java.awt.Color.yellow);
        text_sistBombeo.setText("Sistema de Bombeo  OK");
        getContentPane().add(text_sistBombeo);
        text_sistBombeo.setBounds(50, 180, 180, 18);

        text_alarma.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_alarma.setForeground(java.awt.Color.red);
        text_alarma.setText("Alarma Encendida");
        getContentPane().add(text_alarma);
        text_alarma.setBounds(60, 180, 160, 18);

        luzniv_tanque_critico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_encendido.png"))); // NOI18N
        getContentPane().add(luzniv_tanque_critico);
        luzniv_tanque_critico.setBounds(280, 610, 40, 40);

        operador_planta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/operador_planta.png"))); // NOI18N
        getContentPane().add(operador_planta);
        operador_planta.setBounds(20, 600, 59, 120);
        getContentPane().add(jSeparator5);
        jSeparator5.setBounds(40, 560, 240, 10);

        parada_emergencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/parada_emergencia3.png"))); // NOI18N
        parada_emergencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parada_emergenciaMouseClicked(evt);
            }
        });
        getContentPane().add(parada_emergencia);
        parada_emergencia.setBounds(100, 20, 90, 100);

        text_nveltanque_ok.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        text_nveltanque_ok.setForeground(java.awt.Color.yellow);
        text_nveltanque_ok.setText("Nivel de Agua del Tanque OK");
        getContentPane().add(text_nveltanque_ok);
        text_nveltanque_ok.setBounds(20, 500, 210, 20);

        luz_tanque_ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_parada.png"))); // NOI18N
        getContentPane().add(luz_tanque_ok);
        luz_tanque_ok.setBounds(280, 610, 60, 50);

        text_nveltanque_mini.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        text_nveltanque_mini.setForeground(java.awt.Color.red);
        text_nveltanque_mini.setText("Nivel Minimo del Tanque.");
        getContentPane().add(text_nveltanque_mini);
        text_nveltanque_mini.setBounds(30, 500, 180, 20);

        text_nveltanque_maxi.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        text_nveltanque_maxi.setForeground(java.awt.Color.red);
        text_nveltanque_maxi.setText("Nivel Maximo del Tanque.");
        getContentPane().add(text_nveltanque_maxi);
        text_nveltanque_maxi.setBounds(30, 500, 180, 20);

        text_modalidad.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_modalidad.setForeground(java.awt.Color.yellow);
        text_modalidad.setText("Modalidad de Bombeo");
        getContentPane().add(text_modalidad);
        text_modalidad.setBounds(60, 270, 170, 18);

        luz_modalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_parada.png"))); // NOI18N
        getContentPane().add(luz_modalida);
        luz_modalida.setBounds(280, 310, 120, 140);

        luz_aviso_emitido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/foco_aviso2.png"))); // NOI18N
        getContentPane().add(luz_aviso_emitido);
        luz_aviso_emitido.setBounds(270, 270, 60, 60);

        luz_aviso_ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/luz_señal_parada.png"))); // NOI18N
        getContentPane().add(luz_aviso_ok);
        luz_aviso_ok.setBounds(280, 270, 47, 40);

        text_aviso_ok.setBackground(java.awt.Color.yellow);
        text_aviso_ok.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_aviso_ok.setForeground(java.awt.Color.yellow);
        text_aviso_ok.setText("Aviso por Mant. Inactivo");
        getContentPane().add(text_aviso_ok);
        text_aviso_ok.setBounds(40, 240, 180, 18);

        text_aviso_encendido.setBackground(java.awt.Color.yellow);
        text_aviso_encendido.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_aviso_encendido.setForeground(java.awt.Color.red);
        text_aviso_encendido.setText("Aviso por Mant. Activado");
        getContentPane().add(text_aviso_encendido);
        text_aviso_encendido.setBounds(40, 238, 180, 18);

        opcion_automatico.setBackground(new java.awt.Color(255, 255, 0));
        opcion_automatico.setText("Automatico");
        opcion_automatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcion_automaticoActionPerformed(evt);
            }
        });
        getContentPane().add(opcion_automatico);
        opcion_automatico.setBounds(120, 310, 100, 23);

        opcion_manual.setBackground(new java.awt.Color(255, 255, 0));
        opcion_manual.setText("Manual");
        opcion_manual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcion_manualActionPerformed(evt);
            }
        });
        getContentPane().add(opcion_manual);
        opcion_manual.setBounds(50, 310, 70, 23);

        motor1_on.setBackground(new java.awt.Color(204, 204, 204));
        motor1_on.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        motor1_on.setText("Motor Nro. 1");
        motor1_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motor1_onActionPerformed(evt);
            }
        });
        getContentPane().add(motor1_on);
        motor1_on.setBounds(430, 150, 100, 21);

        motor2_on.setBackground(new java.awt.Color(204, 204, 204));
        motor2_on.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        motor2_on.setText("Motor Nro. 2");
        motor2_on.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motor2_onActionPerformed(evt);
            }
        });
        getContentPane().add(motor2_on);
        motor2_on.setBounds(490, 530, 100, 21);

        parada_emer.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        parada_emer.setText("Parada de Emergencia");
        parada_emer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parada_emerActionPerformed(evt);
            }
        });
        getContentPane().add(parada_emer);
        parada_emer.setBounds(50, 110, 210, 30);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel1.setText("Reportes Operatividad de la Planta");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(40, 560, 230, 30);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 255));
        jLabel8.setText("ESSAP");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(220, 20, 120, 80);

        desligado.setBackground(java.awt.Color.yellow);
        desligado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        desligado.setText("Inactivo");
        desligado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desligadoActionPerformed(evt);
            }
        });
        getContentPane().add(desligado);
        desligado.setBounds(90, 290, 80, 20);

        jButton1.setText("Ver Informe Caudal");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(90, 660, 160, 30);

        textalarma.setText("jTextField1");
        textalarma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textalarmaActionPerformed(evt);
            }
        });
        getContentPane().add(textalarma);
        textalarma.setBounds(1180, 680, 60, 20);

        txtfecha.setText("jTextField1");
        getContentPane().add(txtfecha);
        txtfecha.setBounds(1200, 690, 60, 20);

        jButton2.setText("Ver Informe Alarmas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(90, 610, 170, 30);

        motorReservorio3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/motor1_encendido.png"))); // NOI18N
        motorReservorio3.setText("jLabel9");
        getContentPane().add(motorReservorio3);
        motorReservorio3.setBounds(770, 250, 82, 80);

        motor2_on1.setBackground(new java.awt.Color(204, 204, 204));
        motor2_on1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        motor2_on1.setText("Motor Nro. 2");
        motor2_on1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                motor2_on1MousePressed(evt);
            }
        });
        motor2_on1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motor2_on1ActionPerformed(evt);
            }
        });
        getContentPane().add(motor2_on1);
        motor2_on1.setBounds(780, 200, 100, 21);

        motor5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/motor1_encendido.png"))); // NOI18N
        getContentPane().add(motor5);
        motor5.setBounds(510, 210, 50, 70);

        jPanel7.setLayout(null);

        contmotor3.setEditable(false);
        contmotor3.setBackground(new java.awt.Color(153, 153, 153));
        contmotor3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        contmotor3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contmotor3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel7.add(contmotor3);
        contmotor3.setBounds(0, 20, 120, 25);
        jPanel7.add(jSeparator4);
        jSeparator4.setBounds(10, 20, 100, 2);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setText("Ultimo Trabajo M2 ");
        jPanel7.add(jLabel9);
        jLabel9.setBounds(10, 0, 110, 22);

        getContentPane().add(jPanel7);
        jPanel7.setBounds(770, 140, 120, 50);

        barra3.setBackground(new java.awt.Color(0, 0, 0));
        barra3.setForeground(new java.awt.Color(0, 102, 255));
        barra3.setMinimum(1);
        barra3.setOrientation(1);
        barra3.setBorderPainted(false);
        barra3.setString("");
        barra3.setStringPainted(true);
        getContentPane().add(barra3);
        barra3.setBounds(390, 360, 80, 110);

        barraReservorio2.setMaximum(110);
        barraReservorio2.setMinimum(1);
        barraReservorio2.setBorderPainted(false);
        barraReservorio2.setVerifyInputWhenFocusTarget(false);
        getContentPane().add(barraReservorio2);
        barraReservorio2.setBounds(606, 380, 100, 110);

        labelC.setBackground(new java.awt.Color(255, 255, 255));
        labelC.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelC.setForeground(new java.awt.Color(204, 0, 0));
        labelC.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelC);
        labelC.setBounds(1080, 150, 240, 30);

        labelA.setBackground(new java.awt.Color(255, 255, 255));
        labelA.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelA.setForeground(new java.awt.Color(255, 0, 0));
        labelA.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelA);
        labelA.setBounds(1080, 60, 240, 30);

        labelB.setBackground(new java.awt.Color(255, 255, 255));
        labelB.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelB.setForeground(new java.awt.Color(255, 0, 0));
        labelB.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelB);
        labelB.setBounds(1080, 100, 240, 30);

        barraReservorio3.setMinimum(1);
        barraReservorio3.setBorderPainted(false);
        barraReservorio3.setVerifyInputWhenFocusTarget(false);
        getContentPane().add(barraReservorio3);
        barraReservorio3.setBounds(926, 440, 110, 100);

        jTextField1.setBackground(new java.awt.Color(255, 0, 0));
        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("Mensaje Reservorio tres");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(1070, 410, 200, 28);

        txtbarra2.setBackground(new java.awt.Color(51, 153, 255));
        txtbarra2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtbarra2.setForeground(new java.awt.Color(255, 0, 51));
        txtbarra2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Monitoreo_central_oficial.jpg"))); // NOI18N
        getContentPane().add(txtbarra2);
        txtbarra2.setBounds(0, -40, 1070, 810);

        jTextField2.setBackground(new java.awt.Color(255, 0, 0));
        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setText("Mensaje Reservorio uno");
        getContentPane().add(jTextField2);
        jTextField2.setBounds(1070, 20, 200, 28);

        jTextField3.setBackground(new java.awt.Color(255, 0, 0));
        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.setText("Mensaje Reservorio dos");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField3);
        jTextField3.setBounds(1070, 200, 200, 28);

        labelR2A.setBackground(new java.awt.Color(255, 255, 255));
        labelR2A.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelR2A.setForeground(new java.awt.Color(255, 0, 0));
        labelR2A.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelR2A);
        labelR2A.setBounds(1080, 250, 240, 30);

        labelR2B.setBackground(new java.awt.Color(255, 255, 255));
        labelR2B.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelR2B.setForeground(new java.awt.Color(255, 0, 0));
        labelR2B.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelR2B);
        labelR2B.setBounds(1080, 290, 240, 30);

        labelR2C.setBackground(new java.awt.Color(255, 255, 255));
        labelR2C.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelR2C.setForeground(new java.awt.Color(204, 0, 0));
        labelR2C.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelR2C);
        labelR2C.setBounds(1080, 340, 240, 30);

        labelR3A.setBackground(new java.awt.Color(255, 255, 255));
        labelR3A.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelR3A.setForeground(new java.awt.Color(255, 0, 0));
        labelR3A.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelR3A);
        labelR3A.setBounds(1080, 480, 240, 30);

        labelR3C.setBackground(new java.awt.Color(255, 255, 255));
        labelR3C.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelR3C.setForeground(new java.awt.Color(204, 0, 0));
        labelR3C.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelR3C);
        labelR3C.setBounds(1080, 570, 240, 30);

        labelR3B.setBackground(new java.awt.Color(255, 255, 255));
        labelR3B.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelR3B.setForeground(new java.awt.Color(255, 0, 0));
        labelR3B.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        getContentPane().add(labelR3B);
        labelR3B.setBounds(1080, 520, 240, 30);

        text_motor2_marcha1.setBackground(java.awt.Color.red);
        text_motor2_marcha1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_motor2_marcha1.setForeground(new java.awt.Color(51, 255, 0));
        text_motor2_marcha1.setText("Motor Nº 2 Encendido");
        getContentPane().add(text_motor2_marcha1);
        text_motor2_marcha1.setBounds(50, 420, 169, 20);

        jLabel6.setText("jLabel6");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(960, 480, 34, 14);

        text_motor1_parada1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        text_motor1_parada1.setForeground(java.awt.Color.yellow);
        text_motor1_parada1.setText("Motor Nº 1 Parado");
        getContentPane().add(text_motor1_parada1);
        text_motor1_parada1.setBounds(60, 370, 155, 18);

        setSize(new java.awt.Dimension(1342, 747));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    }//GEN-LAST:event_formWindowOpened

    private void opcion_manualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcion_manualActionPerformed
        if (opcion_manual.isSelected() == true) {
            opcion_automatico.setSelected(false);
            opcion_automatico.setEnabled(true);
            desligado.setSelected(false);
            motor2_on1.setVisible(true);
            //   motor1_on.setVisible(true);
            //   motor2_on.setVisible(true);
            automatico = false;
            cont5 = 90;
            contR3 = 90;
            cont7 = 100;
            contR3mas = 1;
            barra2.setValue(90);
            barra2.setStringPainted(true);
            barraReservorio3.setValue(90);
            barraReservorio3.setStringPainted(false);
            timer1 = new Timer(600 , new TimerBarraReservorio2());
            timer1.start();
            barraReservorio2.setValue(90);
            barraReservorio2.setStringPainted(false);
            timer3 = new Timer(500 , new TimerBarraReservorio3());
            timer3.start();
        
           
            
           
        } else {
            timer1.stop();
            segundo2.stop();
            timer3.stop();
        }

    }//GEN-LAST:event_opcion_manualActionPerformed

    private void opcion_automaticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcion_automaticoActionPerformed
        if (opcion_automatico.isSelected() == true) {
            opcion_manual.setSelected(false);
            opcion_manual.setEnabled(true);
            desligado.setSelected(false);
//                cont = 70;
//                barra.setValue(70);
//                barra.setStringPainted(true);
//                segundo = new Timer(ONE_SECOND, new TimerListener());
            automatico = true;
            cont5 = 90;
            contR3 = 90;
            cont7 = 100;
            contR3mas = 1;
            barra2.setValue(90);
            barra2.setStringPainted(true);
            barraReservorio3.setValue(90);
            barraReservorio3.setStringPainted(true);
            timer1 = new Timer(600 , new TimerBarraReservorio2());
            timer1.start();
            barraReservorio2.setValue(90);
            barraReservorio2.setStringPainted(true);
            timer3 = new Timer(600 , new TimerBarraReservorio3());
            timer3.start();
        } else {
            segundo2.stop();
            timer1.stop();
            timer3.stop();
        }
    }//GEN-LAST:event_opcion_automaticoActionPerformed

    private void motor1_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motor1_onActionPerformed

        motor2_on.setSelected(false);
        String codu;
        if (motor1_on.isSelected() == true) {
            //cont6 = cont5;
            segundo2 = new Timer(600, new TimerListener5());
            segundo2.start();
            luz_sondido_alarma.setVisible(false);
            text_alarma.setVisible(false);
            luz_sistbombeo_ok.setVisible(true);
            text_sistBombeo.setVisible(true);
             //Encendemos motor
            text_motor1_parada.setForeground(Color.GREEN);
            text_motor1_parada.setText("Motor 1 . Encendido");
            text_motor1_parada.setVisible(true);
        } else {
            segundo2.stop();
            motor1.setVisible(false);
            luz_sondido_alarma.setVisible(false);
            luz_sistbombeo_ok.setVisible(true);
            text_alarma.setVisible(false);
            text_sistBombeo.setVisible(true);
//            text_motor1_marcha.setVisible(false);
//            text_motor1_parada.setVisible(true);
//            luz_motor1_marcha.setVisible(false);
//            luz_motor1_parada.setVisible(true);
            text_nveltanque_maxi.setVisible(false);
            text_nveltanque_ok.setVisible(true);
            luzniv_tanque_critico.setVisible(false);
             //Apagamos motor
            text_motor1_parada.setForeground(Color.YELLOW);
            text_motor1_parada.setText("Motor 1 . Apagado");
            text_motor1_parada.setVisible(true);
            JOptionPane.showMessageDialog(null, "Motor Nro 2 Parado");

            int horacu = h + h;

            if (banbomb == 0) {
                autonumericobombeo();
                banbomb = 1;
                op = "A";
            } else {
                op = "M";
            }

            captura_hora();

            h = 0;
            m = 0;
            s = 0;

        }
    }//GEN-LAST:event_motor1_onActionPerformed

    private void motor2_onActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motor2_onActionPerformed
        motor1_on.setSelected(false);
        if (motor2_on.isSelected() == true) {
            barraReservorio2.setStringPainted(false);
            timer3 = new Timer(600, new TimerBarraReservorio3Aumenta());
            timer3.start();
            luz_sondido_alarma.setVisible(false);
            text_alarma.setVisible(false);
            luz_sistbombeo_ok.setVisible(true);
            text_sistBombeo.setVisible(true);
            text_motor1_marcha.setForeground(Color.GREEN);
            text_motor1_marcha.setText("Motor 2 . Encendido");
            text_motor1_marcha.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Motor Nro 2 Parado");
            //segundo2.stop();
            timer3.stop();
            motorR2.setVisible(false);
            luz_sondido_alarma.setVisible(false);
            luz_sistbombeo_ok.setVisible(true);
            text_alarma.setVisible(false);
            text_sistBombeo.setVisible(true);
//            text_motor2_marcha.setVisible(false);
//            text_motor2_parada.setVisible(true);
//            luz_motor2_marcha.setVisible(false);
//            luz_motor2_parada.setVisible(true);
            text_nveltanque_maxi.setVisible(false);
            text_nveltanque_ok.setVisible(true);
            luzniv_tanque_critico.setVisible(false);
            //apagamos motor
             text_motor1_marcha.setForeground(Color.YELLOW);
            text_motor1_marcha.setText("Motor 2 . Apagado");
            text_motor1_marcha.setVisible(true);
        }
    }//GEN-LAST:event_motor2_onActionPerformed

    private void parada_emergenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_parada_emergenciaMouseClicked
        parada_emer.setSelected(true);
    }//GEN-LAST:event_parada_emergenciaMouseClicked

    private void parada_emerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parada_emerActionPerformed
        if (parada_emer.isSelected() == true) {
            ctr.grabarAlarma(ALARMAS.PARADA_EMERGENCIA);
            opcion_automatico.setSelected(false);
            opcion_manual.setSelected(false);
            //segundo.stop();
            //segundo2.stop();
              //segundo.stop();
        
              segundo2.stop();
              
              //segundo3.stop();
        
            timer1.stop();
            //timer2.stop();
            timer3.stop();
            motor1.setVisible(false);
            motorR2.setVisible(false);
            text_motor1_marcha.setVisible(false);
            text_motor2_marcha.setVisible(false);
            luz_motor1_marcha.setVisible(false);
            luz_motor2_marcha.setVisible(false);
            JOptionPane.showMessageDialog(null, "Parada de Emergencia Activada");
        } else {
        }
    }//GEN-LAST:event_parada_emerActionPerformed

    private void desligadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desligadoActionPerformed
        if (desligado.isSelected() == true) {
            motor1_on.setSelected(false);
            motor2_on.setSelected(false);
            motor1_on.setVisible(false);
            motor2_on.setVisible(false);
            motor2_on1.setVisible(false);
            motor1.setVisible(false);
            motorR2.setVisible(false);
            text_motor1_marcha.setVisible(false);
            text_motor2_marcha.setVisible(false);
            luz_motor1_marcha.setVisible(false);
            luz_motor2_marcha.setVisible(false);
            opcion_automatico.setSelected(false);
            opcion_manual.setSelected(false);
            opcion_manual.setEnabled(true);
            text_motor1_parada.setVisible(true);
            text_motor2_parada.setVisible(true);
            opcion_automatico.setEnabled(true);
            segundo2.stop();
            
           
            //segundo3.stop();
            

            timer1.stop();
            timer3.stop();
            timer2.stop();
            
            
            labelA.setText("");
            labelB.setText("");
            labelC.setText("");
            labelR2A.setText("");
            labelR2B.setText("");
            labelR2C.setText("");
            labelR3A.setText("");
            labelR3B.setText("");
            labelR3C.setText("");
            cont5 = 90;
            contR3 = 90;
            cont7 = 90;
            contR3mas = 1;
            barra2.setValue(90);
            barra2.setStringPainted(true);
            barraReservorio3.setValue(90);
            barraReservorio3.setStringPainted(true);
            barraReservorio2.setValue(90);
            barraReservorio2.setStringPainted(true);
        } else {
        }
    }//GEN-LAST:event_desligadoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ctr.verReporteCaudal();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void textalarmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textalarmaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textalarmaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AlarmasEmitidas a = new AlarmasEmitidas(this, false);
        a.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void motor2_on1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motor2_on1ActionPerformed
        //motor2_on1.setSelected(false);
        String codu;
        if (motor2_on1.isSelected() == true && motor1_on.isSelected() == false) {
//            cont6 = 8;
//            cont5 = 90;
            segundo2 = new Timer(600, new TimerListener4());
            segundo2.start();
            timer2 = new Timer(300, new TimerBarraReservorio2Aumenta());
            timer2.start();
            luz_sondido_alarma.setVisible(false);
            text_alarma.setVisible(false);
            luz_sistbombeo_ok.setVisible(true);
            text_sistBombeo.setVisible(true);
             //Encendemos motor
            text_motor2_parada.setForeground(Color.GREEN);
            text_motor2_parada.setText("Motor 3 . Encendido");
            text_motor2_parada.setVisible(true);
        } else {
            segundo2.stop();
            motor1.setVisible(false);
            luz_sondido_alarma.setVisible(false);
            luz_sistbombeo_ok.setVisible(true);
            text_alarma.setVisible(false);
            text_sistBombeo.setVisible(true);
//            text_motor1_marcha.setVisible(false);
//            text_motor1_parada.setVisible(true);
//            luz_motor1_marcha.setVisible(false);
//            luz_motor1_parada.setVisible(true);
            text_nveltanque_maxi.setVisible(false);
            text_nveltanque_ok.setVisible(true);
            luzniv_tanque_critico.setVisible(false);
             //Apagamos motor
            text_motor2_parada.setForeground(Color.YELLOW);
            text_motor2_parada.setText("Motor 3 . Apagado");
            text_motor2_parada.setVisible(true);
            //bombeamos agua al reservorio 3
//             if (barra2.getValue() > 8) {
//            contR3mas = 8;
//            barraReservorio3.setStringPainted(false);
//            timer2 = new Timer(300, new TimerBarraReservorio2Aumenta());
//            timer2.start();
//             }
            
            JOptionPane.showMessageDialog(null, "Motor Nro 3 Parado");

            int horacu = h + h;

            if (banbomb == 0) {
                autonumericobombeo();
                banbomb = 1;
                op = "A";
            } else {
                op = "M";
            }

            captura_hora();

            h = 0;
            m = 0;
            s = 0;

        }
    }//GEN-LAST:event_motor2_on1ActionPerformed

    private void barra2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_barra2StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_barra2StateChanged

    private void motor2_on1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_motor2_on1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_motor2_on1MousePressed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MonitoreoCentral().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barra2;
    private javax.swing.JProgressBar barra3;
    private javax.swing.JProgressBar barraReservorio2;
    private javax.swing.JProgressBar barraReservorio3;
    private javax.swing.JTextField contmotor1;
    private javax.swing.JTextField contmotor2;
    private javax.swing.JTextField contmotor3;
    private javax.swing.JTextField contmotor4;
    private javax.swing.JRadioButton desligado;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel labelA;
    private javax.swing.JLabel labelB;
    private javax.swing.JLabel labelC;
    private javax.swing.JLabel labelR2A;
    private javax.swing.JLabel labelR2B;
    private javax.swing.JLabel labelR2C;
    private javax.swing.JLabel labelR3A;
    private javax.swing.JLabel labelR3B;
    private javax.swing.JLabel labelR3C;
    private javax.swing.JLabel luz_aviso_emitido;
    private javax.swing.JLabel luz_aviso_ok;
    private javax.swing.JLabel luz_modalida;
    private javax.swing.JLabel luz_motor1_marcha;
    private javax.swing.JLabel luz_motor1_parada;
    private javax.swing.JLabel luz_motor2_marcha;
    private javax.swing.JLabel luz_motor2_parada;
    private javax.swing.JLabel luz_sistbombeo_ok;
    private javax.swing.JLabel luz_sondido_alarma;
    private javax.swing.JLabel luz_tanque_ok;
    private javax.swing.JLabel luzniv_tanque_critico;
    private javax.swing.JLabel motor1;
    private javax.swing.JRadioButton motor1_on;
    private javax.swing.JRadioButton motor2_on;
    private javax.swing.JRadioButton motor2_on1;
    private javax.swing.JLabel motor5;
    private javax.swing.JLabel motorR2;
    private javax.swing.JLabel motorReservorio3;
    private javax.swing.JRadioButton opcion_automatico;
    private javax.swing.JRadioButton opcion_manual;
    private javax.swing.JLabel operador_planta;
    private javax.swing.JRadioButton parada_emer;
    private javax.swing.JLabel parada_emergencia;
    private javax.swing.JLabel text_alarma;
    private javax.swing.JLabel text_aviso_encendido;
    private javax.swing.JLabel text_aviso_ok;
    private javax.swing.JLabel text_modalidad;
    private javax.swing.JLabel text_motor1_marcha;
    private javax.swing.JLabel text_motor1_parada;
    private javax.swing.JLabel text_motor1_parada1;
    private javax.swing.JLabel text_motor2_marcha;
    private javax.swing.JLabel text_motor2_marcha1;
    private javax.swing.JLabel text_motor2_parada;
    private javax.swing.JLabel text_nveltanque_maxi;
    private javax.swing.JLabel text_nveltanque_mini;
    private javax.swing.JLabel text_nveltanque_ok;
    private javax.swing.JLabel text_sistBombeo;
    private javax.swing.JTextField textalarma;
    private javax.swing.JLabel txtbarra2;
    private javax.swing.JTextField txtfecha;
    // End of variables declaration//GEN-END:variables

    private void crono() {
        s = s + 1;
        if (s >= 5) {
            s = 0;
            m = m + 1;
            if (m >= 5) {
                m = 0;
                h = h + 1;
                if (h >= 5) {
                    h = 0;
                }
            }
        }
        if (conmut == 1) {
            contmotor1.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
            //acumotor1.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
        } else {
            contmotor2.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
            //acumotor2.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
        }
    }

    private void animar_motor1() {
        crono();
        if (m1 == 1) {
            motor1.setVisible(true);
            m1 = 0;
        } else {
            motor1.setVisible(false);
            m1 = 1;
        }
    }

    private void animar_motor2() {
        crono();
        if (m2 == 1) {
            motorR2.setVisible(true);
            m2 = 0;
        } else {
            motorR2.setVisible(false);
            m2 = 1;
        }
    }

    private void avisosvisibles_false() {
        motor1.setVisible(false);
        motorR2.setVisible(false);
        text_alarma.setVisible(false);
        luz_sondido_alarma.setVisible(false);
        text_motor1_marcha.setVisible(false);
        luz_motor1_marcha.setVisible(false);
        //text_motor2_marcha.setVisible(false);
        luz_motor2_marcha.setVisible(false);
        text_nveltanque_maxi.setVisible(false);
        text_nveltanque_mini.setVisible(false);
        //luzniv_reserv_critico1.setVisible(false);
        luzniv_tanque_critico.setVisible(false);
        luz_aviso_emitido.setVisible(false);
        //text_nvreservrio_critico.setVisible(false);
        text_aviso_encendido.setVisible(false);
    }

    private void cronometro_manual_m1() {
        s = s + 1;
        if (s >= 10) {
            s = 0;
            m = m + 1;
            if (m >= 10) {
                m = 0;
                h = h + 1;
                if (h >= 10) {
                    h = 0;
                }
            }
        }
        contmotor1.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
//        acumotor1.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
    }

    private void cronometro_manual_m2() {
        seg2 = seg2 + 1;
        if (seg2 >= 10) {
            seg2 = 0;
            minuto2 = minuto2 + 1;
            if (minuto2 >= 10) {
                minuto2 = 0;
                hora2 = hora2 + 1;
                if (hora2 >= 10) {
                    hora2 = 0;
                }
            }
        }
        contmotor2.setText("" + String.valueOf(hora2) + ":" + String.valueOf(minuto2) + ":" + String.valueOf(seg2));
        //acumotor2.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
    }
        private void cronometro_manual_m2(String numero) {
        segundo1 = segundo1 + 1;
        if (segundo1 >= 10) {
            segundo1 = 0;
            minuto1 = minuto1 + 1;
            if (minuto1 >= 10) {
                minuto1 = 0;
                hora1 = hora1 + 1;
                if (hora1 >= 10) {
                    hora1 = 0;
                }
            }
        }
        contmotor3.setText("" + String.valueOf(hora1) + ":" + String.valueOf(minuto1) + ":" + String.valueOf(segundo1));
        //acumotor2.setText("" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
    }

    private void animarmotor1_manual() {
        cronometro_manual_m1();
        if (m1 == 1) {
            motor1.setVisible(true);
            m1 = 0;
        } else {
            motor1.setVisible(false);
            m1 = 1;
        }
    }
    
     private void animarmotor3_manual() {
        cronometro_manual_m2("m");
       
            motorReservorio3.setVisible(true);
            if (m1 == 1) {
            motorReservorio3.setVisible(true);
            m1 = 0;
            } else {
                motorReservorio3.setVisible(false);
                m1 = 1;
            }
        
    }

    private void animarmotor2_manual() {
        cronometro_manual_m2();
        if (m2 == 1) {
            motorR2.setVisible(true);
            m2 = 0;
        } else {
            motorR2.setVisible(false);
            m2 = 1;
        }
    }

    private void autonumerico_alarmas() {
//               try {
//                bd.instanciacion = bd.conexion.createStatement();
//                bd.resultinstan = bd.instanciacion.executeQuery("select ifnull(max(cod_alar),0)+1 as xx from alarmas");
//                try {
//                    while (bd.resultinstan.next()) {
//                        if (bd.resultinstan.getString(1)!= null) {
//                            textalarma.setText(bd.resultinstan.getString("xx"));
//                        }
//                }
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null,"Error al Comparar Resultados de Alarmas");
//                }
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null,"Error al Instanciar a la Table Alarmas");
//            }

    }

    private void captura_hora() {
        java.util.Date utilDate = new java.util.Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
        java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

        //hora.setText("" + sqlTime);
       // String horac = String.valueOf(hora.getText().toString());
       // txthoraconver.setText(horac);
    }

    private String visualizafecha(String fe) {
        String Dia = "";
        String Mes = "";
        String Ano = "";
        Dia = fe.substring(0, 2);
        Mes = fe.substring(3, 5);
        Ano = fe.substring(6, 10);
        fe = Ano + "-" + Mes + "-" + Dia;
        return fe;
    }

    private void autonumericobombeo() {
//               try {
//                bd.instanciacion = bd.conexion.createStatement();
//                bd.resultinstan = bd.instanciacion.executeQuery("select ifnull(max(cod_bombeo),0)+1 as xx from control_bombeos");
//                try {
//                    while (bd.resultinstan.next()) {
//                        if (bd.resultinstan.getString(1)!= null) {
//                            codbombeo.setText(bd.resultinstan.getString("xx"));
//                        }
//                }
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null,"Error al Comparar Resultados de Bombeo");
//                }
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null,"Error al Instanciar a la Table Control Bombeo");
//            }
    }
    
    //CRONOMETRO NUEVO
    //private Timer t;
   
    private  ActionListener acciones = new ActionListener(){
   
        
    
        @Override
        public void actionPerformed(ActionEvent ae) {
            ++cs; 
            if(cs==100){
                cs = 0;
                ++segundo1;
            }
            if(segundo1==60) 
            {
                segundo1 = 0;
                ++minuto1;
            }
            if(minuto1==60)
            {
                minuto1 = 0;
                ++hora1;
            }
            actualizarLabel();
            contmotor1.setText(actualizarLabel());
        }
        
    };
    
    public String actualizarLabel() {
        String tiempo = (h<=9?"0":"")+h+":"+(m<=9?"0":"")+m+":"+(s<=9?"0":"")+s+":"+(cs<=9?"0":"")+cs;
        //label.concat(".").concat("setText(").concat(tiempo).concat(");");
        return tiempo;
    }
    
    public void btnStart() {                                         
        t.start();
    }                                        

    public void btnPause() {                                         
        t.stop();
      
    }                                        

    public void btnStop() {                                        
        if(t.isRunning()) 
        {
            t.stop();
         
        }
       
        h=0; m=0; s=0; cs=0;
       
    }

}
