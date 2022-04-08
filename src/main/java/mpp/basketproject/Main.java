package mpp.basketproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mpp.basketproject.controller.LoginController;
import mpp.basketproject.domain.User;
import mpp.basketproject.repository.RepositoryException;
import mpp.basketproject.repository.db.MatchDBRepository;
import mpp.basketproject.repository.db.TicketDBRepository;
import mpp.basketproject.repository.db.UserDBRepository;
import mpp.basketproject.repository.interfaces.MatchRepository;
import mpp.basketproject.repository.interfaces.TicketRepository;
import mpp.basketproject.repository.interfaces.UserRepository;
import mpp.basketproject.service.Service;
import mpp.basketproject.validator.MatchValidator;
import mpp.basketproject.validator.TicketValidator;
import mpp.basketproject.validator.UserValidator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("db.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserRepository userRepository = new UserDBRepository(properties);
        UserValidator userValidator = new UserValidator();
        MatchRepository matchRepository = new MatchDBRepository(properties);
        MatchValidator matchValidator = new MatchValidator();
        TicketRepository ticketRepository = new TicketDBRepository(properties);
        TicketValidator ticketValidator = new TicketValidator();

        Service service = new Service(userRepository, userValidator, matchRepository, matchValidator,ticketRepository, ticketValidator);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setService(service);

        Scene scene = new Scene(parent);
        stage.setTitle("Login!");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("ball.png")));

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}