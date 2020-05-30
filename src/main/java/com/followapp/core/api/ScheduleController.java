package com.followapp.core.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.followapp.core.data.ScheduleRepository;
import com.followapp.core.model.Schedule;

@Controller
@RequestMapping(path = "/schedule")
public class ScheduleController {

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@GetMapping(("/all"))
	public @ResponseBody Iterable<Schedule> getAllSchedules() {
		return scheduleRepository.getAllSchedules();
	}

	@GetMapping(("/all/{type}"))
	public @ResponseBody Iterable<Schedule> getSchedules(@PathVariable String type) {
		return scheduleRepository.getSchedules(type);
	}

	@GetMapping("/{id}")
	public @ResponseBody Optional<Schedule> getScheduleById(@PathVariable Integer id) {
		return Optional.ofNullable(scheduleRepository.getScheduleById(id));
	}

	@PostMapping(path = "/create")
	public @ResponseBody Optional<Schedule> createSchedule(@RequestBody Schedule newSchedule) {
		return Optional.ofNullable(scheduleRepository.createNewSchedule(newSchedule));
	}
	
	@PostMapping(path = "/update/{id}")
	public @ResponseBody Optional<Schedule> updateSchedule(@RequestBody Schedule newSchedule) {
		return Optional.ofNullable(scheduleRepository.createNewSchedule(newSchedule));
	}
	
	@DeleteMapping("/{id}")
    public @ResponseBody String deleteSchedule(@PathVariable Integer id) {
		scheduleRepository.deleteScheduleById(id);
        return "Deleted " + id;
    }
}
