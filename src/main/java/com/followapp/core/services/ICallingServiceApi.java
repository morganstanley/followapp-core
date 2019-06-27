package com.followapp.core.services;

import java.util.List;

import com.followapp.core.model.CallDetails;

public interface ICallingServiceApi {

	String call(String phoneNumber, List<String> audioFiles, CallDetails callDetails);
}
