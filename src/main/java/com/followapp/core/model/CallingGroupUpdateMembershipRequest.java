package com.followapp.core.model;

import java.util.List;


public class CallingGroupUpdateMembershipRequest {
		   
	    private Integer id;
	    
	    private List<Integer> newRecipients;
	    
	    private List<Integer> deletedRecipients;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public List<Integer> getNewRecipients() {
			return newRecipients;
		}

		public void setNewRecipients(List<Integer> newRecipients) {
			this.newRecipients = newRecipients;
		}

		public List<Integer> getDeletedRecipients() {
			return deletedRecipients;
		}

		public void setDeletedRecipients(List<Integer> deletedRecipients) {
			this.deletedRecipients = deletedRecipients;
		}	    	   
}
