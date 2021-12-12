package edu.tridenttech.cpt237.johnson.last.program.model;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Project

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Store models the store and manages all
 * transactions. Only one store may exist
 * during project runtime.
 * @author James Daniel Johnson
 *
 */
public class Store 
{
	private static final String INVENTORY_FILE = "game_inventory.txt";
	private ArrayList<Game> inventory;
	private Map<GameFormat, Double> salesByFormat;
	private ArrayList<Transaction> pendingTransactions;
	private ArrayList<Transaction> completedTransactions;
	private static final Store INSTANCE = new Store();
	
	private Store()
	{
		this.inventory = new ArrayList<Game>();
		this.pendingTransactions = new ArrayList<Transaction>();
		this.completedTransactions = new ArrayList<Transaction>();
		initSalesByFormat();
		loadInventory();
	}

	/**
	 * Returns the Store Singleton
	 * @return The instance of <code>Store</code>
	 */
	public static Store getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Initiates the internal structure for
	 * keeping track of sales statistics.
	 */
	private void initSalesByFormat()
	{
		salesByFormat = new HashMap<GameFormat, Double>();
		for(GameFormat format : GameFormat.values())
		{
			salesByFormat.put(format, Double.valueOf(0));
		}
	}
	
	/**
	 * Loads the inventory from the file.  Skips lines
	 * where it finds errors.
	 */
	private void loadInventory()
	{
		try(Scanner inputFile = new Scanner(new FileInputStream(INVENTORY_FILE)))
		{
			int line = 1;
			while(inputFile.hasNextLine())
			{
				String[] record = inputFile.nextLine().split(",");
				addGameToInventory(record, line);
				++line;
			}
			inputFile.close();
		}
		catch(FileNotFoundException ex)
		{
			System.err.print(ex.getMessage());
		}
		
	}
	
	/**
	 * Removes the game from inventory and then returns it
	 * @param format The format of the game
	 * @param title The title of the game
	 * @return The game that matches the provided information
	 */
	public Game takeFromInventory(GameFormat format, String title)
	{	
		Game game = inventory.stream().
			filter(e -> e.getFormat().
					equals(format) && e.getTitle().equals(title)).
			findAny().orElseThrow();
		inventory.remove(game);
		
		return game;
	}
	
	/**
	 * Returns a game to inventory after it
	 * had been removed.
	 * @param game The game to return to cart
	 */
	public void returnToInventory(Game game)
	{
		inventory.add(game);
	}
	
	/**
	 * Returns a list of games in the inventory filtered by
	 * their format.
	 * @param format The format of the games to return
	 * @return A list of games in the specified format
	 */
	public ArrayList<Game> getGameListByFormat(GameFormat format)
	{
		ArrayList<Game> games = new ArrayList<Game>(inventory);
		games.removeIf(game -> 
		{
			return !game.getFormat().equals(format);
		});
		return games;
	}
	
	/**
	 * Initiates the transaction and adds it to the 
	 * pending transaction list
	 * @param cart The cart of games for this transaction
	 * @return The id of the transaction
	 * @throws Exception If there are no items in the cart
	 */
	public int initiateTransaction(Cart cart) throws Exception
	{
		if(cart.isEmpty())
		{
			throw new Exception("ERROR: No Items in cart. "
					+"Can Not initiate transaction.");
		}
		Transaction transaction = new Transaction(cart);
		pendingTransactions.add(transaction);
		return transaction.getId();
	}
	
	/**
	 * Returns the transaction from the provided list
	 * that matches the provided ID
	 * @param id The ID of the transaction to search for
	 * @return The transaction with the specified ID
	 */
	public Transaction findTransactionById(ArrayList<Transaction> list,
			int id)
	{
		return list.stream()
				.filter(e -> e.getId() == id)
				.findAny()
				.orElseThrow();
	}
	
	public Transaction findPendingTransactionById(int id)
	{
		return findTransactionById(pendingTransactions, id);
	}
	
