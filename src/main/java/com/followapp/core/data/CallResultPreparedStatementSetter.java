package com.followapp.core.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.stereotype.Component;

import com.followapp.core.model.CallResult;


@Component
public class CallResultPreparedStatementSetter implements ItemPreparedStatementSetter<CallResult> {

	@Override
	public void setValues(CallResult callResult, PreparedStatement ps) throws SQLException {
		ps.setString(1, callResult.getSid());
		ps.setInt(2, callResult.getCallDetails().getCareRecipientId());
		ps.setInt(3, callResult.getCallDetails().getPrescriptionDetailsId());
		ps.setString(4, callResult.getCallStatus().toString());
	}

}
