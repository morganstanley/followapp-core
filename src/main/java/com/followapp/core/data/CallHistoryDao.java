package com.followapp.core.data;


import com.followapp.core.model.CallResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class CallHistoryDao {

    private static final Logger LOG = LoggerFactory.getLogger(CallHistoryDao.class);
    private static final String SAVE_CALL_HISTORY = "call update_call_status( ?, ?, ?, CURDATE(), ?)";
    private static final String SAVE_CALL_DURATION = "Update schedule_run SET call_duration = ?, updated_datetime = ? WHERE ivr_request_id = ?";
    private static final String SAVE_MESSAGE_STATUS = "Update schedule_run SET status = ?, updated_datetime = ? WHERE ivr_request_id = ?";
    private static final String SCHEDULE_RUN_REQUEST_WITHSTATUS = "select sr.ivr_request_id from schedule_run sr" +
            " join schedules s on s.id = sr.schedule_id and sr.status in (:status) and sr.run_date_time > :runDateTime and s.action_type = :actionType";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CallHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public void updateMessageStatus(final String ivrRequestId, final String scheduleRunStatus) {
        LOG.info("Ivr Request Id {} message status {} ", ivrRequestId, scheduleRunStatus);

        jdbcTemplate.execute(SAVE_MESSAGE_STATUS, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, scheduleRunStatus);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, ivrRequestId);
                return ps.execute();
            }
        });
    }

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
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, uuid);
                return ps.execute();
            }
        });
    }

    public List<String> requestWithStatus(List<String> status, String actionType, LocalDateTime runDateTime) {
        LOG.info("Query schedule run with status {} actionType {} runDateTime", status, actionType, runDateTime);
        MapSqlParameterSource parameters = new MapSqlParameterSource("status", status);
        parameters.addValue("runDateTime", runDateTime);
        parameters.addValue("actionType", actionType);

        return this.namedParameterJdbcTemplate.queryForList(SCHEDULE_RUN_REQUEST_WITHSTATUS, parameters, String.class);
    }
}

