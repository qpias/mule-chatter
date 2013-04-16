/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.module.chatter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mule.api.annotations.Module;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.lifecycle.Stop;
import org.mule.api.annotations.param.ConnectionKey;

import com.salesforce.chatter.ChatterService;
import com.salesforce.chatter.authentication.AuthenticationException;
import com.salesforce.chatter.authentication.UnauthenticatedSessionException;
import com.salesforce.chatter.commands.ChatterCommand;
import com.salesforce.chatter.commands.PostToStatusCommand;
import com.salesforce.chatter.message.LinkSegment;
import com.salesforce.chatter.message.Message;
import com.salesforce.chatter.message.TextSegment;

/**
 * Generic module
 *
 * @author MuleSoft, Inc.
 */
@Module(name="chatter", schemaVersion="1.0")
public class ChatterModule
{	
	/**
     * Set property
     *
     * @param username My property
     */
    
    public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	public void setService(ChatterService service) {
		this.service = service;
	}
	/**
     * Partner connection
     */
    @Configurable
    private String username;
    
    /**
     * Partner connection
     */
    @Configurable
    private String password;
    
    /**
     * Partner connection
     */
    @Configurable
    private String securityToken;
    
    /**
     * Partner connection
     */
    @Configurable
    private String clientKey;
    
    /**
     * Partner connection
     */
    @Configurable
    private String clientSecret;

    /**
     * Partner connection
     */
    @Configurable
    private String instanceUrl;
    
    /**
     * Partner connection
     */
    private ChatterService service;

    
    @Start
    public void connect() {
    	service = new ChatterService(new ChatterData(username, password, securityToken, clientKey, clientSecret, instanceUrl));
    }
    
    @Stop
    public void disconnect() {
    	
    }

    /**
     * Custom processor
     *
     * {@sample.xml ../../../doc/Chatter-connector.xml.sample chatter:my-processor}
     *
     * @param messageSegments Content to be processed
     * @return Some string
     */
    @Processor
    public String myProcessor(List<Map<String, String>> messageSegments)
    {
    	Message msg = new Message();
    	for (Map<String, String> messageSegment : messageSegments) {
    		String messageType = messageSegment.get("type");
    		String content = messageSegment.get("content");
    		if (messageType.equals("text"))
    			msg.addSegment(new TextSegment(content));
    		else if (messageType.equals("link"))
    			msg.addSegment(new LinkSegment(content));

    	}
    	
    	//msg.addSegment(new LinkSegment("www.salesforce.com"));
    	//msg.addSegment(new TextSegment(" #salesforce"));

    	ChatterCommand cmd = new PostToStatusCommand();

    	return executeCommand(cmd, msg);
    }
    
    private String executeCommand(ChatterCommand cmd, Message msg) {
    	String reply = "";
    	try {
			service.executeCommand(cmd, msg);
			reply = "message sent";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthenticatedSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return reply;
    }
}
