package mpp.basketproject.repository.db;

import mpp.basketproject.domain.Match;
import mpp.basketproject.domain.Stage;
import mpp.basketproject.repository.RepositoryException;
import mpp.basketproject.repository.interfaces.MatchRepository;
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

public class MatchDBRepository implements MatchRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public MatchDBRepository(Properties properties) {
        logger.info("Initializing MatchDBRepository with properties: {} ", properties);

        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Match entity) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "insert into matches (team1, team2, stage, price, seats_available, date_time) " +
                     "values (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, entity.getTeam1());
            preparedStatement.setString(2, entity.getTeam2());
            preparedStatement.setString(3, entity.getStage().toString());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setInt(5, entity.getSeatsAvailable());
            preparedStatement.setString(6, entity.getDateTime().toString());
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
        String sql = "delete from matches where id = ?";
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
    public void update(Match entity) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "update matches set team1 = ?, team2 = ?, stage = ?, price = ?, seats_available = ?, " +
                     "date_time = ? where id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, entity.getTeam1());
            preparedStatement.setString(2, entity.getTeam2());
            preparedStatement.setString(3, entity.getStage().toString());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setInt(5, entity.getSeatsAvailable());
            preparedStatement.setString(6, entity.getDateTime().toString());
            preparedStatement.setInt(7, entity.getId());
            int numberOfAffectedRows = preparedStatement.executeUpdate();
            logger.info("{} rows affected", numberOfAffectedRows);
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public Match find(Integer id) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "select * from matches where id = ?";
        Match match = null;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            String team1 = resultSet.getString("team1");
            String team2 = resultSet.getString("team2");
            Stage stage = Stage.valueOf(resultSet.getString("stage"));
            Double price = resultSet.getDouble("price");
            Integer seatsAvailable = resultSet.getInt("seats_available");
            LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date_time"));
            match = new Match(team1, team2, stage, price, seatsAvailable, dateTime);
            match.setId(id);

        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
        return match;
    }

    @Override
    public List<Match> getAll() throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "select * from matches";
        List<Match> matchList = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String team1 = resultSet.getString("team1");
                String team2 = resultSet.getString("team2");
                Stage stage = Stage.valueOf(resultSet.getString("stage"));
                Double price = resultSet.getDouble("price");
                Integer seatsAvailable = resultSet.getInt("seats_available");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date_time"));

                Match match = new Match(team1, team2, stage, price, seatsAvailable, dateTime);
                match.setId(id);
                matchList.add(match);
            }
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
        return matchList;
    }

    @Override
    public List<Match> getAllOrderedByAvailableSeats() throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "select * from matches where seats_available > 0 order by seats_available DESC ";
        List<Match> matchList = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String team1 = resultSet.getString("team1");
                String team2 = resultSet.getString("team2");
                Stage stage = Stage.valueOf(resultSet.getString("stage"));
                Double price = resultSet.getDouble("price");
                Integer seatsAvailable = resultSet.getInt("seats_available");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date_time"));

                Match match = new Match(team1, team2, stage, price, seatsAvailable, dateTime);
                match.setId(id);
                matchList.add(match);
            }
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
        return matchList;
    }
}
