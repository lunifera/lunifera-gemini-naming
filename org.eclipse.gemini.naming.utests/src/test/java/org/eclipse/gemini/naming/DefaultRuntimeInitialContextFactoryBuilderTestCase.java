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
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import junit.framework.TestCase;

public class DefaultRuntimeInitialContextFactoryBuilderTestCase extends TestCase {

	public void testNullEnvironment() throws Exception {
		InitialContextFactory factoryResult = 
			new DefaultRuntimeInitialContextFactoryBuilder().createInitialContextFactory(null);
		
		assertNull("DefaultRuntime builder should have returned a null", factoryResult);
	}
	
	public void testEnvironmentWithNoFactorySet() throws Exception {
		// create factory with empty environment
		InitialContextFactory factoryResult = 
			new DefaultRuntimeInitialContextFactoryBuilder().createInitialContextFactory(new Hashtable<String, Object>());
		
		assertNull("DefaultRuntime builder should have returned a null", factoryResult);
	}
	
	public void testEnvironmentWithFactorySet() throws Exception {
		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, TestInitialContextFactory.class.getName());

		InitialContextFactory factoryResult = 
			new DefaultRuntimeInitialContextFactoryBuilder().createInitialContextFactory(environment);
		
		assertNotNull("DefaultRuntime builder did not return a factory", factoryResult);
		assertTrue("DefaultRuntime builder did not return a factory of the expected type",
				    factoryResult instanceof TestInitialContextFactory);
	}
	
	public void testEnvironmentWithFactoryThatDoesNotExist() throws Exception {
		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "this.factory.does.not.exist");

		InitialContextFactory factoryResult = 
			new DefaultRuntimeInitialContextFactoryBuilder().createInitialContextFactory(environment);

		assertNull("DefaultRuntime builder should not have returned a factory",
				   factoryResult);
	}
	
	public void testFactoryThrowsException() throws Exception {
		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, TestInitialContextFactoryThrowsException.class.getName());

		InitialContextFactory factoryResult = 
			new DefaultRuntimeInitialContextFactoryBuilder().createInitialContextFactory(environment);
		
		assertNull("DefaultRuntime builder should not have returned a factory",
				   factoryResult);
	}
	
	static class TestInitialContextFactory implements InitialContextFactory {
		public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
			return null;
		}
	}
	
	static class TestInitialContextFactoryThrowsException implements InitialContextFactory {
		
		TestInitialContextFactoryThrowsException() {
			// exception for unit test
			throw new NullPointerException();
		}
		
		public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
			return null;
		}
		
	}
}
