package edu.tridenttech.cpt237.johnson.last.program.view;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Program
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import edu.tridenttech.cpt237.johnson.last.program.model.Store;
import edu.tridenttech.cpt237.johnson.last.program.model.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

/**
 * ReportWindow displays a daily transaction
 * report to the user.
 * @author James Daniel Johnson
 *
 */
public class ReportWindow 
{
	
	/**
	 * Manages all of the ReportWindows controls.
	 * @author James Daniel Johnson
	 *
	 */
	private class ReportWindowController implements Initializable
	{
		
		@FXML private TextArea reportArea;
		@FXML private Button closeButton;
		
		public TextArea getReportArea()
		{
			return reportArea;
		}
		
		@Override
		public void initialize(URL location, ResourceBundle resources) 
		{
			reportArea.setEditable(false);
			closeButton.setOnAction(e ->
			{
				stage.close();
			});
		}	
	}
	
	private Store store;
	private Stage stage;
	private ReportWindowController controller;
	
	/**
	 * Loads the document through the FMXLLoader.
	 * @param stage The stage to use for this window.
	 */
	public ReportWindow(Stage stage)
	{
		this.stage = stage;
		this.stage.initStyle(StageStyle.UNDECORATED);
		try
		{			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
					getClass().getResource("fxml/ReportWindow.fxml"));
			loader.setController(new ReportWindowController());
			Parent root = loader.load();
			Scene scene = new Scene(root);
			this.stage.setScene(scene);
			this.controller = loader.getController();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Writes receipts to the TextArea
	 */
	private void writeReceipts()
	{
		List<Transaction> transactions = 
				store.getTransactionHistory();
		TextArea reportArea = controller.getReportArea();
		StringBuilder reportBuilder = new StringBuilder();
		transactions.forEach(trans -> 
		{
			reportBuilder.append(String.format("%-51s%8d%n",
					"Transaction ID:",
					trans.getId()));
			reportBuilder.append(trans.getReceipt());
			reportBuilder.append("\n\n");
		});
		reportBuilder.append(String.format("%-51s%2s%6.2f", 
				"Total Sales:",
				"$",
				store.getDailyTotal()));
		reportArea.setText(reportBuilder.toString());
	}
	
	/**
	 * Writes the receipts from the store,
	 * then displays the window.
	 * @param store The store to associate with this window.
	 */
	public void show(Store store)
	{
		this.store = store;
		writeReceipts();
		this.stage.show();
	}

}
