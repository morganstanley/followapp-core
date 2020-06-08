package com.followapp.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.followapp.core.model.CallingGroup;
import com.followapp.core.model.CallingGroupUpdateMembershipRequest;
import com.followapp.core.model.Recipient;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import org.springframework.jdbc.core.ResultSetExtractor;


@Repository
public class RecipientRepository {

    private static final String SQL_FIND_BY_ID = "SELECT * FROM recipients WHERE ID = :id";
    private static final BeanPropertyRowMapper<Recipient> ROW_MAPPER = new BeanPropertyRowMapper<>(Recipient.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Recipient findById(Integer id) {
        try {
            final SqlParameterSource paramSource = new MapSqlParameterSource("id", id);
            return namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID, paramSource, ROW_MAPPER);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Iterable<Recipient> getAllRecipients() {
    	
    	SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GET_RECIPIENTS")
    			.returningResultSet("recipients", BeanPropertyRowMapper.newInstance(Recipient.class));    			
    	
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute();    
		
		List<Recipient> recipients = (List<Recipient>) simpleJdbcCallResult.get("recipients");
    	
        return recipients;                                
    }
    
    //check if needed
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public CallingGroup getCallingGroupById(Integer id) {
    	
    	SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("GET_CALLING_GROUP_DETAILS")
    			.returningResultSet("recipients", BeanPropertyRowMapper.newInstance(CallingGroup.class));    			
    	
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("id", id);
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
				
		List<CallingGroup> callingGroups = (List<CallingGroup>) simpleJdbcCallResult.get("calling-groups");		
		List<LinkedCaseInsensitiveMap> map = (List<LinkedCaseInsensitiveMap>) simpleJdbcCallResult.get("#result-set-2");
		
		Map<Integer, List<Integer>> recipientGroupMap = new LinkedHashMap<Integer, List<Integer>>();
		
		map.forEach(m -> {
			recipientGroupMap.computeIfAbsent((Integer) m.get("group_id"), k -> new ArrayList<>()).add((Integer) m.get("recipient_id"));
		});
			
		callingGroups.forEach(group -> {
			group.setRecipients(recipientGroupMap.computeIfAbsent(group.getId(), k -> new ArrayList<>()));
		});
						
		 System.out.print(callingGroups.size()); 
		 
	 	 return callingGroups.get(0);    	
    }

    public void createRecipient(Recipient recipient) {
    	
    	SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_CREATE_RECIPIENT");    			   		
    	
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("name", recipient.getName());
		inParamMap.put("mobile", recipient.getMobile());
		inParamMap.put("gender", recipient.getGender());
		inParamMap.put("zone", recipient.getZone());		
		inParamMap.put("created_user", "admin");
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
       
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);		    	
    }

    public void updateRecipient(Recipient recipient, int recipientId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("");

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("name", recipient.getName());
        inParamMap.put("mobile", recipient.getMobile());
        inParamMap.put("gender", recipient.getGender());
        inParamMap.put("zone", recipient.getZone());
        inParamMap.put("updated_user", "admin");inParamMap.put("id", recipientId);

        simpleJdbcCall.execute(new MapSqlParameterSource(inParamMap));
    }

    //check if needed
    public void updateGroupMembership(CallingGroupUpdateMembershipRequest callingGroupUpdateMembershipRequest) {
    	
    	SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("UPDATE_CALLING_GROUP_MEMBERSHIP_LIST");    			   		
    	
		Map<String, Object> inParamMap = new HashMap<String, Object>();
				
		SQLServerDataTable dt =null;
		try {
			dt = new SQLServerDataTable();
			dt.addColumnMetadata("recipientId", java.sql.Types.NUMERIC);
			dt.addColumnMetadata("action", java.sql.Types.NVARCHAR);
			
			if (callingGroupUpdateMembershipRequest.getNewRecipients() != null) {
				for (int recipientId : callingGroupUpdateMembershipRequest.getNewRecipients()) {
					dt.addRow(recipientId, "A");
				}
			}

			if (callingGroupUpdateMembershipRequest.getDeletedRecipients() != null) {
				for (int recipientId : callingGroupUpdateMembershipRequest.getDeletedRecipients()) {
					dt.addRow(recipientId, "D");
				}
			}
			
		} catch (SQLServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
								
		inParamMap.put("recipients", dt);
		inParamMap.put("callingGroupId", callingGroupUpdateMembershipRequest.getId());
		inParamMap.put("updated_user", "admin");
		
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
       
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);		    	
    }

    public void deleteById(Integer id) {
    	
    	SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_DELETE_RECIPIENT");    			
    	
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("id", id);
		inParamMap.put("update_user", "admin");
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);		
    }
}
