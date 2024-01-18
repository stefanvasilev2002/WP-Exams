package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.june2022.g1.repository.VirtualServerRepository;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VirtualServerServiceImpl implements VirtualServerService {
    private final VirtualServerRepository virtualServerRepository;
    private final UserRepository userRepository;

    public VirtualServerServiceImpl(VirtualServerRepository virtualServerRepository, UserRepository userRepository) {
        this.virtualServerRepository = virtualServerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<VirtualServer> listAll() {
        return virtualServerRepository.findAll();
    }

    @Override
    public VirtualServer findById(Long id) {
        return virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException::new);
    }

    @Override
    public VirtualServer create(String name, String ipAddress, OSType osType, List<Long> owners, LocalDate launchDate) {
        List<User> userList = userRepository.findAllById(owners);
        VirtualServer virtualServer = new VirtualServer(name,ipAddress,osType,userList,launchDate);
        return virtualServerRepository.save(virtualServer);
    }

    @Override
    public VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners) {
        VirtualServer virtualServer = findById(id);
        virtualServer.setInstanceName(name);
        virtualServer.setIpAddress(ipAddress);
        virtualServer.setOSType(osType);
        List<User> userList = userRepository.findAllById(owners);
        virtualServer.setOwners(userList);
        return virtualServerRepository.save(virtualServer);
    }

    @Override
    public VirtualServer delete(Long id) {
        VirtualServer virtualServer = findById(id);
        virtualServerRepository.delete(virtualServer);
        return virtualServer;
    }

    @Override
    public VirtualServer markTerminated(Long id) {
        VirtualServer virtualServer = findById(id);
        virtualServer.setTerminated(true);
        return virtualServerRepository.save(virtualServer);
    }

    @Override
    public List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays) {
        User user;
        LocalDate date;
        if (ownerId != null && activeMoreThanDays != null) {
            user = userRepository.findById(ownerId).orElseThrow(InvalidUserIdException::new);
            date = LocalDate.now().minusDays(activeMoreThanDays);
            return virtualServerRepository.findAllByOwnersAndLaunchDateBefore(user,date);
        }
        else if(ownerId != null){
            user = userRepository.findById(ownerId).orElseThrow(InvalidUserIdException::new);
            return virtualServerRepository.findAllByOwners(user);
        }
        else if(activeMoreThanDays != null){
            date = LocalDate.now().minusDays(activeMoreThanDays);
            return virtualServerRepository.findAllByLaunchDateBefore(date);
        }
        return listAll();
    }
}
