package Core;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;


public class MusicClips{
	private String name;
	private Clip soundClip;
	
	// Name of clip is same as filename for ease
	// vol controls volume; 1.0 = 100% and .0 = 0%
	public MusicClips(String name, double vol) {
		
		this.name = name;
		try
		{	//File object that will be used for the audio stream
			File musicpath = new File("Music/"+name+".wav");		
			if(musicpath.exists()) 
			{
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicpath);
				this.soundClip = AudioSystem.getClip();				
				this.soundClip.open(audioInput);
				// volume controls for clip
				setVol(vol, this.soundClip);
				if (name.contentEquals("BACKGROUND") ||
					name.contentEquals("BachWellTempKlavier")) {
					this.soundClip.loop(this.soundClip.LOOP_CONTINUOUSLY);
				}
			}
			else 
			{
				System.out.println("Can't find file");
			}
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	public void playMusic() {
		this.soundClip.start();
	}
	
	// Called when closing the game and switching back to the title screen.
	public void stopMusic() {
		this.soundClip.stop();
	}
	
	public String getName() {
		return this.name;
	}
	
	public Clip getClip() {
		return this.soundClip;
	}
	// Sets volume for clip
	public static void setVol(double vol, Clip clip){
		FloatControl gain = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		float db = (float) (Math.log(vol) / Math.log(10) * 20);
		gain.setValue(db);		
	}


}



