/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.local.layermanager.tool.export.exporter;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.util.Base64;
import org.geowe.client.local.util.BasicAuthenticationProvider;
import org.geowe.client.shared.rest.GitHubContentRequest;
import org.geowe.client.shared.rest.GitHubService;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.http.client.Request;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * https://api.github.com/repos/{user}/{repository}/contents/{path}/{filenma.
 * extension}
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class GitHubExporter implements Exporter {
	private final static String URL_BASE = "https://api.github.com/repos/";
	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	@Override
	public void export(final List<String> parameters) {
		Info.display("GitHub", "GitHub");
		
		final String contentLayer = parameters.get(0);
		final String extension = parameters.get(1);
		final String fileName = parameters.get(2)+ "." + extension;
		
		
		String userName = "jmmluna";
		String password = "k45o0eb";
		String repository = "geodata";
		String path = "tmp";
		String message = "create file by geowe";
		
		GitHubContentRequest content = new GitHubContentRequest();
		content.setContent(Base64.encode(contentLayer));
		content.setMessage(message);
		String authorizationHeaderValue = BasicAuthenticationProvider.getAuthorizationHeaderValue(userName, password);
        					
		RestClient.create(GitHubService.class, URL_BASE, getRemoteCallback(), getErrorCallback()).createFile(userName, repository, path, fileName, authorizationHeaderValue, content);
	}
	
	private RestErrorCallback getErrorCallback() {
		return new RestErrorCallback() {

			@Override
			public boolean error(Request message, Throwable throwable) {
				messageDialogBuilder.createError("Error", message.toString()).show();
				return false;
			}
		};
	}
	
	private RemoteCallback<String> getRemoteCallback() {
		return new RemoteCallback<String>() {

			@Override
			public void callback(String response) {
				messageDialogBuilder.createInfo("OK", response).show();
				
			}			
		};
	}
}
