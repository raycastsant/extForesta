package org.geocuba.foresta.gestion_datos.importacion.gui;

import java.io.File;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.cresques.cts.IProjection;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.CuencaManager;
import org.geocuba.foresta.gestion_datos.EmbalseManager;
import org.geocuba.foresta.gestion_datos.ParcelaManager;
import org.geocuba.foresta.gestion_datos.ParteaguasManager;
import org.geocuba.foresta.gestion_datos.RelieveManager;
import org.geocuba.foresta.gestion_datos.RioManager;
import org.geocuba.foresta.gestion_datos.SueloManager;
import org.geocuba.foresta.gestion_datos.importacion.ImportWT;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.utiles.swing.fileFilter.ExampleFileFilter;

/**
 *
 * @author Raisel
 */
public class pAdvancedImport extends Panel {//javax.swing.JPanel {

    /** Creates new form pAdvancedImport */
    public pAdvancedImport()
    {
        initComponents();
        _title = "Importaci�n de datos al sistema";
        _panel_id = "advanced_import";
        UIUtils.AddPanel(this);
        proyection = CRSFactory.getCRS("EPSG:2085");
        Global.projeccionActiva = CRSFactory.getCRS("EPSG:2085");;
        links = new HashMap<String, String>();
        db = new JDBCAdapter(ConnectionExt.getConexionActiva());
        setNewPanel(CUENCAS);
        TableObjectType = FShape.POLYGON;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.ButtonGroup();
        bgpry = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jRBcuencas = new javax.swing.JRadioButton();
        jRBHidro_areal = new javax.swing.JRadioButton();
        jRBParteaguas = new javax.swing.JRadioButton();
        jRBParcelas = new javax.swing.JRadioButton();
        jRBHidro_lineal = new javax.swing.JRadioButton();
        jRBSuelos = new javax.swing.JRadioButton();
        jRBRelieve = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mFields = new DefaultListModel();
        jLFields = new javax.swing.JList(mFields);
        jScrollPane2 = new javax.swing.JScrollPane();
        mValues = new DefaultListModel();
        jLValues = new javax.swing.JList(mValues);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mEnlaces = new DefaultListModel();
        jLEnlaces = new javax.swing.JList(mEnlaces);
        jLabel3 = new javax.swing.JLabel();
        jBRemove = new javax.swing.JButton();
        jBAdd = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jBImport = new javax.swing.JButton();
        jBImport1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jBUrl = new javax.swing.JButton();
        jTFUrl = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jRBCubaNorte = new javax.swing.JRadioButton();
        jRBCubaSur = new javax.swing.JRadioButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabla"));
        bg.add(jRBcuencas);
        //bg.add(jRBFajas);
        bg.add(jRBHidro_areal);
        bg.add(jRBHidro_lineal);
        bg.add(jRBParcelas);
        bg.add(jRBParteaguas);
        //bg.add(jRBPendiente);
        bg.add(jRBRelieve);
        bg.add(jRBSuelos);

        jRBcuencas.setSelected(true);
        jRBcuencas.setText("Cuencas");
        jRBcuencas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBcuencasActionPerformed(evt);
            }
        });

        jRBHidro_areal.setText("Hidro_areal");
        jRBHidro_areal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBHidro_arealActionPerformed(evt);
            }
        });

        jRBParteaguas.setText("Parteaguas");
        //jRBParteaguas.setEnabled(false);
        jRBParteaguas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBParteaguasActionPerformed(evt);
            }
        });

        jRBParcelas.setText("Parcelas");
        jRBParcelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBParcelasActionPerformed(evt);
            }
        });

        jRBHidro_lineal.setText("Hidro_lineal");
        jRBHidro_lineal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBHidro_linealActionPerformed(evt);
            }
        });

        jRBSuelos.setText("Suelos");
        jRBSuelos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBSuelosActionPerformed(evt);
            }
        });

        jRBRelieve.setText("Relieve");
        jRBRelieve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBRelieveActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRBcuencas)
                    .add(jRBRelieve)
                    .add(jRBParteaguas)
                    .add(jRBHidro_lineal)
                    .add(jRBHidro_areal)
                    .add(jRBParcelas)
                    .add(jRBSuelos))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jRBcuencas)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRBSuelos)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRBParcelas)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRBRelieve)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRBParteaguas)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRBHidro_lineal)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRBHidro_areal)
                .addContainerGap(126, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Sincronizar campos"));

        /*
        jLFields.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        */
        jScrollPane1.setViewportView(jLFields);

        /*
        jLValues.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        */
        jScrollPane2.setViewportView(jLValues);

        jLabel1.setText("Campos_Fichero");

        jLabel2.setText("Campos_BD");

        /*
        jLEnlaces.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        */
        jScrollPane3.setViewportView(jLEnlaces);

        jLabel3.setText("Enlaces");

        jBRemove.setText("<<");
        jBRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBRemoveActionPerformed(evt);
            }
        });

        jBAdd.setText(">>");
        jBAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddActionPerformed(evt);
            }
        });

        jBImport.setText("Importar");
        jBImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBImportActionPerformed(evt);
            }
        });

        jBImport1.setText("Eliminar datos");
        jBImport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBImport1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2))
                        .add(18, 18, 18)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jBAdd)
                            .add(jBRemove))
                        .add(7, 7, 7)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(jBImport1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jBImport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(jLabel1)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 142, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(62, 62, 62)
                        .add(jBAdd)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBRemove)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBImport)
                    .add(jBImport1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccionar fichero (.shp)"));

        jBUrl.setText("...");
        jBUrl.setEnabled(true);
        jBUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBUrlActionPerformed(evt);
            }
        });

        jTFUrl.setEditable(false);
        jTFUrl.setEnabled(true);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jBUrl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTFUrl, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTFUrl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBUrl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Proyecci�n"));
        bgpry.add(jRBCubaNorte);
        bgpry.add(jRBCubaSur);

        jRBCubaNorte.setSelected(true);
        jRBCubaNorte.setText("Cuba Norte");
        jRBCubaNorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBCubaNorteActionPerformed(evt);
            }
        });

        jRBCubaSur.setText("Cuba Sur");
        jRBCubaSur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBCubaSurActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jRBCubaNorte))
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jRBCubaSur)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jRBCubaNorte, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                .add(jRBCubaSur, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRBcuencasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBcuencasActionPerformed
     setNewPanel(CUENCAS);
     TableObjectType = FShape.POLYGON;
    }//GEN-LAST:event_jRBcuencasActionPerformed

    private void jRBHidro_arealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBHidro_arealActionPerformed
        // TODO add your handling code here:
      setNewPanel(EMBALSES);
      TableObjectType = FShape.POLYGON;
    }//GEN-LAST:event_jRBHidro_arealActionPerformed

    private void jRBHidro_linealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBHidro_linealActionPerformed
        // TODO add your handling code here:
        setNewPanel(RIOS);
        TableObjectType = FShape.LINE;
    }//GEN-LAST:event_jRBHidro_linealActionPerformed

    private void jRBParcelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBParcelasActionPerformed
        // TODO add your handling code here:
        setNewPanel(PARCELAS);
        TableObjectType = FShape.POLYGON;
    }//GEN-LAST:event_jRBParcelasActionPerformed

    private void jRBParteaguasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBParteaguasActionPerformed
        // TODO add your handling code here:
        setNewPanel(PARTEAGUAS);
        TableObjectType = FShape.POLYGON;
    }//GEN-LAST:event_jRBParteaguasActionPerformed

    private void jRBRelieveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBRelieveActionPerformed
        // TODO add your handling code here:
        setNewPanel(RELIEVE);
        TableObjectType = FShape.LINE;
    }//GEN-LAST:event_jRBRelieveActionPerformed

    private void jRBSuelosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBSuelosActionPerformed
        // TODO add your handling code here:
        setNewPanel(SUELOS);
        TableObjectType = FShape.POLYGON;
    }//GEN-LAST:event_jRBSuelosActionPerformed

    private void jBUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBUrlActionPerformed
        // TODO add your handling code here:
        int res = showFileChooser(false, "Seleccionar fichero", "shp", "Ficheros .shp");
        if (res == jFileChooser1.APPROVE_OPTION) {
        file = jFileChooser1.getSelectedFile();
        if (file != null)
        {
	          try {
	        	  
				layer = LayerFactory.createLayer(activeTable, (VectorialFileDriver)LayerFactory.getDM().getDriver("gvSIG shp driver"), file,  proyection);
				
			  //Verifico que el fichero a cargar sea del mismo tipo de geometr�a que la tabla hacia la que se importar�	
				int shapetype = ((FLyrVect)layer).getShapeType();
				if(shapetype != TableObjectType)
				{
				 JOptionPane.showMessageDialog(null, "El tipo de geometr�a del fichero difiere del tipo de geometr�a de la tabla seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
				 return;
				}
				
				jTFUrl.setText(file.getAbsolutePath());
				
				DataSource ds = ((AlphanumericData)layer).getRecordset();
				
				mValues.removeAllElements();
				for(int i=0; i<ds.getFieldCount(); i++)
				 mValues.addElement(ds.getFieldName(i));	
				
				mValues.addElement("Vac�o");
	          
	           } catch (DriverLoadException e) {
				e.printStackTrace();
			   } catch (ReadDriverException e) {
				e.printStackTrace();
			   }
        }
        }
}//GEN-LAST:event_jBUrlActionPerformed

    private void jRBCubaNorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBCubaNorteActionPerformed
    	proyection = CRSFactory.getCRS("EPSG:2085");
    	Global.projeccionActiva = CRSFactory.getCRS("EPSG:2085");;
    }//GEN-LAST:event_jRBCubaNorteActionPerformed

    private void jRBCubaSurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBCubaSurActionPerformed
        proyection = CRSFactory.getCRS("EPSG:2086");
        Global.projeccionActiva =  CRSFactory.getCRS("EPSG:2086");;
    }//GEN-LAST:event_jRBCubaSurActionPerformed

    private void jBAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddActionPerformed

      int fieldIndex = jLFields.getSelectedIndex();
      int valueIndex = jLValues.getSelectedIndex();
      String mess = null;
      
      if(fieldIndex<0  && valueIndex<0)
       mess = "Debe seleccionar un elemento de la lista de campos de la tabla "+activeTable+", "+"\n"+
              "y un elemento de la lista de campos del fichero para que puedan ser 'enlazados'";
      else
      if(fieldIndex < 0)
       mess = "Debe seleccionar un elemento de la lista de campos de la tabla "+activeTable;
      else
      if(valueIndex < 0)	  
       mess = "Debe seleccionar un elemento de la lista de campos del fichero";
      
      if(mess !=null)
      {
       JOptionPane.showMessageDialog(null, mess, "Error tratando de enlazar campos", JOptionPane.WARNING_MESSAGE);
       return;
      }	  
      
      String field = mFields.getElementAt(fieldIndex).toString();
      String value = mValues.getElementAt(valueIndex).toString();

      links.put(field, value);
      mEnlaces.addElement(field +"-->"+ value);
      jLEnlaces.setSelectedIndex(0);
      
      mFields.remove(jLFields.getSelectedIndex());
      if(!value.equals("Vac�o"))
       mValues.remove(jLValues.getSelectedIndex());
    }//GEN-LAST:event_jBAddActionPerformed

    private void jBRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBRemoveActionPerformed

      int index = jLEnlaces.getSelectedIndex();
      
      if(index < 0)
      {
    	JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la lista", "Informaci�n", JOptionPane.WARNING_MESSAGE);
    	return;
      } 	
      
      String selected = mEnlaces.getElementAt(index).toString();
      String field = selected.substring(0, selected.indexOf("-"));
      String value = selected.substring(selected.indexOf(">")+1, selected.length());

      links.remove(field);
      mEnlaces.remove(index);
      
      mFields.addElement(field);
      mValues.addElement(value);
    }//GEN-LAST:event_jBRemoveActionPerformed

    private void jBImportActionPerformed(java.awt.event.ActionEvent evt) 
    {
     if( mFields.getSize() < 1)
     {
      try {
		PluginServices.cancelableBackgroundExecution(new ImportWT(links, layer, coldef));
	  } catch (ReadDriverException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
     }  	
     else
      JOptionPane.showMessageDialog(null, "Faltan campos por enlazar");	
     }

    private void jBImport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBImport1ActionPerformed
     PluginServices.getMDIManager().setWaitCursor();
     
		try {
		    	
			if(activeTable.equals(CUENCAS))
			{
			 CuencaManager cman = new CuencaManager();
			 cman.eliminarCuencas();
			}	
			else
			if(activeTable.equals(EMBALSES))
			 EmbalseManager.eliminarEmbalses();
			else
			if(activeTable.equals(RIOS))
			 RioManager.eliminarRios();
			else
			if(activeTable.equals(PARCELAS))
			 ParcelaManager.eliminarParcelas();
			else
		    if(activeTable.equals(PARTEAGUAS))
			 ParteaguasManager.eliminarParteaguas();
		    else
			if(activeTable.equals(RELIEVE))
			 RelieveManager.eliminarCurvasDeNivel();
			else
		    if(activeTable.equals(SUELOS))
			 SueloManager.eliminarSuelos();
			
		} catch (ReadDriverException e) {
			e.printStackTrace();
		}
     
     
     JOptionPane.showMessageDialog(null, "Elementos de "+activeTable+" eliminados");
     PluginServices.getMDIManager().restoreCursor();
    }//GEN-LAST:event_jBImport1ActionPerformed

    private void setNewPanel(String table)
    {
     activeTable = table;
     mFields.removeAllElements();
     mValues.removeAllElements();
     mEnlaces.removeAllElements();
     jTFUrl.setText("");
     links.clear();
     
//     String sql = "select a.attname from pg_attribute a, pg_class b where b.relfilenode=a.attrelid " +
//     "and b.relname = '" + table + "' and a.attname not in ('tableoid','cmax','xmax','cmin''xmin','ctid','cmin','xmin','gid')"; 
//      db.executeQuery(sql);
//     
//     if(!db.isEmpty())
//     { 	 
//      for(int i=0; i<db.getRowCount(); i++)
//       mFields.addElement(db.getValueAt(i, 0));
         
        setFieldsToListModel(table);
        coldef = mFields.toArray();
//    //  for(int i=0; i<coldef.length; i++)
//      // System.out.println(coldef[i]);
//      
//     // mFields.removeElement("gid");
       mFields.removeElement("the_geom");
//     } 
    }

    private void setFieldsToListModel(String table)
    {
     String otrosCampos = "";	
     if(table.equals(PARCELAS))
      otrosCampos = ",'uso','suelo'"; 	
     
     String sql = "select a.attname from pg_attribute a, pg_class b where b.relfilenode=a.attrelid " +
     "and b.relname = '" + table + "' and a.attname not in ('tableoid','cmax','xmax','cmin''xmin','ctid','cmin','xmin','gid','municipio','cuenca'"+otrosCampos+")"; 
     db.ejecutarConsulta(sql);
     if(!db.isEmpty())
     { 	 
      for(int i=0; i<db.getRowCount(); i++)
       mFields.addElement(db.getValueAt(i,0));
      
    //-----------------------------------------------------------------------------------------------------  
      if(table.equals(RIOS) || table.equals(EMBALSES))
      {
       sql = "select a.attname from pg_attribute a, pg_class b where b.relfilenode=a.attrelid " +
       	     "and b.relname = 'hidrografia' and a.attname not in ('tableoid','cmax','xmax','cmin''xmin','ctid','cmin','xmin','id','tipo_hidrografia','parteaguas','cuenca')"; 
       db.ejecutarConsulta(sql);
       if(!db.isEmpty())
        for(int i=0; i<db.getRowCount(); i++)
         mFields.addElement(db.getValueAt(i,0));
      }
    //-----------------------------------------------------------------------------------------------------  
      if(table.equals(PARCELAS))
       mFields.addElement("codigo_uso");
     } 
    }
    
    /**Segun los parametros establecidos inicializa un filechooser y devuelve el valor de seleccion*/
    private int showFileChooser(boolean multisel, String title, String filterExt, String filterDesc)
    {
    	jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser1.setMultiSelectionEnabled(multisel);
    	jFileChooser1.setDialogTitle(title);
        if(file != null)
         jFileChooser1.setCurrentDirectory(file);
        filter=new ExampleFileFilter(filterExt, filterDesc);
        jFileChooser1.addChoosableFileFilter(filter);
         
        return jFileChooser1.showOpenDialog(null);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bg;
    private javax.swing.ButtonGroup bgpry;
    private javax.swing.JButton jBAdd;
    private javax.swing.JButton jBImport;
    private javax.swing.JButton jBImport1;
    private javax.swing.JButton jBRemove;
    private javax.swing.JButton jBUrl;
    private javax.swing.JList jLEnlaces;
    private javax.swing.JList jLFields;
    private javax.swing.JList jLValues;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRBCubaNorte;
    private javax.swing.JRadioButton jRBCubaSur;
    private javax.swing.JRadioButton jRBHidro_areal;
    private javax.swing.JRadioButton jRBHidro_lineal;
    private javax.swing.JRadioButton jRBParcelas;
    private javax.swing.JRadioButton jRBParteaguas;
    private javax.swing.JRadioButton jRBRelieve;
    private javax.swing.JRadioButton jRBSuelos;
    private javax.swing.JRadioButton jRBcuencas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTFUrl;
    // End of variables declaration//GEN-END:variables
    private DefaultListModel mFields;
    private DefaultListModel mValues;
    private DefaultListModel mEnlaces;
    private ExampleFileFilter filter=null;
    private JFileChooser jFileChooser1;
    private File file;
    private IProjection proyection;
    private Object[] coldef;
    private String activeTable;
    private int TableObjectType;
    private HashMap<String,String> links;
    private JDBCAdapter db; 
    private FLayer layer;
    public static final String CUENCAS = "cuencas";
    public static final String EMBALSES = "embalses";
    public static final String RIOS = "rios";
    public static final String PARCELAS = "parcelas";
    public static final String PARTEAGUAS = "parteaguas";
    public static final String RELIEVE = "relieve";
    public static final String SUELOS = "_suelos";
}
