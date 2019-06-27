use followapp;

drop table if exists call_history;
drop table if exists vaccination_details;
drop table if exists care_recipient;
drop table if exists vaccination_description;

CREATE TABLE vaccination_description (
	vaccination_description_id 	int 		NOT NULL AUTO_INCREMENT,
	disease 					varchar(50) NOT NULL,
	dose 						int 		NOT NULL,
	pre_req 					int 		NULL,
	days_from_prev 				int 		NOT NULL,
	PRIMARY KEY (vaccination_description_id),
	FOREIGN KEY (pre_req) REFERENCES vaccination_description (vaccination_description_id)
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

CREATE TABLE vaccination_details (
	vaccination_details_id 		int 		NOT NULL AUTO_INCREMENT,
	care_recipient_id 			int 		NOT NULL,
	vaccination_description_id 	int 		NOT NULL,
	planned_date 				date 		DEFAULT NULL,
	vaccination_status 			varchar(50) DEFAULT NULL,
	PRIMARY KEY (vaccination_details_id),
	FOREIGN KEY (care_recipient_id) REFERENCES care_recipient (care_recipient_id) ,
	FOREIGN KEY (vaccination_description_id) REFERENCES vaccination_description (vaccination_description_id),
	UNIQUE KEY (care_recipient_id, vaccination_description_id)
);

CREATE TABLE call_history (
	call_track_id 				int 		NOT NULL AUTO_INCREMENT,
	ivr_call_id 				varchar(200)	NOT NULL UNIQUE,
	care_recipient_id 			int 		NOT NULL,
	vaccination_details_id 		int 		NOT NULL,  
	date_time_called 			datetime 	NOT NULL,  
	call_status 				varchar(50) NOT NULL,
	PRIMARY KEY (call_track_id),
	FOREIGN KEY (care_recipient_id) REFERENCES care_recipient (care_recipient_id),
	FOREIGN KEY (vaccination_details_id) REFERENCES vaccination_details(vaccination_details_id)
);

show tables;