package edu.tridenttech.cpt237.johnson.last.program.model;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Project
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cart represents the cart that the user will add games
 * into before proceeding to checkout
 * @author James Daniel Johnson
 *
 */
public class Cart 
{
	private ArrayList<Game> cart;
	
	public Cart()
	{
		cart = new ArrayList<Game>();
	}
	
	/**
	 * Adds the game to the cart
	 * @param game the game to add to the cart
	 * @return True if the game was added.
	 */
	public boolean add(Game game)
	{
		if(cart.contains(game))
		{
			return false;
		}
		
		return cart.add(game);
	}
	
	/**
	 * Returns true if the game is contained
	 * in this cart.
	 * @param game The game to check for
	 * @return True if the game is in the cart
	 */
	public boolean contains(Game game)
	{
		return cart.contains(game);
	}
	
	/**
	 * Returns true if the cart is empty
	 * @return True if the cart is empty
	 */
	public boolean isEmpty()
	{
		return cart.isEmpty();
	}
	
	/**
	 * Removes an instance of a game that 
	 * matches this format and title, and returns it
	 * @param format The format of the game
	 * @param title The title of the game
	 * @return The game removed from the list
	 */
	public Game remove(GameFormat format, String title)
	{
		int index = cart.indexOf(new Game(format, title));
		return cart.remove(index);
	}
	
	/**
	 * Completely empties the cart and returns
	 * all games to the store inventory.
	 * @param store The store to return the games to.
	 */
	public void emptyCart(Store store)
	{
		cart.forEach( game ->
		{
			store.returnToInventory(game);	
		});
		cart.clear();
	}
	
	public List<Game> getList()
	{
		return Collections.unmodifiableList(cart);
	}
}
