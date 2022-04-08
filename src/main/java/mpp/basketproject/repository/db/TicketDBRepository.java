package mpp.basketproject.repository.db;


import mpp.basketproject.domain.Match;
import mpp.basketproject.domain.Stage;
import mpp.basketproject.domain.Ticket;
import mpp.basketproject.domain.User;
import mpp.basketproject.repository.RepositoryException;
import mpp.basketproject.repository.interfaces.TicketRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketDBRepository implements TicketRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public TicketDBRepository(Properties properties) {
        logger.info("Initializing TicketDBRepository with properties: {} ", properties);

        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Ticket entity) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "insert into tickets (user_id, match_id, client_name, number_of_seats) " +
                "values (?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, entity.getSeller().getId());
            preparedStatement.setInt(2, entity.getMatch().getId());
            preparedStatement.setString(3, entity.getClientName());
            preparedStatement.setInt(4, entity.getNumberOfSeats());

            int numberOfAffectedRows = preparedStatement.executeUpdate();
            logger.info("{} rows affected", numberOfAffectedRows);
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public void delete(Integer id) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "delete from tickets where id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, id);
            int numberOfAffectedRows = preparedStatement.executeUpdate();
            logger.info("{} rows affected", numberOfAffectedRows);
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public void update(Ticket entity) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "update tickets set user_id = ?, match_id = ?, client_name = ?, number_of_seats = ? " +
                     "where id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, entity.getSeller().getId());
            preparedStatement.setInt(2, entity.getMatch().getId());
            preparedStatement.setString(3, entity.getClientName());
            preparedStatement.setInt(4, entity.getNumberOfSeats());
            preparedStatement.setInt(5, entity.getId());
            int numberOfAffectedRows = preparedStatement.executeUpdate();
            logger.info("{} rows affected", numberOfAffectedRows);
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public Ticket find(Integer id) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql =    "select client_name, number_of_seats,\n" +
                        "       m.id as match_id, m.team1, m.team2, m.stage, m.price, m.seats_available, m.date_time,\n" +
                        "       u.id as user_id, username\n" +
                        "from tickets t\n" +
                        "inner join matches m on t.match_id = m.id\n" +
                        "inner join users u on t.user_id = u.id\n" +
                        "where t.id = ?;";
        Ticket ticket = null;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            //Match
            Integer matchId = resultSet.getInt("match_id");
            String team1 = resultSet.getString("team1");
            String team2 = resultSet.getString("team2");
            Stage stage = Stage.valueOf(resultSet.getString("stage"));
            Double price = resultSet.getDouble("price");
            Integer seatsAvailable = resultSet.getInt("seats_available");
            LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date_time"));
            Match match = new Match(team1, team2, stage, price, seatsAvailable, dateTime);
            match.setId(matchId);

            //User
            Integer userId = resultSet.getInt("user_id");
            String username = resultSet.getString("username");
            User user = new User(username);
            user.setId(userId);

            //Ticket
            String clientName = resultSet.getString("client_name");
            Integer numberOfSeats = resultSet.getInt("number_of_seats");
            ticket = new Ticket(user, clientName, match, numberOfSeats);
            ticket.setId(id);


        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
        return ticket;
    }

    @Override
    public List<Ticket> getAll() throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql =    "select t.id as ticket_id, client_name, number_of_seats,\n" +
                        "       m.id as match_id, m.team1, m.team2, m.stage, m.price, m.seats_available, m.date_time,\n" +
                        "       u.id as user_id, username\n" +
                        "from tickets t\n" +
                        "inner join matches m on t.match_id = m.id\n" +
                        "inner join users u on t.user_id = u.id\n";

        List<Ticket> ticketList = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                //Match
                Integer matchId = resultSet.getInt("match_id");
                String team1 = resultSet.getString("team1");
                String team2 = resultSet.getString("team2");
                Stage stage = Stage.valueOf(resultSet.getString("stage"));
                Double price = resultSet.getDouble("price");
                Integer seatsAvailable = resultSet.getInt("seats_available");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date_time"));
                Match match = new Match(team1, team2, stage, price, seatsAvailable, dateTime);
                match.setId(matchId);

                //User
                Integer userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                User user = new User(username);
                user.setId(userId);

                //Ticket
                Integer ticketId = resultSet.getInt("ticket_id");
                String clientName = resultSet.getString("client_name");
                Integer numberOfSeats = resultSet.getInt("number_of_seats");
                Ticket ticket = new Ticket(user, clientName, match, numberOfSeats);
                ticket.setId(ticketId);

                ticketList.add(ticket);
            }
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
        return ticketList;
    }
}
