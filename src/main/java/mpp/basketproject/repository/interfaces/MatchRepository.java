package mpp.basketproject.repository.interfaces;

import mpp.basketproject.domain.Match;
import mpp.basketproject.repository.RepositoryException;

import java.util.List;

public interface MatchRepository extends Repository<Integer, Match> {

    List<Match> getAllOrderedByAvailableSeats() throws RepositoryException;
}
