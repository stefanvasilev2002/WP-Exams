package mk.ukim.finki.wp.june2022.g1.service;


import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidUserIdException;

import java.time.LocalDate;
import java.util.List;

public interface VirtualServerService {

    /**
     * @return List of all entities in the database
     */
    List<VirtualServer> listAll();

    /**
     * returns the entity with the given id
     *
     * @param id The id of the entity that we want to obtain
     * @return
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     */
    VirtualServer findById(Long id);

    /**
     * This method is used to create a new entity, and save it in the database.
     *
     * @return The entity that is created. The id should be generated when the entity is created.
     * @throws InvalidUserIdException when there is no user with the given id
     */
    VirtualServer create(String name, String ipAddress, OSType osType, List<Long> owners, LocalDate launchDate);

    /**
     * This method is used to modify an entity, and save it in the database.
     *
     * @param id          The id of the entity that is being edited
     * @return The entity that is updated.
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     * @throws InvalidUserIdException    when there is no user with the given id
     */
    VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners);

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidVirtualServerIdException.
     *
     * @param id
     * @return The entity that is deleted.
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     */
    VirtualServer delete(Long id);

    /**
     * Method that should mark as terminated the virtual server. If the id is invalid, it should throw InvalidVirtualMachineIdException.
     *
     * @param id
     * @return The entity that should be marked as terminated.
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     */
    VirtualServer markTerminated(Long id);

    /**
     * The implementation of this method should use repository implementation for the filtering.
     * All arguments are nullable. When an argument is null, we should not filter by that attribute
     *
     * @return The entities that meet the filtering criteria
     */
    List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays);
}
