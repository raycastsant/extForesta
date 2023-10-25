package org.geocuba.foresta.herramientas.utiles.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Hashtable;

import javax.swing.InputMap;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.theme.Theme;

public class UIUtils {
	
	private static Hashtable<String, Panel> _ui;
	
	static Hashtable<String, Panel> UI()
	{
		if(_ui==null)
		{
			_ui=new Hashtable<String, Panel>();
		}
		return _ui;
	}
	
	public static void AddPanel(Panel panel)
	{
		UI().put(panel.get_panel_id(),panel);
		inhabilitaPegar(panel);
	}
	
	public static void RemovePanel(Panel panel)
	{
		UI().remove(panel.get_panel_id());
	}
	
	public static Panel GetPanel(String panel_id)
	{
		return UI().get(panel_id);
	}
	
	public static Image getThemeIcon() {
    	Theme theme=new Theme();
    	String name=PluginServices.getArgumentByName("andamiTheme");
    	File file;
    	if (name==null){
    		file=new File("theme/andami-theme.xml");
    	}else{
    		file=new File(name);
    	}

    	if (file.exists()) {
			theme.readTheme(file);
		}
		return theme.getIcon().getImage();
	}
	
	public static void inhabilitaPegar(Container contenedor) 
	   { 
	       Component [] componentes = contenedor.getComponents(); 
	        for (int i = 0; i < componentes.length; i++) {            
	           if (componentes[i] instanceof JTextField) 
	           { 
	                InputMap map = ((JTextField)componentes[i]).getInputMap(); 
	                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK), "null"); 
	                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, Event.SHIFT_MASK), "null"); 
	           } 
	           else 
	               if (componentes [i] instanceof Container) 
	                   inhabilitaPegar((Container)componentes[i]); 
	       } 
	    } 


}
