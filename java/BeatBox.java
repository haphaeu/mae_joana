//package chap13;
// chapter 13

import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class BeatBox {

   JPanel mainPanel;
   ArrayList<JCheckBox> checkboxList;
   Sequencer sequencer;
   Sequence sequence;
   Track track;
   JFrame theFrame;

   String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", 
       "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap", 
       "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", 
       "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", 
       "Open Hi Conga"};
   int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
    

   public static void main (String[] args) {
      new BeatBox().buildGUI();
   }
  
   public void buildGUI() {
      theFrame = new JFrame("Cyber BeatBox");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      BorderLayout layout = new BorderLayout();
      JPanel background = new JPanel(layout);
      background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
   
      checkboxList = new ArrayList<JCheckBox>();
      Box buttonBox = new Box(BoxLayout.Y_AXIS);
   
      JButton start = new JButton("Start");
      start.addActionListener(new MyStartListener());
      buttonBox.add(start);         
          
      JButton stop = new JButton("Stop");
      stop.addActionListener(new MyStopListener());
      buttonBox.add(stop);
   
      JButton upTempo = new JButton("Tempo Up");
      upTempo.addActionListener(new MyUpTempoListener());
      buttonBox.add(upTempo);
   
      JButton downTempo = new JButton("Tempo Down");
      downTempo.addActionListener(new MyDownTempoListener());
      buttonBox.add(downTempo);
	  
	  JButton clearBeats = new JButton("Clear Beats");
	  clearBeats.addActionListener(new MyClearBeatsListener());
	  buttonBox.add(clearBeats);
	  
	  JButton savePattern = new JButton("Save");
	  savePattern.addActionListener(new MySavePatternListener());
	  buttonBox.add(savePattern);
	  
	  JButton loadPattern = new JButton("Load");
	  loadPattern.addActionListener(new MyLoadPatternListener());
	  buttonBox.add(loadPattern);
   
      Box nameBox = new Box(BoxLayout.Y_AXIS);
      for (int i = 0; i < 16; i++) {
         nameBox.add(new Label(instrumentNames[i]));
      }
        
      background.add(BorderLayout.EAST, buttonBox);
      background.add(BorderLayout.WEST, nameBox);
   
      theFrame.getContentPane().add(background);
          
      GridLayout grid = new GridLayout(16,16);
      grid.setVgap(1);
      grid.setHgap(2);
      mainPanel = new JPanel(grid);
      background.add(BorderLayout.CENTER, mainPanel);
   
      // make all the 256 checkboxes, set them to unchecked
	  // add them to the ArrayList and to the GUI panel
	  for (int i = 0; i < 256; i++) {                    
         JCheckBox c = new JCheckBox();
         c.setSelected(false);
         checkboxList.add(c);
         mainPanel.add(c);            
      } // end loop
   
      setUpMidi();
   
      theFrame.setBounds(50,50,300,300);
      theFrame.pack();
      theFrame.setVisible(true);
   } // close method


   public void setUpMidi() {
	  // typical MIDI setup
      try {
         sequencer = MidiSystem.getSequencer();
         sequencer.open();
         sequence = new Sequence(Sequence.PPQ,4);
         track = sequence.createTrack();
         sequencer.setTempoInBPM(120);
        
      } 
      catch(Exception e) {e.printStackTrace();}
   } // close method

   public void buildTrackAndStart() {
   // this is where the midi track is created
   // the checkboxes are turned into midi events and
   // added to the track

      // 16-element array to hold values for one instrument across all 16 beats
	  // if the instrument is supposed to plat at a certain beat, the value at
	  // that element will be the key to that instrument, otherwise zero.
      int[] trackList = null;
    
      sequence.deleteTrack(track);
      track = sequence.createTrack();
   
      // loop through all the 16 instruments (bass, congo, hit-hat, etc)
      for (int i = 0; i < 16; i++) {
		  
		 // values for each of the 16 beats
         trackList = new int[16];  
      
	     // set the key that represents the instrument.
		 // this will be passed to the midi event command to create he track
         int key = instruments[i];   
      
	     // loop through the 16 beats for the current instrument "i"
         for (int j = 0; j < 16; j++ ) {         
            JCheckBox jc = (JCheckBox) checkboxList.get(j + (16*i));
			// if the checkbox for this instrument "i" at beat "j" is checked
			// then add the key value in this slot of the array
            if ( jc.isSelected()) {
               trackList[j] = key;
            } 
            else {
               trackList[j] = 0;
            }                    
         } // close inner loop
         
         makeTracks(trackList);
         track.add(makeEvent(176,1,127,0,16));  
      } // close outer
   
      // this is to make sure that there is at least one event at the last beat,
	  // otherwise the track will loop prematurely (beat <16 will be the last)
      track.add(makeEvent(192,9,1,0,15));      
	  
	  // PLAY IT!
      try {
         sequencer.setSequence(sequence); 
         sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);                   
         sequencer.start();
         sequencer.setTempoInBPM(120);
      } 
      catch(Exception e) {e.printStackTrace();}
   } // close buildTrackAndStart method
            
   // listener for the start button
   public class MyStartListener implements ActionListener {
      public void actionPerformed(ActionEvent a) {
         buildTrackAndStart();
      }
   } // close inner class

   // listener for the stop button
   public class MyStopListener implements ActionListener {
      public void actionPerformed(ActionEvent a) {
         sequencer.stop();
      }
   } // close inner class
   
   // listener for the tempo up button
   public class MyUpTempoListener implements ActionListener {
      public void actionPerformed(ActionEvent a) {
         float tempoFactor = sequencer.getTempoFactor(); 
         sequencer.setTempoFactor((float)(tempoFactor * 1.03));
      }
   } // close inner class

   // listener for the tempo down button
   public class MyDownTempoListener implements ActionListener {
      public void actionPerformed(ActionEvent a) {
         float tempoFactor = sequencer.getTempoFactor();
         sequencer.setTempoFactor((float)(tempoFactor * .97));
      }
   } // close inner class
   
   // listener for the clear button
   public class MyClearBeatsListener implements ActionListener {
	   public void actionPerformed(ActionEvent a) {
		   for (JCheckBox c: checkboxList) {
			   c.setSelected(false);
		   }
	   }
   }
   
   // listener for the save button
   public class MySavePatternListener implements ActionListener {
	   public void actionPerformed(ActionEvent a) {
		   boolean[] checkboxStates = new boolean[256];
		   for (int i=0; i < 256; i++) {
			   JCheckBox c = (JCheckBox) checkboxList.get(i);
			   if (c.isSelected()) {
				   checkboxStates[i] = true;
			   }
		   }
		   try {
			   JFileChooser fileSave = new JFileChooser();
			   fileSave.showSaveDialog(theFrame);
			   FileOutputStream fileStream = new FileOutputStream(fileSave.getSelectedFile());
			   ObjectOutputStream os = new ObjectOutputStream(fileStream);
			   os.writeObject(checkboxStates);
		   } catch (Exception ex) {
			   ex.printStackTrace();
		   }
	   }
   }
   
   // listener for the load button
   public class MyLoadPatternListener implements ActionListener {
	   public void actionPerformed(ActionEvent a) {
		   boolean[] checkboxStates = null;
		   try {
			   JFileChooser fileLoad = new JFileChooser();
			   fileLoad.showOpenDialog(theFrame);
			   FileInputStream fileStream = new FileInputStream(fileLoad.getSelectedFile());
			   ObjectInputStream is = new ObjectInputStream(fileStream);
			   checkboxStates = (boolean[]) is.readObject();
		   } catch (Exception ex) {
			   ex.printStackTrace();
		   }
		   for (int i=0; i < 256; i++) {
			   JCheckBox c = (JCheckBox) checkboxList.get(i);
			   if (checkboxStates[i]) {
				   c.setSelected(true);
			   } else {
				   c.setSelected(false);
			   }
		   }
		   sequencer.stop();
		   buildTrackAndStart();
	   }
   }

   // Make events for one instrument, for all 16 beats.
   public void makeTracks(int[] list) {        
       
      for (int i = 0; i < 16; i++) {
         int key = list[i];
      
         if (key != 0) {
            track.add(makeEvent(144,9,key, 100, i));   // NOTE ON
            track.add(makeEvent(128,9,key, 100, i+1)); // NOTE OFF
         }
      }
   }
     
   // utility method to make a message and return a MidiEvent
   // the 4 first parameters are the message, the last "tick" is when it should play
   public  MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
      MidiEvent event = null;
      try {
         ShortMessage a = new ShortMessage();
         a.setMessage(comd, chan, one, two);
         event = new MidiEvent(a, tick);
      
      } 
      catch(Exception e) {e.printStackTrace(); }
      return event;
   }

} // close class

        
             
          
          
