package com.followapp.core.services;

import java.util.List;

public interface ICallingServiceApi {

    String call(String phoneNumber, List<String> audioFiles, CallingServiceApiAttributes callingServiceApiAttributes);
}
