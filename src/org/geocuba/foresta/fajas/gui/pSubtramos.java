/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * pGestion.java
 *
 * Created on 13-feb-2012, 7:56:07
 */

package org.geocuba.foresta.fajas.gui;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.TableSorter;

/**
 *
 * @author Raisel
 */
public class pSubtramos extends Panel {

    /** Creates new form pGestion */
    public pSubtramos(String title) {
        initComponents();
        _title  = title;
        _panel_id = "_"+title;
        //SetResizable(true);
        UIUtils.AddPanel(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBZoom = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jBCerrar = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jBCalcularAnchos = new javax.swing.JButton();
        jBFaja = new javax.swing.JButton();
        jBValores = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jLAnchos = new javax.swing.JLabel();

        jBZoom.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\zoomToSel.png")); // NOI18N
        jBZoom.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"zoomToSel.png"));
        jBZoom.setToolTipText("Zoom a lo seleccionado");
        jBZoom.setEnabled(false);

        jBCerrar.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\cerrar.png")); // NOI18N
        jBCerrar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"cerrar.png"));
        jBCerrar.setText("Cerrar");

        jBCalcularAnchos.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\calcularancho.png")); // NOI18N
        jBCalcularAnchos.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"calcularancho.png"));
        jBCalcularAnchos.setToolTipText("Calcular el ancho para cada perfil");
        jBCalcularAnchos.setEnabled(false);

        jBFaja.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\calcularfaja.png")); // NOI18N
        jBFaja.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"calcularfaja.png"));
        jBFaja.setToolTipText("Calcular las fajas forestales");
        jBFaja.setEnabled(false);

        jBValores.setText("Generar valores");
        jBValores.setVisible(false);

        /*
        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(Table);
        */

        db = new JDBCAdapter(ConnectionExt.getConexionActiva());
        jScrollPane1 = crearTabla();
        sorter.setModel(db);
        Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jLAnchos.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLAnchos.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCalcularAnchos, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBFaja, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addComponent(jLAnchos, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
            .addComponent(jSeparator6, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(475, Short.MAX_VALUE)
                .addComponent(jBValores)
                .addGap(92, 92, 92)
                .addComponent(jBCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLAnchos)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jBZoom)
                        .addComponent(jBCalcularAnchos)
                        .addComponent(jBFaja)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCerrar)
                    .addComponent(jBValores))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

 public JTable getTable(){
   return Table;
 }
 
 public JButton getButtonCerrar(){
   return jBCerrar;
 }

 public JButton getButtonZoom(){
   return jBZoom;
 }
 
 public JButton getButtonCalcularAnchos(){
	   return jBCalcularAnchos;
 }
 
 public JButton getButtonFajas(){
	   return jBFaja;
 }
 
 public JButton getButtonValores(){
  return jBValores;
 }

 public JLabel getLabelAnchos(){
  return jLAnchos;
 }

/**Clase para poner editable una columna del
     * TableSorter*/
    private static class MyTableSorter extends TableSorter
    {
     @Override
     public boolean isCellEditable(int row, int column)
     {
      if(column == 10)  //Columna de la Velocidad de Infiltraci�n
       return true;
      else;
       return false;
     }
    }

    public JScrollPane crearTabla()
    {
        sorter = new MyTableSorter();
        Table = new JTable(sorter);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //sorter.addMouseListenerToHeaderInTable(Table);
        JScrollPane scrollpane = new JScrollPane(Table);
        scrollpane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollpane;
    }

    public void ActualizarTabla(String query)
    {
     db.executeQuery2(query);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton jBCalcularAnchos;
    private javax.swing.JButton jBCerrar;
    private javax.swing.JButton jBFaja;
    private javax.swing.JButton jBValores;
    private javax.swing.JButton jBZoom;
    private javax.swing.JLabel jLAnchos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
    private MyTableSorter sorter;
    private JDBCAdapter db;
}