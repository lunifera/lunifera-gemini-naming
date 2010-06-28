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
 *     Bob Nettleton - Initial Developer tests for Reference Implementation
 ******************************************************************************/

/**
 * Test Case to verify the behavior of the Gemini Naming 
 * implementation of the JNDIProviderAdmin interface.  
 */

package org.eclipse.gemini.naming.test;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.directory.Attributes;
import javax.naming.spi.DirObjectFactory;
import javax.naming.spi.ObjectFactory;
import javax.naming.spi.ObjectFactoryBuilder;


import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jndi.JNDIProviderAdmin;


public class ContextAdminTestCase extends NamingTestCase  {
	
	/**
	 * Verifies that the JNDIProviderAdmin service is made 
	 * available by the Gemini Naming implementation.
	 */
	public void testServiceAvailable() throws Exception {
		ServiceReference serviceReference = 
			getContext().getServiceReference("org.osgi.service.jndi.JNDIProviderAdmin");
		assertNotNull("JNDIProviderAdmin service was not published as expected", serviceReference);
		
		JNDIProviderAdmin contextAdmin = 
			(JNDIProviderAdmin) getContext().getService(serviceReference);
		assertNotNull("JNDIProviderAdmin service not available via factory", contextAdmin);
	}
	
	/**
	 * Verifies basic behavior of the JNDIProviderAdmin.getObjectInstance() method.
	 */
	public void testGetObjectInstance() throws Exception {
		// test setup
		final int expectedValue = 100;
		// stub builder to be used in test
		ObjectFactoryBuilder factoryBuilder = new ObjectFactoryBuilder() {
			public ObjectFactory createObjectFactory(Object var0, Hashtable var1) throws NamingException {
				return new ObjectFactory() {
				    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
				        return new Integer(expectedValue);
				    }
				};
			}
        };
		
		ServiceReference serviceReference = 
			getContext().getServiceReference("org.osgi.service.jndi.JNDIProviderAdmin");
		assertNotNull("JNDIProviderAdmin service was not published as expected", serviceReference);
		
		JNDIProviderAdmin contextAdmin = 
			(JNDIProviderAdmin) getContext().getService(serviceReference);
        
        // only register the builder implementation
        registerService(ObjectFactoryBuilder.class.getName(), factoryBuilder, null);


