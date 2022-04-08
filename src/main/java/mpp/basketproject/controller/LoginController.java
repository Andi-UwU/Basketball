package mpp.basketproject.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mpp.basketproject.Main;

import mpp.basketproject.domain.User;
import mpp.basketproject.service.Service;
import mpp.basketproject.utils.InfoBox;

public class LoginController {


    @FXML
    public TextField usernameTextField;

    private Service service;

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    public void loginButtonAction(ActionEvent actionEvent) {

        try{
            String username = usernameTextField.getText();
            if (username.length() < 3){
                InfoBox.show("The username must have at least 3 characters!");
                return;
            }
            User user = service.findUserByUsername(username);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainPage.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            MainPageController controller = fxmlLoader.getController();
            controller.setService(service);
            controller.setUser(user);
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setTitle("Basketball");
            stage.setScene(scene);

            stage.show();


        }catch (Exception ex){
            InfoBox.show(ex.getMessage());
        }
    }

    @FXML
    public void createAccountButtonAction(ActionEvent actionEvent) {
        try{
            String username = usernameTextField.getText();

            service.addUser(username);

            InfoBox.show("Account created successfully with the username: " + username);
        }catch (Exception ex){
            InfoBox.show(ex.getMessage());
        }
    }
}