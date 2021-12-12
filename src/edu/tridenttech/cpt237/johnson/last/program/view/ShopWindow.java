package edu.tridenttech.cpt237.johnson.last.program.view;
//AUTHOR: James Daniel Johnson
//COURSE: CPT 237
//ASSIGNMENT: Final Program
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import edu.tridenttech.cpt237.johnson.last.program.model.Cart;
import edu.tridenttech.cpt237.johnson.last.program.model.Game;
import edu.tridenttech.cpt237.johnson.last.program.model.GameFormat;
import edu.tridenttech.cpt237.johnson.last.program.model.Store;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The shop window allows the user to shop,
 * add and remove items from their cart,
 * as well as proceeding to checkout
 * @author James Daniel Johnson
 *
 */
public class ShopWindow 
{

	/**
	 * Controls all of the controls in this window
	 * @author James Daniel Johnson
	 *
	 */
	private class ShopWindowController implements Initializable
	{
		@FXML private ListView<String> consoleChooser;
		@FXML private ListView<String> gameChooser;
		@FXML private ListView<String> cart;

		@FXML private Button addToCart;
		@FXML private Button cancelOrder;
		@FXML private Button removeFromCart;
		@FXML private Button checkout;

		public ListView<String> getConsoleChooser()
		{
			return this.consoleChooser;
		}
		@Override
		public void initialize(
				URL location, 
				ResourceBundle resources) 
		{
			initConsoleChooser();
			initAddToCart();
			initCancelOrder();
			initRemoveFromCart();
			initCheckout();
			gameChooser.
			getSelectionModel().
			setSelectionMode(SelectionMode.MULTIPLE);
			cart.getSelectionModel().
			setSelectionMode(SelectionMode.MULTIPLE);
		}

		private void initCheckout() 
		{
			checkout.setOnAction(e -> proceedToCheckout());
		}
		
		private void initConsoleChooser()
		{
			consoleChooser.
			getSelectionModel().
			setSelectionMode(SelectionMode.SINGLE);
			consoleChooser.
			getSelectionModel().
			selectedItemProperty().
			addListener(e -> 
			{
				populateGames(consoleChooser, gameChooser);
	
			});
		}

		private void initAddToCart()
		{
			addToCart.setOnAction(e -> 
			{
				addItemsToCart(gameChooser, cart);
			});
		}
		
		private void initCancelOrder()
		{
			cancelOrder.setOnAction(e ->
			{
				onCancelOrder();
			});
		}
		
		private void initRemoveFromCart()
		{
			removeFromCart.setOnAction(e -> 
			{
				removeItemsFromCart(cart, gameChooser);
			});
		}
	}

	private Stage stage;
	private Store store;
	private GameFormat currentFormat;
	private Cart shoppingCart;
	private ShopWindowController controller;


