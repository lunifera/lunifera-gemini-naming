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
 *     Bob Nettleton (Oracle) - Initial Reference Implementation Unit Tests
 ******************************************************************************/

package org.eclipse.gemini.naming;

import junit.framework.TestCase;

public class OSGiURLParserTestCase extends TestCase {

	public void testCreate() throws Exception {
		new OSGiURLParser("osgi:service/com.oracle.TestService");
	}
	
	public void testBasicParse() throws Exception {
		OSGiURLParser urlParser = new OSGiURLParser("osgi:service/com.oracle.TestService");
		// should return without exception
		urlParser.parse();
		// verify correct information was parsed
		assertEquals("Parser did not correctly return service interface",
				     "com.oracle.TestService", urlParser.getServiceInterface());
		assertNull("Parser did not correctly return a null filter", urlParser.getFilter());
		assertFalse("Parser did not correctly parse URL, no filter present",
				    urlParser.hasFilter());
		assertFalse("Parser did not correctly interpret URL, no servicelist",
				    urlParser.isServiceListURL());

		
		OSGiURLParser urlParserServiceList = 
			new OSGiURLParser("osgi:servicelist/com.oracle.AnotherTestService");
		// should return without exception
		urlParserServiceList.parse();
		// verify correct information was parsed
		assertEquals("Parser did not correctly return service interface",
			     "com.oracle.AnotherTestService", urlParserServiceList.getServiceInterface());
		assertNull("Parser did not correctly return a null filter", urlParserServiceList.getFilter());
		assertFalse("Parser did not correctly parse URL, no filter present",
			    urlParserServiceList.hasFilter());
		assertTrue("Parser did not correctly interpret URL, servicelist",
			    urlParserServiceList.isServiceListURL());
	}
	
	public void testParseFilter() throws Exception {
		OSGiURLParser urlParser = new OSGiURLParser("osgi:service/com.oracle.TestService/testFilter");
		// should return without exception
		urlParser.parse();
		// verify correct information was parsed
		assertEquals("Parser did not correctly return service interface",
				     "com.oracle.TestService", urlParser.getServiceInterface());
		assertEquals("Parser did not correctly return the expected filter", 
				     "testFilter", urlParser.getFilter());
		assertTrue("Parser did not correctly parse URL, filter present",
				    urlParser.hasFilter());
		assertFalse("Parser did not correctly interpret URL, no servicelist",
				    urlParser.isServiceListURL());
	}
	
	public void testParseError() throws Exception {
		// "testURL" is not a supported URL
		OSGiURLParser urlParser = new OSGiURLParser("testURL:com.oracle.TestService");
		try {
			urlParser.parse();
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException exception) {
			// expected Exception
		}
		
		// zero-length service interface is an error
		OSGiURLParser urlParser2 = new OSGiURLParser("osgi:service//testFilter");
		try {
			urlParser2.parse();
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException exception) {
			// expected Exception
		}
	}
	
	public void testPreParseErrorChecks() throws Exception {
		OSGiURLParser urlParser = new OSGiURLParser("osgi:service/com.oracle.TestService");
		// verify that all accessor methods throw an IllegalStateException
		// prior to the parse() method being called
		try {
			urlParser.getServiceInterface();
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException exception) {
			// expected Exception
		}
		
		try {
			urlParser.getFilter();
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException exception) {
			// expected Exception
		}
		
		try {
			urlParser.hasFilter();
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException exception) {
			// expected Exception
		}
		
		try {
			urlParser.isServiceListURL();
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException exception) {
			// expected Exception
		}
	}
	
}
