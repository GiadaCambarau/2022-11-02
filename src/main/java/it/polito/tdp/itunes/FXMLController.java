/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPlaylist(ActionEvent event) {
    	String input = txtDTOT.getText();
    	if (input.compareTo("")==0) {
    		txtResult.setText("Inserisci una durata massima");
    		return;
    	}
    	double durata = 0;
    	try {
    		durata = Double.parseDouble(input);
    	}catch(NumberFormatException e) {
    		txtResult.setText("La durata deve essere un numero");
    		return;
    	}
    	txtResult.appendText("\n La mia Playlist è formata da: "+"\n");
    	List<Track> lista = model.calcolaPercorso(durata);
    	for (Track t : lista) {
    		txtResult.appendText(t+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Genre genere = cmbGenere.getValue();
    	if (genere.equals(null)) {
    		txtResult.setText("Seleziona un genere");
    		return;
    	}
    	String input1 = txtMin.getText();
    	String input2 = txtMax.getText();
    	if (input1.compareTo("")==0 || input2.compareTo("")==0) {
    		txtResult.appendText("Inserisci un minimo e un massimo");
    		return;
    	}
    	double min=0;
    	double max =0;
    	try {
    		min = Double.parseDouble(input1);
    		max = Double.parseDouble(input2);
    	} catch(NumberFormatException e ) {
    		txtResult.setText("Devono essere due numeri");
    		return;
    	}
    	if (min<model.getMin(genere) || max>model.getMax(genere)) {
    		txtResult.setText("Il minimo e massimo devono essere compresi tra i valori presenti nel db");
    		return;
    	}
    	model.creaGrafo(min, max, genere);
    	txtResult.appendText("Verici: "+ model.getV()+"\n");
    	txtResult.appendText("Archi: "+ model.getA()+ "\n");
    	List<Set<Track>> connesse = model.getConnesse();
    	for (Set<Track> set :connesse ) {
    		List<Track> track = new ArrayList<>(set);
    		txtResult.appendText("Componente con "+ set.size()+" vertici e "+ model.getP(min, max, genere, track.get(0)) +"\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbGenere.getItems().addAll(model.getGeneri());
    }

}
