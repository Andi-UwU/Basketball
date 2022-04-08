package mpp.basketproject.repository.interfaces;


import mpp.basketproject.domain.Entity;
import mpp.basketproject.repository.RepositoryException;


import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {

    /**
     * adds an entity to the repository
     * @param entity E
     */
    void add(E entity) throws RepositoryException;

    /**
     * deletes an entity
     * @param id ID - the id of the deleted entity
     * @throws RepositoryException "Inexistent entity" - if the entity does not exist
     */
    void delete(ID id) throws RepositoryException;

    /**
     *
     * @param entity E
     */
    void update(E entity) throws RepositoryException;

    /**
     * finds an entity with a given id
     * @param id ID
     * @return E
     * @throws RepositoryException "Inexistent entity" - if the entity does not exist
     */
    E find(ID id) throws RepositoryException;

    /**
     * returns all entities from the repository
     * @return List<E> - a list with all the entities from the repository
     */
    List<E> getAll() throws RepositoryException;


}
