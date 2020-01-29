use followapp;

drop table if exists call_history;
drop table if exists prescription_reference;
drop table if exists care_recipient;
drop table if exists prescription_details;

CREATE TABLE prescription_reference (
	prescription_reference_id 	int 		NOT NULL AUTO_INCREMENT,
	prescription_name 					varchar(50) NOT NULL,
	pre_req 					int 		NULL,
	days_from_prev 				int 		NOT NULL,
    is_recurring                bit         NOT NULL,
    recurring_cron_schedule     varchar(50) NULL,
    recurrence_instance_count   int         NULL;
    call_config_id              int         NOT NULL;
	PRIMARY KEY (prescription_reference_id),
	FOREIGN KEY (pre_req) REFERENCES prescription_reference (prescription_reference_id),
    FOREIGN KEY (call_config_id) REFERENCES prescription_reference (call_config)
);

CREATE TABLE call_config (
	call_config_id	int 		NOT NULL AUTO_INCREMENT,
	call_type 					varchar(50) NOT NULL,
	call_flow_id 				varchar(50) NOT NULL,
	gap_btwn_retries 			int 		NULL,
	max_number_of_retries 		int 		NULL,
    capture_feedback            bit         NOT NULL;
	PRIMARY KEY (call_config_id)
    );

CREATE TABLE care_recipient (
	care_recipient_id 			int 			NOT NULL AUTO_INCREMENT,
	name 						varchar(100) 	NOT NULL,
	date_of_birth 				date 			NOT NULL,
	gender 						varchar(20) 	NOT NULL,
	guardian_name 				varchar(100) 	NOT NULL,
	mobile_number 				varchar(11) 	NOT NULL,
	address_line_1 				varchar(255) 	NOT NULL,
	address_line_2				varchar(255)	NOT NULL,
	pin_code					varchar(12)		NOT NULL,
	city 						varchar(50) 	NOT NULL,
	address_state				varchar(50) 	NOT NULL,
	PRIMARY KEY (care_recipient_id)
);

--This needs to be enhanced further to the level of external Organization 
CREATE TABLE care_recipient_extn (
	id							int 			NOT NULL AUTO_INCREMENT,
	care_recipient_id 			int 			NOT NULL,
	extrnl_system_id			varchar(100) 	NOT NULL,
	onboarding_date				date 			NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE prescription_details (
	prescription_details_id 		int 		NOT NULL AUTO_INCREMENT,
	care_recipient_id 			int 		NOT NULL,
	prescription_reference_id 	int 		NOT NULL,
	planned_date 				date 		DEFAULT NULL,
	call_status 	            varchar(50) NOT NULL,
	PRIMARY KEY (prescription_details_id),
	FOREIGN KEY (care_recipient_id) REFERENCES care_recipient (care_recipient_id) ,
	FOREIGN KEY (prescription_reference_id) REFERENCES prescription_reference (prescription_reference_id),
	UNIQUE KEY (care_recipient_id, prescription_reference_id)
)

CREATE TABLE call_history (
	call_history_id 				int 		NOT NULL AUTO_INCREMENT,
	ivr_call_id 				varchar(200)	NOT NULL UNIQUE,
	care_recipient_id 			int 		NOT NULL,
	prescription_details_id 	int 		NOT NULL,  
	date_time_called 			datetime 	NOT NULL,  
	call_status 				varchar(50) NOT NULL,
    call_duration               int         NOT NULL;
	PRIMARY KEY (call_history_id),
	FOREIGN KEY (care_recipient_id) REFERENCES care_recipient (care_recipient_id),
	FOREIGN KEY (prescription_details_id) REFERENCES prescription_details(prescription_details_id)
);



