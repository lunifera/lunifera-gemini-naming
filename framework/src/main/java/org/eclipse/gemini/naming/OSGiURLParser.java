/*******************************************************************************
 * Copyright (c) 2010 Oracle.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution. 
 * The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at 
 *     http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *     Bob Nettleton (Oracle) - Initial Reference Implementation
 ******************************************************************************/ 

package org.eclipse.gemini.naming;

/**
 * Utility Class to parse the "osgi:services" URL syntax
 * 
 */
class OSGiURLParser {

	private static final String OSGI_SERVICE_PREFIX    = "osgi:service/";
	
	private static final String OSGI_SERVICE_LIST_PREFIX = "osgi:servicelist/";

	private final String		m_osgiURL;
	private String				m_serviceInterface		= null;
	private String				m_filter				= null;
	private boolean				m_parsingCompleted		= false;
	private boolean             m_isServiceList           = false;

	public OSGiURLParser(String osgiURL) {
		m_osgiURL = osgiURL;
	}

	public void parse() {
		if (m_osgiURL.startsWith(OSGI_SERVICE_PREFIX)) {
			parseURLData(OSGI_SERVICE_PREFIX);
		}
		else {
			if (m_osgiURL.startsWith(OSGI_SERVICE_LIST_PREFIX)) {
				parseURLData(OSGI_SERVICE_LIST_PREFIX);
				m_isServiceList = true;
			}
			else {
				throw new IllegalStateException(
						"URL '" + m_osgiURL + "'" +  "did not conform to the OSGi URL Syntax");
			}

		}
	}
	

	public String getServiceInterface() {
		checkParserState();
		return m_serviceInterface;
	}

	public String getFilter() {
		checkParserState();
		return m_filter;
	}

	public boolean hasFilter() {
		checkParserState();
		return getFilter() != null;
	}
	
	public boolean isServiceListURL() {
		checkParserState();
		return m_isServiceList;
	}

	private void checkParserState() {
		if (!m_parsingCompleted)
			throw new IllegalStateException("OSGi URL has not been parsed");
	}
	
	private void parseURLData(final String prefix) {
		String urlData = m_osgiURL.substring(prefix.length());
		int indexOfSlash = urlData.indexOf("/");
		if (indexOfSlash != -1) {
			// interpret everything after the slash to be an OSGi filter
			// string
			m_serviceInterface = urlData.substring(0, indexOfSlash);
			m_filter = urlData.substring(indexOfSlash + 1);
		}
		else {
			m_serviceInterface = urlData;
		}

		if (m_serviceInterface.length() == 0) {
			throw new IllegalStateException(
					"URL did not conform to the OSGi URL Syntax - No Service Interface specified");
		}

		m_parsingCompleted = true;
	}
}
