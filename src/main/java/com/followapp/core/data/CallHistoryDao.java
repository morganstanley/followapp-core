package com.followapp.core.data;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import com.followapp.core.model.CallResult;

@Component
public class CallHistoryDao {

	private static final Logger LOG = LoggerFactory.getLogger(CallHistoryDao.class);
	private static final String SAVE_CALL_HISTORY = "call update_call_status( ?, ?, ?, CURDATE(), ?)";
	private static final String SAVE_CALL_DURATION = "Update schedule_run SET call_duration = ? WHERE ivr_request_id = ?";

	@Autowired
	private JdbcTemplate jdbcTemplate;

    public void updateCallStatus(final CallResult callResult) {
        LOG.info("Updating call status for " + callResult);

        jdbcTemplate.execute(SAVE_CALL_HISTORY, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, callResult.getSid());
                if (callResult.getCallDetails() == null) {
                    ps.setNull(2, Types.INTEGER);
                    ps.setNull(3, Types.INTEGER);
                } else {
                    ps.setInt(2, callResult.getCallDetails().getCareRecipientId());
                    ps.setInt(3, callResult.getCallDetails().getPrescriptionDetailsId());
                }
                ps.setString(4, callResult.getCallStatus().toString());
                return ps.execute();
            }
        });
    }

    public void updateCallDuration(final String uuid, final Integer callDuration) {
        LOG.info("Uuid {} CallDuration {} ", uuid, callDuration);

        jdbcTemplate.execute(SAVE_CALL_DURATION, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, callDuration);
                ps.setString(2, uuid);
                return ps.execute();
            }
        });
    }
}

