package resultsvisualiser;

import java.io.File;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;


/**
 * FXML_GUIController
 * 
 * Controller class for the ResultsVisualiser application.
 * 
 * Graphical tool to visualize results from the repeated lowering control
 * module. 
 * 
 * Imports a text file, typically "results.txt", and plots the sample
 * cdf in a Gumbel paper (log-log).
 * 
 * @author Rafael Rossi
 * @date 10.09.2017
 */
public class FXML_GUIController implements Initializable {
    
    @FXML private Label lblTest;
    @FXML private ComboBox<String> comboBox;
    @FXML private ScatterChart chart;
    @FXML private NumberAxis xAxis, yAxis;
    @FXML private ListView<String> listHs;
    @FXML private ListView<String> listTp;
    @FXML private ListView<String> listHeading;
    @FXML private Button btnOpen;
    @FXML private Button btnCopy;
    @FXML private RadioButton radioLogLog, radioCDF, radioMin, radioMax;

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
        
        listHs.getSelectionModel().selectedItemProperty().addListener(new ListenerListHs());
        listTp.getSelectionModel().selectedItemProperty().addListener(new ListenerLists());
        listHeading.getSelectionModel().selectedItemProperty().addListener(new ListenerLists());
        
        comboBox.valueProperty().addListener(new ListenerComboBoxChange());
        
        selectedHs = listHs.getSelectionModel().getSelectedItems();
        selectedTp = listTp.getSelectionModel().getSelectedItems();
        selectedHeading = listHeading.getSelectionModel().getSelectedItems();
        
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(1);
        xAxis.setTickUnit(0.1);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
    }    

    private void openFile(File file) {
        results = new ResultsMatrix(file);
        comboBox.getItems().clear();
        populateItems();
    }

    // Populates the GUI items, except listTp, with the values read from file.
    private void populateItems() {
        comboBox.getItems().addAll(results.getVars());
        comboBox.getSelectionModel().selectFirst();
        
        ObservableList<String> data;
  
        data = FXCollections.observableArrayList(results.getHsSetasString());
        listHs.setItems(data);
        
        data = FXCollections.observableArrayList(results.getWDSetasString());
        listHeading.setItems(data);
        
        listHs.getSelectionModel().selectFirst();
        listHeading.getSelectionModel().selectFirst();
        updateTpList();
        listTp.getSelectionModel().selectFirst();
        plot();
    }
    // Populates ot updates the list of Tps according to the selected Hs values
    private void updateTpList() {
        ObservableList<String> data;
        HashSet<String> hashTp = new HashSet<String>();
        for (int i=0; i < selectedHs.size(); i++) {
            hashTp.addAll(Arrays.asList(results.getTpSetasString(
                    Double.parseDouble(selectedHs.get(i)))));
        }
        data = FXCollections.observableArrayList(hashTp);
        sortCollection(data);
        listTp.setItems(data);
    }
    // Auxiliary function to sort the items in a list.
    // These items are usually string, which do not sort as numbers.
    // So need to convert to numbers, sort, and then back to string...
    private void sortCollection(ObservableList<String> data) {
        double[] ddata = new double[data.size()];
        String[] sdata = new String[data.size()];
        for (int i=0; i < ddata.length; i++) {
            ddata[i] = Double.parseDouble(data.get(i));
        }
        Arrays.sort(ddata);
        for (int i=0; i < sdata.length; i++) {
            sdata[i] = Double.toString(ddata[i]);
        }
        data.clear();
        data.addAll(sdata);
    }

    // Make the plots for all selected Hs, Tp and directions.
    //
    // TBI: 
    //   - Gumbel fit plot
    //   - key percentile P90
    //
    private void plot() {
        chart.getData().clear();
        if (!readyToPlot) {
            return;
        }
        String var = comboBox.getSelectionModel().getSelectedItem();
        
        double hs, tp, wd, min, max, range, y;
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
                    series1.setName("Hs" + hs + "Tp" + tp + "wd" + wd);
                           
                    for (int i=0; i < data.length; i++) {
                        if (radioMax.isSelected()) {
                            y = (i+0.5)/data.length;
                        } else {
                            y = (data.length-i-0.5)/data.length;
                        }
                        if (radioLogLog.isSelected()) {
                            y = -Math.log(-Math.log(y));
                        } 
                        series1.getData().add(new XYChart.Data(data[i], y));
                        if (data[i] > max) { max = data[i]; }
                        if (data[i] < min) { min = data[i]; }
                    }
                    chart.getData().addAll(series1);
                    range = max - min;
                    xAxis.setLowerBound(min - 0.05*range);
                    xAxis.setUpperBound(max + 0.05*range);
                    xAxis.setTickUnit(1+(int)range/10);
                    
                }
            }
        }            
    }
    
    // Handler for Open button
    @FXML protected void handleBtnOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(btnOpen.getScene().getWindow());
        if (file != null) {
            openFile(file);
        }
    }
    // Handler for Copy button
    @FXML protected void handleBtnCopy(ActionEvent event) {
        WritableImage image = chart.snapshot(new SnapshotParameters(), null);
        ClipboardContent cc = new ClipboardContent();
        cc.putImage(image);
        Clipboard.getSystemClipboard().setContent(cc);
    }
    // Handler for radio button changes affecting the plot
    @FXML protected void handleRadios(ActionEvent event) {
        if (radioLogLog.isSelected()) {
            yAxis.setLabel("-log(-log(cdf))");
        } else {
            yAxis.setLabel("cdf");
        }
        plot();
    }
    // Handler for changes in the comboBox
    class ListenerComboBoxChange implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String>obv, String ov, String nv) {
            String var = comboBox.getSelectionModel().getSelectedItem();
            if (var.toLowerCase().contains("max")) {
                radioMax.setSelected(true);
            } else if (var.toLowerCase().contains("min")) {
                radioMin.setSelected(true);
            }
            plot();
        }
    }
    // Handler for changes in the Hs list
    class ListenerListHs implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> obv, String ov, String nv) {
            selectedHs = listHs.getSelectionModel().getSelectedItems();
            readyToPlot = !(selectedHs.isEmpty() 
                            || selectedTp.isEmpty()
                            || selectedHeading.isEmpty());
            updateTpList();
            plot();
        }
    }
    // Handler for changes in the Tp and headings lists
    class ListenerLists implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> obv, String ov, String nv) {
            selectedTp = listTp.getSelectionModel().getSelectedItems();
            selectedHeading = listHeading.getSelectionModel().getSelectedItems();
            readyToPlot = !(selectedHs.isEmpty() 
                            || selectedTp.isEmpty()
                            || selectedHeading.isEmpty());
            plot();
        }
    }
}
