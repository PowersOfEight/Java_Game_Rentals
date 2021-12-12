package edu.tridenttech.cpt237.johnson.last.program.view;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Program
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

/**
 * ConfirmationWindow displays information about
 * the current transaction and allows the user
 * to cancel or complete this transaction.
 * @author James Daniel Johnson
 *
 */
public class ConfirmationWindow 
{
	
	/**
	 * Controls all of the FXML elements
	 * @author James Daniel Johnson
	 *
	 */
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
		}

	}

	private Stage stage;
	private Store store;
	private int transactionId;
	private ConfirmationWindowController controller;

	/**
	 * Sets up the document using FXMLLoader
	 * @param stage The stage associated with this window
	 */
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
			e.printStackTrace();
		}
	}

	/**
	 * Prints the transaction receipt to the text area
	 */
	private void printTransactionReceipt()
	{
		Transaction trans = 
				store.findPendingTransactionById(transactionId);
		controller.getTransaction().setText(trans.getReceipt());
	}
	
	/**
	 * Confirms and processes the transaction
	 */
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
	
	/**
	 * Sets the store associated with this window,
	 * and the transaction ID, then shows the window.
	 * @param store The store to associate with this window
	 * @param transactionId The id of the transaction to associate with
	 * 	this window
	 */
	public void show(Store store, int transactionId)
	{
		this.store = store;
		this.transactionId = transactionId;
		printTransactionReceipt();
		this.stage.show();
	}
}
