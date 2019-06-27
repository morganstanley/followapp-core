package com.followapp.core.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.followapp.core.model.CareRecipient;
import com.followapp.core.model.Gender;


public class CareRecipientRowMapper implements RowMapper<CareRecipient>{

	public CareRecipient mapRow(ResultSet rs, int rowNum) throws SQLException {
		CareRecipient cr = new CareRecipient();
		cr.setId(rs.getLong("CARE_RECIPIENT_ID"));
		cr.setName(rs.getString("NAME"));
		cr.setDateOfBirth(rs.getDate("DATE_OF_BIRTH"));
		cr.setGender(Gender.valueOf(rs.getString("GENDER")));
		cr.setGuardianName(rs.getString("GUARDIAN_NAME"));
		cr.setGuardianMobile(rs.getString("MOBILE_NUMBER"));
		return cr;
	}

}
