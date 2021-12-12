package edu.tridenttech.cpt237.johnson.last.program.model;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Project

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Transaction is a uniquely identified
 * transaction. Each transaction is processed
 * on a <code>Cart</code>.
 * @author James Daniel Johnson
 *
 */
public class Transaction 
{
	
	private static final double COST_PER_GAME = 2.00;
	private static int ID = 1001;
	
	private int id;
	private ArrayList<Game> purchaseList;
	
	public Transaction(Cart cart)
	{
		id = ID++;
		purchaseList = 
				new ArrayList<Game>(cart.getList());
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getNumberOfItems()
	{
		return purchaseList.size();
	}
	
	/**
	 * Returns a String representation of the receipt
	 * for this transaction
	 * @return A String representation of the receipt
	 */
	public String getReceipt()
	{
		StringBuilder receipt = new StringBuilder();
		receipt.append(String.format("%-15s%-36s%8s%n", 
				"System",
				"Title",
				"Price"));
		for(Game game : getList())
		{
			receipt.append(String.format("%-15s%-36s%2s%6.2f%n",
					game.getStringFormat(),
					game.getTitle(),
					"$",
					COST_PER_GAME));
		}
		receipt.append(String.format("%-51s%2s%6.2f",
				"Total",
				"$",
				calculateCost()));
		return receipt.toString();
	}
	
	/**
	 * Returns a sorted list of games
	 * @return A sorted list of games
	 */
	public List<Game> getList()
	{
		purchaseList.sort((left, right)->
		{
			int primaryCheck = 
					left.getStringFormat().
					compareToIgnoreCase(right.getStringFormat());
			if(primaryCheck != 0)
			{
				return primaryCheck;
			}
			return left.getTitle().compareToIgnoreCase(right.getTitle());
		});
		return Collections.unmodifiableList(purchaseList);
	}
	
	
	public double costPerItem()
	{
		return COST_PER_GAME;
	}
	
	/**
	 * Calculates the total cost of this transaction
	 * @return The total cost
	 */
	public double calculateCost()
	{
		return purchaseList.size() * COST_PER_GAME;
	}

	/**
	 * Returns the cost attributed to the format value
	 * @param format The format to associate with this cost
	 * @return The total sales for this transaction of this format
	 */
	public double getCostPerFormat(GameFormat format) 
	{
		ArrayList<Game> games = new ArrayList<Game>(purchaseList);
		games.removeIf(game -> 
		{
			return !game.getFormat().equals(format);
		}); 
		
		return games.size() * COST_PER_GAME;
	}
}
