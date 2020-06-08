package com.followapp.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.followapp.core.data.CallingGroupRepository;
import com.followapp.core.data.RecipientRepository;
import com.followapp.core.model.Recipient;
import com.followapp.core.model.CallingGroup;
import com.followapp.core.model.CallingGroupUpdateMembershipRequest;

import java.util.Optional;


@Controller
@RequestMapping(path="/recipients")
public class RecipientController {
    @Autowired
    private RecipientRepository recipientRepository;

    @PostMapping(path = "/create-recipient")
    public @ResponseBody String createNewGroup(@RequestBody Recipient recipient) {        	        	
        recipientRepository.createRecipient(recipient);
        return String.format("Created New Recipient %s", recipient);
    }

    @PostMapping(path = "/updateRecipient/{id}")
    public @ResponseBody String updateRecipient(@RequestBody Recipient recipient, @PathVariable Integer id) {
        recipientRepository.updateRecipient(recipient, id);
        return String.format("Updated Recipient %s", id);
    }

    @PostMapping(path = "/update-group-membership")
    public @ResponseBody String updateGroupMembership(@RequestBody CallingGroupUpdateMembershipRequest callingGroupUpdateMembershipRequest) {        	        	
        recipientRepository.updateGroupMembership(callingGroupUpdateMembershipRequest);
        return String.format("Updated Group Membership %s", callingGroupUpdateMembershipRequest);
    }    

    @GetMapping(("/get-all-recipients"))
    public @ResponseBody Iterable<Recipient> getAllGroups() {    	    	    
        return recipientRepository.getAllRecipients();    	    	
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<Recipient> getCallingGroup(@PathVariable Integer id) {        
    	return Optional.ofNullable(recipientRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteCallingGroup(@PathVariable Integer id) {
        recipientRepository.deleteById(id);
        return "Deleted " + id;
    }
}
