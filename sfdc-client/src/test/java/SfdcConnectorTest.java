import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.salesfloors.sfdc.SfdcConnector;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;


public class SfdcConnectorTest {
	private SfdcConnector conn;
	
	@BeforeClass
	public void classSetup() throws ConnectionException {
		conn = new SfdcConnector();
	}
	
	@Test
	public void testSetIsPresent() throws ConnectionException {
		// create lead
		SObject lead = new SObject();
		lead.setType("lead");
		lead.setField("FirstName", "Marc");
		lead.setField("LastName", "Benioff");
		lead.setField("Company", "SFDC");
		SaveResult[] result = conn.getPartnerConnection().create(new SObject[] {lead});
		Assert.assertTrue(result[0].getSuccess());
		String leadId = result[0].getId();
		// assert default value for isPresent is false
		String queryForIsPresent = "select " + SfdcConnector.isPresentField + " from lead where id='" + leadId + "'";
		QueryResult qr = conn.getPartnerConnection().query(queryForIsPresent);
		Assert.assertFalse(Boolean.parseBoolean(qr.getRecords()[0].getField(SfdcConnector.isPresentField).toString()));
		// set isPresent to true
		conn.setIsPresent("Marc", "Benioff", true);
		qr = conn.getPartnerConnection().query(queryForIsPresent);
		Assert.assertTrue(Boolean.parseBoolean(qr.getRecords()[0].getField(SfdcConnector.isPresentField).toString()));
		//clean up
		conn.getPartnerConnection().delete(new String[] {leadId});
	}

}
