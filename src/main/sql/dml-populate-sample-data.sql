-- A simple script that populates the tables to enable testing
-- Before running this script, ensure that you have run followapp_ddl, 
-- which drops all tables and then recreates them,
-- Since we need fresh values for the auto-increment

use comcare;

insert into vaccination_description(disease, dose, pre_req, days_from_prev) values('Dedh Mahine ka tikka', 1, null, 44);

insert into vaccination_description(disease, dose, pre_req, days_from_prev) values('Dhai Mahine ka tikka', 2, 1, 28);

insert into vaccination_description(disease, dose, pre_req, days_from_prev) values('Sade teen Mahine ka tikka', 3, 2, 28);

insert into vaccination_description(disease, dose, pre_req, days_from_prev) values('Khasra', 1, null, 270);

insert into vaccination_description(disease, dose, pre_req, days_from_prev) values('MMR', 1, null, 450);

insert into vaccination_description(disease, dose, pre_req, days_from_prev) values('OPV_Booster', 1, null, 500);
insert into vaccination_description(disease, dose, pre_req, days_from_prev) values('DPT_Booster', 1, null, 500);

-- Some random care recipients
-- Phone number has to be the same, for testability
-- Also note that all of these names are from our audioresources directory on the AWS host
insert into care_recipient(name, date_of_birth, gender, guardian_name, mobile_number, address_line_1, address_line_2, pin_code, city, address_state) values('aditi', '2016-07-30', 'M', 'aditya', '09769510815', '', '', '400080', 'Mumbai', 'Maharashtra');
insert into care_recipient(name, date_of_birth, gender, guardian_name, mobile_number, address_line_1, address_line_2, pin_code, city, address_state) values('diya', '2016-07-29', 'F', 'divya', '09769510815', '', '', '400088', 'Mumbai', 'Maharashtra');
insert into care_recipient(name, date_of_birth, gender, guardian_name, mobile_number, address_line_1, address_line_2, pin_code, city, address_state) values('isha', '2016-07-28', 'F', 'ishika', '09769510815', '', '', '400087', 'Mumbai', 'Maharashtra');
insert into care_recipient(name, date_of_birth, gender, guardian_name, mobile_number, address_line_1, address_line_2, pin_code, city, address_state) values('jatin', '2016-07-37', 'M', 'john', '09769510815', '', '', '400080', 'Mumbai', 'Maharashtra');
insert into care_recipient(name, date_of_birth, gender, guardian_name, mobile_number, address_line_1, address_line_2, pin_code, city, address_state) values('kavya', '2016-07-26', 'M', 'kartik', '09769510815', '', '', '400088', 'Mumbai', 'Maharashtra');
insert into care_recipient(name, date_of_birth, gender, guardian_name, mobile_number, address_line_1, address_line_2, pin_code, city, address_state) values('ramesh', '2016-07-25', 'F', 'radhika', '09769510815', '', '', '400087', 'Mumbai', 'Maharashtra');

-- Test cases from 15 days ago to 2 days from now
-- These values should be such that the result is only one row for calls today
-- Otherwise, the application will not behave nicely 
insert into vaccination_details(care_recipient_id, vaccination_description_id, planned_date, vaccination_status) 
	values (1, 1, '2016-08-02', 'COMPLETE');
insert into vaccination_details(care_recipient_id, vaccination_description_id, planned_date, vaccination_status) 
	values (2, 1, '2016-08-02', 'COMPLETE');
insert into vaccination_details(care_recipient_id, vaccination_description_id, planned_date, vaccination_status) 
	values (3, 1, '2016-07-22', 'COMPLETE');
insert into vaccination_details(care_recipient_id, vaccination_description_id, planned_date, vaccination_status) 
	values (4, 1, '2016-07-16', 'COMPLETE');
insert into vaccination_details(care_recipient_id, vaccination_description_id, planned_date, vaccination_status) 
	values (5, 1, '2016-07-02', 'COMPLETE');
insert into vaccination_details(care_recipient_id, vaccination_description_id, planned_date, vaccination_status) 
	values (6, 1, '2016-07-12', 'COMPLETE');