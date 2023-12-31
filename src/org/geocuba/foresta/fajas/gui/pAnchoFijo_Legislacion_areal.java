package org.geocuba.foresta.fajas.gui;

import javax.swing.JOptionPane;

import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import org.geocuba.foresta.fajas.extensiones.AnchoFijo_LegislacionExt;
import org.geocuba.foresta.fajas.writerTasks.GenerarFaja_AnchoLegislacion;
import org.geocuba.foresta.fajas.writerTasks.GenerarFaja_Embalse;
import com.iver.andami.PluginServices;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;

/**
 *
 * @author Raisel
 */
public class pAnchoFijo_Legislacion_areal extends Panel{/*/javax.swing.JPanel {

    /** Creates new form pAnchoFijo_Legislacion_lineal */
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public pAnchoFijo_Legislacion_areal() {
        initComponents();
        _title="Faja Forestal (hidrograf�a areal)";
        _panel_id="anchofijo_legisl_lineal";
        //_centered=true;
        jTFAncho.requestFocusInWindow();
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

        bgSeleccion = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jTFAncho = new javax.swing.JTextField();
        jRBAnchoLegisl = new javax.swing.JRadioButton();
        jRBAnchoFijo = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jBAceptar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();

        setFocusCycleRoot(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("M�todo"));
        bgSeleccion.add(jRBAnchoFijo);
        bgSeleccion.add(jRBAnchoLegisl);

        jTFAncho.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFAncho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFAnchoKeyTyped(evt);
            }
        });

        jRBAnchoLegisl.setText("Ancho por legislaci�n");
        jRBAnchoLegisl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBAnchoLegislActionPerformed(evt);
            }
        });

        jRBAnchoFijo.setSelected(true);
        jRBAnchoFijo.setText("Ancho fijo (m)");
        jRBAnchoFijo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBAnchoFijoActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
            .add(jPanel2Layout.createSequentialGroup()
                .add(11, 11, 11)
                .add(jRBAnchoFijo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jTFAncho, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jRBAnchoLegisl)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTFAncho, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jRBAnchoFijo))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 6, Short.MAX_VALUE)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5)
                .add(jRBAnchoLegisl)
                .addContainerGap())
        );

        jBAceptar.setText("Aceptar");
        jBAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAceptarActionPerformed(evt);
            }
        });

        jBCancelar.setText("Cancelar");
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .add(jBAceptar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jBCancelar)
                .add(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBCancelar)
                    .add(jBAceptar))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jBAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAceptarActionPerformed
        // TODO add your handling code here:

    	if(jRBAnchoFijo.isSelected())
    	{
    	  if(jTFAncho.getText().trim().equals(""))
    	  {
    	   JOptionPane.showMessageDialog(null, "Debe introducir un valor");
    	   return;
    	  } 	
    	  
    	  double value = Double.parseDouble(jTFAncho.getText());	
    	  PluginServices.cancelableBackgroundExecution(new GenerarFaja_Embalse(value, "Generando Faja...", AnchoFijo_LegislacionExt.selectedLayer));
    	} 
    	else
    	{	 
    	 //Faja de ancho legislativo
      	   PluginServices.cancelableBackgroundExecution(new GenerarFaja_AnchoLegislacion("Generando Fajas...", AnchoFijo_LegislacionExt.selectedLayer));
        }	
    	Close();
    }//GEN-LAST:event_jBAceptarActionPerformed

    private void jRBAnchoLegislActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBAnchoLegislActionPerformed

     jTFAncho.setEnabled(false);
    }//GEN-LAST:event_jRBAnchoLegislActionPerformed

    private void jRBAnchoFijoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBAnchoFijoActionPerformed

     jTFAncho.setEnabled(true);
    }//GEN-LAST:event_jRBAnchoFijoActionPerformed

    private void jTFAnchoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFAnchoKeyTyped
       char caracter = evt.getKeyChar();
       if(!Funciones_Utiles.formatoInputTextField( jTFAncho.getText(), caracter, 'D', 7))
        evt.consume();
    }//GEN-LAST:event_jTFAnchoKeyTyped

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        // TODO add your handling code here:
        Close();
}//GEN-LAST:event_jBCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgSeleccion;
    private javax.swing.JButton jBAceptar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRBAnchoFijo;
    private javax.swing.JRadioButton jRBAnchoLegisl;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTFAncho;
    // End of variables declaration//GEN-END:variables

}
