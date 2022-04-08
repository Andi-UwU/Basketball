package mpp.basketproject.repository.interfaces;


import mpp.basketproject.domain.User;
import mpp.basketproject.repository.RepositoryException;

public interface UserRepository extends Repository <Integer, User>{

    User findByUsername(String username) throws RepositoryException;
}
