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
import javax.swing.JTextField;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

/**
 *
 * @author Raisel
 */
public class pCuenca extends Panel {

    /** Creates new form pParteaguas */
    public pCuenca(String title) {
        initComponents();
        _title  = title;
//        _panel_id = "_"+title;
//        UIUtils.AddPanel(this);
        jTFNombre.requestFocusInWindow();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBGuardar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTFNombre = new javax.swing.JTextField();

        jBGuardar.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\guardar.png")); // NOI18N
        jBGuardar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"guardar.png"));
        jBGuardar.setText("Guardar");
        jBGuardar.setEnabled(false);

        jBCancelar.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\cancelar.png")); // NOI18N
        jBCancelar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+"\\images\\cancelar.png"));
        jBCancelar.setText("Cancelar");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Nombre");

        jTFNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFNombreKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(174, Short.MAX_VALUE)
                .addComponent(jBGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar)
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar)
                    .addComponent(jBGuardar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTFNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFNombreKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jTFNombre.getText(), caracter, 'S', 50))
      evt.consume();
    }//GEN-LAST:event_jTFNombreKeyTyped

public JButton getButtonGuardar(){
 return jBGuardar;
}

public JButton getButtonCancelar(){
 return jBCancelar;
}

public JTextField getTFNombre(){
 return jTFNombre;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFNombre;
    // End of variables declaration//GEN-END:variables

}
