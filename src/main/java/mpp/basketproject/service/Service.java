package mpp.basketproject.service;

import mpp.basketproject.domain.*;
import mpp.basketproject.dto.MatchDTO;
import mpp.basketproject.repository.RepositoryException;
import mpp.basketproject.repository.interfaces.MatchRepository;
import mpp.basketproject.repository.interfaces.TicketRepository;
import mpp.basketproject.repository.interfaces.UserRepository;
import mpp.basketproject.validator.MatchValidator;
import mpp.basketproject.validator.TicketValidator;
import mpp.basketproject.validator.UserValidator;
import mpp.basketproject.validator.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Service {

    private UserRepository userRepository;
    private UserValidator userValidator;
    private MatchRepository matchRepository;
    private MatchValidator matchValidator;
    private TicketRepository ticketRepository;
    private TicketValidator ticketValidator;

    public Service(UserRepository userRepository, UserValidator userValidator, MatchRepository matchRepository, MatchValidator matchValidator, TicketRepository ticketRepository, TicketValidator ticketValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.matchRepository = matchRepository;
        this.matchValidator = matchValidator;
        this.ticketRepository = ticketRepository;
        this.ticketValidator = ticketValidator;
    }
    // ========== MATCH ===========
    public List<Match> getAllMatches() throws RepositoryException {
        return matchRepository.getAll();
    }

    public void addMatch(String team1, String team2, Stage stage, Double price, Integer seatsAvailable, LocalDateTime dateTime)
            throws ValidationException, RepositoryException {

        Match match = new Match(team1, team2, stage, price, seatsAvailable, dateTime);
        matchValidator.validate(match);
        matchRepository.add(match);
    }

    public void deleteMatch(Integer id) throws RepositoryException {
        matchRepository.delete(id);
    }

    public List<MatchDTO> getMatchDTO() throws RepositoryException {
        return matchRepository.getAll()
                .stream()
                .map(match -> new MatchDTO(match))
                .collect(Collectors.toList());
    }

    public List<MatchDTO> getMatchDTOOrdered() throws RepositoryException{
        return matchRepository.getAllOrderedByAvailableSeats()
                .stream()
                .map(match -> new MatchDTO(match))
                .collect(Collectors.toList());
    }


    // ========== USER =============
    public void addUser(String username) throws ValidationException, RepositoryException {
        User user = new User(username);
        userValidator.validate(user);
        userRepository.add(user);
    }
    public User findUserByUsername(String username) throws RepositoryException {
        return userRepository.findByUsername(username);
    }

    // ============ TICKET =============
    public void sellTicket(Integer sellerId, String clientName, Integer matchId, Integer numberOfSeats)
            throws RepositoryException, ValidationException {
        Match match = matchRepository.find(matchId);
        User seller = userRepository.find(sellerId);
        Ticket ticket = new Ticket(seller, clientName, match, numberOfSeats);
        ticketValidator.validate(ticket);
        Integer oldSeatsAvailable = match.getSeatsAvailable();
        match.setSeatsAvailable(oldSeatsAvailable - numberOfSeats);
        matchValidator.validate(match);
        matchRepository.update(match);
        ticketRepository.add(ticket);
    }

}
