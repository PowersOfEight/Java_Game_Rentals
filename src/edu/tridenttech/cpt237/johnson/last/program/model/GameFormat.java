package edu.tridenttech.cpt237.johnson.last.program.model;

public enum GameFormat 
{
	NINTENDO	("Nintendo", "Switch", 1),
	PLAYSTATION ("Sony", "Playstation 4", 2),
	XBOX		("Microsoft", "X-Box One", 3);
	
	private final String manufacturer;
	private final String console;
	private final int index;
	
	GameFormat(String manufacturer,
			String console, 
			int index)
	{
		this.manufacturer = manufacturer;
		this.console = console;
		this.index = index;
	}
	
	public static GameFormat findFormatByIndex(int formatCode)
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
						"Bad game format: %d is not a legal game format", 
						formatCode));
	}
	
	public boolean equals(GameFormat other)
	{
		return this.console.equals(other.getGameConsole());
	}
	
	public static GameFormat select(
			String manufacturer, 
			String console)
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
		
		return null;
	}
	
	public String getManufacturer()
	{
		return this.manufacturer;
	}
	
	public String getGameConsole()
	{
		return this.console;
	}
	
	public int getIndex()
	{
		return this.index;
	}
}
