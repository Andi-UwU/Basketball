package mpp.basketproject.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mpp.basketproject.Main;
import mpp.basketproject.dto.MatchDTO;
import mpp.basketproject.domain.User;
import mpp.basketproject.repository.RepositoryException;
import mpp.basketproject.service.Service;
import mpp.basketproject.utils.InfoBox;
import mpp.basketproject.validator.ValidationException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MainPageController {

    // =============== Add match ==================
    @FXML
    public TextField team1TextField;
    @FXML
    public TextField team2TextField;
    @FXML
    public ComboBox stageComboBox;
    @FXML
    public TextField priceTextField;
    @FXML
    public TextField seatsAvailableTextField;
    @FXML
    public Button addMatchButton;
    @FXML
    public DatePicker datePicker;
    @FXML
    public TextField hourTextField;
    @FXML
    public TextField minuteTextField;

    // ============ Sell Ticket ==============
    @FXML
    public TextField clientNameTextField;
    @FXML
    public TextField seatsTextField;
    @FXML
    public TextField idTextField;
    @FXML
    public Button sellTicketButton;



    // ========== Scene ================

    private Service service;
    private User user;

    // =========== Buttons ==============

    @FXML
    public Button backToLoginButton;

    // ======== Match Table View ========
    @FXML
    public TableView<MatchDTO> matchTable;
    @FXML
    public TableColumn<MatchDTO, Integer> idColumn;
    @FXML
    public TableColumn<MatchDTO, String> titleColumn;
    @FXML
    public TableColumn<MatchDTO, String> stageColumn;
    @FXML
    public TableColumn<MatchDTO, Double> priceColumn;
    @FXML
    public TableColumn<MatchDTO, Integer> seatsAvailableColumn;
    @FXML
    public TableColumn<MatchDTO, String> dateColumn;
    @FXML
    public TableColumn<MatchDTO, String> statusColumn;

    private ObservableList<MatchDTO> matchList = FXCollections.observableArrayList();

    @FXML
    public CheckBox orderCheckBox;



    // ============ Others =================
    @FXML
    public Label welcomeLabel;



    public void setService(Service service) {
        this.service = service;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void updateMatchTable(){
        try {
            if (orderCheckBox.isSelected())
                matchList.setAll(service.getMatchDTOOrdered());
            else
                matchList.setAll(service.getMatchDTO());
            matchTable.setItems(matchList);
        } catch (RepositoryException e) {
            InfoBox.show(e.getMessage());
        }
    }

    private void initializeMatchTable(){
        matchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //hides side scrollbar

        matchTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idTextField.setText(newSelection.getId().toString());
            }
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<MatchDTO, Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<MatchDTO, String>("title"));
        stageColumn.setCellValueFactory(new PropertyValueFactory<MatchDTO, String>("stage"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<MatchDTO, Double>("price"));
        seatsAvailableColumn.setCellValueFactory(new PropertyValueFactory<MatchDTO, Integer>("seatsAvailable"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<MatchDTO, String>("dateTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<MatchDTO, String>("status"));

        updateMatchTable();
    }

    @FXML
    private void initialize(){
        stageComboBox.getItems().addAll(
                "FINAL",
                "SEMIFINAL1",
                "SEMIFINAL2"
        );
        Platform.runLater(() -> {
            welcomeLabel.setText("Welcome " + user.getUsername() + "!");
            initializeMatchTable();
        });
    }

    @FXML
    public void backToLoginButtonAction(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            controller.setService(service);
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setTitle("Login!");
            stage.setScene(scene);

            stage.show();
        } catch (IOException ex) {
            InfoBox.show(ex.getMessage());
        }

    }

    @FXML
    public void addMatchButtonAction(ActionEvent actionEvent) {
        try{
            String team1 = team1TextField.getText();
            String team2 = team2TextField.getText();
            mpp.basketproject.domain.Stage stage = null;
            if (stageComboBox.getValue() != null){
                stage = mpp.basketproject.domain.Stage.valueOf(stageComboBox.getValue().toString());
            }
            Double price = null;
            try{
                price = Double.parseDouble(priceTextField.getText());
            }catch (NumberFormatException ex){
                InfoBox.show("The price must be a number!");
                return;
            }
            Integer seatsAvailable = null;
            try{
                seatsAvailable = Integer.parseInt(seatsAvailableTextField.getText());
            }catch (NumberFormatException ex){
                InfoBox.show("Seats available must be a number!");
                return;
            }

            LocalDate date = datePicker.getValue();
            String hour = hourTextField.getText();
            String minute = minuteTextField.getText();
            LocalTime time = null;
            try{
                time = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
            }catch (NumberFormatException ex){
                InfoBox.show("Hour and Minute must be numbers!");
                return;
            }

            LocalDateTime dateTime = LocalDateTime.of(date, time);

            service.addMatch(team1, team2, stage, price, seatsAvailable, dateTime);
            updateMatchTable();

            // reset text fields
            team1TextField.clear();
            team2TextField.clear();
            priceTextField.clear();
            seatsAvailableTextField.clear();
            hourTextField.clear();
            minuteTextField.clear();

        }catch (Exception ex){
            InfoBox.show(ex.getMessage());
        }
    }

    @FXML
    public void sellTicketButtonAction(ActionEvent actionEvent){
        try{
            Integer matchId = Integer.parseInt(idTextField.getText());
            String clientName = clientNameTextField.getText();
            Integer numberOfSeats = Integer.parseInt(seatsTextField.getText());

            service.sellTicket(user.getId(), clientName, matchId, numberOfSeats);


            updateMatchTable();
        } catch (ValidationException e) {
            InfoBox.show(e.getMessage());
        } catch (RepositoryException e) {
            InfoBox.show(e.getMessage());
        } catch (NumberFormatException e){
            InfoBox.show("ID and seats must be numbers!");
        }
    }


    public void orderCheckBoxAction(ActionEvent actionEvent) {

        updateMatchTable();
    }
}