        // reference data does not matter, since we're testing that
        // the factory manager can locate the only ObjectFactory registered.
        Reference reference = new Reference("test", "com.test.factory.DoesNotExist", null);
        // invoke getObjectInstance on the JNDIProviderAdmin service
        Object result = contextAdmin.getObjectInstance(reference, null, null, new Hashtable());
        assertEquals("JNDI Factory Manager did not locate the correct ObjectFactory", 
                      new Integer(expectedValue), result);
	}
	
	
    /**
     * Verifies the behavior of the JNDIProviderAdmin.getObjectInstance() method
     * that takes an Attributes object as a parameter.  
     */
	public void testGetObjectInstanceWithAttributes() throws Exception {
		String[] serviceInterfaces = { DirObjectFactory.class.getName(), 
				                       TestDirObjectFactory.class.getName() };
		ServiceRegistration serviceRegistration = 
			getContext().registerService(serviceInterfaces, 
				                         new TestDirObjectFactory(),
				                         null);
		
		try {
			ServiceReference serviceReference = 
				getContext().getServiceReference("org.osgi.service.jndi.JNDIProviderAdmin");
			assertNotNull("JNDIProviderAdmin service was not published as expected", serviceReference);
			
			JNDIProviderAdmin contextAdmin = 
				(JNDIProviderAdmin) getContext().getService(serviceReference);
			
			Reference reference = new Reference("test", 
					                            TestDirObjectFactory.class.getName(), null);
			Object result = contextAdmin.getObjectInstance(reference, null, null, new Hashtable(), null);
			assertNotNull("JNDIProviderAdmin did not properly consult the DirObjectFactory", 
					       result);
			assertTrue("JNDIProviderAdmin returned an incorrect type", 
					    result instanceof Integer);
			assertEquals("JNDIProviderAdmin returned an incorrect value",
					      new Integer(100), result);
		}
		finally {
			serviceRegistration.unregister();
		}
	}
	
	/**
	 * Verifies that the JNDIProviderAdmin.getObjectInstance() implementation
	 * will query on ObjectFactoryBuilder services first in an attempt to 
	 * resolve an object.  
	 */
	public void testGetObjectInstanceWithAttributesUsingBuilder() throws Exception {
		// setup test builder
		final ObjectFactoryBuilder builder = new ObjectFactoryBuilder() {
			public ObjectFactory createObjectFactory(Object refInfo, Hashtable environment) throws NamingException {
				return new TestDirObjectFactory();
			}
		};
		
		// should be ignored, since it only supports ObjectFactory
		final ObjectFactoryBuilder builder2 = new ObjectFactoryBuilder() {
			public ObjectFactory createObjectFactory(Object refInfo, Hashtable environment) throws NamingException {
				return new ObjectFactory() {
					public Object getObjectInstance(Object var0, Name var1,
							Context var2, Hashtable var3)
							throws Exception {
						return null;
					}
					
				};
			}
		};
		
		registerService(ObjectFactoryBuilder.class.getName(), 
				        builder, null);
		
		registerService(ObjectFactoryBuilder.class.getName(), 
						builder2, null);
		
		// test should resolve reference with builder installed, 
		// since the DirObjectFactory is indirectly created by the builder
		ServiceReference serviceReference = 
			getContext().getServiceReference("org.osgi.service.jndi.JNDIProviderAdmin");
		assertNotNull("JNDIProviderAdmin service was not published as expected", serviceReference);
			
		JNDIProviderAdmin contextAdmin = 
			(JNDIProviderAdmin) getContext().getService(serviceReference);
			
		Reference reference = new Reference("test", 
					                            TestDirObjectFactory.class.getName(), null);
		Object result = contextAdmin.getObjectInstance(reference, null, null, new Hashtable(), null);
		assertNotNull("JNDIProviderAdmin did not properly consult the DirObjectFactory", 
					   result);
		
		assertFalse("JNDIProviderAdmin should not have returned reference",
				     result instanceof Reference);
		assertTrue("JNDIProviderAdmin returned an incorrect type", 
				result instanceof Integer);
		
		assertEquals("JNDIProviderAdmin returned an incorrect value",
					  new Integer(100), result);
	}
	
	/**
	 * Verifies that JNDIProviderAdmin.getObjectInstance() can support 
	 * calls that pass in an Object to be resolved that is not a Reference.  
	 */
	public void testGetObjectInstanceWithAttributesUsingNonReference()
			throws Exception {
		// setup test builder
		final ObjectFactoryBuilder builder = new ObjectFactoryBuilder() {
			public ObjectFactory createObjectFactory(Object refInfo, Hashtable environment) throws NamingException {
				return new TestDirObjectFactory();
			}
		};
		
		registerService(ObjectFactoryBuilder.class.getName(), builder, null);

		ServiceReference serviceReference = 
			getContext().getServiceReference(
					"org.osgi.service.jndi.JNDIProviderAdmin");
		assertNotNull("JNDIProviderAdmin service was not published as expected",
					      serviceReference);

		JNDIProviderAdmin contextAdmin = 
			(JNDIProviderAdmin) getContext().getService(serviceReference);
			
		Object result = 
			contextAdmin.getObjectInstance("This is only a test, and is not a Reference", null, null, new Hashtable(), null);
		assertNotNull(
				"JNDIProviderAdmin did not properly consult the DirObjectFactory",
				result);
		assertTrue("JNDIProviderAdmin returned an incorrect type",
				result instanceof Integer);
		assertEquals("JNDIProviderAdmin returned an incorrect value",
			new Integer(100), result);
		

	}
    
	
	/* Test classes */
	private static class TestDirObjectFactory implements DirObjectFactory {

		public Object getObjectInstance(Object refInfo, Name name, Context context,
				Hashtable environment, Attributes attributes) throws Exception {
			return new Integer(100);
		}

		public Object getObjectInstance(Object refInfo, Name name, Context context,
				Hashtable environment) throws Exception {
			return null;
		}
		
	}
}
