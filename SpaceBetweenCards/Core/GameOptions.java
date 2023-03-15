package Core;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameOptions
{
	// This variable can be used to modify the volume value for
	// a clip instance.
	private double masterVolume;
	
	// Record of seed that has been saved to file.
	private long savedSeed;
	
	// Record of the seed used to generate decks. Could be used
	// to reconstitute the game/deck outcomes.
	private long seed;
	
	public GameOptions()
	{
		masterVolume = 1.0;
		
		try
		{
			File file = new File("Data/game_options.txt");
			Scanner scanner = new Scanner(file);
			String optionsData = scanner.nextLine();
			scanner.close();
			
			String[] optionsSplit = optionsData.split(",");
			setVolume(Double.parseDouble(optionsSplit[1]));
			savedSeed = Long.parseLong(optionsSplit[2]);
			
		} catch(Exception ex)
		{
		   System.out.println("Failed to load game options - check folder.");
		}
	}
	
	public Double getVolume()
	{
		return masterVolume;
	}
	
	public void setVolume(double volume)
	{
		if(volume >= 0.0 && volume <= 2.0)
		{
			masterVolume = volume;
		} else
		{
			System.out.println("Invalid volume setting requested.");
		}
	}
	
	public void setSeed(long seed)
	{
		this.seed = seed;
	}
	
	// The user has selected to save the current seed - write over
	// the previously saved seed.
	public boolean saveSeed()
	{
		savedSeed = seed;
		return saveOptions();
	}
	
	// When saving options data - the method will return true if
	// the save was successful, false if not successful.
	public boolean saveOptions()
	{
		String saveData = "Volume," + masterVolume;
		if(savedSeed != 0) saveData += "," + savedSeed;
		saveData += "\n";
		
		try
		{
			File file = new File("Data/game_options.txt");
			PrintWriter printWriter = new PrintWriter(file);
			printWriter.println(saveData);
			printWriter.close();
			
		} catch(Exception ex)
		{
			System.out.println("Save error - could not save options.");
			return false;
		}
		
		return true;
	}
}
