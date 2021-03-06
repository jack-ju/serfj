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
package net.sf.serfj.finders;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.serfj.Config;

/**
 * Finds a serializer for a request. It finds a class with the the name
 * ExtensionResourceNameSerializer in the directory expected depending on the
 * package style used. Extension could be .xml, .json, .anything, so the finder
 * will look for JsonResourceNameSerializer, AnythingResourceNameSerializer,
 * etc...<br/>
 * <br/>
 * 
 * There are some default serializers:<br/>
 * - net.sf.serfj.serializers.JsonSerializer<br/>
 * - net.sf.serfj.serializers.XmlSerializer<br/>
 * - net.sf.serfj.serializers.PageSerializer<br/>
 * <br/>
 * 
 * that will be used if there aren't any serializers found for a resource.
 * 
 * @author Eduardo Yáñez 
 */
public class SerializerFinder extends ResourceFinder {

	protected static final String DEFAULT_SERIALIZERS_PACKAGE = "net.sf.serfj.serializers";
	private static final String PAGE_EXTENSION = "page";
	private static final String JSON_EXTENSION = "json";
	private static final String B64_EXTENSION = "base64";
    private static final String XML_EXTENSION = "xml";
    private static final String FILE_EXTENSION = "file";

	private static Map<String, String> contentType2Extension = new HashMap<String, String>(4);

	public SerializerFinder(String extension) {
		super(null, null, (extension == null ? PAGE_EXTENSION : extension), OFF_OPTION, null);
		this.initExtensionsCache();
	}

	public SerializerFinder(Config config, String extension) {
		super(config.getString(Config.MAIN_PACKAGE), config.getString(Config.ALIAS_SERIALIZERS_PACKAGE), 
		        (extension == null ? PAGE_EXTENSION : extension), config.getString(Config.SUFFIX_SERIALIZER), 
		        config.getString(Config.PACKAGES_STYLE));
        this.initExtensionsCache();
	}

	private void initExtensionsCache() {
	    if (contentType2Extension.isEmpty()) {
            contentType2Extension.put("application/json", JSON_EXTENSION);
            contentType2Extension.put("text/xml", XML_EXTENSION);
            contentType2Extension.put("application/octect-stream", B64_EXTENSION);
            contentType2Extension.put("application/octect-stream", FILE_EXTENSION);
	    }
	}
	
	/**
	 * Returns a extension from a content-type. If the content-type is not
	 * valid, then a null will be returned.
	 * 
	 * @param contentType
	 *            A content-type. Valid content-types are: - application/json -
	 *            text/xml - application/octect-stream
	 * 
	 * @return a extension (json, 64 or xml).
	 */
	public static String getExtension(String contentType) {
		return contentType2Extension.get(contentType);
	}

	@Override
	protected String defaultResource(String model) {
		String serializer = super.defaultResource(model);
		if (serializer == null && isDefaultImplementation()) {
			serializer = MessageFormat.format("{0}.{1}Serializer", DEFAULT_SERIALIZERS_PACKAGE, 
			        this.getUtils().capitalize(this.getPrefix()));
		}
		return serializer;
	}

	private Boolean isDefaultImplementation() {
		if (JSON_EXTENSION.equals(this.getPrefix().toLowerCase()) 
                || FILE_EXTENSION.equals(this.getPrefix().toLowerCase()) 
                || B64_EXTENSION.equals(this.getPrefix().toLowerCase()) 
		        || XML_EXTENSION.equals(this.getPrefix().toLowerCase())) {
			return true;
		}
		return false;
	}
}
