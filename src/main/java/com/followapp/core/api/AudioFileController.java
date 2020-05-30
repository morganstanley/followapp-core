package com.followapp.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.followapp.core.data.AudioFileRepository;
import com.followapp.core.model.AudioFile;

@Controller
@RequestMapping(path = "/audioFiles")
public class AudioFileController {
	
	@Autowired
	private AudioFileRepository audioFileRepository;
	
	@GetMapping(("/all"))
	public @ResponseBody Iterable<AudioFile> getAllAudioFiles() {
		return audioFileRepository.getAllAudioFiles();
	}
}
