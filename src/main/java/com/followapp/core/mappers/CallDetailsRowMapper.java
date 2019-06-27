package com.followapp.core.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.followapp.core.model.CallDetails;

public class CallDetailsRowMapper implements RowMapper<CallDetails>{

    private static final Logger LOG = LoggerFactory.getLogger(CallDetailsRowMapper.class); 

    @Override
    public CallDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY");
	
	CallDetails callDetails = new CallDetails();
	callDetails.setChildName(rs.getString("CHILD_NAME").toLowerCase());
	callDetails.setGuardianName(rs.getString("GUARDIAN_NAME").toLowerCase());
	callDetails.setPhoneNumber(rs.getString("PHONE_NUMBER"));
	callDetails.setPrescriptionName(rs.getString("PRESCRIPTION").toLowerCase());
	callDetails.setDateForPrescription(sdf.format(rs.getDate("PRESCRIPTION_DUE")));
	callDetails.setCareRecipientId(rs.getInt("CARE_RECIPIENT_ID"));
	callDetails.setPrescriptionDetailsId(rs.getInt("PRESCRIPTION_DETAILS_ID"));
	
	callDetails.setCallFlowId(rs.getString("CALL_FLOW_ID"));
	callDetails.setCallbackEndPoint(rs.getString("CALLBACK_END_POINT"));
	
	// TODO: Change database schema to contain language
	// for now, we set it to hindi by default
	// When it is done, remove the logging as well
	LOG.warn("Unimplemented code for preferred language. Setting it to hindi.");
	callDetails.setPreferredLanguage("hindi");
	
	LOG.info("Created CallDetails instance from db: " + callDetails);
	
	return callDetails;
    }
}
