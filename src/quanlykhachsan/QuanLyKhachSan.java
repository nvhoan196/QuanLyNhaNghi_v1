/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quanlykhachsan;

import fxmls.FXMLDangNhapController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Admin
 */
public class QuanLyKhachSan extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("/fxmls/FXMLDangNhap.fxml"));
        VBox root = loader.load();
        FXMLDangNhapController controller = loader.getController();
        
        Scene scene = new Scene(root);
        controller.scene = scene;
        controller.stage = primaryStage;
        
        primaryStage.setTitle("Đăng nhập hệ thống Khách Sạn !");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
