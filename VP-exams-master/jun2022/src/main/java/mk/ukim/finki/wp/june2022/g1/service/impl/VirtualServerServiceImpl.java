package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.repository.VirtualServerRepository;
import mk.ukim.finki.wp.june2022.g1.service.UserService;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import org.springframework.security.core.parameters.P;
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
        return virtualServerRepository.save(new VirtualServer(name, ipAddress, osType, owners.stream().map(userService::findById).collect(Collectors.toList()), launchDate));
    }

    @Override
    public VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners) {
        VirtualServer v = findById(id);

        v.setInstanceName(name);
        v.setIpAddress(ipAddress);
        v.setOSType(osType);
        v.setOwners(owners.stream().map(userService::findById).collect(Collectors.toList()));

        return virtualServerRepository.save(v);
    }

    @Override
    public VirtualServer delete(Long id) {
        VirtualServer v = findById(id);

        virtualServerRepository.delete(v);

        return v;
    }

    @Override
    public VirtualServer markTerminated(Long id) {
        VirtualServer v = findById(id);

        v.setTerminated(true);

        return virtualServerRepository.save(v);
    }

    @Override
    public List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays) {
        if (ownerId != null && activeMoreThanDays != null) {
            return virtualServerRepository.findAllByOwnersContainsAndLaunchDateBefore(userService.findById(ownerId), LocalDate.now().minusDays(activeMoreThanDays));
        } else if (ownerId == null && activeMoreThanDays == null) {
            return listAll();
        } else if (ownerId == null) {
            return virtualServerRepository.findAllByLaunchDateBefore(LocalDate.now().minusDays(activeMoreThanDays));
        } else {
            return virtualServerRepository.findAllByOwnersContains(userService.findById(ownerId));
        }
    }
}
