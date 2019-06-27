package com.followapp.core.model;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CallResult {

	private static final Logger LOG = LoggerFactory.getLogger(CallResult.class);

	private CallDetails callDetails;
	private CallStatus callStatus;

	private String sid;

	private int firstInput;
	private int confirmationInput;

	public CallResult(CallDetails callDetails) {
		this.callDetails = callDetails;
		this.callStatus = CallStatus.CALL_FAILED;
		
		// We set these to -1 because it is practically impossible
		// for the user to enter this on the call
		this.firstInput = -1;
		this.confirmationInput = -1;
	}

    public CallResult(String sid, CallStatus callStatus) {
        this.sid = sid;
        this.callStatus = callStatus;
    }

	public CallDetails getCallDetails() {
		return callDetails;
	}

	public CallStatus getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(CallStatus callStatus) {
		this.callStatus = callStatus;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		Objects.requireNonNull(sid, "Sid cannot be null");
		this.sid = sid;
	}
	
	public int getFirstInput() {
		return firstInput;
	}

	public void setFirstInput(int firstInput) {
		this.firstInput = firstInput;
		if (firstInput == 1) {
			this.callStatus = CallStatus.PRESCRIPTION_TAKEN;
		} else {
			this.callStatus = CallStatus.PRESCRIPTION_NOT_TAKEN;
		}
	}

	public int getConfirmationInput() {
		return confirmationInput;
	}

	public void setConfirmationInput(int confirmationInput) {

		this.confirmationInput = confirmationInput;

		if (confirmationInput == 4) {
			this.negateCallStatus();
		}
	}

	private void negateCallStatus() {

		LOG.debug("Negating call status");

		if (this.callStatus == CallStatus.PRESCRIPTION_NOT_TAKEN) {
			this.callStatus = CallStatus.PRESCRIPTION_TAKEN;
		} else if (this.callStatus == CallStatus.PRESCRIPTION_TAKEN) {
			this.callStatus = CallStatus.PRESCRIPTION_NOT_TAKEN;
		}
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CallResult [callDetails=").append(callDetails).append(", callStatus=").append(callStatus)
                .append(", sid=").append(sid).append(", firstInput=").append(firstInput).append(", confirmationInput=")
                .append(confirmationInput).append("]");
        return builder.toString();
    }
}
