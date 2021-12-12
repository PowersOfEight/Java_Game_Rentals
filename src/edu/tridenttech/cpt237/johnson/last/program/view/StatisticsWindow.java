package edu.tridenttech.cpt237.johnson.last.program.view;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Program

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import edu.tridenttech.cpt237.johnson.last.program.model.GameFormat;
import edu.tridenttech.cpt237.johnson.last.program.model.Store;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * The Statistics Window displays statistics for daily sales
 * by <code>GameFormat</code>
 * @author James Daniel Johnson
 *
 */
public class StatisticsWindow 
{
	
	private Stage stage;
	private StatisticsWindowController controller;
	private Store store;
	
	/**
	 * Controls the <code>Control</code> elements.
	 * Provides access to <code>TextArea</code>.
	 * @author James Daniel Johnson
	 *
	 */
	private class StatisticsWindowController implements Initializable
	{
		@FXML private Button closeButton;
		@FXML private TextArea reportArea;
		
		@Override
		public void initialize(URL location, ResourceBundle resources) 
		{
			reportArea.setEditable(false);
			closeButton.setOnAction(e -> stage.close());
		}
		
		/**
		 * Sets the report <code>TextArea</code> to the
		 * provided <code>String</code>.
		 * @param report The <code>String</code> to set the
		 * 	content to.
		 */
		public void setReportArea(String report)
		{
			reportArea.setText(report);
		}
	}
	
	public StatisticsWindow(Stage stage)
	{
		this.stage = stage;
		this.stage.setTitle("Statistics");
		this.controller = new StatisticsWindowController();
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
					getClass().getResource("fxml/StatisticsWindow.fxml"));
			loader.setController(this.controller);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			this.stage.setScene(scene);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * Shows this window.
	 * @param store The <code>Store</code> object
	 * 	to provide this window.
	 */
	public void show(Store store)
	{
		this.store = store;
		initReportArea();
		this.stage.show();
	}

	
	private void initReportArea() 
	{
		Map<GameFormat, Double> salesByFormat = 
				store.getSalesByFormat();
		StringBuilder text = new StringBuilder();
		text.append(
				String.format("%-22s%11s%n",
						"Format",
						"Total Sales"));
		salesByFormat.forEach((format, total) -> 
		{
			text.append(String.format("%-25s%-2s%6.2f%n", 
					format.getManufacturer() + " " + format.getGameConsole(),
					"$",
					total.doubleValue()));
		});
		text.append(String.format("%-25s%-2s%6.2f%n",
				"Total Sales",
				"$",
				this.store.getDailyTotal()));
		controller.setReportArea(text.toString());
	}
}
