package edu.tridenttech.cpt237.johnson.last.program.view;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Program

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.tridenttech.cpt237.johnson.last.program.model.Store;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This is the window that appears at startup.
 * @author James Daniel Johnson
 */
public class StartupWindow 
{

	private Stage stage;
	private Store store;
	
	/**
	 * Controller for the StartupWindow
	 * @author James Daniel Johnson
	 *
	 */
	private class StartupWindowController implements Initializable
	{
		@FXML private Button shop;
		@FXML private Button showReport;
		@FXML private Button statisticsButton;
		@FXML private Button close;
		
		@Override
		public void initialize(URL location, ResourceBundle resources) 
		{
			shop.setOnAction(e -> startShopping());
			close.setOnAction(e -> stage.close());
			showReport.setOnAction(e -> 
			{
				Stage stage = new Stage();
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(getStage());
				ReportWindow reportWindow = 
						new ReportWindow(stage);
				reportWindow.show(store);
			});
			statisticsButton.setOnAction(e -> 
			{
				new StatisticsWindow(
						new Stage()).show(store);
			});
		}
		
	}
	
	//	Image for the front window: https://www.pexels.com/photo/flatlay-of-gaming-equipments-3165335/
	public StartupWindow(Stage stage)
	{
		this.stage = stage;
		this.stage.initStyle(StageStyle.UNDECORATED);
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setController(new StartupWindowController());
			loader.setLocation(getClass().getResource
					("fxml/StartupWindow.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			this.stage.setScene(scene);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	private void startShopping()
	{
		Stage shopStage = new Stage();
		ShopWindow shopWindow = new ShopWindow(shopStage);
		shopStage.initOwner(this.stage);
		shopStage.initModality(Modality.WINDOW_MODAL);
		shopStage.initStyle(StageStyle.UNDECORATED);
		shopWindow.show(store);
	}
	
	public Stage getStage()
	{
		return this.stage;
	}
	
	public void show(Store store)
	{
		this.store = store;
		this.stage.show();
	}
}
