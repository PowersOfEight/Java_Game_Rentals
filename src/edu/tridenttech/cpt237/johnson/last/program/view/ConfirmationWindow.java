package edu.tridenttech.cpt237.johnson.last.program.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.tridenttech.cpt237.johnson.last.program.model.Game;
import edu.tridenttech.cpt237.johnson.last.program.model.Store;
import edu.tridenttech.cpt237.johnson.last.program.model.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ConfirmationWindow 
{
	private class ConfirmationWindowController implements Initializable
	{
		@FXML private TextArea transaction;
		@FXML private Button confirm;
		@FXML private Button cancelTransaction;

		public TextArea getTransaction()
		{
			return transaction;
		}
		
		@Override
		public void initialize(URL location, ResourceBundle resources) 
		{
			cancelTransaction.setOnAction( e -> 
			{
				store.cancelTransaction(transactionId);
				stage.close();
			});
			confirm.setOnAction(e ->
			{
				confirmTransaction();
			});
			transaction.setStyle("-fx-font-family: 'Consolas';");
		}

	}

	private Stage stage;
	private Store store;
	private int transactionId;
	private ConfirmationWindowController controller;

	public ConfirmationWindow(Stage stage)
	{
		try 
		{
			this.stage = stage;
			this.stage.initStyle(StageStyle.UNDECORATED);
			this.controller = new ConfirmationWindowController();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(
					"fxml/ConfirmationWindow.fxml"));
			loader.setController(controller);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			this.stage.setScene(scene);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printTransactionReceipt()
	{
		Transaction trans = 
				store.findPendingTransactionById(transactionId);
		StringBuilder receipt = new StringBuilder();
		receipt.append(String.format("%-25s%-36s%8s%n", 
				"System",
				"Title",
				"Price"));
		for(Game game : trans.getList())
		{
			receipt.append(String.format("%-25s%-36s%2s%6.2f%n",
					game.getStringFormat(),
					game.getTitle(),
					"$",
					trans.costPerItem()));
		}
		receipt.append(String.format("%-61s%2s%6.2f",
				"Total",
				"$",
				trans.calculateCost()));
		controller.getTransaction().setText(receipt.toString());
	}
	
	private void confirmTransaction()
	{
		Stage owner = (Stage) stage.getOwner();
		owner.removeEventHandler(
				WindowEvent.WINDOW_CLOSE_REQUEST, 
				owner.getOnCloseRequest());
		store.finalizeTransaction(transactionId);
		stage.close();
		owner.close();
	}
	
	public void show(Store store, int transactionId)
	{
		this.store = store;
		this.transactionId = transactionId;
		printTransactionReceipt();
		this.stage.show();
	}
}
