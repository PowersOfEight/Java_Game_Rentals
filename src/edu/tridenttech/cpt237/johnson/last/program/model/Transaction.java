package edu.tridenttech.cpt237.johnson.last.program.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Project

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
	
	public List<Game> getList()
	{
		purchaseList.sort((left, right)->
		{
			int primaryCheck = 
					left.getStringFormat().compareToIgnoreCase(right.getStringFormat());
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
	
	public double calculateCost()
	{
		return purchaseList.size() * COST_PER_GAME;
	}
}
