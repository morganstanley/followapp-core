package com.followapp.core.data;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.followapp.core.model.AudioFile;

@Repository
public class AudioFileRepository {
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Iterable<AudioFile> getAllAudioFiles() {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GET_AUDIO_FILES")
				.returningResultSet("audioFiles", BeanPropertyRowMapper.newInstance(AudioFile.class));

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute();

		@SuppressWarnings("unchecked")
		List<AudioFile> allAudioFiles = (List<AudioFile>) simpleJdbcCallResult.get("audioFiles");

		return allAudioFiles;
	}
}
