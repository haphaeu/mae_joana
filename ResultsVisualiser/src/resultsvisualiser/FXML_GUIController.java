/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resultsvisualiser;

import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;


/**
 *
 * @author rarossi
 */
public class FXML_GUIController implements Initializable {
    
    @FXML private Label lblTest;
    @FXML private ComboBox<String> comboBox;
    @FXML private ScatterChart chart;
    @FXML private NumberAxis xAxis, yAxis;
    @FXML private ListView<String> listHs;
    @FXML private ListView<String> listTp;
    @FXML private ListView<String> listHeading;
    @FXML private MenuBar menuBar;

    
    ObservableList<String> selectedHs;
    ObservableList<String> selectedTp;
    ObservableList<String> selectedHeading;
    ResultsMatrix results;
    boolean readyToPlot = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        listHs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listTp.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listHeading.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        listHs.getSelectionModel().selectedItemProperty().addListener(new ListenerLists());
        listTp.getSelectionModel().selectedItemProperty().addListener(new ListenerLists());
        listHeading.getSelectionModel().selectedItemProperty().addListener(new ListenerLists());
        
        comboBox.valueProperty().addListener(new ListenerComboBoxChange());
        
        xAxis.setAutoRanging(false);
        
    }    
    

    
    private void openFile(File file) {
        results = new ResultsMatrix(file);
        populateItems();
    }

    private void populateItems() {
        comboBox.getItems().addAll(results.getVars());
        comboBox.getSelectionModel().selectFirst();
        
        ObservableList<String> data;
        
        data = FXCollections.observableArrayList(results.getHsSetasString());
        listHs.setItems(data);
        
        
        data = FXCollections.observableArrayList(results.getTpSetasString());
        listTp.setItems(data);
        
        data = FXCollections.observableArrayList(results.getWDSetasString());
        listHeading.setItems(data);
        
        listHs.getSelectionModel().selectFirst();
        listTp.getSelectionModel().selectFirst();
        listHeading.getSelectionModel().selectFirst();
        
        plot();
    }

    private void plot() {
        chart.getData().clear();
        if (!readyToPlot) {
            return;
        }
        // sketch
        // only 1 selected item
        String var = comboBox.getSelectionModel().getSelectedItem();
        
        double hs, tp, wd, min, max, range;
        double[] data = null;
        XYChart.Series series;
        min = 99999.9;
        max = -99999.9;
        
        for (int iHs=0; iHs < selectedHs.size(); iHs++) {
            hs = Double.parseDouble(selectedHs.get(iHs));
            
            for (int iTp=0; iTp < selectedTp.size(); iTp++) {
                tp = Double.parseDouble(selectedTp.get(iTp));
                
                for (int iwd=0; iwd < selectedHeading.size(); iwd++) {
                    wd = Double.parseDouble(selectedHeading.get(iwd));
                    
                    data = results.getSample(var, hs, tp, wd);
                    if (data == null) {
                        continue;
                    }
                    XYChart.Series series1 = new XYChart.Series();
                    series1.setName("Hs"+hs+"Tp"+tp+"wd"+wd);
                    
                    for (int i=0; i < data.length; i++) {
                        series1.getData().add(
                                new XYChart.Data(data[i], 
                                                 -Math.log(-Math.log((i+0.5)/data.length))));
                        if (data[i] > max) { max = data[i]; }
                        if (data[i] < min) { min = data[i]; }
                    }
                    chart.getData().addAll(series1);
                    range = max - min;
                    xAxis.setLowerBound(min - 0.05*range);
                    xAxis.setUpperBound(max + 0.05*range);
                    xAxis.setTickUnit(1);
                }
            }
        }            
    }
    
    @FXML protected void handleButtonAction(ActionEvent event) {
        lblTest.setText("Hello World!");
        plot();
    }
    @FXML protected void handleMenuFileOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            openFile(file);
        }
    }
    
    class ListenerComboBoxChange implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String>obv, String ov, String nv) {
            System.out.println("Combo listener called");
            plot();
        }
    }
    class ListenerLists implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> obv, String ov, String nv) {
            System.out.println("Listener called.");
            
            System.out.print("Selected Hs:");
            selectedHs = listHs.getSelectionModel().getSelectedItems();
            if (! selectedHs.isEmpty()) {
                selectedHs.forEach((si) -> { System.out.print(" " + si); });
            }
            System.out.println("");
            
            System.out.print("Selected Tps:");
            selectedTp = listTp.getSelectionModel().getSelectedItems();
            if (! selectedTp.isEmpty()) {
                selectedTp.forEach((si) -> { System.out.print(" " + si); });
            }
            System.out.println("");
            
            System.out.print("Selected headings:");
            selectedHeading = listHeading.getSelectionModel().getSelectedItems();
            if (! selectedHeading.isEmpty()) {
                selectedHeading.forEach((si) -> { System.out.print(" " + si); });
            }
            System.out.println("");
            
            readyToPlot = !(selectedHs.isEmpty() 
                            || selectedTp.isEmpty()
                            || selectedHeading.isEmpty());

            plot();
        }
    }
    
}
