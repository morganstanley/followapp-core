package com.followapp.core.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.followapp.core.model.CallDetails;
import com.followapp.core.model.CallResult;
import com.followapp.core.services.CallingService;

@Component
public class IvrProcessor implements ItemProcessor<CallDetails, CallResult> {
	
   private static final Logger LOG = LoggerFactory.getLogger(IvrProcessor.class);
    
    @Autowired
    private CallingService callingService;
    
    @Value("${app.calls.mocked}")
    private Boolean isCallMocked;
    
    public CallResult process(CallDetails callDetails) throws Exception {
	CallResult callResult = callGuardian(callDetails);
	return callResult;
    }

    private CallResult callGuardian(CallDetails callDetails) {
	if (isCallMocked) {
	    LOG.info("Call is being mocked. No call will actually be made");
	    return new CallResult(callDetails);
	}
	else {
	    return callingService.callUser(callDetails);
	}
    }
}
