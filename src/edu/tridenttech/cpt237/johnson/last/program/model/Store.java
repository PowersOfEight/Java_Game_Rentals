package edu.tridenttech.cpt237.johnson.last.program.model;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Project

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Store 
{
	private static final String INVENTORY_FILE = "game_inventory.txt";
	private ArrayList<Game> inventory;
	private ArrayList<Transaction> pendingTransactions;
	private ArrayList<Transaction> completedTransactions;
	private static final Store INSTANCE = new Store();
	
	private Store()
	{
		this.inventory = new ArrayList<Game>();
		this.pendingTransactions = new ArrayList<Transaction>();
		this.completedTransactions = new ArrayList<Transaction>();
		loadInventory();
	}

	public static Store getInstance()
	{
		return INSTANCE;
	}
	
	private void loadInventory()
	{
		try(Scanner inputFile = new Scanner(new FileInputStream(INVENTORY_FILE)))
		{
			while(inputFile.hasNextLine())
			{
				String[] record = inputFile.nextLine().split(",");
				addGameToInventory(record);
			}
			inputFile.close();
		}
		catch(FileNotFoundException ex)
		{
			System.err.print(ex.getMessage());
		}
		
	}
	
	public Game takeFromInventory(GameFormat format, String title)
	{	
		Game game = inventory.stream().
			filter(e -> e.getFormat().
					equals(format) && e.getTitle().equals(title)).
			findAny().orElseThrow();
		inventory.remove(game);
		
		return game;
	}
	
	public void returnToInventory(Game game)
	{
		inventory.add(game);
	}
	
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
	 */
	public int initiateTransaction(Cart cart)
	{
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
		completedTransactions.add(
				pendingTransactions.remove(
						pendingTransactions.indexOf(transaction)));
	}
	
	private void addGameToInventory(String[] record)
	{
		final int QUANTITY = 0;
		final int FORMAT = 1;
		final int TITLE = 2;
		int quantity;
		int format;
		try
		{
			quantity = Integer.parseInt(record[QUANTITY]);
			format = Integer.parseInt(record[FORMAT]);
			for(int i = 0; i < quantity; ++i)
			{
				inventory.add(new Game(format, record[TITLE]));
			}
		}
		catch(IllegalArgumentException ex)
		{
			System.err.println(ex.getMessage());
		}
	}
	
	
	public void addTransaction(Transaction transaction)
	{
		this.completedTransactions.add(transaction);
	}
	
	public double getDailyTotal()
	{
		double dailyTotal = 0;
		for(Transaction transaction : completedTransactions)
		{
			dailyTotal += transaction.calculateCost();
		}
		return dailyTotal;
	}
	
	public List<Transaction> getTransactionHistory()
	{
		return Collections.unmodifiableList(completedTransactions);
	}
	
	public List<Game> getInventory()
	{
		return Collections.unmodifiableList(inventory);
	}
	
}
