/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmls;

import com.sun.javafx.tk.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javax.swing.JDialog;

/**
 *
 * @author Admin
 */
public class LoadingDialog extends JDialog{
    static int width = 250, height = 75;
    ArrayList<Runnable> jobList = new ArrayList<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    
    public LoadingDialog() {
        try {
            setSize(width, height);
            setType(Type.UTILITY);
            setUndecorated(true);
            setAlwaysOnTop(true);
            
            
            BorderPane root = FXMLLoader.load(getClass().getResource("/fxmls/FXMLLoading.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            JFXPanel fxpanel = new JFXPanel();
            getContentPane().add(fxpanel);
            fxpanel.setScene(scene);
            getContentPane().setBackground(new java.awt.Color(255,255,255,0));
            setBackground(new java.awt.Color(255,255,255,0));
            
            Platform.runLater(() -> {
                int x = (int)Screen.getPrimary().getBounds().getWidth()/2-width/2;
                int y = (int)Screen.getPrimary().getBounds().getHeight()/2-height/2;
                setLocation(x, y);
                toFront();
                repaint();
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            });
        } catch (IOException ex) {
            Logger.getLogger(LoadingDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addRunnable(Runnable run) {
        jobList.add(run);
    }
    
    public void run() {
        setVisible(true);
        for (int i=0; i<jobList.size(); i++) {
            executor.execute(jobList.get(i));
        }
        executor.execute(() -> {
            dispose();
            executor.shutdownNow();
        });
    }
    
    public void run(Boolean isNestedLoop) {
        setVisible(true);
        
        for (int i=0; i<jobList.size(); i++) {
            executor.execute(jobList.get(i));
        }
        executor.execute(() -> {
            if (isNestedLoop == true) {
                Platform.runLater(() -> {
                    Toolkit.getToolkit().exitNestedEventLoop(this, null);
                });
            }
            dispose();
            executor.shutdownNow();
        });
        if (isNestedLoop == true) {
            Toolkit.getToolkit().enterNestedEventLoop(this);
        }
    }
    
    public void clearRunnable(){
        jobList.clear();
    }
    
    public static void execute(Runnable run) {
        LoadingDialog ld = new LoadingDialog();
        ld.addRunnable(run);
        ld.run();
    }
    
    public static void executeNonModal(Runnable run) {
        LoadingDialog ld = new LoadingDialog();
        ld.addRunnable(run);
        ld.run(false);
    }

}
