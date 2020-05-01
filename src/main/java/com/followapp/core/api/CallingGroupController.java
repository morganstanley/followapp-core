/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.followapp.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.followapp.core.data.CallingGroupRepository;
import com.followapp.core.model.CallingGroup;
import com.followapp.core.model.CallingGroupUpdateMembershipRequest;

import java.util.Optional;


@Controller
@RequestMapping(path="/calling-groups")
public class CallingGroupController {
    @Autowired
    private CallingGroupRepository callingGroupRepository;

    @PostMapping(path = "/create-new-group")
    public @ResponseBody String createNewGroup(@RequestBody CallingGroup callingGroup) {        	        	
        callingGroupRepository.createNewGroup(callingGroup);
        return String.format("Created New Group %s", callingGroup);
    }
    
    @PostMapping(path = "/update-group-membership")
    public @ResponseBody String updateGroupMembership(@RequestBody CallingGroupUpdateMembershipRequest callingGroupUpdateMembershipRequest) {        	        	
        callingGroupRepository.updateGroupMembership(callingGroupUpdateMembershipRequest);
        return String.format("Updated Group Membership %s", callingGroupUpdateMembershipRequest);
    }    

    @GetMapping(("/get-all-groups"))
    public @ResponseBody Iterable<CallingGroup> getAllGroups() {    	    	    
        return callingGroupRepository.getAllGroups();    	    	
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<CallingGroup> getCallingGroup(@PathVariable Integer id) {        
    	return Optional.ofNullable(callingGroupRepository.getCallingGroupById(id));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteCallingGroup(@PathVariable Integer id) {
        callingGroupRepository.deleteById(id);
        return "Deleted " + id;
    }
}
