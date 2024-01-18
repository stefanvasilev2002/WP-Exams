package mk.ukim.finki.wp.june2022.g1.web;

import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import mk.ukim.finki.wp.june2022.g1.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class VirtualServerController {

    private final VirtualServerService service;
    private final UserService userService;

    public VirtualServerController(VirtualServerService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * This method should use the "list.html" template to display all entities.
     * The method should be mapped on paths '/' and '/VirtualServers'.
     * The arguments that this method takes are optional and can be 'null'.
     *
     * @return The view "list.html".
     */

    @GetMapping({"/","/VirtualServers"})
    public String showList(@RequestParam(required = false) Integer activeMoreThanDays,
                           @RequestParam(required = false) Long ownerId, Model model) {
        List<VirtualServer> virtualServers;
        if (ownerId == null && activeMoreThanDays == null) {
            virtualServers = this.service.listAll();
        } else {
            virtualServers = this.service.filter(ownerId, activeMoreThanDays);
        }
        List<User> users=this.userService.listAll();
        model.addAttribute("servers",virtualServers);
        model.addAttribute("users",users);
        return "list.html";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/VirtualServers/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/VirtualServers/add")
    public String showAdd(Model model) {

        List<User> users=this.userService.listAll();


        model.addAttribute("users",users);

        return "form.html";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case all 'input' elements should be filled with the appropriate value for the entity that is updated.
     * The method should be mapped on path '/VirtualServers/[id]/edit'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/VirtualServers/{id}/edit")
    public String showEdit(@PathVariable Long id,Model model) {

        List<User> users=this.userService.listAll();
        VirtualServer virtualServer=this.service.findById(id);

        model.addAttribute("server",virtualServer);
        model.addAttribute("users",users);
        return "form.html";
    }

    /**
     * This method should create an entity given the arguments it takes.
     * The method should be mapped on path '/VirtualServers'.
     * After the entity is created, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/VirtualServers")
    public String create(@RequestParam String instanceName,
                         @RequestParam String ipAddress,
                         @RequestParam OSType osType,
                         @RequestParam List<Long> owners,
                         @RequestParam String launchDate) {
        this.service.create(instanceName, ipAddress, osType, owners, LocalDate.parse(launchDate));
        return "redirect:/VirtualServers";
    }

    /**
     * This method should update an entity given the arguments it takes.
     * The method should be mapped on path '/VirtualServers/[id]'.
     * After the entity is updated, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/VirtualServers/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String instanceName,
                         @RequestParam String ipAddress,
                         @RequestParam OSType osType,
                         @RequestParam List<Long> owners) {
        this.service.update(id, instanceName, ipAddress, osType, owners);
        return "redirect:/VirtualServers";
    }

    /**
     * This method should delete the entities that have the appropriate identifier.
     * The method should be mapped on path '/VirtualServers/[id]/delete'.
     * After the entity is deleted, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/VirtualServers/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/VirtualServers";
    }

    /**
     * This method should mark the virtual server that has the appropriate identifier as terminated.
     * The method should be mapped on path '/VirtualServers/[id]/terminate'.
     * After its execution, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/VirtualServers/{id}/terminate")
    public String terminate(@PathVariable Long id) {
        this.service.markTerminated(id);
        return "redirect:/VirtualServers";
    }
}
