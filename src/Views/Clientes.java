/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.ConnectionDB;
import Models.Cliente;
import com.apple.eawt.Application;
import java.awt.Event;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author javierfuenteshuertas
 */
public class Clientes extends javax.swing.JFrame {

    /**
     * Creates new form Clientes
     */
    private int idEmpresa;
    private ConnectionDB conexion;
    private Connection conn;
    private String tipoDocumento;
    private ArrayList<Cliente> listClientes;
    private Cliente cliente;
    private Comprobante comprobante;
    private ViewReporteCliente vista;
    private boolean band;
  
   public Clientes(int idEmpresa,String tipoDocumento, Comprobante comprobante) {
        this.idEmpresa=idEmpresa;
        this.tipoDocumento=tipoDocumento;
        this.comprobante=comprobante;
        cliente=new Cliente();
        initComponents();
        conexion=new ConnectionDB();
        conn=conexion.connect(conn);
        initializeEvents();
        buscarClientes("%");
        String sSistemaOperativo = System.getProperty("os.name");
        if(sSistemaOperativo.equals("Mac OS X")){
            Application application = Application.getApplication();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logoKipu.png"));
            application.setDockIconImage(image);
        }
        else{
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logoKipu.png"));
            this.setIconImage(icon);
        }
        
    }
    public Clientes(int idEmpresa,String tipoDocumento, ViewReporteCliente vista) {
        this.idEmpresa=idEmpresa;
        this.tipoDocumento=tipoDocumento;
        this.vista=vista;
        band=true;
        cliente=new Cliente();
        initComponents();
        conexion=new ConnectionDB();
        conn=conexion.connect(conn);
        initializeEvents();
        buscarClientes("%");
        String sSistemaOperativo = System.getProperty("os.name");
        if(sSistemaOperativo.equals("Mac OS X")){
            Application application = Application.getApplication();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logoKipu.png"));
            application.setDockIconImage(image);
        }
        else{
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logoKipu.png"));
            this.setIconImage(icon);
        }
    }
    private void initializeEvents() {
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
        
        getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
               setVisible(false);
            }
        });
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), "Buscar");
        
        getRootPane().getActionMap().put("Buscar", new javax.swing.AbstractAction(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
               //Buscar clientes
            }
        });
        jtClientes.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
            JTable table =(JTable) me.getSource();
            Point p = me.getPoint();
            int row = table.rowAtPoint(p);
            if (me.getClickCount() == 2) {
                PreparedStatement stmt;
                try {
                    stmt = conn.prepareStatement("select * from cliente where numero=?");
                    stmt.setString(1, String.valueOf(jtClientes.getValueAt(row, 0)));
                    ResultSet rs=stmt.executeQuery();
                    if(rs.next()){
                        cliente.setNumero(rs.getString("numero"));
                        cliente.setTipoDocumento(rs.getString("tipoDocumento"));
                        cliente.setDireccion(rs.getString("direccion"));
                        cliente.setEmail(rs.getString("email"));
                        cliente.setNombre(rs.getString("nombre"));
                    }
                    if(comprobante!=null)comprobante.asignarCliente(cliente);
                    else if(vista!=null)vista.asignarCliente(cliente);
                    setVisible(false);
                } catch (SQLException ex) {
                    Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }

        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfBuscar = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtClientes = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Buscar:");

        jtfBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfBuscarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados"));

        jtClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "R.U.C.", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtClientes);
        if (jtClientes.getColumnModel().getColumnCount() > 0) {
            jtClientes.getColumnModel().getColumn(1).setMinWidth(250);
            jtClientes.getColumnModel().getColumn(1).setPreferredWidth(250);
            jtClientes.getColumnModel().getColumn(1).setMaxWidth(250);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jtfBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfBuscarKeyPressed
        if(evt.getKeyCode()==Event.ENTER){
            buscarClientes(jtfBuscar.getText()+"%");
        }
    }//GEN-LAST:event_jtfBuscarKeyPressed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtClientes;
    private javax.swing.JTextField jtfBuscar;
    // End of variables declaration//GEN-END:variables

    private void buscarClientes(String nomCliente) {
        PreparedStatement stmt;
        try {
            if( band || comprobante.getTipoComprobante().equals("F") || comprobante.getTipoComprobante().equals("B")){
                stmt = conn.prepareStatement("select c.numero,c.nombre from cliente c inner join Empresa_has_Cliente e on c.numero=e.Cliente_numero where Empresa_idEmpresa=? and tipoDocumento=? and c.nombre like ? order by nombre asc");
                stmt.setInt(1, idEmpresa);
                stmt.setString(3, nomCliente);
                stmt.setString(2, tipoDocumento);
            }
            else{
                stmt = conn.prepareStatement("select c.numero,c.nombre from cliente c inner join Empresa_has_Cliente e on c.numero=e.Cliente_numero where Empresa_idEmpresa=?  and c.nombre like ? order by nombre asc");
                stmt.setInt(1, idEmpresa);
                stmt.setString(2, nomCliente);
            }
            ResultSet rs=stmt.executeQuery();
            listClientes=new ArrayList<Cliente>();
            while(rs.next()){
                Cliente tempCliente=new Cliente();
                tempCliente.setNumero(rs.getString("numero"));
                tempCliente.setNombre(rs.getString("nombre"));
                listClientes.add(tempCliente);
            }
            actualizarTablaCliente();
        } catch (SQLException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    private void actualizarTablaCliente() {
        DefaultTableModel dtm=(DefaultTableModel) jtClientes.getModel();
        Cliente temp;
        Object[] object = null;
        limpiarTabla(dtm);
        for(int i=0;i<listClientes.size();i++){
            temp=listClientes.get(i);
            dtm.addRow(object);
            dtm.setValueAt(temp.getNumero(), i, 0);
            dtm.setValueAt(temp.getNombre(), i, 1);
        }
    }

    private void limpiarTabla(DefaultTableModel dtm) {
        for(int i=0;i<dtm.getRowCount();i++){
            dtm.removeRow(i);
        }
    }
}
