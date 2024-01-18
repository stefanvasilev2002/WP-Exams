package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.repository.VirtualServerRepository;
import mk.ukim.finki.wp.june2022.g1.service.UserService;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirtualServerServiceImpl implements VirtualServerService {
    private final VirtualServerRepository virtualServerRepository;
    private final UserService userService;

    public VirtualServerServiceImpl(VirtualServerRepository virtualServerRepository, UserService userService) {
        this.virtualServerRepository = virtualServerRepository;
        this.userService = userService;
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
        return virtualServerRepository.save(new VirtualServer(
                name,
                ipAddress,
                osType,
                owners.stream().map(userService::findById).collect(Collectors.toList()),
                launchDate
        ));
    }

    @Override
    public VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners) {
        VirtualServer server = findById(id);

        server.setInstanceName(name);
        server.setIpAddress(ipAddress);
        server.setOSType(osType);
        server.setOwners(owners.stream().map(userService::findById).collect(Collectors.toList()));

        return virtualServerRepository.save(server);
    }

    @Override
    public VirtualServer delete(Long id) {
        VirtualServer server = findById(id);
        virtualServerRepository.delete(server);
        return server;
    }

    @Override
    public VirtualServer markTerminated(Long id) {
        VirtualServer server = findById(id);
        server.setTerminated(true);
        virtualServerRepository.save(server);
        return server;
    }

    @Override
    public List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays) {
        if (ownerId == null && activeMoreThanDays == null){
            return listAll();
        }
        else if (ownerId != null && activeMoreThanDays != null){
            return virtualServerRepository.
                    findByOwnersContainingAndLaunchDateBefore(
                            userService.findById(ownerId),
                            LocalDate.now().minusDays(activeMoreThanDays));
        }
        else if (ownerId == null && activeMoreThanDays != null) {
            return virtualServerRepository.findByLaunchDateBefore(LocalDate.now().minusDays(activeMoreThanDays));
        }
        else return virtualServerRepository.findByOwnersContaining(userService.findById(ownerId));
    }
}
