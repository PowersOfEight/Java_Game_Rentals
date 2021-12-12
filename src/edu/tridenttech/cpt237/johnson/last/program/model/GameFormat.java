package edu.tridenttech.cpt237.johnson.last.program.model;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Project

/**
 * These are constant values that relate to the
 * game formats.
 * @author James Daniel Johnson
 *
 */
public enum GameFormat 
{
	
	NINTENDO	("Nintendo", "Switch", 1),
	PLAYSTATION ("Sony", "Playstation 4", 2),
	XBOX		("Microsoft", "X-Box One", 3);
	
	private final String manufacturer;
	private final String console;
	private final int index;
	
	/**
	 * Stores the values in this constant
	 * @param manufacturer The console manufacturer's name
	 * @param console The console that this is formatted for
	 * @param index The index code that relates to this value
	 */
	GameFormat(String manufacturer,
			String console, 
			int index)
	{
		this.manufacturer = manufacturer;
		this.console = console;
		this.index = index;
	}
	
	/**
	 * Finds the correct format for the given values or
	 * throws an <code>IllegalArgumentException</code>.  Line
	 * is included to create a readable error message.
	 * @param formatCode The code that relates to the format
	 * @param line The line number for the possible error message
	 * @return A <code>GameFormat</code> value.
	 * @throws IllegalArgumentException If a legal game format is not
	 * 	found
	 */
	public static GameFormat findFormatByIndex(int formatCode, int line)
		throws IllegalArgumentException
	{
		for(GameFormat value : values())
		{
			if(formatCode == value.getIndex())
			{
				return value;
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"%s%d%s%d is not a legal game format", 
						"ERROR on line ",
						line,
						", bad game format: ",
						formatCode));
	}
	
	/**
	 * Used to filter values
	 * @param other The other format value to check against
	 * @return True if the values are equivalent, false otherwise
	 */
	public boolean equals(GameFormat other)
	{
		return this.console.equals(other.getGameConsole());
	}
	
	
	/**
	 * Selects a value based on the provided text
	 * @param manufacturer The name of the manufacturer
	 * @param console The name of the console
	 * @return A <code>GameFormat</code> value
	 * @throws Exception If a valid value is not found
	 */
	public static GameFormat select(
			String manufacturer, 
			String console) throws Exception
	{
		for(GameFormat format : values())
		{
			if(manufacturer.equals(
					format.getManufacturer()) &&
					console.equals(format.getGameConsole()))
			{
				return format;
			}
		}
		throw new Exception("Invalid format selected.");
	}
	
	/**
	 * Returns the manufacturer's name
	 * @return The manufacturer's name
	 */
	public String getManufacturer()
	{
		return this.manufacturer;
	}
	
	/**
	 * Returns the game console's name
	 * @return The game console's name
	 */
	public String getGameConsole()
	{
		return this.console;
	}
	
	/**
	 * Returns the index for this value
	 * @return The index value
	 */
	public int getIndex()
	{
		return this.index;
	}
}
