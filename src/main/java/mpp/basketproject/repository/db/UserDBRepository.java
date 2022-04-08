package mpp.basketproject.repository.db;


import mpp.basketproject.domain.User;
import mpp.basketproject.repository.RepositoryException;
import mpp.basketproject.repository.interfaces.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements UserRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserDBRepository(Properties properties) {
        logger.info("Initializing UserDBRepository with properties: {} ", properties);
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(User entity) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "insert into users (username) values (?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, entity.getUsername());
            int numberOfAffectedRows = preparedStatement.executeUpdate();
            logger.info("{} rows affected", numberOfAffectedRows);
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("This username is already used!\n");
        }

        logger.traceExit();
    }

    @Override
    public void delete(Integer id) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "delete from users where id = ?";
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
    public void update(User entity) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "update users set username = ? where id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setInt(2, entity.getId());
            int numberOfAffectedRows = preparedStatement.executeUpdate();
            logger.info("{} rows affected", numberOfAffectedRows);
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
    }

    @Override
    public User find(Integer id) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "select * from users where id = ?";
        User user = null;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String username = resultSet.getString("username");
            user = new User(username);
            user.setId(id);

        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
        return user;
    }

    @Override
    public List<User> getAll() throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "select * from users";
        List<User> userList = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                User user = new User(username);
                user.setId(id);
                userList.add(user);
            }
        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("SQL Exception " + ex.getMessage());
        }

        logger.traceExit();
        return userList;
    }

    @Override
    public User findByUsername(String username) throws RepositoryException {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        String sql = "select * from users where username = ?";
        User user = null;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Integer id = resultSet.getInt("id");
            user = new User(username);
            user.setId(id);

        } catch (SQLException ex) {
            logger.error("SQL Exception: " + ex.getMessage());
            throw new RepositoryException("This account does not exist!\n");
        }

        logger.traceExit();
        return user;
    }
}
