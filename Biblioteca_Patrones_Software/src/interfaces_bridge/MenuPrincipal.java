/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces_bridge;

import excepciones.AsegurarOpcionCorrecta;
import gestionPublicsUsurs_singleton.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import usuarios_factoryMethod.Alumno;
import usuarios_factoryMethod.Fabrica;
import usuarios_factoryMethod.Usuario;
import publicaciones_builder.*;
import publicaciones_builder.EstadoPublicacion_State.*;
import usuarios_factoryMethod.Profesor;

/**
 *
 * @author kevin
 */
public class MenuPrincipal extends javax.swing.JFrame {

    private static HashMap<String, Usuario> usuarios, usuariosPrestamo;
    private static HashMap<String, PublicacionBuilder> publicaciones;
    private static HashMap<String, Prestamo> publicacionPrestadas;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private Date date;
    private Fabrica miFabrica;
    private Usuario usuario;
    private AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

    public MenuPrincipal() {
        initComponents();
        this.setResizable(false);
        jLabelEspec1.setText("Editorial");
        jLabelEspec2.setText("Localidad");
        jLabelEspec3.setText("Edicion");
        jComboBoxPeriodicidad.setVisible(false);

        cargarTablas();
        actualizarTablas();
    }
    
    public final void cargarTablas(){
        GestionUsuarios.cargarUsuariosSerializacion();
        GestionPublicaciones.cargarPublicacionesSerializacion();
        GestionPublicaciones.cargarPublicacionesPrestadasSerializacion();                
    }
    
    public final void actualizarTablas(){
        getUsuarios();
        getUsuariosPrestamo();
        
        getPublicaciones();
        getPublicacionesPrestadas();
        getPublicacionesLibres();
    }
    
    public final void borrarTablaPublicaciones(){

        DefaultTableModel modelo = (DefaultTableModel) jTablePublicaciones.getModel();
        int filas = modelo.getRowCount();
        for (int i = 0 ; i<filas;i++){
            modelo.removeRow(0);
        }
        
    }
    
    public final void getPublicacionesLibres(){
        
        DefaultTableModel modeloPublicacionesLibres = (DefaultTableModel) jTablePublicacionesLibres.getModel();
        
        int filasTabla = modeloPublicacionesLibres.getRowCount();

        // Se borra la tabla para despues cargarla
        for (int i = 0; i < filasTabla; i++) {
            modeloPublicacionesLibres.removeRow(0);
        }
        
        publicaciones = GestionPublicaciones.getPublicaciones();
        ArrayList<PublicacionBuilder> lista = new ArrayList(publicaciones.values());

        int filas = lista.size();
        Object[] fila = new Object[modeloPublicacionesLibres.getColumnCount()];    
        
        for (int i = 0; i < filas; i++) {
            
            PublicacionBuilder publicacion = lista.get(i);
            
            if(publicacion.getPublicacion().getiEstado() instanceof NoPrestado){
                fila[0] = publicacion.getPublicacion().getIsbn();
                fila[1] = publicacion.getPublicacion().getTitulo();                

            modeloPublicacionesLibres.addRow(fila);
            }                       
        }
    }

    
    public final void getUsuariosPrestamo(){
        
        DefaultTableModel modeloUsuariosPrestamo = (DefaultTableModel) jTableUsuariosPrestamo.getModel();

        usuariosPrestamo = GestionUsuarios.getUsuarios();
        
        ArrayList<Usuario> lista = new ArrayList(usuariosPrestamo.values());
        
        int filas = jTableUsuariosPrestamo.getRowCount();
        //Lo mismo con otra tabla de usuarios
        while (modeloUsuariosPrestamo.getRowCount() > 0) {
            modeloUsuariosPrestamo.removeRow(0);
        }

        Object[] filaPrestamo = new Object[modeloUsuariosPrestamo.getColumnCount()];

        for (int i = 0; i < lista.size(); i++) {
            filaPrestamo[0] = lista.get(i).getNif();
            filaPrestamo[1] = lista.get(i).getNombreCompleto();
            
            modeloUsuariosPrestamo.addRow(filaPrestamo);
        }
    }
    
    public final void getUsuarios() {

        DefaultTableModel modelo = (DefaultTableModel) jTableUsuarios.getModel();
                
        usuarios = GestionUsuarios.getUsuarios();
        ArrayList<Usuario> lista = new ArrayList(usuarios.values());

        int filas = jTableUsuarios.getRowCount();

        // Se borra la tabla para despues cargarla
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        
        
        Object[] fila = new Object[modelo.getColumnCount()];
        for (int i = 0; i < lista.size(); i++) {
            fila[0] = lista.get(i).getNif();
            fila[1] = lista.get(i).getNombreCompleto();
            fila[2] = lista.get(i).getFechaInscripcion();
            if (lista.get(i) instanceof Alumno) {
                fila[3] = "Alumno";

            } else {
                fila[3] = "Profesor";

            }
            modelo.addRow(fila);
        }
        
    }

    public final void getPublicaciones() {

        DefaultTableModel modelo = (DefaultTableModel) jTablePublicaciones.getModel();
       
        borrarTablaPublicaciones();
        
        publicaciones = GestionPublicaciones.getPublicaciones();
        ArrayList<PublicacionBuilder> lista = new ArrayList(publicaciones.values());

        int filas = lista.size();
        Object[] fila = new Object[modelo.getColumnCount()];    
        
        for (int i = 0; i < filas; i++) {
            PublicacionBuilder publicacion = lista.get(i);
            fila[0] = publicacion.getPublicacion().getIsbn();
            fila[1] = publicacion.getPublicacion().getTitulo();
            fila[2] = publicacion.getPublicacion().getAutores();
            fila[3] = publicacion.getPublicacion().getFechaPublicacion();
            fila[4] = publicacion.getPublicacion().getMateria();
            String estado = publicacion.getPublicacion().getiEstado().ejecutarAccion();

            if (publicacion instanceof LibroBuilder) {
                fila[5] = ((LibroBuilder) publicacion).getEditorial();
                fila[8] = ((LibroBuilder) publicacion).getLocalidad();
                fila[11] = String.valueOf(((LibroBuilder) publicacion).getEdicion());
                fila[6] = "";
                fila[9] = "";
                fila[12] = "";
                fila[7] = "";
                fila[10] = "";
                fila[13] = "";                
                fila[14] = "Libro";
            } else if (publicacion instanceof RevistaBuilder) {
                fila[6] = ((RevistaBuilder) publicacion).getPeriodicidad();
                fila[9] = String.valueOf(((RevistaBuilder) publicacion).getVolumen());
                fila[12] = String.valueOf(((RevistaBuilder) publicacion).getNumero());
                fila[7] = "";
                fila[10] = "";
                fila[13] = "";
                fila[5] = "";
                fila[8] = "";
                fila[11] = "";
                fila[14] = "Revista";
            } else if (publicacion instanceof ProyectoBuilder) {
                fila[7] = ((ProyectoBuilder) publicacion).getTribunal();
                fila[10] = ((ProyectoBuilder) publicacion).getTitulacion();
                fila[13] = String.valueOf(((ProyectoBuilder) publicacion).getCalificacion());
                fila[6] = "";
                fila[9] = "";
                fila[12] = "";
                fila[5] = "";
                fila[8] = "";
                fila[11] = "";
                fila[14] = "Proyecto";
            }
            
            fila[15] = publicacion.getPublicacion().getiEstado().ejecutarAccion();
            modelo.addRow(fila);

        }
    }
    
