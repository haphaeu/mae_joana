/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package curvebuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author rarossi
 */
public class ResultsVisualiser extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //stage.getIcons().add(new Image("icon.png"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon16.png"))); 
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon24.png"))); 
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon32.png"))); 
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon48.png"))); 
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon96.png"))); 
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon128.png"))); 
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon256.png"))); 
        stage.setTitle("ResultsVisualiser for rlc - v1.0");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setMinWidth(640);
        stage.setMinHeight(360);
        
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
