package edu.tridenttech.cpt237.johnson.last.program.model;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Project

/**
 * The <code>Game</code> class holds information regarding
 * a game that might be rented.
 * @author James Daniel Johnson
 */
public class Game 
{	
	private GameFormat gameFormat;
	private String title;
	
	/**
	 * Creates an instance of the game using the format index 
	 * to choose a format, and setting the title.
	 * @param format The format index
	 * @param title The title of the game
	 * @throws Exception 
	 */
	public Game(int format, 
			String title) throws IllegalArgumentException
	{
		this.gameFormat = GameFormat.findFormatByIndex(format);
//		this.format = FORMAT_VALUES[format - FORMAT_INDEX_OFFSET];
		this.title = title;
	}
	
	public Game(GameFormat format,
			String title)
	{
		this.gameFormat = format;
		this.title = title;
	}
	

	@Override
	public boolean equals(Object other)
	{
		if(other == null || 
			!(other instanceof Game))
		{			
			return false;
		}
		Game game = (Game) other;
		return this.gameFormat.equals(game.getFormat()) &&
				this.title.equals(game.getTitle());
	}
	
	@Override
	public int hashCode()
	{
		
		return gameFormat.hashCode() ^ this.getTitle().hashCode();
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public GameFormat getFormat()
	{
		return this.gameFormat;
	}
	
	public String getStringFormat()
	{
		return this.gameFormat.getGameConsole();
	}
	
}
