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

import java.util.Hashtable;

import javax.naming.Context;

import junit.framework.TestCase;

public class DefaultInitialContextFactoryTestCase extends TestCase {
	
	public void testCreate() throws Exception {
		Context resultContext = 
			new DefaultInitialContextFactory().getInitialContext(null);
		assertNotNull("Default Context not created", resultContext);
		
		Context resultContextWithEnvironment = 
			new DefaultInitialContextFactory().getInitialContext(new Hashtable<String, Object>());
		assertNotNull("Default Context not created", resultContextWithEnvironment);
	}
	
	public void testClose() throws Exception {
		Context resultContext = 
			new DefaultInitialContextFactory().getInitialContext(null);
		
		// close() should execute without any exceptions
		resultContext.close();
	}
	
	public void testGetEnvironment() throws Exception {
		Context resultContext = 
			new DefaultInitialContextFactory().getInitialContext(null);
		
		Hashtable<?, ?> environment = resultContext.getEnvironment();
		assertNotNull("Default Context did not properly handle the getEnvironment() call",
				      environment);
		assertTrue("Default Context did not return the correct Hashtable",
				    environment.size() == 0);
		
		Hashtable<?, ?> environment2 = resultContext.getEnvironment();
		assertNotNull("Default Context did not properly handle the getEnvironment() call",
				      environment2);
		assertNotSame("Default Context did not create a new Hashtable environment",
				       environment, environment2);
	}
}
