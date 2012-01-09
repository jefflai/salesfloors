package com.salesfloors.sfdc;

import com.force.sdk.connector.ForceConnectorConfig;
import com.force.sdk.connector.ForceServiceConnector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;

public class SfdcConnector {
	
	private ForceServiceConnector serviceConn;
	
	public static final String isPresentField = "isPresent__c";
	
	public SfdcConnector() throws ConnectionException {
		ForceConnectorConfig config = new ForceConnectorConfig();
		config.setUsername("dtia.face@salesforce.com");
		config.setPassword("test1234");
		config.setAuthEndpoint("https://na14.salesforce.com/");
		serviceConn = new ForceServiceConnector(config);
	}
	
	public SaveResult[] setIsPresent(String firstName, String lastName, boolean present) throws ConnectionException {
		QueryResult result = getPartnerConnection().
				query("select Id from lead where firstName ='" + firstName + "' and lastName = '" + lastName + "'");
		SObject lead = new SObject();
		SObject[] records = result.getRecords();
		String recordId = records.length == 0 ? createNewLead(firstName, lastName, present) : records[0].getId();
				
		lead.setType("lead");
	    lead.setId(recordId);
		lead.setField(isPresentField, present);
		SaveResult[] sr =  getPartnerConnection().update( new SObject[] {lead});
		return sr;
	}
	
	public PartnerConnection getPartnerConnection() throws ConnectionException {
		return serviceConn.getConnection();
	}
	
	private String createNewLead(String firstName, String lastName, boolean isPresent) throws ConnectionException {
		SObject sObject = new SObject();
    	sObject.setType("Lead");
    	sObject.setField("Firstname", firstName);
    	sObject.setField("Lastname", lastName);
    	sObject.setField("Status", "Open");
    	sObject.setField("Company", "Unaffiliated"); // change this to pull company info from FB
    	sObject.setField("isPresent__c", isPresent);    	
    	SaveResult[] sr = getPartnerConnection().create(new SObject[] {sObject});
    	
    	for(int i=0; i<sr.length; i++) {
    		if(sr[i].isSuccess()) {
    			return sr[i].getId();
    		}
    	}
    	
    	return null;
	}

}
