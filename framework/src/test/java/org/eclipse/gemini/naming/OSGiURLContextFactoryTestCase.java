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

import javax.naming.Context;
import javax.naming.OperationNotSupportedException;
import javax.naming.spi.ObjectFactory;

import org.easymock.EasyMockSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class OSGiURLContextFactoryTestCase extends TestCase {

	public void testCreate() throws Exception {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		
		mockSupport.replayAll();
		// begin test
		new OSGiURLContextFactory(bundleContextMock);
		
		mockSupport.verifyAll();
	}
	
	public void testGetObjectInstance() throws Exception {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		
		mockSupport.replayAll();
		// begin test
		ObjectFactory testFactory = 
			new OSGiURLContextFactory(bundleContextMock);
		Object result = 
			testFactory.getObjectInstance(null, null, null, null);
		assertTrue("OSGiURLContextFactory returned an object that is not a Context",
				   result instanceof Context);
		
		mockSupport.verifyAll();
	}
	
	public void testLookupBundleContext() throws Exception {
		// not sure if this is easily unit testable, since 
		// there is interaction with the AccessController
		
				// mock setup
//		EasyMockSupport mockSupport = new EasyMockSupport();
//		BundleContext bundleContextMock = 
//			mockSupport.createMock(BundleContext.class);
//		Bundle bundleMock = 
//			mockSupport.createMock(Bundle.class);
//		
//		expect(bundleContextMock.getBundle()).andReturn(bundleMock);
//		expect(bundleMock.getBundleId()).andReturn(new Long(10));
//		expect(bundleMock.hasPermission(isA(Object.class))).andReturn(true);
//		
//		mockSupport.replayAll();
//		// begin test
//		ObjectFactory testFactory = 
//			new OSGiURLContextFactory(bundleContextMock);
//		Object result = 
//			testFactory.getObjectInstance(null, null, null, null);
//		assertTrue("OSGiURLContextFactory returned an object that is not a Context",
//				   result instanceof Context);
//		Context context = (Context)result;
//		
//		assertSame("OSGiURLContextFactory did not return the expected BundleContext instance",
//				   bundleContextMock, context.lookup("osgi:framework/bundleContext"));
//		
//		
//		mockSupport.verifyAll();
		fail("test not implemented yet");
	}
	
	public void testLookupService() throws Exception {
		final String expectedServiceInterface = TestService.class.getName();
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		Bundle bundleMock = 
			mockSupport.createMock(Bundle.class);
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		ServiceReference serviceReferenceMock = 
			mockSupport.createMock(ServiceReference.class);
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		TestService serviceMock = 
			mockSupport.createMock(TestService.class);
		expect(serviceReferenceMock.getProperty(Constants.SERVICE_ID)).andReturn("10");
		expect(serviceReferenceMock.getBundle()).andReturn(bundleMock);
		expect(bundleContextMock.getServiceReferences(expectedServiceInterface, null)).andReturn(new ServiceReference[] {serviceReferenceMock});
		expect(bundleContextMock.getService(serviceReferenceMock)).andReturn(serviceMock).anyTimes();
		expect(bundleContextMock.createFilter("(service.id=10)")).andReturn(filterMock);
		bundleContextMock.addServiceListener(isA(ServiceListener.class), eq("(service.id=10)"));
		
		mockSupport.replayAll();

		ObjectFactory testFactory = 
			new OSGiURLContextFactory(bundleContextMock);
		Object result = 
			testFactory.getObjectInstance(null, null, null, null);
		assertTrue("OSGiURLContextFactory returned an object that is not a Context",
				   result instanceof Context);
		Context context = (Context)result;
		
		assertTrue("OSGiURLContextFactory did not return the correct Context type",
				   context instanceof NotSupportedContext);
		
		TestService service = (TestService)context.lookup("osgi:service/" + expectedServiceInterface);
		assertNotNull("OSGiURLContextFactory did not return the expected OSGi service",
				      service);
		
		try {
			context.bind("name", "value");
			fail("OperationNotSupportedException should have been thrown");
		} catch (OperationNotSupportedException namingException) {
			// expected exception
		}
		
		
		mockSupport.verifyAll();
	}
	
	interface TestService {
		public String getData();
	}
	
}