    public final void getPublicacionesPrestadas(){
        
        DefaultTableModel tablaPrestadas = (DefaultTableModel) jTablePublicacionesPrestadas.getModel();
        
        int filasTabla = tablaPrestadas.getRowCount();
        for (int i = 0; i < filasTabla; i++) {
            tablaPrestadas.removeRow(0);
        }
        
        publicacionPrestadas = GestionPublicaciones.getPublicacionesPrestadas();
        ArrayList<Prestamo> listaPrestadas = new ArrayList(publicacionPrestadas.values());

        int filas = listaPrestadas.size();
        Object[] filaPublicacionPrestada = new Object[tablaPrestadas.getColumnCount()];    

        for (int i = 0; i < filas; i++) {
            Prestamo prestamo = listaPrestadas.get(i);
            
            filaPublicacionPrestada[0] = prestamo.getIsbn();
            filaPublicacionPrestada[1] = prestamo.getPublicacionBuilder().getPublicacion().getTitulo();
            filaPublicacionPrestada[2] = prestamo.getNif();
            filaPublicacionPrestada[3] = prestamo.getDiasPermitidos();
            filaPublicacionPrestada[4] = prestamo.getFechaPrestamo();
            
            tablaPrestadas.addRow(filaPublicacionPrestada);
        }     

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabelLogoDescripcion = new javax.swing.JLabel();
        jTabbedPanePrincipal = new javax.swing.JTabbedPane();
        jPanelGestionPublicaciones = new javax.swing.JPanel();
        jPanelPublicacionesGeneral = new javax.swing.JPanel();
        jLabelISBN = new javax.swing.JLabel();
        jTextFieldISBN = new javax.swing.JTextField();
        jLabelTitulo = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jLabelAutores = new javax.swing.JLabel();
        jTextFieldAutores = new javax.swing.JTextField();
        jLabelFechaGestorPublicaciones = new javax.swing.JLabel();
        jLabelMateria = new javax.swing.JLabel();
        jTextFieldMateria = new javax.swing.JTextField();
        jFormattedTextFieldFecha = new javax.swing.JFormattedTextField();
        jLabelBorrarPub = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePublicaciones = new javax.swing.JTable();
        jPanelTipoPublicaciones = new javax.swing.JPanel();
        jLabelTipoPublicacion = new javax.swing.JLabel();
        jComboBoxTipoPublicacion = new javax.swing.JComboBox<>();
        jLabelEspec1 = new javax.swing.JLabel();
        jTextFieldEspec1 = new javax.swing.JTextField();
        jLabelEspec2 = new javax.swing.JLabel();
        jTextFieldEspec2 = new javax.swing.JTextField();
        jLabelEspec3 = new javax.swing.JLabel();
        jTextFieldEspec3 = new javax.swing.JTextField();
        jComboBoxPeriodicidad = new javax.swing.JComboBox<>();
        jButtonCrear = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTituloBuscador = new javax.swing.JTextField();
        jButtonBuscadorPublicaciones = new javax.swing.JButton();
        jComboBoxBuscadorPublicaciones = new javax.swing.JComboBox<>();
        jButtonModificar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButtonBorrar = new javax.swing.JButton();
        jTextFieldISBNBorrar = new javax.swing.JTextField();
        jLabelISBNBorrar = new javax.swing.JLabel();
        jTextFieldNombreBuscador = new javax.swing.JTextField();
        jFormattedTextFieldFechaBuscador = new javax.swing.JFormattedTextField();
        jLabelNombreBuscador = new javax.swing.JLabel();
        jLabelTituloBuscador = new javax.swing.JLabel();
        jLabelFechaBuscador = new javax.swing.JLabel();
        jButtonMostrar = new javax.swing.JButton();
        jPanelGestionUsuarios = new javax.swing.JPanel();
        jLabelBorrarUsuario = new javax.swing.JLabel();
        jPanelBorrarUsuario = new javax.swing.JPanel();
        jLabelNIF = new javax.swing.JLabel();
        jTextFieldNIF = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jComboBoxTipoUsuario = new javax.swing.JComboBox<>();
        jButtonAgregar = new javax.swing.JButton();
        jLabelNombre = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jLabelTipoUsuario = new javax.swing.JLabel();
        jFormattedFecha = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldNIFBorrar = new javax.swing.JTextField();
        jButtonBorrarUsuario = new javax.swing.JButton();
        jLabelNIFBorrar = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableUsuarios = new javax.swing.JTable();
        jPanelGestionPrestamo = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablePublicacionesLibres = new javax.swing.JTable();
        jLabelPublicacionesLibres = new javax.swing.JLabel();
        jLabelUsuariosPrestamo = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableUsuariosPrestamo = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldISBNPrestamo = new javax.swing.JTextField();
        jTextFieldNIFPrestamo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabelPublicacionesPrestadas = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTablePublicacionesPrestadas = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldISBNDevolucion = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menú principal");
        setIconImage(getIconImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(java.awt.Color.white);
        jPanel4.setMaximumSize(new java.awt.Dimension(970, 450));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelLogoDescripcion.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        jLabelLogoDescripcion.setForeground(new java.awt.Color(0, 90, 170));
        jLabelLogoDescripcion.setText("Biblioteca de la Universidad Estatal");
        jLabelLogoDescripcion.setMaximumSize(new java.awt.Dimension(500, 100));
        jPanel4.add(jLabelLogoDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 660, 70));

        jTabbedPanePrincipal.setBackground(new java.awt.Color(0, 90, 170));
        jTabbedPanePrincipal.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPanePrincipal.setMaximumSize(new java.awt.Dimension(970, 350));
        jTabbedPanePrincipal.setMinimumSize(new java.awt.Dimension(970, 350));
        jTabbedPanePrincipal.setOpaque(true);
        jTabbedPanePrincipal.setPreferredSize(new java.awt.Dimension(970, 350));

        jPanelGestionPublicaciones.setBackground(new java.awt.Color(255, 255, 255));
        jPanelGestionPublicaciones.setForeground(new java.awt.Color(255, 255, 255));

        jPanelPublicacionesGeneral.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPublicacionesGeneral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelPublicacionesGeneral.setPreferredSize(new java.awt.Dimension(116, 157));

        jLabelISBN.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelISBN.setText("ISBN");

        jTextFieldISBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldISBNActionPerformed(evt);
            }
        });
        jTextFieldISBN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldISBNKeyReleased(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelTitulo.setText("Titulo");

        jTextFieldTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTituloActionPerformed(evt);
            }
        });

        jLabelAutores.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelAutores.setText("Autores");

        jLabelFechaGestorPublicaciones.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelFechaGestorPublicaciones.setText("Fecha");

        jLabelMateria.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelMateria.setText("Materia");

        jTextFieldMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMateriaActionPerformed(evt);
            }
        });

        jFormattedTextFieldFecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yy"))));
        jFormattedTextFieldFecha.setText("dd/MM/YYYY");
        jFormattedTextFieldFecha.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jFormattedTextFieldFecha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldFechaFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanelPublicacionesGeneralLayout = new javax.swing.GroupLayout(jPanelPublicacionesGeneral);
        jPanelPublicacionesGeneral.setLayout(jPanelPublicacionesGeneralLayout);
        jPanelPublicacionesGeneralLayout.setHorizontalGroup(
            jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addComponent(jLabelISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jTextFieldISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jTextFieldTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addComponent(jLabelAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jTextFieldAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addComponent(jLabelFechaGestorPublicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jFormattedTextFieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addComponent(jLabelMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jTextFieldMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanelPublicacionesGeneralLayout.setVerticalGroup(
            jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabelISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextFieldISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabelTitulo))
                    .addComponent(jTextFieldTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabelAutores))
                    .addComponent(jTextFieldAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabelFechaGestorPublicaciones))
                    .addComponent(jFormattedTextFieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanelPublicacionesGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPublicacionesGeneralLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabelMateria))
                    .addComponent(jTextFieldMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jLabelBorrarPub.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabelBorrarPub.setText("Gestor Publicaciones");

        jTablePublicaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Titulo", "Autores", "Fecha Publicacion", "Materia", "Editorial", "Periodicidad", "Tribunal", "Localidad", "Volumen", "Titulacion", "Edicion", "Numero", "Calificacion", "Tipo", "Prestado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePublicaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTablePublicaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePublicacionesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTablePublicaciones);
        if (jTablePublicaciones.getColumnModel().getColumnCount() > 0) {
            jTablePublicaciones.getColumnModel().getColumn(0).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(0).setPreferredWidth(75);
            jTablePublicaciones.getColumnModel().getColumn(1).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTablePublicaciones.getColumnModel().getColumn(2).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTablePublicaciones.getColumnModel().getColumn(3).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(3).setPreferredWidth(250);
            jTablePublicaciones.getColumnModel().getColumn(4).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(4).setPreferredWidth(150);
            jTablePublicaciones.getColumnModel().getColumn(5).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTablePublicaciones.getColumnModel().getColumn(6).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(6).setPreferredWidth(100);
            jTablePublicaciones.getColumnModel().getColumn(7).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(7).setPreferredWidth(150);
            jTablePublicaciones.getColumnModel().getColumn(8).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(8).setPreferredWidth(100);
            jTablePublicaciones.getColumnModel().getColumn(9).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(9).setPreferredWidth(100);
            jTablePublicaciones.getColumnModel().getColumn(10).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(10).setPreferredWidth(150);
            jTablePublicaciones.getColumnModel().getColumn(11).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(11).setPreferredWidth(100);
            jTablePublicaciones.getColumnModel().getColumn(12).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(12).setPreferredWidth(100);
            jTablePublicaciones.getColumnModel().getColumn(13).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(13).setPreferredWidth(100);
            jTablePublicaciones.getColumnModel().getColumn(14).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(15).setResizable(false);
            jTablePublicaciones.getColumnModel().getColumn(15).setPreferredWidth(200);
        }

        jPanelTipoPublicaciones.setBackground(new java.awt.Color(255, 255, 255));
        jPanelTipoPublicaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelTipoPublicaciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTipoPublicacion.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelTipoPublicacion.setText("Tipo Publicacion");
        jPanelTipoPublicaciones.add(jLabelTipoPublicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 20, -1, -1));

        jComboBoxTipoPublicacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libro", "Revista", "Proyecto" }));
        jComboBoxTipoPublicacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoPublicacionActionPerformed(evt);
            }
        });
        jPanelTipoPublicaciones.add(jComboBoxTipoPublicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 13, 122, -1));

        jLabelEspec1.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelEspec1.setText("jLabel7");
        jPanelTipoPublicaciones.add(jLabelEspec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 53, 91, -1));
        jPanelTipoPublicaciones.add(jTextFieldEspec1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 120, 20));

        jLabelEspec2.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelEspec2.setText("jLabel7");
        jPanelTipoPublicaciones.add(jLabelEspec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 86, 91, -1));
        jPanelTipoPublicaciones.add(jTextFieldEspec2, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 79, 122, -1));

        jLabelEspec3.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelEspec3.setText("jLabel7");
        jPanelTipoPublicaciones.add(jLabelEspec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 119, 91, -1));
        jPanelTipoPublicaciones.add(jTextFieldEspec3, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 112, 122, -1));

        jComboBoxPeriodicidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "trimestral", "semestral", "anual" }));
        jPanelTipoPublicaciones.add(jComboBoxPeriodicidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 46, 122, -1));

        jButtonCrear.setText("Crear");
        jButtonCrear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCrearActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel2.setText("Buscador de Publicaciones");

        jTextFieldTituloBuscador.setMaximumSize(new java.awt.Dimension(125, 29));
        jTextFieldTituloBuscador.setMinimumSize(new java.awt.Dimension(125, 29));
        jTextFieldTituloBuscador.setName(""); // NOI18N
        jTextFieldTituloBuscador.setPreferredSize(new java.awt.Dimension(125, 29));

        jButtonBuscadorPublicaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces_bridge/images/buscar.png"))); // NOI18N
        jButtonBuscadorPublicaciones.setMaximumSize(new java.awt.Dimension(27, 27));
        jButtonBuscadorPublicaciones.setMinimumSize(new java.awt.Dimension(27, 27));
        jButtonBuscadorPublicaciones.setPreferredSize(new java.awt.Dimension(27, 27));
        jButtonBuscadorPublicaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscadorPublicacionesActionPerformed(evt);
            }
        });

        jComboBoxBuscadorPublicaciones.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jComboBoxBuscadorPublicaciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Autor", "Titulo", "Autor y Titulo", "Materia", "Fecha de Publicacion", "Materia y Fecha de Publicación" }));
        jComboBoxBuscadorPublicaciones.setPreferredSize(new java.awt.Dimension(79, 28));
        jComboBoxBuscadorPublicaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBuscadorPublicacionesActionPerformed(evt);
            }
        });

        jButtonModificar.setText("Modificar");
        jButtonModificar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 100));

        jButtonBorrar.setText("Borrar");
        jButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarActionPerformed(evt);
            }
        });

        jTextFieldISBNBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldISBNBorrarActionPerformed(evt);
            }
        });

        jLabelISBNBorrar.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelISBNBorrar.setText("ISBN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabelISBNBorrar)
                .addGap(3, 3, 3)
                .addComponent(jTextFieldISBNBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonBorrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBorrar)
                    .addComponent(jTextFieldISBNBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelISBNBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jTextFieldNombreBuscador.setMaximumSize(new java.awt.Dimension(125, 29));
        jTextFieldNombreBuscador.setMinimumSize(new java.awt.Dimension(125, 29));
        jTextFieldNombreBuscador.setName(""); // NOI18N
        jTextFieldNombreBuscador.setPreferredSize(new java.awt.Dimension(125, 29));

        jFormattedTextFieldFechaBuscador.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        jFormattedTextFieldFechaBuscador.setText("dd/MM/yyyy");
        jFormattedTextFieldFechaBuscador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldFechaBuscadorFocusGained(evt);
            }
        });

        jLabelNombreBuscador.setText("Nombre Autor");

        jLabelTituloBuscador.setText("Titulo");

        jLabelFechaBuscador.setText("Fecha");

        jButtonMostrar.setText("Mostrar publicaciones");
        jButtonMostrar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMostrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelGestionPublicacionesLayout = new javax.swing.GroupLayout(jPanelGestionPublicaciones);
        jPanelGestionPublicaciones.setLayout(jPanelGestionPublicacionesLayout);
        jPanelGestionPublicacionesLayout.setHorizontalGroup(
            jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGestionPublicacionesLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelBorrarPub, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                        .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelPublicacionesGeneral, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .addComponent(jButtonCrear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonModificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelTipoPublicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonMostrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelNombreBuscador)
                                    .addComponent(jTextFieldNombreBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                                .addComponent(jComboBoxBuscadorPublicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jButtonBuscadorPublicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                                .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldTituloBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                                        .addComponent(jLabelTituloBuscador)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaBuscador)
                                    .addComponent(jFormattedTextFieldFechaBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(26, 26, 26))
        );
        jPanelGestionPublicacionesLayout.setVerticalGroup(
            jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelBorrarPub)
                .addGap(13, 13, 13)
                .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                        .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelTipoPublicaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelPublicacionesGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanelGestionPublicacionesLayout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(3, 3, 3)
                            .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jComboBoxBuscadorPublicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonBuscadorPublicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(15, 15, 15)
                            .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelNombreBuscador)
                                .addComponent(jLabelTituloBuscador)
                                .addComponent(jLabelFechaBuscador))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanelGestionPublicacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldTituloBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldNombreBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jFormattedTextFieldFechaBuscador)))
                        .addComponent(jButtonMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );

        jTabbedPanePrincipal.addTab("Gestión de publicaciones", jPanelGestionPublicaciones);

        jPanelGestionUsuarios.setBackground(new java.awt.Color(255, 255, 255));
        jPanelGestionUsuarios.setForeground(new java.awt.Color(255, 255, 255));

        jLabelBorrarUsuario.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabelBorrarUsuario.setText("Gestor Usuarios");

        jPanelBorrarUsuario.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBorrarUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelBorrarUsuario.setPreferredSize(new java.awt.Dimension(116, 157));

        jLabelNIF.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelNIF.setText("NIF");

        jTextFieldNIF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNIFActionPerformed(evt);
            }
        });
        jTextFieldNIF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldNIFKeyReleased(evt);
            }
        });

        jComboBoxTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "alumno", "profesor" }));
        jComboBoxTipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoUsuarioActionPerformed(evt);
            }
        });

        jButtonAgregar.setText("Agregar");
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });

        jLabelNombre.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelNombre.setText("Nombre");

        jLabelFecha.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelFecha.setText("Fecha Inscripción");

        jLabelTipoUsuario.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabelTipoUsuario.setText("Tipo Usuario");

        jFormattedFecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yy"))));
        jFormattedFecha.setText("dd/MM/YYYY");
        jFormattedFecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jFormattedFechaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelBorrarUsuarioLayout = new javax.swing.GroupLayout(jPanelBorrarUsuario);
        jPanelBorrarUsuario.setLayout(jPanelBorrarUsuarioLayout);
        jPanelBorrarUsuarioLayout.setHorizontalGroup(
            jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBorrarUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelBorrarUsuarioLayout.createSequentialGroup()
                        .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelTipoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelNIF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabelNombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNombre)
                            .addComponent(jComboBoxTipoUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldNIF)
                            .addComponent(jFormattedFecha))))
                .addContainerGap())
        );
        jPanelBorrarUsuarioLayout.setVerticalGroup(
            jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBorrarUsuarioLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNIF, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNIF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFecha)
                    .addComponent(jFormattedFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBorrarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTipoUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAgregar)
                .addGap(5, 5, 5))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonBorrarUsuario.setText("Borrar");
        jButtonBorrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarUsuarioActionPerformed(evt);
            }
        });

        jLabelNIFBorrar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabelNIFBorrar.setText("NIF");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabelNIFBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNIFBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonBorrarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNIFBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNIFBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonBorrarUsuario)
                .addGap(5, 5, 5))
        );

        jTableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIF", "Nombre Usuario", "Fecha Inscripción", "Tipo Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableUsuarios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableUsuarios);
        if (jTableUsuarios.getColumnModel().getColumnCount() > 0) {
            jTableUsuarios.getColumnModel().getColumn(0).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTableUsuarios.getColumnModel().getColumn(1).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTableUsuarios.getColumnModel().getColumn(2).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(2).setPreferredWidth(300);
            jTableUsuarios.getColumnModel().getColumn(3).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(3).setPreferredWidth(94);
        }

        javax.swing.GroupLayout jPanelGestionUsuariosLayout = new javax.swing.GroupLayout(jPanelGestionUsuarios);
        jPanelGestionUsuarios.setLayout(jPanelGestionUsuariosLayout);
        jPanelGestionUsuariosLayout.setHorizontalGroup(
            jPanelGestionUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestionUsuariosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelGestionUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelBorrarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    .addComponent(jLabelBorrarUsuario)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        jPanelGestionUsuariosLayout.setVerticalGroup(
            jPanelGestionUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestionUsuariosLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabelBorrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanelGestionUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestionUsuariosLayout.createSequentialGroup()
                        .addComponent(jPanelBorrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPanePrincipal.addTab("Gestión de usuarios", jPanelGestionUsuarios);

        jPanelGestionPrestamo.setBackground(new java.awt.Color(255, 255, 255));
        jPanelGestionPrestamo.setForeground(new java.awt.Color(255, 255, 255));

        jTablePublicacionesLibres.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTablePublicacionesLibres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Titulo Publicacion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePublicacionesLibres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePublicacionesLibresMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTablePublicacionesLibres);
        if (jTablePublicacionesLibres.getColumnModel().getColumnCount() > 0) {
            jTablePublicacionesLibres.getColumnModel().getColumn(0).setResizable(false);
            jTablePublicacionesLibres.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTablePublicacionesLibres.getColumnModel().getColumn(1).setResizable(false);
            jTablePublicacionesLibres.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        jLabelPublicacionesLibres.setText("Publicaciones Libres");

        jLabelUsuariosPrestamo.setText("Usuarios");

        jTableUsuariosPrestamo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableUsuariosPrestamo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIF", "Nombre Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableUsuariosPrestamo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableUsuariosPrestamoMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableUsuariosPrestamo);
        if (jTableUsuariosPrestamo.getColumnModel().getColumnCount() > 0) {
            jTableUsuariosPrestamo.getColumnModel().getColumn(0).setResizable(false);
            jTableUsuariosPrestamo.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableUsuariosPrestamo.getColumnModel().getColumn(1).setResizable(false);
            jTableUsuariosPrestamo.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabel8.setText("ISBN");

        jLabel9.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabel9.setText("NIF");

        jButton1.setText("Prestar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldISBNPrestamo)
                            .addComponent(jTextFieldNIFPrestamo))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldISBNPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextFieldNIFPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(12, 12, 12))
        );

        jLabelPublicacionesPrestadas.setText("Publicaciones Prestadas");

        jTablePublicacionesPrestadas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTablePublicacionesPrestadas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Titulo Publicacion", "NIF", "Dias permitidos", "Fecha préstamo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePublicacionesPrestadas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePublicacionesPrestadasMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTablePublicacionesPrestadas);
        if (jTablePublicacionesPrestadas.getColumnModel().getColumnCount() > 0) {
            jTablePublicacionesPrestadas.getColumnModel().getColumn(0).setResizable(false);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(0).setPreferredWidth(75);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(1).setResizable(false);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(2).setResizable(false);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(2).setPreferredWidth(75);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(3).setResizable(false);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(3).setPreferredWidth(75);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(4).setResizable(false);
            jTablePublicacionesPrestadas.getColumnModel().getColumn(4).setPreferredWidth(225);
        }

        jLabel6.setText("Prestamo");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabel11.setText("ISBN");

        jButton2.setText("Devolver");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldISBNDevolucion))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextFieldISBNDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(12, 12, 12))
        );

        jLabel13.setText("Devolucion");

        javax.swing.GroupLayout jPanelGestionPrestamoLayout = new javax.swing.GroupLayout(jPanelGestionPrestamo);
        jPanelGestionPrestamo.setLayout(jPanelGestionPrestamoLayout);
        jPanelGestionPrestamoLayout.setHorizontalGroup(
            jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestionPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelUsuariosPrestamo)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPublicacionesLibres))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestionPrestamoLayout.createSequentialGroup()
                        .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(99, 99, 99)
                        .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelGestionPrestamoLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(179, 179, 179))
                            .addGroup(jPanelGestionPrestamoLayout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12))))
                    .addGroup(jPanelGestionPrestamoLayout.createSequentialGroup()
                        .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelPublicacionesPrestadas)
                            .addComponent(jScrollPane6))
                        .addContainerGap())))
        );
        jPanelGestionPrestamoLayout.setVerticalGroup(
            jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestionPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPublicacionesLibres)
                    .addComponent(jLabelPublicacionesPrestadas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuariosPrestamo)
                    .addComponent(jLabel6)
                    .addComponent(jLabel13))
                .addGap(8, 8, 8)
                .addGroup(jPanelGestionPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );

        jTabbedPanePrincipal.addTab("Gestión prestamo / publicaciones", jPanelGestionPrestamo);

        jPanel4.add(jTabbedPanePrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 970, 350));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces_bridge/images/fondoflama2.jpeg"))); // NOI18N
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 100));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 450));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldISBNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldISBNKeyReleased

    }//GEN-LAST:event_jTextFieldISBNKeyReleased

    private void jTextFieldISBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldISBNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldISBNActionPerformed

    private void jTextFieldNIFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNIFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNIFActionPerformed

    private void jTextFieldNIFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNIFKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNIFKeyReleased

    private void jComboBoxTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoUsuarioActionPerformed

    }//GEN-LAST:event_jComboBoxTipoUsuarioActionPerformed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed

        if (jTextFieldNombre.getText().equals("") || jTextFieldNIF.getText().equals("")
                || jFormattedFecha.getText().equals("")
                || jComboBoxTipoUsuario.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Rellene todos los campos para agregar un usuario");
        } else {

            String nif = jTextFieldNIF.getText();
            String nombre = jTextFieldNombre.getText();
            try {
                date = formatoFecha.parse(jFormattedFecha.getText());
            } catch (ParseException ex) {
                Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
            String fecha = jFormattedFecha.getText();
            String tipo = jComboBoxTipoUsuario.getSelectedItem().toString();

            if (tipo.equals("alumno")) {
                miFabrica = new Fabrica("alumno");
            } else {
                miFabrica = new Fabrica("profesor");
            }
            usuario = miFabrica.creaUsuario();

            usuario.rellenarDatosUsuario(nif, nombre, date);

            actualizarTablas();

            jTextFieldNIF.setText("");
            jTextFieldNombre.setText("");
            jFormattedFecha.setText("");
            jComboBoxTipoUsuario.setSelectedIndex(0);
        }
        
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jTableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableUsuariosMouseClicked
        int fila = jTableUsuarios.getSelectedRow();
        jTextFieldNIF.setText(jTableUsuarios.getValueAt(fila, 0).toString());
        jTextFieldNombre.setText(jTableUsuarios.getValueAt(fila, 1).toString());
        
        String fecha = formatoFecha.format(jTableUsuarios.getValueAt(fila, 2));

        jFormattedFecha.setText(fecha);


        if(jTableUsuarios.getValueAt(fila, 3).equals("Alumno")){
            jComboBoxTipoUsuario.setSelectedIndex(1);
        }else{
            jComboBoxTipoUsuario.setSelectedIndex(2);        
        }
        jComboBoxTipoUsuario.setSelectedItem(0);
        jTextFieldNIFBorrar.setText(jTableUsuarios.getValueAt(fila, 0).toString());
        

    }//GEN-LAST:event_jTableUsuariosMouseClicked

    private void jButtonBorrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarUsuarioActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) jTableUsuarios.getModel();
        if (jTableUsuarios.getSelectedRow() == (-1)) {
            int filas = jTableUsuarios.getRowCount();
            for (int i = 0; i < filas; i++) {
                if (jTableUsuarios.getValueAt(i, 0).equals(jTextFieldNIFBorrar.getText())) {
                    modelo.removeRow(i);
                }
            }
        } else {
            modelo.removeRow(jTableUsuarios.getSelectedRow());
        }
        if (jTextFieldNIFBorrar.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Introduzca o seleccione de la tabla un NIF , por favor", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            GestionUsuarios.borrarUsuario(jTextFieldNIFBorrar.getText());
            
            actualizarTablas();
        }

        jTextFieldNIF.setText("");
        jTextFieldNombre.setText("");
        jFormattedFecha.setText("");
        jComboBoxTipoUsuario.setSelectedIndex(0);
        jTextFieldNIFBorrar.setText("");
        jButtonAgregar.setText("Agregar");
    }//GEN-LAST:event_jButtonBorrarUsuarioActionPerformed

    private void jFormattedFechaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jFormattedFechaMouseClicked
        jFormattedFecha.setText("");
    }//GEN-LAST:event_jFormattedFechaMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        GestionUsuarios.guardarUsuariosSerializacion();
        GestionPublicaciones.guardarPublicacionesSerializacion();
        GestionPublicaciones.guardarPublicacionesPrestadasSerializacion();
    }//GEN-LAST:event_formWindowClosing

    private void jTextFieldISBNBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldISBNBorrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldISBNBorrarActionPerformed

    private void jComboBoxBuscadorPublicacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBuscadorPublicacionesActionPerformed

        jTextFieldNombreBuscador.setVisible(true);
        jLabelNombreBuscador.setVisible(true);
        jFormattedTextFieldFechaBuscador.setVisible(true);
        jLabelFechaBuscador.setVisible(true);
        jTextFieldTituloBuscador.setVisible(true);
        jLabelTituloBuscador.setVisible(true);
        if (jComboBoxBuscadorPublicaciones.getSelectedItem().equals("Titulo")) {
            jTextFieldNombreBuscador.setVisible(false);
            jLabelNombreBuscador.setVisible(false);
            jFormattedTextFieldFechaBuscador.setVisible(false);
            jLabelFechaBuscador.setVisible(false);
            jLabelTituloBuscador.setText("Titulo");
        } else if (jComboBoxBuscadorPublicaciones.getSelectedItem().equals("Autor")) {
            jTextFieldTituloBuscador.setVisible(false);
            jLabelTituloBuscador.setVisible(false);
            jFormattedTextFieldFechaBuscador.setVisible(false);
            jLabelFechaBuscador.setVisible(false);

        } else if (jComboBoxBuscadorPublicaciones.getSelectedItem().equals("Autor y Titulo")) {
            jLabelTituloBuscador.setText("Titulo");
            jFormattedTextFieldFechaBuscador.setVisible(false);
            jLabelFechaBuscador.setVisible(false);

        } else if (jComboBoxBuscadorPublicaciones.getSelectedItem().equals("Materia")) {
            jTextFieldNombreBuscador.setVisible(false);
            jLabelNombreBuscador.setVisible(false);
            jFormattedTextFieldFechaBuscador.setVisible(false);
            jLabelFechaBuscador.setVisible(false);
            jLabelTituloBuscador.setText("Materia");

        } else if (jComboBoxBuscadorPublicaciones.getSelectedItem().equals("Fecha de Publicacion")) {
            jTextFieldTituloBuscador.setVisible(false);
            jLabelTituloBuscador.setVisible(false);
            jTextFieldNombreBuscador.setVisible(false);
            jLabelNombreBuscador.setVisible(false);

        } else if (jComboBoxBuscadorPublicaciones.getSelectedItem().equals("Materia y Fecha de Publicación")) {
            jLabelTituloBuscador.setText("Materia");
            jTextFieldNombreBuscador.setVisible(false);
            jLabelNombreBuscador.setVisible(false);

        }
    }//GEN-LAST:event_jComboBoxBuscadorPublicacionesActionPerformed

    private void jButtonBuscadorPublicacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscadorPublicacionesActionPerformed
        String autor, titulo, materia, fecha;

        String seleccion = jComboBoxBuscadorPublicaciones.getSelectedItem().toString();
        
        if(seleccion.isEmpty() ||
                seleccion.equals("Autor") && jTextFieldNombreBuscador.getText().isEmpty() ||
                seleccion.equals("Titulo") && jTextFieldTituloBuscador.getText().isEmpty() ||
                seleccion.equals("Autor y Titulo") && (jTextFieldNombreBuscador.getText().isEmpty() || jTextFieldTitulo.getText().isEmpty()) ||
                seleccion.equals("Materia") && jTextFieldTituloBuscador.getText().isEmpty() ||
                seleccion.equals("Fecha de publicacion") && jFormattedTextFieldFechaBuscador.getText().isEmpty() ||
                seleccion.equals("Materia y Fecha de publicacion") && (jTextFieldTituloBuscador.getText().isEmpty() || jFormattedTextFieldFechaBuscador.getText().isEmpty()) ){
            
            JOptionPane.showMessageDialog(null, "Por favor rellene el tipo de publicación y/o los campos, gracias", "Información", JOptionPane.INFORMATION_MESSAGE);                   
            
        }else{

            getPublicaciones();
            DefaultTableModel modelo = (DefaultTableModel) jTablePublicaciones.getModel();

            int filas = modelo.getRowCount();

            switch(seleccion){
                case "Autor":
                    autor = jTextFieldNombreBuscador.getText();
                    GestionPublicaciones.busquedaPublicaciones(1, autor);
                                                           
                    for (int i = 0; i < filas; i++) {
                                                
                        if(!modelo.getValueAt(i, 2).equals(autor)){
                            modelo.removeRow(i);
                            filas--;
                            i--;
                        }    
                    }

                    break;
                case "Titulo":
                    titulo = jTextFieldTituloBuscador.getText();
                    GestionPublicaciones.busquedaPublicaciones(2, titulo);
                    
                    for (int i = 0; i < filas; i++) {
                        if(!modelo.getValueAt(i, 1).equals(titulo)){
                            modelo.removeRow(i);
                            filas--;
                            i--;
                        }       
                    }

                    break;
                case "Autor y Titulo":
                    autor = jTextFieldNombreBuscador.getText();
                    titulo = jTextFieldTitulo.getText();
                    GestionPublicaciones.busquedaPublicaciones(3, autor, titulo);
                    
                    for (int i = 0; i < filas; i++) {
                        if(!( (modelo.getValueAt(i, 1).equals(titulo)) && (modelo.getValueAt(i, 2).equals(autor)) )){
                            modelo.removeRow(i);
                            filas--;
                            i--;
                        }       
                    }

                    break;
                case "Materia":
                    materia = jTextFieldTituloBuscador.getText();
                    GestionPublicaciones.busquedaPublicaciones(4, materia);                
                    
                    for (int i = 0; i < filas; i++) {
                        if(!modelo.getValueAt(i, 4).equals(materia)){
                            modelo.removeRow(i);
                            filas--;
                            i--;
                        }       
                    }
                    
                    break;                   
                case "Fecha de Publicacion":
                    fecha = jFormattedTextFieldFechaBuscador.getText();

                    try{
                        date = formatoFecha.parse(fecha);                    
                    } catch(ParseException p){
                        JOptionPane.showMessageDialog(null, "Por favor introduzca una fecha correcta , gracias", "Información", JOptionPane.INFORMATION_MESSAGE);                   
                    }

                    GestionPublicaciones.busquedaPublicaciones(5, fecha);                
                    
                    for (int i = 0; i < filas; i++) {
                        if(!modelo.getValueAt(i, 3).equals(fecha)){
                            modelo.removeRow(i);
                            filas--;
                            i--;
                        }       
                    }                    

                    break;
                case "Materia y Fecha de Publicacion":
                    materia =jTextFieldMateria.getText();
                    fecha = jFormattedTextFieldFechaBuscador.getText();
                    try{
                        date = formatoFecha.parse(fecha);                    
                    } catch(ParseException p){
                        JOptionPane.showMessageDialog(null, "Por favor introduzca una fecha correcta , gracias", "Información", JOptionPane.INFORMATION_MESSAGE);                   
                    }

                    GestionPublicaciones.busquedaPublicaciones(6, fecha, materia );
                    
                    for (int i = 0; i < filas; i++) {
                        if(!( (modelo.getValueAt(i, 4).equals(materia)) && (modelo.getValueAt(i, 3).equals(fecha)) )){
                            modelo.removeRow(i);
                            filas--;
                            i--;
                        }       
                    }

                    break;
                default:             
                    
            }        
            
        }
        
    }//GEN-LAST:event_jButtonBuscadorPublicacionesActionPerformed

    private void jButtonCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCrearActionPerformed

        Publicacion publicacion = new Publicacion();
        CreadorPublicaciones creador = new CreadorPublicaciones();
        String tipoPublicacion = jComboBoxTipoPublicacion.getSelectedItem().toString();            


        if (jTextFieldISBN.getText().equals("") || jTextFieldTitulo.getText().equals("") 
                || jTextFieldAutores.getText().equals("")
                || jFormattedTextFieldFecha.getText().equals("") 
                || jTextFieldMateria.getText().equals("")
                || jTextFieldEspec2.getText().equals("")
                || jTextFieldEspec3.getText().equals("")) {
            
            JOptionPane.showMessageDialog(null, "Por favor introduzca datos en todos los campos , gracias", "Información", JOptionPane.INFORMATION_MESSAGE);

        } else if( (tipoPublicacion.equals("Libro") && (!asegurar.isNumeric(jTextFieldEspec3.getText())) ) ||
                (tipoPublicacion.equals("Revista") && ( (!asegurar.isNumeric(jTextFieldEspec2.getText())) || (!asegurar.isNumeric(jTextFieldEspec3.getText())) ) ) ||
                (tipoPublicacion.equals("Proyecto") && (!asegurar.isNumeric(jTextFieldEspec3.getText())) ) ){

            JOptionPane.showMessageDialog(null, "Por favor introduzca números en los campos necesarios, gracias", "Información", JOptionPane.INFORMATION_MESSAGE);            

        }else if(GestionPublicaciones.getPublicaciones().containsKey(jTextFieldISBN.getText())){
            JOptionPane.showMessageDialog(null, "ISBN ya existe en la biblioteca, por favor introduzca otro", "Información", JOptionPane.INFORMATION_MESSAGE);                        
        }else{
            String isbn = jTextFieldISBN.getText();
            String titulo = jTextFieldTitulo.getText();
            String autores = jTextFieldAutores.getText();

            try {
                date = formatoFecha.parse(jFormattedTextFieldFecha.getText());
            } catch (ParseException ex) {
                Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            String materia = jTextFieldMateria.getText();            
            String espec1 = jTextFieldEspec1.getText();

            if(tipoPublicacion.equals("Revista")){
                espec1 = jComboBoxPeriodicidad.getSelectedItem().toString();
            }
            
            String espec2 = jTextFieldEspec2.getText();
            String espec3 = jTextFieldEspec3.getText();
            
            if(tipoPublicacion.equals("Libro")){
                                
                LibroBuilder lb = new LibroBuilder(publicacion);
                //Establece el constructor concreto
                creador.setPublicacionBuilder(lb);

            }else if(tipoPublicacion.equals("Revista")){
                RevistaBuilder rb = new RevistaBuilder(publicacion);
                //Crear revista
                creador.setPublicacionBuilder(rb);

            }else if(tipoPublicacion.equals("Proyecto")){
                    ProyectoBuilder pb  = new ProyectoBuilder(publicacion);
                    //Crear proyecto
                    creador.setPublicacionBuilder(pb);
            }

            //Construye la publicacion paso a paso
            creador.crearPublicacion(isbn, titulo, autores, date, materia, espec1, espec2, espec3);
            
            
            borrarTablaPublicaciones();                        
            actualizarTablas();
            
            jTextFieldISBN.setText("");
            jTextFieldTitulo.setText("");
            jTextFieldAutores.setText("");
            jTextFieldMateria.setText("");
            jFormattedTextFieldFecha.setText("");
            jComboBoxTipoPublicacion.setSelectedIndex(0);
            jTextFieldEspec1.setText("");
            jTextFieldEspec2.setText("");
            jTextFieldEspec3.setText("");           
        }
    }//GEN-LAST:event_jButtonCrearActionPerformed

    private void jComboBoxTipoPublicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoPublicacionActionPerformed

        if (jComboBoxTipoPublicacion.getSelectedItem().equals("Libro")) {
            jLabelEspec1.setText("Editorial");
            jLabelEspec2.setText("Localidad");
            jLabelEspec3.setText("Edicion");
            jComboBoxPeriodicidad.setVisible(false);
            jTextFieldEspec1.setVisible(true);
        } else if (jComboBoxTipoPublicacion.getSelectedItem().equals("Revista")) {
            jLabelEspec1.setText("Periocidad");
            jLabelEspec2.setText("Volumen");
            jLabelEspec3.setText("Número");
            jComboBoxPeriodicidad.setVisible(true);
            jTextFieldEspec1.setVisible(false);

        } else if (jComboBoxTipoPublicacion.getSelectedItem().equals("Proyecto")) {
            jLabelEspec1.setText("Tribunal");
            jLabelEspec2.setText("Titulación");
            jLabelEspec3.setText("Calificación");
            jComboBoxPeriodicidad.setVisible(false);
            jTextFieldEspec1.setVisible(true);
        }

    }//GEN-LAST:event_jComboBoxTipoPublicacionActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String tipoUsuario;
        
        if(jTextFieldISBNPrestamo.getText().isEmpty() ||
                jTextFieldNIFPrestamo.getText().isEmpty()){
            
            JOptionPane.showMessageDialog(null, "Rellene los campos ISBN y NIF, por favor", "Información", JOptionPane.INFORMATION_MESSAGE);
        }else{
            String isbn = jTextFieldISBNPrestamo.getText();
            String nif = jTextFieldNIFPrestamo.getText();
            
            if( (!GestionUsuarios.getUsuarios().containsKey(nif))){
                JOptionPane.showMessageDialog(null, "NIF no existe en la librería, por favor introduzca un NIF existente", "Información", JOptionPane.INFORMATION_MESSAGE);
                
            }else if(!GestionPublicaciones.getPublicaciones().containsKey(isbn)){
                JOptionPane.showMessageDialog(null, "ISBN no existe en la librería, por favor introduzca un ISBN existente", "Información", JOptionPane.INFORMATION_MESSAGE);                
            }else{
                if(asegurar.siEstaPrestadoAvisar(isbn)){
                    JOptionPane.showMessageDialog(null, "La publicación está prestada, le avisaremos cuando se devuelva", "Información", JOptionPane.INFORMATION_MESSAGE);
                    GestionPublicaciones.anadirObservadorAPublicacion(nif, isbn);

                }else{

                    int dias = GestionPublicaciones.prestarPublicacion(nif,isbn);

                    if(GestionUsuarios.getUsuarios().get(nif) instanceof Profesor){
                        tipoUsuario = ((Profesor) GestionUsuarios.getUsuarios().get(nif)).getTipo();
                    }else{
                        tipoUsuario = ((Alumno) GestionUsuarios.getUsuarios().get(nif)).getTipo();
                    }
                    

                    String mensaje = "Los días permitidos para prestar la publicación al "+tipoUsuario+
                    ", con NIF "+GestionUsuarios.getUsuarios().get(nif).getNif()+ " y nombre "+
                    GestionUsuarios.getUsuarios().get(nif).getNombreCompleto()+" \ncon la publicacion " +
                    GestionPublicaciones.getPublicaciones().get(isbn).getPublicacion().getTitulo()+" son" +
                    " de "+dias+" dias.";
                    
                    JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);                    
                    
                    actualizarTablas();
                    
                    jTextFieldISBNPrestamo.setText("");
                    jTextFieldNIFPrestamo.setText("");
                }
                
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        String isbnDevolucion = jTextFieldISBNDevolucion.getText();
        int diasSancion;
        
        if(isbnDevolucion.isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor introduzca datos en el campo ISBN, gracias", "Información", JOptionPane.INFORMATION_MESSAGE);
        }else if(!asegurar.existeEnHashMapPublicaciones(isbnDevolucion)){
            JOptionPane.showMessageDialog(null, "ISBN no existe en la biblioteca, introduce un ISBN existente", "Información", JOptionPane.INFORMATION_MESSAGE);            

        }else if(!(GestionPublicaciones.getPublicaciones().get(isbnDevolucion).getPublicacion().getiEstado() instanceof Prestado)){
            JOptionPane.showMessageDialog(null, "La publicación no está prestada, no se devuelve", "Información", JOptionPane.INFORMATION_MESSAGE);                        
        }else{
            diasSancion = GestionPublicaciones.devolucionPublicacion(isbnDevolucion);            

            if(diasSancion<=0){
                JOptionPane.showMessageDialog(null, "Devolución de la publicación correcta y entregada dentro del plazo", "Información", JOptionPane.INFORMATION_MESSAGE);                        
            }else{
                JOptionPane.showMessageDialog(null, "Devolución fuera de plazo indicado, dias de sanción: "+diasSancion, "Información", JOptionPane.INFORMATION_MESSAGE);                        
            }
            
            //Avisamos a los usuarios que lo necesiten que el libro esta devuelto
            GestionPublicaciones.avisarAObservadoresDePublicacionConcreta(isbnDevolucion);

            //Eliminamos los observadores de la lista a avisar
            GestionPublicaciones.eliminarObservadoresDePublicacion(isbnDevolucion);
            
            actualizarTablas();            
            
            jTextFieldISBNDevolucion.setText("");           
        }                        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jFormattedTextFieldFechaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFechaFocusGained
        jFormattedTextFieldFecha.setText("");
    }//GEN-LAST:event_jFormattedTextFieldFechaFocusGained

    private void jButtonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) jTablePublicaciones.getModel();
        
        if (jTablePublicaciones.getSelectedRow() == (-1)) {
            int filas = jTablePublicaciones.getRowCount();
            for (int i = 0; i < filas; i++) {
                if (jTablePublicaciones.getValueAt(i, 0).equals(jTextFieldISBNBorrar.getText())) {
                    modelo.removeRow(i);
                }
            }
        } else {
            modelo.removeRow(jTablePublicaciones.getSelectedRow());
        }
        if (jTextFieldISBNBorrar.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Introduzca o seleccione de la tabla un ISBN , por favor", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            GestionPublicaciones.borrarPublicacion(jTextFieldISBNBorrar.getText());
        }

        jTextFieldISBN.setText("");
        jTextFieldTitulo.setText("");
        jTextFieldAutores.setText("");
        jFormattedTextFieldFecha.setText("");
        jTextFieldMateria.setText("");        
        jComboBoxTipoPublicacion.setSelectedIndex(0);
        jTextFieldEspec1.setText("");
        jTextFieldEspec2.setText("");
        jTextFieldEspec3.setText("");
        
                
        jTextFieldISBNBorrar.setText("");
        
