/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * pParteaguas.java
 *
 * Created on 27-feb-2012, 10:53:37
 */

package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

/**
 *
 * @author Raisel
 */
public class pRed_drenaje extends Panel {

    /** Creates new form pParteaguas */
    public pRed_drenaje(String title) {
        initComponents();
        _title  = title;
//        _panel_id = "_"+title;
//        UIUtils.AddPanel(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLParteaguas = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jBCancelar = new javax.swing.JButton();
        jBGuardar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTFOrden = new javax.swing.JTextField();
        jTFSiguiente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setFocusCycleRoot(true);

        jLParteaguas.setText("cuenca:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Parteaguas:");

        jBCancelar.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\cancelar.png")); // NOI18N
        jBCancelar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"cancelar.png"));
        jBCancelar.setText("Cancelar");

        jBGuardar.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\guardar.png")); // NOI18N
        jBGuardar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"guardar.png"));
        jBGuardar.setText("Guardar");
        jBGuardar.setEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Orden");

        jTFOrden.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFOrden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFOrdenKeyTyped(evt);
            }
        });

        jTFSiguiente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFSiguiente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFSiguienteKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Siguiente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTFOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTFSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jBGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBCancelar)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLParteaguas, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLParteaguas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar)
                    .addComponent(jBGuardar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTFOrdenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFOrdenKeyTyped
       char caracter = evt.getKeyChar();
       if(!Funciones_Utiles.formatoInputTextField(jTFOrden.getText(), caracter, 'I', 7))
        evt.consume();
    }//GEN-LAST:event_jTFOrdenKeyTyped

    private void jTFSiguienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSiguienteKeyTyped
       char caracter = evt.getKeyChar();
       if(!Funciones_Utiles.formatoInputTextField(jTFSiguiente.getText(), caracter, 'I', 7))
        evt.consume();
    }//GEN-LAST:event_jTFSiguienteKeyTyped

public JButton getButtonGuardar(){
 return jBGuardar;
}

public JButton getButtonCancelar(){
 return jBCancelar;
}

public JTextField getTForden(){
 return jTFOrden;
}

public JTextField getTFSiguiente(){
 return jTFSiguiente;
}

public JLabel getLabelCuenca(){
 return jLParteaguas;
}

public void windowClosed() 
{
	super.windowClosed();
	try {
		//Si se esta mostrando la ficha de informacion.
		//Pregunto para que si se esta mostrando la informacion de un elemento
		//y a la misma vez se esta editando un elemento de otra capa, no se termine la edicion 
		//en esa otra capa
	    if(jBCancelar.isVisible())	
		 pGestionManager.terminarEdicion();
		
	} catch (CancelEditingTableException e) {
		e.printStackTrace();
	} catch (ReloadLayerException e) {
		e.printStackTrace();
	} catch (CancelEditingLayerException e) {
		e.printStackTrace();
	} catch (StartEditionLayerException e) {
		e.printStackTrace();
	}
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JLabel jLParteaguas;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFOrden;
    private javax.swing.JTextField jTFSiguiente;
    // End of variables declaration//GEN-END:variables

}
