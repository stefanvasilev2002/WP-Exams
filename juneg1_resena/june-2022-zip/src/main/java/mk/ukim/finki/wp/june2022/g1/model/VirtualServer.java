package mk.ukim.finki.wp.june2022.g1.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class VirtualServer {

    public VirtualServer() {
    }

    public VirtualServer(String instanceName, String ipAddress, OSType osType, List<User> owners, LocalDate launchDate) {
        this.instanceName = instanceName;
        this.ipAddress = ipAddress;
        this.OSType = osType;
        this.owners = owners;
        this.launchDate = launchDate;
    }

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate launchDate;

    private String instanceName;

    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private OSType OSType;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> owners;

    private Boolean terminated = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public OSType getStatus() {
        return OSType;
    }

    public void setStatus(OSType OSType) {
        this.OSType = OSType;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
        this.owners = owners;
    }

    public Boolean getTerminated() {
        return terminated;
    }

    public void setTerminated(Boolean terminated) {
        this.terminated = terminated;
    }

    public mk.ukim.finki.wp.june2022.g1.model.OSType getOSType() {
        return OSType;
    }

    public void setOSType(mk.ukim.finki.wp.june2022.g1.model.OSType OSType) {
        this.OSType = OSType;
    }
}