//        GestionPublicaciones.guardarPublicacionesSerializacion();

        actualizarTablas();

        
    }//GEN-LAST:event_jButtonBorrarActionPerformed

    private void jTextFieldTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTituloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTituloActionPerformed

    private void jTextFieldMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMateriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMateriaActionPerformed

    private void jTablePublicacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePublicacionesMouseClicked

        int fila = jTablePublicaciones.getSelectedRow();
        jTextFieldISBNBorrar.setText(jTablePublicaciones.getValueAt(fila, 0).toString());
         
         
        jTextFieldISBN.setText(jTablePublicaciones.getValueAt(fila, 0).toString());
        jTextFieldTitulo.setText(jTablePublicaciones.getValueAt(fila, 1).toString());
        jTextFieldAutores.setText(jTablePublicaciones.getValueAt(fila, 2).toString());

        String fecha = formatoFecha.format(jTablePublicaciones.getValueAt(fila, 3));

        jFormattedTextFieldFecha.setText(fecha);
        jTextFieldMateria.setText(jTablePublicaciones.getValueAt(fila, 4).toString());
         
        int especialidad1;
        int especialidad2;
        int especialidad3;         
         
        if(!jTablePublicaciones.getValueAt(fila, 5).toString().isEmpty()){
           especialidad1 = 5;
           especialidad2 = 8;
           especialidad3 = 11;
           
           jComboBoxTipoPublicacion.setSelectedIndex(0);
           jLabelEspec1.setText("Editorial");
           jLabelEspec2.setText("Localidad");
           jLabelEspec3.setText("Edicion");
           
        }else if(!jTablePublicaciones.getValueAt(fila, 6).toString().isEmpty()){
            especialidad1 = 6;   
            especialidad2 = 9;
            especialidad3 =12;

            jComboBoxTipoPublicacion.setSelectedIndex(1);
            jLabelEspec1.setText("Periocidad");
            jLabelEspec2.setText("Volumen");
            jLabelEspec3.setText("Número");
           
        }else{
           especialidad1 = 7;             
           especialidad2 = 10;
           especialidad3 = 13;
           
           jComboBoxTipoPublicacion.setSelectedIndex(2);
           jLabelEspec1.setText("Tribunal");
           jLabelEspec2.setText("Titulación");
           jLabelEspec3.setText("Calificación");
        }

        jTextFieldEspec1.setText(jTablePublicaciones.getValueAt(fila, especialidad1).toString());
        jTextFieldEspec2.setText(jTablePublicaciones.getValueAt(fila, especialidad2).toString());
        jTextFieldEspec3.setText(jTablePublicaciones.getValueAt(fila, especialidad3).toString());
         
         
    }//GEN-LAST:event_jTablePublicacionesMouseClicked

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed

        Publicacion publicacion = new Publicacion();
        String tipoPublicacion = jComboBoxTipoPublicacion.getSelectedItem().toString();
        CreadorPublicaciones creador = new CreadorPublicaciones();

        if (jTextFieldISBN.getText().equals("") || jTextFieldTitulo.getText().equals("") 
                || jTextFieldAutores.getText().equals("")
                || jFormattedTextFieldFecha.getText().equals("") 
                || jTextFieldMateria.getText().equals("")
                || jTextFieldEspec2.getText().equals("")
                || jTextFieldEspec3.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Por favor introduzca datos en todos los campos , gracias", "Información", JOptionPane.INFORMATION_MESSAGE);
        }else if( (tipoPublicacion.equals("Libro") && (!asegurar.isNumeric(jTextFieldEspec3.getText())) ) ||
                (tipoPublicacion.equals("Revista") && ( (!asegurar.isNumeric(jTextFieldEspec2.getText())) || (!asegurar.isNumeric(jTextFieldEspec3.getText())) ) ) ||
                (tipoPublicacion.equals("Proyecto") && (!asegurar.isNumeric(jTextFieldEspec3.getText())) ) ){

            JOptionPane.showMessageDialog(null, "Por favor introduzca números en los campos necesarios, gracias", "Información", JOptionPane.INFORMATION_MESSAGE);            

        } else {
            String isbn = jTextFieldISBN.getText();
            String titulo = jTextFieldTitulo.getText();
            String autores = jTextFieldAutores.getText();

            try {
                date = formatoFecha.parse(jFormattedTextFieldFecha.getText());
            } catch (ParseException ex) {
                Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

                    
            String materia = jTextFieldMateria.getText();
                     
            String espec1 = jTextFieldEspec1.getText();

            if(tipoPublicacion.equals("Revista")){
                espec1 = jComboBoxPeriodicidad.getSelectedItem().toString();
            }
            
            String espec2 = jTextFieldEspec2.getText();
            System.out.println(espec2+"*********************************");    
            String espec3 = jTextFieldEspec3.getText();
            
            if(tipoPublicacion.equals("Libro")){
                                
                LibroBuilder lb = new LibroBuilder(publicacion);
                //Establece el constructor concreto
                creador.setPublicacionBuilder(lb);

            }else if(tipoPublicacion.equals("Revista")){
                RevistaBuilder rb = new RevistaBuilder(publicacion);
                //Crear revista
                creador.setPublicacionBuilder(rb);

            }else if(tipoPublicacion.equals("Proyecto")){
                ProyectoBuilder pb  = new ProyectoBuilder(publicacion);
                //Crear proyecto
                creador.setPublicacionBuilder(pb);
            }

            //Construye la publicacion paso a paso
            creador.modificarPublicacion(isbn, titulo, autores, date, materia, espec1, espec2, espec3);
            
                        
            borrarTablaPublicaciones();
            actualizarTablas();
            
            jTextFieldISBN.setText("");
            jTextFieldTitulo.setText("");
            jTextFieldAutores.setText("");
            jFormattedTextFieldFecha.setText("");
            jTextFieldMateria.setText("");
            jTextFieldEspec1.setText("");
            jTextFieldEspec2.setText("");
            jTextFieldEspec3.setText("");
            
        }
          
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jFormattedTextFieldFechaBuscadorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFechaBuscadorFocusGained
        jFormattedTextFieldFechaBuscador.setText("");
    }//GEN-LAST:event_jFormattedTextFieldFechaBuscadorFocusGained

    private void jButtonMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMostrarActionPerformed

        actualizarTablas();
    }//GEN-LAST:event_jButtonMostrarActionPerformed

    private void jTablePublicacionesPrestadasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePublicacionesPrestadasMouseClicked

        int filaPublicsPrestadas = jTablePublicacionesPrestadas.getSelectedRow();
        jTextFieldISBNDevolucion.setText(jTablePublicacionesPrestadas.getValueAt(filaPublicsPrestadas, 0).toString());                
    }//GEN-LAST:event_jTablePublicacionesPrestadasMouseClicked

    private void jTableUsuariosPrestamoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableUsuariosPrestamoMouseClicked
        
        int filaUsuarios = jTableUsuariosPrestamo.getSelectedRow();
        jTextFieldNIFPrestamo.setText(jTableUsuariosPrestamo.getValueAt(filaUsuarios, 0).toString());                
    }//GEN-LAST:event_jTableUsuariosPrestamoMouseClicked

    private void jTablePublicacionesLibresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePublicacionesLibresMouseClicked

        int filaPublicsLibres = jTablePublicacionesLibres.getSelectedRow();
        jTextFieldISBNPrestamo.setText(jTablePublicacionesLibres.getValueAt(filaPublicsLibres, 0).toString());                
    }//GEN-LAST:event_jTablePublicacionesLibresMouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonBorrar;
    private javax.swing.JButton jButtonBorrarUsuario;
    private javax.swing.JButton jButtonBuscadorPublicaciones;
    private javax.swing.JButton jButtonCrear;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JButton jButtonMostrar;
    private javax.swing.JComboBox<String> jComboBoxBuscadorPublicaciones;
    private javax.swing.JComboBox<String> jComboBoxPeriodicidad;
    private javax.swing.JComboBox<String> jComboBoxTipoPublicacion;
    private javax.swing.JComboBox<String> jComboBoxTipoUsuario;
    private javax.swing.JFormattedTextField jFormattedFecha;
    private javax.swing.JFormattedTextField jFormattedTextFieldFecha;
    private javax.swing.JFormattedTextField jFormattedTextFieldFechaBuscador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAutores;
    private javax.swing.JLabel jLabelBorrarPub;
    private javax.swing.JLabel jLabelBorrarUsuario;
    private javax.swing.JLabel jLabelEspec1;
    private javax.swing.JLabel jLabelEspec2;
    private javax.swing.JLabel jLabelEspec3;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelFechaBuscador;
    private javax.swing.JLabel jLabelFechaGestorPublicaciones;
    private javax.swing.JLabel jLabelISBN;
    private javax.swing.JLabel jLabelISBNBorrar;
    private javax.swing.JLabel jLabelLogoDescripcion;
    private javax.swing.JLabel jLabelMateria;
    private javax.swing.JLabel jLabelNIF;
    private javax.swing.JLabel jLabelNIFBorrar;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelNombreBuscador;
    private javax.swing.JLabel jLabelPublicacionesLibres;
    private javax.swing.JLabel jLabelPublicacionesPrestadas;
    private javax.swing.JLabel jLabelTipoPublicacion;
    private javax.swing.JLabel jLabelTipoUsuario;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTituloBuscador;
    private javax.swing.JLabel jLabelUsuariosPrestamo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelBorrarUsuario;
    private javax.swing.JPanel jPanelGestionPrestamo;
    private javax.swing.JPanel jPanelGestionPublicaciones;
    private javax.swing.JPanel jPanelGestionUsuarios;
    private javax.swing.JPanel jPanelPublicacionesGeneral;
    private javax.swing.JPanel jPanelTipoPublicaciones;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPanePrincipal;
    private javax.swing.JTable jTablePublicaciones;
    private javax.swing.JTable jTablePublicacionesLibres;
    private javax.swing.JTable jTablePublicacionesPrestadas;
    private javax.swing.JTable jTableUsuarios;
    private javax.swing.JTable jTableUsuariosPrestamo;
    private javax.swing.JTextField jTextFieldAutores;
    private javax.swing.JTextField jTextFieldEspec1;
    private javax.swing.JTextField jTextFieldEspec2;
    private javax.swing.JTextField jTextFieldEspec3;
    private javax.swing.JTextField jTextFieldISBN;
    private javax.swing.JTextField jTextFieldISBNBorrar;
    private javax.swing.JTextField jTextFieldISBNDevolucion;
    private javax.swing.JTextField jTextFieldISBNPrestamo;
    private javax.swing.JTextField jTextFieldMateria;
    private javax.swing.JTextField jTextFieldNIF;
    private javax.swing.JTextField jTextFieldNIFBorrar;
    private javax.swing.JTextField jTextFieldNIFPrestamo;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldNombreBuscador;
    private javax.swing.JTextField jTextFieldTitulo;
    private javax.swing.JTextField jTextFieldTituloBuscador;
    // End of variables declaration//GEN-END:variables
}
