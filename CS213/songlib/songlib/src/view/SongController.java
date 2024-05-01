/*
 * Author: Xiaoyu Chen, Dong Shu
 * 
 */

package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SongController {
	
	@FXML Button b1;
	@FXML Button b2;
	@FXML Button b3;
	@FXML TextField textbox;
	@FXML ListView<String> listView;
	@FXML private Label Detail;
	@FXML private TextField songname;
	@FXML private TextField songartist;
	@FXML private TextField songalbum;
	@FXML private TextField songyear;
	

	Stage mainstage;
	public int stageIndex = 0; // global variable for the stage index. 0 for the mainStage; 1 for the addStage; 2 for the editStage; 3 for the delete stage
	private ArrayList<SongName> songList; // list of all songs
	private ObservableList<String> obsList; // list for listview
	private String songs = "songs.txt"; // name of file which stores song names
	

	public void start(Stage mainStage) {                
		songList = readFrom(songs);
		
		obsList = FXCollections.observableArrayList(connect(songList)); 

		listView.setItems(obsList); 
		
		listView.getSelectionModel().select(0);

		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showDetails(mainStage));
		
		songname.setEditable(false);
		songartist.setEditable(false);
		songalbum.setEditable(false);
		songyear.setEditable(false);

	}
	@FXML
	public void handleAddAction(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == b1) {
			if (stageIndex == 0) {
				b1.setText("cancel");
				b2.setVisible(false);
				Detail.setVisible(false);
				b3.setText("confirm");
				stageIndex = 1;
				
				songname.setEditable(true);
				songartist.setEditable(true);
				songalbum.setEditable(true);
				songyear.setEditable(true);
				
				songname.setText("");
				songartist.setText("");
				songalbum.setText("");
				songyear.setText("");
								
			} else if (stageIndex == 1) { // cancel
				b1.setText("Click here to add a new song");
				b2.setVisible(true);
				Detail.setVisible(true);
				b3.setText("delete the selected song");
				
				songname.setEditable(false);
				songartist.setEditable(false);
				songalbum.setEditable(false);
				songyear.setEditable(false);
				
				songyear.setPromptText("year");
				
				stageIndex = 0;
			}else if (stageIndex == 2) {
				b1.setText("Click here to add a new song");
				b2.setVisible(true);
				Detail.setVisible(true);
				b3.setText("delete the selected song");
				
				songname.setEditable(false);
				songartist.setEditable(false);
				songalbum.setEditable(false);
				songyear.setEditable(false);
				
				stageIndex = 0;
			} else if (stageIndex == 3) {
				b1.setText("Click here to add a new song");
				b2.setVisible(true);
				Detail.setVisible(true);
				b3.setText("delete the selected song");
				
				songname.setEditable(false);
				songartist.setEditable(false);
				songalbum.setEditable(false);
				songyear.setEditable(false);
				
				stageIndex = 0;
			}
		}
	}
	public void handleEditAction(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == b2) {
			if (stageIndex == 0) {
				
				songname.setEditable(true);
				songartist.setEditable(true);
				songalbum.setEditable(true);
				songyear.setEditable(true);
				
				b1.setText("cancel");
				b2.setVisible(false);
				Detail.setVisible(false);
				b3.setText("confirm");
				stageIndex = 2;
			}
		}
	}
	
	
	public void handleDeleteAction(ActionEvent e) {
		Button b = (Button)e.getSource();
		if(b == b3) {
			if (stageIndex == 0) {
				b1.setText("cancel");
				b2.setVisible(false);
				Detail.setVisible(false);
				b3.setText("confirm");
				stageIndex = 3;
			} else if (stageIndex == 1) { // confirm
				SongName temp = new SongName();
				
				songname.setEditable(true);
				songartist.setEditable(true);
				songalbum.setEditable(true);
				songyear.setEditable(true);
				
				temp.name = getRidOfSpace(songname.getText());
				temp.artist = getRidOfSpace(songartist.getText());
				temp.album = getRidOfSpace(songalbum.getText());
				temp.year = getRidOfSpace(songyear.getText());
				if(temp.name.isEmpty() || temp.artist.isEmpty()) {
					//alert "cannot be empty"
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainstage);
					alert.setTitle("ERROR");
					alert.setHeaderText("Lack of Infomation");
					alert.setContentText("Must Include Name AND Artist!!!");
					alert.showAndWait();
					
				//duplicate
				}else if(exist(obsList, SongName.getHalf(temp))){
					songname.setText("");
					songartist.setText("");
					songalbum.setText("");
					songyear.setText("");
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainstage);
					alert.setTitle("ERROR");
					alert.setHeaderText("Duplicate Error");
					alert.setContentText("Name and Artist already exist!!!");
					alert.showAndWait();
					
				}else {
					songList.add(temp);
					String str = SongName.getHalf(temp);
					sort();
					listView.getSelectionModel().select(obsList.indexOf(str));
				}
				
				songname.setEditable(false);
				songartist.setEditable(false);
				songalbum.setEditable(false);
				songyear.setEditable(false);
				
				songyear.setPromptText("year");
				
				b1.setText("Click here to add a new song");
				b2.setVisible(true);
				Detail.setVisible(true);

				b3.setText("delete the selected song");
				stageIndex = 0;
				
				
			} else if (stageIndex == 2) { //confirm
				int selectedIndex = listView.getSelectionModel().getSelectedIndex();
				SongName temp = new SongName();
				
				songname.setEditable(true);
				songartist.setEditable(true);
				songalbum.setEditable(true);
				songyear.setEditable(true);
				
				temp.name = getRidOfSpace(songname.getText());
				temp.artist = getRidOfSpace(songartist.getText());
				temp.album = getRidOfSpace(songalbum.getText());
				temp.year = getRidOfSpace(songyear.getText());
				if(temp.name.isEmpty() || temp.artist.isEmpty()) {
					//alert "cannot be empty"
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainstage);
			        alert.setTitle("ERROR");
			        alert.setHeaderText("Lack of Infomation");
			        alert.setContentText("Must Include Name AND Artist!!!");
			        alert.showAndWait();
				}else if(obsList.contains(SongName.getHalf(temp))){
					songname.setText("");
					songartist.setText("");
					songalbum.setText("");
					songyear.setText("");
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainstage);
					alert.setTitle("ERROR");
					alert.setHeaderText("Duplicate Error");
					alert.setContentText("Name and Artist already exist!!!");
					alert.showAndWait();
					
				}else {
					songList.remove(selectedIndex);
					songList.add(temp);
					sort();
				}
				
				songname.setEditable(false);
				songartist.setEditable(false);
				songalbum.setEditable(false);
				songyear.setEditable(false);
				
				b1.setText("Click here to add a new song");
				b2.setVisible(true);
				Detail.setVisible(true);

				b3.setText("delete the selected song");
				stageIndex = 0;
			} else if (stageIndex == 3) {
				deleteSong();
				
				songname.setEditable(false);
				songartist.setEditable(false);
				songalbum.setEditable(false);
				songyear.setEditable(false);
				
				b1.setText("Click here to add a new song");
				b2.setVisible(true);
				Detail.setVisible(true);

				b3.setText("delete the selected song");
				stageIndex = 0;
			}
		}
	}
	
	
	private void showDetails(Stage mainStage) {
		songname.setText("");
		songartist.setText("");
		songalbum.setText("");
		songyear.setText("");

		
		int index = listView.getSelectionModel().getSelectedIndex();
		//System.out.print(index);
		//System.out.println(" " + songList.size());
		
		if (index != -1) {
		
		songname.setText(songList.get(index).name);

		
		songartist.setText(songList.get(index).artist);

		if (songList.get(index).album != null)
		songalbum.setText(songList.get(index).album);

		if (songList.get(index).year != null)
		songyear.setText(songList.get(index).year);
		}

	}
	
	private boolean exist(ObservableList<String> a, String b) {
		boolean result = false;
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).equalsIgnoreCase(b)) {
				result = true;
			}
		}
		return result;
	}
	
	// read from file, return with a list of object songNames
	private static ArrayList<SongName> readFrom(String inputfile){
		ArrayList<SongName> song = new ArrayList<SongName>();
		StdIn.setFile(inputfile);
		while(StdIn.hasNextLine()) {
			SongName temp = new SongName();
			String str = StdIn.readLine();
			ArrayList<String> lst = new ArrayList<>(Arrays.asList(str.split(",")));
			temp.name = lst.get(0);
			temp.artist = lst.get(1);
			if (lst.size() == 3) temp.album = lst.get(2);
			if (lst.size() == 4) {temp.album = lst.get(2);temp.year = lst.get(3);}
			song.add(temp);
		}
		
		return song;
	}
	
	// convert from SongName object to String
	private static ArrayList<String> connect(ArrayList<SongName> songName){
		ArrayList<String> lst = new ArrayList<String>();
		for(int i = 0; i < songName.size(); i++) {
			lst.add(SongName.getHalf(songName.get(i)));
		}
		return lst;
	}
	
	
	private void deleteSong() {
		int itemIndex = listView.getSelectionModel().getSelectedIndex();
		songList.remove(itemIndex);
		sort();
		if(itemIndex >= listView.getItems().size()) {
			listView.getSelectionModel().selectLast();
		}else{
			listView.getSelectionModel().select(itemIndex);
		}
	}
	
	// get rid of the leading/trailing spaces
	private String getRidOfSpace(String str) { 		
		
		if (str.isEmpty()) {
			return "";
		}else {
			char[] a = new char[str.length()];
			
        for (int i = 0; i < str.length(); i++) {
        	a[i] = str.charAt(i);
        }
        while (a[0] == ' ') {
        	a = deleteFirst(a);
        }
        int y = a.length - 1;
        while (a[y] == ' ') {
        	a = deleteLast(a);
        	y--;
        }
        return String.valueOf(a);
		}
	}
	
	private char[] deleteFirst(char[] a) {
		char[] b = Arrays.copyOfRange(a, 1, a.length);
		
		return b;
	}
	
	private char[] deleteLast(char[] a) {
		char[] b = Arrays.copyOfRange(a, 0, a.length - 1);
		
		return b;
	}
	
	public void sort() {
		sortSongList();
		sortListView();
		sortSongsTxt();
	}
	
	
	
	// see SongName.compareTo for details
	// compare name first, then artist in alphabet order(case insensitive)
	private void sortSongList() {
		Collections.sort(songList);
	}
	
	
	private void sortListView() {
		obsList = FXCollections.observableArrayList(connect(songList));
		listView.setItems(obsList);
	}
	
	private void sortSongsTxt() {
		StdOut.setFile(songs);
		for (int i = 0; i < songList.size(); i++) {
			StdOut.println(SongName.getName(songList.get(i)));
		}
	}
}