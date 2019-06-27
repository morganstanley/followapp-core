package com.followapp.core.data;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.followapp.core.model.CallResult;

/**
 * An instance of this class contains mappings from a phone number, to the last
 * known CallResult for that call.
 * 
 * This CallResult is updated over the duration of the call, and always reflects
 * the last known status. Once the call is over, this CallResult can be
 * persisted to the database.
 */
@Component
public class UserInputRepository {

	private Map<String, CallResult> userInput = new HashMap<>();

	private static Logger LOG = LoggerFactory.getLogger(UserInputRepository.class);

	public CallResult get(String phoneNumber) {
		return userInput.get(phoneNumber);
	}

	public boolean contains(String phoneNumber) {
		return userInput.containsKey(phoneNumber);
	}

	public void add(String phoneNumber, CallResult callResult) {

		if (this.contains(phoneNumber)) {
			LOG.warn("This should never happen, as we clear the call result as soon as the call is over");
			LOG.warn("This mostly means that either the timeout is too small, or an error occurred along the way");
			LOG.error("STALE_USER_INPUT_ERROR");
		}

		userInput.put(phoneNumber, callResult);
	}

	public void remove(String phoneNumber) {
		userInput.remove(phoneNumber);
	}
}