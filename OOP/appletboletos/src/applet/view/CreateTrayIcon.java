package applet.view;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

import applet.controller.AppletBoletosController;
import applet.model.MessagesList;

public class CreateTrayIcon {
	
    public static final TrayIcon trayIcon = new TrayIcon(createImage("/images/icone.png", "tray icon"));
    public static SystemTray tray = SystemTray.getSystemTray();
    
	public static void startTray() {
      
		createAndShowGUI();
		
    }
    
    private static void createAndShowGUI() {
        
        if (!SystemTray.isSupported()) {
            System.out.println("O Sistema atual n�o suporta tray icon!");
            return;
        }
        
        final PopupMenu popup = new PopupMenu();

        
        // Create a popup menu components
        MenuItem gerarBoletos = new MenuItem("Gerar Boletos");       
        CheckboxMenuItem autoGenerate = new CheckboxMenuItem("Gerar automaticamente");
        MenuItem configItem = new MenuItem("Configura��es");
        MenuItem fecharPrograma = new MenuItem("Fechar Programa");
        
        //Add components to popup menu
        popup.add(gerarBoletos);
        popup.add(autoGenerate);
        popup.addSeparator();
        popup.add(configItem);
        popup.addSeparator();
        popup.add(fecharPrograma);
        
        autoGenerate.setState(AppletBoletosController.appletBoleto.getAutoGenerate());
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("O �cone na barra de notifica��es n�o p�de ser criado");
            return;
        }
        
        trayIcon.addActionListener( al -> AppletBoletosMain.frame.setVisible(true) );
        
        gerarBoletos.addActionListener( 
        		al -> {		        			

        				if (AppletBoletosController.appletBoleto.getAuthenticated() == null || 
        					!AppletBoletosController.appletBoleto.getAuthenticated()) {	        				
        					trayIcon.displayMessage("Autentica��o", MessagesList.AUTH_FAILURE.message , TrayIcon.MessageType.WARNING );
        					return;
        				}
        			    
        			    int cnt = AppletBoletosController.gerarBoletos();
        			    
        			    if(cnt != 0)
        			    	trayIcon.displayMessage("Info", cnt + " Boletos processados!", TrayIcon.MessageType.INFO );
        			    else
        			    	trayIcon.displayMessage("Info", MessagesList.NO_BOL_FILES.message , TrayIcon.MessageType.INFO );
        			    
        				
        		      } 
        );
        
        autoGenerate.addItemListener( il -> {
        	if(il.getStateChange() == ItemEvent.SELECTED)
        		AppletBoletosController.appletBoleto.setAutoGenerate(Boolean.TRUE);
        	else
        		AppletBoletosController.appletBoleto.setAutoGenerate(Boolean.FALSE);
        } );
        
        configItem.addActionListener( al -> ConfigsView.initConfig() );
        
        fecharPrograma.addActionListener( al -> {
        	tray.remove(trayIcon);
        	System.exit(0);
        }); 
                 

    }    
    
    protected static Image createImage(String path, String description) {
        URL imageURL = CreateTrayIcon.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("�cone n�o encontrado! " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}


