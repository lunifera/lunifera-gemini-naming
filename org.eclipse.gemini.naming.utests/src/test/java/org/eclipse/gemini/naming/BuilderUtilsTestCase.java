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

import javax.naming.spi.NamingManager;

import org.easymock.EasyMockSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;
import org.osgi.service.jndi.JNDIConstants;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class BuilderUtilsTestCase extends TestCase {
	
	public void testGetBundleContextDefault() throws Exception {
		BundleContext result = 
			BuilderUtils.getBundleContext(null, NamingManager.class.getName());
		assertNull("BuilderUtils incorrectly returned a BundleContext, should be null",
				    result);
		
		BundleContext result2 = 
			BuilderUtils.getBundleContext(new Hashtable<String, Object>(), NamingManager.class.getName());
		assertNull("BuilderUtils incorrect returned a BundleContext, should be null",
				   result2);
	}
	
	public void testGetBundleContextFromEnvironment() throws Exception {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		BundleContext bundleContextFromEnvironmentMock = 
			mockSupport.createMock(BundleContext.class);
		
		mockSupport.replayAll();
		
		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		environment.put(JNDIConstants.BUNDLE_CONTEXT, bundleContextFromEnvironmentMock);
		BundleContext result = 
			BuilderUtils.getBundleContext(environment, NamingManager.class.getName());

		assertNotNull("BuilderUtils incorrectly returned a null BundleContext", 
				       result);
		assertSame("BuilderUtils did not return the expected BundleContext instance",
				    bundleContextFromEnvironmentMock, result);
		
		mockSupport.verifyAll();
	}
	
	
	public void testGetBundleContextFromContextClassLoader() throws Exception {
		ClassLoader oldContextClassLoader = 
			Thread.currentThread().getContextClassLoader();
		try {
			// mock setup
			EasyMockSupport mockSupport = new EasyMockSupport();
			Bundle bundleMock = 
				mockSupport.createMock(Bundle.class);
			BundleContext bundleContextFromClassLoaderMock = 
				mockSupport.createMock(BundleContext.class);
			expect(bundleMock.getBundleContext()).andReturn(bundleContextFromClassLoaderMock);
			
			mockSupport.replayAll();

			// set Thread Context ClassLoader to use stub TestClassLoader
			Thread.currentThread().setContextClassLoader(new TestClassLoader(bundleMock));
			
			BundleContext result = 
				BuilderUtils.getBundleContext(null, NamingManager.class.getName());

			assertNotNull("BuilderUtils incorrectly returned a null BundleContext", 
					       result);
			assertSame("BuilderUtils did not return the expected BundleContext instance",
					    bundleContextFromClassLoaderMock, result);
			
			mockSupport.verifyAll();
		} finally {
			if(oldContextClassLoader != null) {
				// reset original Context ClassLoader
				Thread.currentThread().setContextClassLoader(oldContextClassLoader);
			}
		}
	
	}
	
	/**
	 * Verifies that a BundleContext specified in an environment map will 
	 * be used before a BundleContext specified on the Context ClassLoader.  
	 * 
	 * TODO, there might not be a way to easily unit test the CallStack strategy, so 
	 *       for now this test will only verify the first two strategies.  
	 *       
	 */
	public void testGetBundleContextPriority() throws Exception {
		ClassLoader oldContextClassLoader = 
			Thread.currentThread().getContextClassLoader();
		try {
			// mock setup
			EasyMockSupport mockSupport = new EasyMockSupport();
			Bundle bundleMock = 
				mockSupport.createMock(Bundle.class);
			BundleContext bundleContextFromClassLoaderMock = 
				mockSupport.createMock(BundleContext.class);
			
			BundleContext bundleContextFromEnvironmentMock = 
				mockSupport.createMock(BundleContext.class);
			
			mockSupport.replayAll();
			
			// set Thread Context ClassLoader to use stub TestClassLoader
			Thread.currentThread().setContextClassLoader(new TestClassLoader(bundleMock));
			
			Hashtable<String, Object> environment = new Hashtable<String, Object>();
			environment.put(JNDIConstants.BUNDLE_CONTEXT, bundleContextFromEnvironmentMock);
			// bundle context from environment should take precedence
			BundleContext result = 
				BuilderUtils.getBundleContext(environment, NamingManager.class.getName());

			assertNotNull("BuilderUtils incorrectly returned a null BundleContext", 
					       result);
			assertSame("BuilderUtils did not return the expected BundleContext instance",
					bundleContextFromEnvironmentMock, result);
			
			mockSupport.verifyAll();
		} finally {
			if(oldContextClassLoader != null) {
				// reset original Context ClassLoader
				Thread.currentThread().setContextClassLoader(oldContextClassLoader);
			}
		}
	}
		
	private static class TestClassLoader extends ClassLoader implements BundleReference {
		private final Bundle m_bundle;
		
		TestClassLoader(Bundle bundle) {
			m_bundle = bundle;
		}
		
		public Bundle getBundle() {
			return m_bundle;
		}
		
	}
}
