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
			int line,
			String title) throws IllegalArgumentException
	{
		this.gameFormat = GameFormat.findFormatByIndex(format, line);
		this.title = title;
	}
	
	/**
	 * Alternate instance creator to create
	 * dummy comparison objects.
	 * @param format The format of the game
	 * @param title The title of the game
	 */
	public Game(GameFormat format,
			String title)
	{
		this.gameFormat = format;
		this.title = title;
	}
	
	/**
	 * Needed for <code>HashSet</code>
	 */
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
	
	/**
	 * Needed for <code>HashSet</code>.
	 * Provides an equivalent hash for 
	 * objects with the same <code>GameFormat</code>
	 * value and title.
	 */
	@Override
	public int hashCode()
	{
		
		return gameFormat.hashCode() ^ this.getTitle().hashCode();
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * Returns the format of this game.
	 * @return The format of this game.
	 */
	public GameFormat getFormat()
	{
		return this.gameFormat;
	}
	
	/**
	 * Returns the format of this game in
	 * a <code>String</code>
	 * @return The format of this game in
	 * 	<code>String</code> form.
	 */
	public String getStringFormat()
	{
		return this.gameFormat.getGameConsole();
	}
	
}
