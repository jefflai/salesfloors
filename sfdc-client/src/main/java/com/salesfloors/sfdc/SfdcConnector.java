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
	
	public static final String isPresentField = "Is_Present__c";
	
	public SfdcConnector() throws ConnectionException {
		ForceConnectorConfig config = new ForceConnectorConfig();
		config.setUsername("test@1325649127425.org");
		config.setPassword("123456");
		config.setAuthEndpoint("https://na1-aps1.t.salesforce.com/");
		serviceConn = new ForceServiceConnector(config);
	}
	
	public SaveResult[] setIsPresent(String firstName, String lastName, boolean present) throws ConnectionException {
		QueryResult result = getPartnerConnection().
				query("select Id from lead where firstName ='" + firstName + "' and lastName = '" + lastName + "'");
		SObject lead = new SObject();
		lead.setType("lead");
	    lead.setId(result.getRecords()[0].getId());
		lead.setField(isPresentField, present);
		SaveResult[] sr =  getPartnerConnection().update( new SObject[] {lead});
		return sr;
	}
	
	public PartnerConnection getPartnerConnection() throws ConnectionException {
		return serviceConn.getConnection();
	}

}
