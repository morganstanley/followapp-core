package com.followapp.core.model;

public class CallDetails {
	private String phoneNumber;

	private String guardianName;
	private String childName;
	private String prescriptionName;
	private String dateForPrescription; // Must be of the form dd mon YYYY

	private String prescriptionDay;
	private String prescriptionMonth;
	private String prescriptionYear;

	private int careRecipientId;
	private int prescriptionDetailsId;

	private String preferredLanguage;
	
	private String callFlowId;
	private String callbackEndPoint;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public int getCareRecipientId() {
		return careRecipientId;
	}

	public void setCareRecipientId(int careRecipientId) {
		this.careRecipientId = careRecipientId;
	}

	public String getDateForPrescription() {
		return dateForPrescription;
	}

	public void setDateForPrescription(String dateForPrescription) {
		this.dateForPrescription = dateForPrescription;
		setPrescriptionDayComponents(dateForPrescription);
	}

	private void setPrescriptionDayComponents(String dateForPrescription) {
		String[] dateComponents = dateForPrescription.split(" ");
		this.prescriptionDay = dateComponents[0];
		this.prescriptionMonth = dateComponents[1];
		this.prescriptionYear = dateComponents[2];
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String getCallFlowId() {
		return callFlowId;
	}

	public void setCallFlowId(String callFlowId) {
		this.callFlowId = callFlowId;
	}

	public String getCallbackEndPoint() {
		return callbackEndPoint;
	}

	public void setCallbackEndPoint(String callbackEndPoint) {
		this.callbackEndPoint = callbackEndPoint;
	}
	
	

	public String getPrescriptionName() {
		return prescriptionName;
	}

	public void setPrescriptionName(String prescriptionName) {
		this.prescriptionName = prescriptionName;
	}

	public String getPrescriptionDay() {
		return prescriptionDay;
	}

	public void setPrescriptionDay(String prescriptionDay) {
		this.prescriptionDay = prescriptionDay;
	}

	public String getPrescriptionMonth() {
		return prescriptionMonth;
	}

	public void setPrescriptionMonth(String prescriptionMonth) {
		this.prescriptionMonth = prescriptionMonth;
	}

	public String getPrescriptionYear() {
		return prescriptionYear;
	}

	public void setPrescriptionYear(String prescriptionYear) {
		this.prescriptionYear = prescriptionYear;
	}

	public int getPrescriptionDetailsId() {
		return prescriptionDetailsId;
	}

	public void setPrescriptionDetailsId(int prescriptionDetailsId) {
		this.prescriptionDetailsId = prescriptionDetailsId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CallDetails [phoneNumber=").append(phoneNumber)
				.append(", guardianName=").append(guardianName)
				.append(", childName=").append(childName)
				.append(", prescriptionName=").append(prescriptionName)
				.append(", dateForPrescription=").append(dateForPrescription)
				.append(", prescriptionDay=").append(prescriptionDay)
				.append(", prescriptionMonth=").append(prescriptionMonth)
				.append(", prescriptionYear=").append(prescriptionYear)
				.append(", careRecipientId=").append(careRecipientId)
				.append(", prescriptionDetailsId=").append(prescriptionDetailsId)
				.append(", preferredLanguage=").append(preferredLanguage)
				.append("]");
		return builder.toString();
	}

}
