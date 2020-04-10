package com.followapp.core.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.followapp.core.imimobile.ImiMobileApi;
import com.followapp.core.imimobile.ImiMobileApiException;
import com.followapp.core.model.CallDetails;
import com.followapp.core.model.CallResult;
import com.followapp.core.model.CallStatus;

@Component
public class CallingService {

	private static final Logger LOG = LoggerFactory.getLogger(CallingService.class);

	private final ICallingServiceApi imiMobileApi;

    @Autowired
    public CallingService(ICallingServiceApi imiMobileApi) {
        this.imiMobileApi = imiMobileApi;
    }

	/**
	 * Call the specified phone number
	 * 
	 * @param phoneNumber
	 *            The number that will be called
	 * @return
	 */
	public CallResult callUser(CallDetails callDetails) {
		String phoneNumber = callDetails.getPhoneNumber();
		Objects.requireNonNull(phoneNumber, "Phone number to be called cannot be null");
		if (phoneNumber.length() != 11) {
			throw new IllegalArgumentException(
					"Phone number is not in the required format. It should be 11 digits long. "
							+ "Try appending a leading 0 to the phone number, in case it is only 10 digits long.");
		}
		CallResult callResult = new CallResult(callDetails);
        List<String> audioFiles = getAudioFiles(callDetails);
        try {
            String id = imiMobileApi.call(phoneNumber, audioFiles, callDetails);
            callResult.setSid(id);
            callResult.setCallStatus(CallStatus.HUNG_UP);
        } catch (ImiMobileApiException exception) {
            LOG.error(String.format("Error while calling %s ", phoneNumber), exception);
            callResult.setCallStatus(CallStatus.CALL_FAILED);
            return null;
        }        
        LOG.info("Returning call result: " + callResult);
		return callResult;
	}

    private List<String> getAudioFiles(CallDetails callDetails) {
        List<String> audioFiles = new ArrayList<>();
        audioFiles.add(getFileNameAudioStorageFormat(callDetails.getGuardianName()));
        audioFiles.add(getFileNameAudioStorageFormat(callDetails.getChildName()));
        audioFiles.add(getFileNameAudioStorageFormat(callDetails.getPrescriptionName()));
        audioFiles.add(getFileNameAudioStorageFormat(callDetails.getPrescriptionDay()));
        audioFiles.add(getFileNameAudioStorageFormat(callDetails.getPrescriptionMonth()));
        audioFiles.add(getFileNameAudioStorageFormat(callDetails.getPrescriptionYear()));
        return audioFiles;
    }
    
	private String getFileNameAudioStorageFormat(String audioFileName) {
	    return StringUtils.lowerCase(StringUtils.replace(audioFileName, " ", "_"));
	}

	
	
}