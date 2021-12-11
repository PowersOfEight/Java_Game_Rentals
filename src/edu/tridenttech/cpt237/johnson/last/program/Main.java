package edu.tridenttech.cpt237.johnson.last.program;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Program
import edu.tridenttech.cpt237.johnson.last.program.model.Store;
import edu.tridenttech.cpt237.johnson.last.program.view.StartupWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application 
{
	@Override
	public void start(Stage stage) throws Exception 
	{
		Store store = Store.getInstance();
		StartupWindow startWindow = new StartupWindow(stage);
		startWindow.show(store);
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
