package com.followapp.core.data;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.followapp.core.model.CallDetails;
import com.followapp.core.services.AudioTemplateLoader;

/**
 * An instance of this class contains mappings from the phone number
 * to the audio that is to be played on that call.
 * 
 * A thin wrapper over a simple Map interface
 *
 */
@Component
public class AudioRepository {

	private static final Logger LOG = LoggerFactory.getLogger(AudioRepository.class);

	private AudioTemplateLoader audioTemplateLoader;
	
	private Map<String, String> audioUrlStore;

	@Autowired
	public AudioRepository(AudioTemplateLoader audioTemplateLoader) {
	    this.audioTemplateLoader = audioTemplateLoader;
	    audioUrlStore = new HashMap<>();
	}
	
	public void add(String phoneNumber, CallDetails callDetails) {
		LOG.info("Adding " + phoneNumber + " to audio URL store");
		String audioUrlList = generateAudioMessage(callDetails);
		audioUrlStore.put(callDetails.getPhoneNumber(), audioUrlList);
	}

	/**
	 * Generate a string containing a newline separated list of audio URL's
	 * that will be played sequentially when the user picks up the call
	 * 
	 * @param phoneNumber
	 * @return A newline separated list of audio URL's
	 */
	public String getUrls(String phoneNumber) {
		LOG.info("Retrieving audio urls for " + phoneNumber);
		return audioUrlStore.get(phoneNumber);
	}

	public int size() {
	    return audioUrlStore.size();
	}

	/**
	 * Given call details for a given person, which contains the guardian name,
	 * child name, prescription name, etc and generate audio URL's from them
	 */
	private String generateAudioMessage(CallDetails callDetails) {
	    	Map<String, String> replacementMap = new HashMap<>();
	    	replacementMap.put("guardianName", callDetails.getGuardianName().toLowerCase());
	    	replacementMap.put("childName", callDetails.getChildName().toLowerCase());
	    	replacementMap.put("prescriptionName", callDetails.getPrescriptionName().toLowerCase());
	    	replacementMap.put("prescriptionDay", callDetails.getPrescriptionDay());
	    	replacementMap.put("prescriptionMonth", callDetails.getPrescriptionMonth());
	    	replacementMap.put("prescriptionYear", callDetails.getPrescriptionYear());

	    	return audioTemplateLoader.generate(replacementMap, callDetails.getPreferredLanguage());
	}
}