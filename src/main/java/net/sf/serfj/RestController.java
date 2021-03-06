/*
 * Copyright 2010 Eduardo Yáñez Parareda
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
package net.sf.serfj;

import java.io.IOException;
import java.util.Map;

/**
 * Class that can be used as parent class for an action which wants to be a
 * Serfj controller.
 * 
 * @author Eduardo Yáñez
 */
public class RestController {
	private ResponseHelper response;

	protected RestController() {
	}

	protected RestController(ResponseHelper response) {
		this.response = response;
	}

	public void setResponseHelper(ResponseHelper response) {
		this.response = response;
	}

	/**
	 * Gets the ResponseHelper associated to the request.<br>
	 * When a controller's action is called, a ResponseHelper is set through RestController.setResponseHelper(ResponseHelper response)<br>
	 * so the action method will be able to access objects like HttpServletResponse, ServletContext and HttpServletRequest. 
	 */
	public ResponseHelper getResponseHelper() {
	    return this.response;
	}
	
    /**
     * Gets a Map containing all the parameters in the query string, and all the
     * attributes in the request.
     */
	protected Map<String, Object> getParams() {
		return this.response.getParams();
	}

    /**
     * Gets a the value of a parameter that came in the URL or in the request.
     */
    protected Object getParam(String name) {
        return this.response.getParam(name);
    }

    /**
     * Adds an object to the request. If a page will be renderer and it needs some objects
     * to work, with this method a developer can add objects to the request, so the page can
     * obtain them.
     * 
     * @param name Parameter name
     * @param object Object to put in the request
     * @deprecated Use putParam(String name, Object object)
     */
    protected void addObject2Request(String name, Object object) {
        this.putParam(name, object);
    }

    /**
     * Adds an object to the request. If a page will be renderer and it needs some objects
     * to work, with this method a developer can add objects to the request, so the page can
     * obtain them.
     * @param name Parameter name
     * @param object Object to put in the request
     */
    protected void putParam(String name, Object object) {
        this.response.getRequest().setAttribute(name, object);
    }

    /**
     * Returns the remote address with including protocol. For example: http://192.168.12.10.<br>
     * Checks for x-forwarded-for and x_forwarded_for headers in case there were proxies between client and server.
     */
    protected String getRemoteAddress() {
        String ipAddress = this.response.getRequest().getHeader("x-forwarded-for");
        if (ipAddress == null) {
            ipAddress = this.response.getRequest().getHeader("X_FORWARDED_FOR");
            if (ipAddress == null){
                ipAddress = this.response.getRequest().getRemoteAddr();
            }
        }
        return this.response.getRequest().getScheme() + "://" + ipAddress;
    }
    
    /**
     * Returns parameter's value that is a String.
     * 
     * @param name
     *            Parameter's name.
     * @return Parameter's value.
     * @throws ClassCastException
     *             if the value is not a {@link String}.
     */
    protected String getStringParam(String name) {
        return (String) this.response.getParam(name);
    }

    /**
     * Gets the value of an Id for the current resource.<br><br>
     * 
     * /sessions/1 -> Id: 1, identifier of 'session'<br>
     * /sessions/1/users/2 -> Id: 2, identifier of 'users'<br>
     */
    protected String getId() {
        return this.response.getId();
    }

    /**
     * Gets the value of an Id given its resource's name.<br><br>
     * 
     * /sessions/1 -> Id: 1, Resource: session<br>
     * /sessions/1/users/2 -> Id: 2, Resource: user<br>
     */
    protected String getId(String resource) {
        return this.response.getId(resource);
    }

	/**
	 * Renders the predefined page.
	 * 
	 * @throws IOException
	 *             if the page doesn't exist.
	 */
	public void renderPage() throws IOException {
		this.response.renderPage();
	}

	/**
	 * Renders some page within 'views' directory.
	 * 
	 * @param page
	 *            The page could have an extension or not. If it doesn't have an
	 *            extension, the framework first looks for page.jsp, then .html
	 *            or .htm extension.
	 * 
	 * @throws IOException
	 *             if the page doesn't exist.
	 */
	public void renderPage(String page) throws IOException {
		this.response.renderPage(page);
	}

	/**
	 * Renders a page from a resource.
	 * 
	 * @param resource
	 *            The name of the resource (bank, account, etc...). It must
	 *            exists below /views directory.
	 * @param page
	 *            The page can have an extension or not. If it doesn't have an
	 *            extension, the framework first looks for page.jsp, then with
	 *            .html or .htm extension.
	 * 
	 * @throws IOException
	 *             if the page doesn't exist.
	 */
	public void renderPage(String resource, String page) throws IOException {
		this.response.renderPage(resource, page);
	}

	/**
	 * Serialize an object. Serializer class used to process the object can be
	 * known using ResponseHelper.getSerializer() method.<br><br>
	 * The serializer used depends on the extension received in the request.
	 * 
	 * @param object Object to serialize.
	 */
	public void serialize(Object object) {
		this.response.serialize(object);
	}

	/**
	 * Gets the URL extension, if any.
	 * 
	 * @return An extension or an empty String if there isn't anyone.
	 */
	public String getExtension() {
		return this.response.getExtension();
	}

	/**
	 * Gets the Serializer class, if any.
	 * 
	 * @return A String with the fully qualified name of the serializer class
	 *         that will be used in case of needed to serialize an object in the
	 *         response.
	 */
	public String getSerializer() {
		return this.response.getSerializer();
	}
}