	/**
	 * Cancels the transaction specified by the provided
	 * ID
	 * @param id The ID of the transaction to cancel
	 */
	public void cancelTransaction(int id)
	{
		Transaction transaction = 
				findTransactionById(pendingTransactions, id);
		pendingTransactions.remove(transaction);
	}
	
	/**
	 * Finalizes the transaction specified by the provided
	 * ID
	 * @param id The ID of the transaction to finalize
	 */
	public void finalizeTransaction(int id)
	{
		Transaction transaction = 
				findTransactionById(pendingTransactions, id);
		for(GameFormat format : GameFormat.values())
		{
			double  total = transaction.getCostPerFormat(format);
			double current = salesByFormat.get(format).doubleValue();
			
			salesByFormat.put(format, Double.valueOf(total + current));
		}
		completedTransactions.add(
				pendingTransactions.remove(
						pendingTransactions.indexOf(transaction)));
	}
	
	/**
	 * Returns an unmodifiable version of the internal
	 * structure mapping <code>GameFormat</code> values
	 * to daily sales values.
	 * @return An unmodifiable version of the sales-by-format
	 * 	statistic.
	 */
	public Map<GameFormat, Double> getSalesByFormat()
	{
		return Collections.unmodifiableMap(salesByFormat);
	}
	
	/**
	 * Adds a game to the inventory from the file fields.
	 * Relays an error message to <code>stderr</code> if
	 * any invalid values are identified.
	 * @param record The record to get the game values from
	 * @param line The line number
	 */
	private void addGameToInventory(String[] record, int line)
	{
		final int QUANTITY = 0;
		final int FORMAT = 1;
		final int TITLE = 2;
		int quantity;
		int format;
		try
		{
			quantity = Integer.parseInt(record[QUANTITY]);
			if(quantity < 1)
			{
				throw new IllegalArgumentException(
						String.format(
								"%s on line %d, value = %d",
								"ERROR: Negative quantity field",
								line, 
								quantity ));
			}
			format = Integer.parseInt(record[FORMAT]);
			for(int i = 0; i < quantity; ++i)
			{
				inventory.add(new Game(format, 
						line, 
						record[TITLE]));
			}
		}
		catch(NumberFormatException ex)
		{
			ArrayList<String> text = new ArrayList<>();
			text.add(record[QUANTITY]);
			text.add(record[FORMAT]);
			System.err.println(getStringErrorMessage(text, line));
			
		}
		catch(IllegalArgumentException ex)
		{
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * Returns an error message featuring the invalid
	 * values that it catches.
	 * @param text The list of values to check
	 * @param line The line number of the values for the error message
	 * @return A composed error message
	 */
	private String getStringErrorMessage(
			ArrayList<String> text, 
			int line)
	{
		StringBuilder output = new StringBuilder();
		output.append(
				String.format(
						"ERROR: Non-numeric format found on line %d: ", 
						line));
		text.forEach(input -> 
		{
			Pattern pattern = Pattern.compile("[^\\d]");
			Matcher match = pattern.matcher(input);
			if(match.find() || input.length() == 0)
			{
				output.append(String.format(" \"%s\" ", input));
			}
		});
		
		output.append("\n");
		return output.toString();
	}
	
	/**
	 * Returns the total of all transactions
	 * @return The total value of all transactions
	 */
	public double getDailyTotal()
	{
		double dailyTotal = 0;
		for(Transaction transaction : completedTransactions)
		{
			dailyTotal += transaction.calculateCost();
		}
		return dailyTotal;
	}
	
	/**
	 * Returns an unmodifiable transaction history.
	 * @return An unmodifiable transaction history.
	 */
	public List<Transaction> getTransactionHistory()
	{
		return Collections.unmodifiableList(completedTransactions);
	}
	
	/**
	 * Returns an unmodifiable inventory
	 * @return An unmodifiable inventory
	 */
	public List<Game> getInventory()
	{
		return Collections.unmodifiableList(inventory);
	}
	
}
