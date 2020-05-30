package com.followapp.core.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.followapp.core.model.Schedule;

@Repository
public class ScheduleRepository {

	private static final String SQL_SOFT_DELETE_SCHEDULE = "UPDATE schedules set delete_flag=1 WHERE ID = :id";

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;

	public Iterable<Schedule> getAllSchedules() {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GET_ALL_SCHEDULES")
				.returningResultSet("schedules", BeanPropertyRowMapper.newInstance(Schedule.class));

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute();

		@SuppressWarnings("unchecked")
		List<Schedule> allSchedules = (List<Schedule>) simpleJdbcCallResult.get("schedules");

		return allSchedules;
	}

	public Iterable<Schedule> getSchedules(String scheduleType) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GET_SCHEDULES")
				.returningResultSet("schedules", BeanPropertyRowMapper.newInstance(Schedule.class));

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("scheduleType", scheduleType);
		SqlParameterSource inP = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inP);

		@SuppressWarnings("unchecked")
		List<Schedule> schedules = (List<Schedule>) simpleJdbcCallResult.get("schedules");

		return schedules;
	}

	public Schedule getScheduleById(Integer id) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GET_SCHEDULE")
				.returningResultSet("schedules", BeanPropertyRowMapper.newInstance(Schedule.class));

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("scheduleId", id);
		SqlParameterSource inP = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inP);

		@SuppressWarnings("unchecked")
		List<Schedule> schedules = (List<Schedule>) simpleJdbcCallResult.get("schedules");

		return schedules.size() > 0 ? schedules.get(0) : null;
	}

	public Schedule createNewSchedule(Schedule newSchedule) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_CREATE_SCHEDULE")
				.returningResultSet("schedules", BeanPropertyRowMapper.newInstance(Schedule.class));

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("groupId", newSchedule.getGroupId());
		inParamMap.put("actionType", newSchedule.getActionType());
		inParamMap.put("description", newSchedule.getDescription());
		inParamMap.put("executeTime", newSchedule.getExecuteTime());
		inParamMap.put("audio_file_id", newSchedule.getAudioFileId());
		inParamMap.put("sms_content_id", newSchedule.getSmsContentId());
		inParamMap.put("smsText", newSchedule.getSmsText());
		inParamMap.put("smsLanguage", newSchedule.getSmsLanguage());
		SqlParameterSource inP = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inP);
		@SuppressWarnings("unchecked")
		List<Schedule> schedules = (List<Schedule>) simpleJdbcCallResult.get("schedules");

		return schedules.size() > 0 ? schedules.get(0) : null;
	}

	public Schedule updateSchedule(Integer scheduleId, Schedule newSchedule) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_CREATE_SCHEDULE")
				.returningResultSet("schedules", BeanPropertyRowMapper.newInstance(Schedule.class));

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("scheduleId", scheduleId);
		inParamMap.put("groupId", newSchedule.getGroupId());
		inParamMap.put("actionType", newSchedule.getActionType());
		inParamMap.put("description", newSchedule.getDescription());
		inParamMap.put("executeTime", newSchedule.getExecuteTime());
		inParamMap.put("audio_file_id", newSchedule.getAudioFileId());
		inParamMap.put("sms_content_id", newSchedule.getSmsContentId());
		inParamMap.put("smsText", newSchedule.getSmsText());
		inParamMap.put("smsLanguage", newSchedule.getSmsLanguage());
		SqlParameterSource inP = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inP);
		@SuppressWarnings("unchecked")
		List<Schedule> schedules = (List<Schedule>) simpleJdbcCallResult.get("schedules");

		return schedules.size() > 0 ? schedules.get(0) : null;
	}
	
	public void deleteScheduleById(Integer scheduleId) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("DELETE_SCHEDULE")
				.returningResultSet("schedules", BeanPropertyRowMapper.newInstance(Schedule.class));

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("scheduleId", scheduleId);
		SqlParameterSource inP = new MapSqlParameterSource(inParamMap);
		simpleJdbcCall.execute(inP);
	}

}
