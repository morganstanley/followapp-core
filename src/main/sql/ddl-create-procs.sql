use followapp;

DROP PROCEDURE IF EXISTS update_call_status;

DELIMITER //
 CREATE PROCEDURE update_call_status(
		IN P_IVR_CALL_ID varchar(200), 
		IN P_CARE_RECIPIENT_ID int,IN P_prescription_details_id int, 
		IN P_DATE_TIME_CALLED datetime, 
		IN P_CALL_STATUS varchar(50))
   BEGIN
        IF NOT EXISTS(SELECT 1 FROM call_history WHERE ivr_call_id=P_IVR_CALL_ID)
        THEN
           INSERT INTO call_history ( ivr_call_id ,care_recipient_id,prescription_details_id, DATE_TIME_CALLED, CALL_STATUS )
               VALUES (P_IVR_CALL_ID, P_CARE_RECIPIENT_ID, P_prescription_details_id, P_DATE_TIME_CALLED, P_CALL_STATUS);
        ELSE
	        SELECT care_recipient_id into P_CARE_RECIPIENT_ID FROM call_history 
	           WHERE ivr_call_id=P_IVR_CALL_ID;
			SELECT prescription_details_id into P_prescription_details_id FROM call_history 
	           WHERE ivr_call_id=P_IVR_CALL_ID;
            UPDATE call_history SET CALL_STATUS=P_CALL_STATUS WHERE ivr_call_id=P_IVR_CALL_ID;
        END IF;

        IF P_CALL_STATUS = 'COMPLETE' OR P_CALL_STATUS = 'VACCINE_TAKEN' THEN

   			UPDATE prescription_details
        	SET prescription_status = 'COMPLETE'
        	WHERE prescription_details_id = P_prescription_details_id ;

			select prescription_reference_id 
			into @vdescr_id
			from prescription_details
			where prescription_details_id = P_prescription_details_id;

            insert into prescription_details(care_recipient_id, 
											prescription_reference_id,
											planned_date,
											prescription_status) 
				select 	P_CARE_RECIPIENT_ID, 
						prescription_reference.prescription_description_Id,
						DATE_ADD(CURDATE(), INTERVAL prescription_description.days_from_prev day),
						'INCOMPLETE'
            	FROM prescription_reference 
                WHERE pre_req = @vdescr_id ;
   		END IF;
   END //
 DELIMITER ;
 
 

DROP PROCEDURE IF EXISTS UPDATE_PREVIOUS_VACCINE;

DELIMITER //
 CREATE PROCEDURE UPDATE_PREVIOUS_VACCINE(
		IN P_CARE_RECIPIENT_ID int)
   BEGIN
   		
   		UPDATE prescription_details SET PRESCRIPTION_STATUS = 'COMPLETE' WHERE CARE_RECIPIENT_ID = P_CARE_RECIPIENT_ID AND 
   		PLANNED_DATE < CURDATE() ;
        
   END //
 DELIMITER ;
 
 
DELIMITER //
 CREATE PROCEDURE get_call_details(IN P_TARGET_DATE datetime)
   BEGIN
		select 
		cr.guardian_name as GUARDIAN_NAME,  
		cr.name as CHILD_NAME, 
		cr.mobile_number as PHONE_NUMBER, 
		cr.care_recipient_id as CARE_RECIPIENT_ID,  
		prescr.prescription_name as PRESCRIPTION, 
		pd.planned_date as PRESCRIPTION_DUE, 
		pd.prescription_details_id as PRESCRIPTION_DETAILS_ID, count(1) as call_count  
		from prescription_details pd 
		join care_recipient cr using (care_recipient_id)   
		join prescription_reference prescr using (prescription_description_id)
		join call_config using cc using (call_config_id)
		left join call_history c on c.prescription_details_id = pd.prescription_details_id  
		where pd.prescription_status='INCOMPLETE' and 
			DATEDIFF(P_TARGET_DATE,pd.planned_date) < 365 and   
			DATEDIFF(P_TARGET_DATE,pd.planned_date) > -15 and
			prescr.is_recurring <> 1	
		group by prescription_details_id having call_count < cc.number_of_retries
			and ((call_count =1 AND max(c.date_time_called) is null) OR (max(c.date_time_called) is not null AND DATEDIFF(P_TARGET_DATE,max(c.date_time_called)) >= cc.gap_btwn_retries))

		union

		select 
		cr.guardian_name as GUARDIAN_NAME,  
		cr.name as CHILD_NAME, 
		cr.mobile_number as PHONE_NUMBER, 
		cr.care_recipient_id as CARE_RECIPIENT_ID,  
		prescr.prescription_name as PRESCRIPTION, 
		pd.planned_date as PRESCRIPTION_DUE, 
		pd.prescription_details_id as PRESCRIPTION_DETAILS_ID,
		null as call_count
		join care_recipient cr using (care_recipient_id)   
		join prescription_reference prescr using (prescription_description_id)
		join call_config using cc (call_config_id)
		pd.prescription_status='INCOMPLETE' 
			and prescr.is_recurring=1
			and pd.planned_date= P_TARGET_DATE;    
   END //
 DELIMITER ;
 
