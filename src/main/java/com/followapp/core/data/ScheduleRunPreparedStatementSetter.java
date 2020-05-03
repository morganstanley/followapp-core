package com.followapp.core.data;

import com.followapp.core.model.ScheduleRun;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class ScheduleRunPreparedStatementSetter implements ItemPreparedStatementSetter<ScheduleRun> {

    @Override
    public void setValues(ScheduleRun scheduleRun, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, scheduleRun.getScheduleId());
        preparedStatement.setInt(2, scheduleRun.getRecipientId());
        preparedStatement.setString(3, scheduleRun.getIvrRequestId());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(scheduleRun.getRunDateTime()));
        preparedStatement.setString(5, scheduleRun.getStatus().name());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(scheduleRun.getUpdateDateTime()));
    }
}