	/**
	 * Loads the window from FXML
	 * @param stage The stage for this window
	 */
	public ShopWindow(Stage stage)
	{
		this.stage = stage;
		stage.setOnCloseRequest(e-> 
		{
			onCancelOrder();
		});
		try
		{
			URL fxmlResource = getClass().
					getResource("fxml/ShopWindow.fxml");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(fxmlResource);
			loader.setController(new ShopWindowController());
			Parent root = loader.load();
			this.controller = loader.getController();
			Scene scene = new Scene(root);
			this.stage.setScene(scene);
			this.controller = loader.getController();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Cleans up in the case of a canceled
	 * order or closed window.
	 */
	private void onCancelOrder()
	{
		this.shoppingCart.emptyCart(store);
		this.stage.close();
	}
	

	/**
	 * Removes the items from this cart and displays them
	 * in the gameChooser list.
	 * Will not add games that do not match the current
	 * <code>GameFormat</code> of the gameChooser.
	 * @param cart The cart to remove the items from
	 * @param gameChooser The list to return the items to
	 */
	private void removeItemsFromCart(ListView<String> cart,
			ListView<String> gameChooser)
	{
		cart.getSelectionModel().
			getSelectedItems().forEach(gameString -> 
		{
			String[] fields = gameString.split(" : ");
			if(fields[0].equals(currentFormat.getGameConsole()))
			{
				gameChooser.getItems().add(fields[1]);
				store.returnToInventory(
						shoppingCart.remove(currentFormat, fields[1]));
			}
			else
			{
				for(GameFormat format : GameFormat.values())
				{
					if(format.getGameConsole().equals(fields[0]))
					{
						store.returnToInventory(
								shoppingCart.remove(format, fields[1]));
					}
				}
			}
		});
		cart.getItems().removeAll(
				cart.getSelectionModel().getSelectedItems());
		gameChooser.getItems().sort((left, right) ->
		{
			return left.compareToIgnoreCase(right);
		});
	}

	/**
	 * Populates the values in the consoleChooser list
	 */
	public void populateConsoleChooser()
	{
		ListView<String> consoleChooser = 
				controller.getConsoleChooser();
		for(GameFormat format : GameFormat.values())
		{
			consoleChooser.getItems().add(String.format("%s : %s", 
					format.getManufacturer(),
					format.getGameConsole()));
		}

	}

	/**
	 * Moves the items from the gameChooser list to
	 * the cart list.
	 * @param gameChooser The list of games to choose from
	 * @param cart The list of games in the cart
	 */
	private void addItemsToCart(ListView<String> gameChooser,
			ListView<String> cart)
	{
		ObservableList<String> chosenGames =
				gameChooser.getSelectionModel().getSelectedItems();
		for(String title : chosenGames)
		{
			if(this.shoppingCart.add(
					store.takeFromInventory(currentFormat, title)))
			{
				cart.getItems().add(String.format("%s : %s", 
						this.currentFormat.getGameConsole(),
						title));
			}
		}
		gameChooser.getItems().removeAll(chosenGames);
	}

	/**
	 * Processes the checkout process and
	 * opens a new <code>ConfirmationWindow</code>.
	 * Handles an empty cart by showing an alert
	 * window to the user.
	 */
	private void proceedToCheckout()
	{
		Stage checkoutStage = new Stage();
		checkoutStage.initOwner(this.stage);
		checkoutStage.initModality(Modality.WINDOW_MODAL);
		try
		{
			new ConfirmationWindow(checkoutStage).show(store, 
					store.initiateTransaction(shoppingCart));			
		}
		catch(Exception ex)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(ex.getMessage());
			alert.getDialogPane().getStylesheets().add(
					getClass().
					getResource("style/theme.css").
					toExternalForm());
			alert.show();
		}
	}
	
	/**
	 * Populates the games that can be selected in the
	 * gameChooser <code>ListView</code>.
	 * @param consoleChooser The consoleChooser list view
	 * @param gameChooser The gameChooser list view
	 */
	private void populateGames(ListView<String> consoleChooser,
			ListView<String> gameChooser)
	{
		String[] formatStrings = 
				consoleChooser.
				getSelectionModel().
				getSelectedItem().split(" : ");
		
		try
		{
			currentFormat = GameFormat.select(
					formatStrings[0], 
					formatStrings[1]);
		} 
		catch (Exception e) 
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error: Format Error");
			alert.getDialogPane().getStylesheets().
				add(getClass().
					getResource("style/theme.css").toExternalForm());
			alert.setHeaderText("Invalid Format");
			alert.setContentText("Invalid Format detected");
		}
		gameChooser.getItems().clear();
		ArrayList<Game> gameList = new ArrayList<Game>
		(new HashSet<Game>(
				store.getGameListByFormat(currentFormat)));
		gameList.sort((a,b) -> 
		{
			return a.getTitle().compareToIgnoreCase(b.getTitle());
		});
		gameList.forEach( a -> 
		{
			if(!shoppingCart.contains(a))
			{
				gameChooser.getItems().add(a.getTitle());
			}
		});
	}

	/**
	 * Sets the store to store, creates
	 * the cart, and shows this window.
	 * @param store The store instance to associate this
	 * 	window with.
	 */
	public void show(Store store)
	{
		this.store = store;
		this.shoppingCart = new Cart();
		populateConsoleChooser();
		this.stage.show();
	}
}
