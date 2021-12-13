package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
	
	//Atributos dos itens do Menu
	@FXML
	private MenuItem menuItemVendedores;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	
	// Metodos para tratar os eventos do Menu
	@FXML
	public void onMenuItemVendedoresAction() {
		System.out.println("onMenuItemVendedoresAction");
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		//loadView("/gui/DepartmentList.fxml");
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});//expressao lambda
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		loadView("/gui/Sobre.fxml", x -> {}); //a tela não faz nenhuma ação
	}
	
	
	//Metodo da interface Initializable
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	//Função para abrir outra tela
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//o getController retorna o controlador do tipo que será chamado nas expressões lambda
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}

}
