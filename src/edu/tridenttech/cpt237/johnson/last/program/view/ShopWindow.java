package edu.tridenttech.cpt237.johnson.last.program.view;

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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShopWindow 
{

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


	public ShopWindow(Stage stage)
	{
		this.stage = stage;
		stage.setOnHidden(e-> 
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
	
	private void onCancelOrder()
	{
		this.shoppingCart.emptyCart(store);
		this.stage.close();
	}

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
	
	public void populateListViews()
	{
		populateConsoleChooser();
	}

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

	private void proceedToCheckout()
	{
		Stage checkoutStage = new Stage();
		checkoutStage.initOwner(this.stage);
		checkoutStage.initModality(Modality.WINDOW_MODAL);
		new ConfirmationWindow(checkoutStage).show(store, 
				store.initiateTransaction(shoppingCart));
	}
	
	private void populateGames(ListView<String> consoleChooser,
			ListView<String> gameChooser)
	{
		String[] formatStrings = 
				consoleChooser.
				getSelectionModel().
				getSelectedItem().split(" : ");
		currentFormat = GameFormat.select(
				formatStrings[0], 
				formatStrings[1]);
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

	public void show(Store store)
	{
		this.store = store;
		this.shoppingCart = new Cart();
		populateListViews();
		this.stage.show();
	}
}
