package org.mule.module.chatter;

import com.salesforce.chatter.authentication.ChatterAuthMethod;
import com.salesforce.chatter.authentication.IChatterData;

public class ChatterData implements IChatterData {

    private final String apiVersion = "24.0";
    private final ChatterAuthMethod authMethod = ChatterAuthMethod.PASSWORD;
    
    private String instanceUrl;
    private String username;
    private String password;
    private String clientKey;
    private String clientSecret;
    
    public ChatterData(String username, String password, String securityToken, String clientKey, String clientSecret, String instanceUrl) {
    	this.username = username;
    	this.password = password+securityToken; //required by SF
    	this.clientKey = clientKey; 
    	this.clientSecret = clientSecret;
    	this.instanceUrl = instanceUrl;
    }
    
	@Override
	public String getApiVersion() {
		// TODO Auto-generated method stub
		return apiVersion;
	}
	@Override
	public ChatterAuthMethod getAuthMethod() {
		// TODO Auto-generated method stub
		return authMethod;
	}
	@Override
	public String getClientCallback() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getClientCode() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getClientKey() {
		// TODO Auto-generated method stub
		return clientKey;
	}
	@Override
	public String getClientSecret() {
		// TODO Auto-generated method stub
		return clientSecret;
	}
	@Override
	public String getInstanceUrl() {
		// TODO Auto-generated method stub
		return instanceUrl;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	@Override
	public String getRefreshToken() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

}