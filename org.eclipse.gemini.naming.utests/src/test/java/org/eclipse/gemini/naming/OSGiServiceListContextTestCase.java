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

import java.io.Closeable;
import java.util.NoSuchElementException;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.OperationNotSupportedException;

import org.easymock.EasyMockSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class OSGiServiceListContextTestCase extends TestCase {

	public void testBasicCreate() throws Exception {
		EasyMockSupport mockSupport = new EasyMockSupport();
		// setup mocks and test fixtures
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		
		mockSupport.replayAll();
		
		OSGiURLParser urlParser = new OSGiURLParser("osgi:servicelist/com.oracle.TestService");
		new OSGiServiceListContext(bundleContextMock, new ServiceReference[0], urlParser);
		
		mockSupport.verifyAll();
	}
	
	public void testCreateWithServiceReferences() throws Exception {
		// setup mocks and test fixtures
		EasyMockSupport mockSupport = new EasyMockSupport();
		Bundle bundleMock = mockSupport.createMock(Bundle.class);
		// service reference
		ServiceReference serviceRefMockOne = 
			createServiceReferenceMock(mockSupport, bundleMock, 1);
		
		ServiceReference serviceRefMockTwo = 
			createServiceReferenceMock(mockSupport, bundleMock, 2);
		
		// mock filter
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		// mock BundleContext
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		Closeable closeable1 = mockSupport.createMock(Closeable.class);
		Closeable closeable2 = mockSupport.createMock(Closeable.class);
		expect(bundleContextMock.getService(serviceRefMockOne)).andReturn(closeable1).anyTimes();
		expect(bundleContextMock.getService(serviceRefMockTwo)).andReturn(closeable2).anyTimes();
		
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.createFilter("(service.id=2)")).andReturn(filterMock).anyTimes();
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		expect(bundleContextMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		mockSupport.replayAll();
		
		// begin test
		OSGiURLParser urlParser = new OSGiURLParser("osgi:servicelist/java.io.Closeable");
		urlParser.parse();
		Context context = 
			new OSGiServiceListContext(bundleContextMock, 
					                   new ServiceReference[]{serviceRefMockOne, serviceRefMockTwo}, 
					                   urlParser);
		
		// test service lookup using service.id
		Object result = context.lookup("1");
		assertNotNull("ServiceListContext incorrectly returned a null value", 
				      result);
		assertTrue("ServiceListContext returned incorrect type",
				    result instanceof Closeable);
		
		Object result2 = context.lookup("2");
		assertNotNull("ServiceListContext incorrectly returned a null value", 
			      result2);
		assertTrue("ServiceListContext returned incorrect type",
			    result2 instanceof Closeable);
		assertNotSame("ServiceListContext incorrectly returned the same object",
				       result, result2);
		
		try {
			// verify that lookup of unknown service results in an exception
			context.lookup("3");
			fail("NameNotFoundException should have been thrown");
		} catch (NameNotFoundException namingException) {
			// expected exception
		}
		
		mockSupport.verifyAll();
	}

	
	
	public void testList() throws Exception {
		// setup mocks and test fixtures
		EasyMockSupport mockSupport = new EasyMockSupport();
		Bundle bundleMock = mockSupport.createMock(Bundle.class);
		// service reference
		ServiceReference serviceRefMockOne = 
			createServiceReferenceMock(mockSupport, bundleMock, 1);
		
		ServiceReference serviceRefMockTwo = 
			createServiceReferenceMock(mockSupport, bundleMock, 2);
		
		// mock filter
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		// mock BundleContext
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		Closeable closeable1 = mockSupport.createMock(Closeable.class);
		Closeable closeable2 = mockSupport.createMock(Closeable.class);
		expect(bundleContextMock.getService(serviceRefMockOne)).andReturn(closeable1).anyTimes();
		expect(bundleContextMock.getService(serviceRefMockTwo)).andReturn(closeable2).anyTimes();
		
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.createFilter("(service.id=2)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		mockSupport.replayAll();
		
		// begin test
		OSGiURLParser urlParser = new OSGiURLParser("osgi:servicelist/java.io.Closeable");
		urlParser.parse();
		Context context = 
			new OSGiServiceListContext(bundleContextMock, 
					                   new ServiceReference[]{serviceRefMockOne, serviceRefMockTwo}, 
					                   urlParser);
		
		// call Context.list("") on returned service list context
		NamingEnumeration<NameClassPair> namingEnumeration = context.list("");
		Object result = namingEnumeration.next();
		// verify the contents of the NamingEnumeration returned
		assertNotNull("NamingEnumeration did not contain the expected service",
				       result);
		assertTrue("NamingEnumeration returned an unexpected type", 
				    result instanceof NameClassPair);

		NameClassPair nameClassPair = (NameClassPair)result;
		assertEquals("NameClassPair did not contain the expected type", 
				     Closeable.class.getName(), 
				     nameClassPair.getClassName());
		assertEquals("NameClassPair did not contain the expected ID", 
				    "1", nameClassPair.getName());
		
		assertTrue("NamingEnumeration should contain one more element", 
			       namingEnumeration.hasMoreElements());
		
		NameClassPair nameClassPairTwo = (NameClassPair)namingEnumeration.next();
		assertEquals("NameClassPair did not contain the expected type", 
				     Closeable.class.getName(),
				     nameClassPairTwo.getClassName());
		assertEquals("NameClassPair did not contain the expected ID", 
			         "2", nameClassPairTwo.getName());
		
		assertFalse("NamingEnumeration should not contain any more elements", 
			        namingEnumeration.hasMoreElements());
		
		// verify exception handling
		try {
			namingEnumeration.nextElement();
			fail("NoSuchElementException should have been thrown");
		} catch (NoSuchElementException e) {
			// expected exception
		}
		
		// verify exception handling
		try {
			namingEnumeration.next();
			fail("NoSuchElementException should have been thrown");
		} catch (NoSuchElementException e) {
			// expected exception
		}
		
		mockSupport.verifyAll();
	}
	
	public void testListWithNonEmptyString() throws Exception {
		//setup mocks and test fixtures
		EasyMockSupport mockSupport = new EasyMockSupport();
		Bundle bundleMock = mockSupport.createMock(Bundle.class);
		// service reference
		ServiceReference serviceRefMockOne = 
			createServiceReferenceMock(mockSupport, bundleMock, 1);
		
		ServiceReference serviceRefMockTwo = 
			createServiceReferenceMock(mockSupport, bundleMock, 2);
		
		// mock filter
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		// mock BundleContext
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		Closeable closeable1 = mockSupport.createMock(Closeable.class);
		Closeable closeable2 = mockSupport.createMock(Closeable.class);
		expect(bundleContextMock.getService(serviceRefMockOne)).andReturn(closeable1).anyTimes();
		expect(bundleContextMock.getService(serviceRefMockTwo)).andReturn(closeable2).anyTimes();
		
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.createFilter("(service.id=2)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		mockSupport.replayAll();
		
		// begin test
		OSGiURLParser urlParser = new OSGiURLParser("osgi:servicelist/java.io.Closeable");
		urlParser.parse();
		Context context = 
			new OSGiServiceListContext(bundleContextMock, 
					                   new ServiceReference[]{serviceRefMockOne, serviceRefMockTwo}, 
					                   urlParser);
		
		// call Context.list("") on returned service list context
		try {
			context.list("this-string-is-not-valid");
			fail("OperationNotSupportedException should have been thrown");
		} catch (OperationNotSupportedException namingException) {
			// expected exception
		}
	}
	
	public void testListBindings() throws Exception {
		// setup mocks and test fixtures
		EasyMockSupport mockSupport = new EasyMockSupport();
		Bundle bundleMock = mockSupport.createMock(Bundle.class);
		// service reference
		ServiceReference serviceRefMockOne = 
			createServiceReferenceMock(mockSupport, bundleMock, 1);
		
		ServiceReference serviceRefMockTwo = 
			createServiceReferenceMock(mockSupport, bundleMock, 2);
		
		// mock filter
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		// mock BundleContext
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		Closeable closeable1 = mockSupport.createMock(Closeable.class);
		Closeable closeable2 = mockSupport.createMock(Closeable.class);
		expect(bundleContextMock.getService(serviceRefMockOne)).andReturn(closeable1).anyTimes();
		expect(bundleContextMock.getService(serviceRefMockTwo)).andReturn(closeable2).anyTimes();
		
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.createFilter("(service.id=2)")).andReturn(filterMock).anyTimes();
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		expect(bundleContextMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		mockSupport.replayAll();
		
		// begin test
		OSGiURLParser urlParser = new OSGiURLParser("osgi:servicelist/java.io.Closeable");
		urlParser.parse();
		Context context = 
			new OSGiServiceListContext(bundleContextMock, 
					                   new ServiceReference[]{serviceRefMockOne, serviceRefMockTwo}, 
					                   urlParser);

		NamingEnumeration<Binding> namingEnumeration = 
			context.listBindings("");
		assertTrue("NamingEnumeration did not contain any elements", 
			       namingEnumeration.hasMoreElements());
	
		Object result = namingEnumeration.next();
		assertNotNull("NamingEnumeration did not contain the expected service",
			          result);
		assertTrue("NamingEnumeration returned an unexpected type", 
			        result instanceof Binding);
		
		Binding bindingOne = (Binding)result;
		assertEquals("Incorrect Binding type", 
				      Closeable.class.getName(), 
				      bindingOne.getClassName());
		assertEquals("Binding's service ID was not expected",
				      "1", bindingOne.getName());
		assertNotNull("Binding's service object was not included", 
				       bindingOne.getObject());
		assertTrue("Binding's service object was not the correct type",
				    bindingOne.getObject() instanceof Closeable);
		
		
		assertTrue("NamingEnumeration should contain one more element", 
			       namingEnumeration.hasMoreElements());
		
		Binding bindingTwo = (Binding)namingEnumeration.next();
		assertEquals("NameClassPair did not contain the expected type", 
				     Closeable.class.getName(),
				     bindingTwo.getClassName());
		assertEquals("NameClassPair did not contain an expected ID", 
			         "2", bindingTwo.getName());
		assertNotNull("Binding's service object was not included", 
			          bindingTwo.getObject());
		assertTrue("Binding's service object was not the correct type",
			        bindingTwo.getObject() instanceof Closeable);
		
		assertFalse("NamingEnumeration should not contain any more elements", 
			        namingEnumeration.hasMoreElements());
		
		// verify exception handling
		try {
			namingEnumeration.nextElement();
			fail("NoSuchElementException should have been thrown");
		} catch (NoSuchElementException e) {
			// expected exception
		}
		
		// verify exception handling
		try {
			namingEnumeration.next();
			fail("NoSuchElementException should have been thrown");
		} catch (NoSuchElementException e) {
			// expected exception
		}
	}

	
	public void testListBindingsWithNonEmptyString() throws Exception {
		//setup mocks and test fixtures
		EasyMockSupport mockSupport = new EasyMockSupport();
		Bundle bundleMock = mockSupport.createMock(Bundle.class);
		// service reference
		ServiceReference serviceRefMockOne = 
			createServiceReferenceMock(mockSupport, bundleMock, 1);
		
		ServiceReference serviceRefMockTwo = 
			createServiceReferenceMock(mockSupport, bundleMock, 2);
		
		// mock filter
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		// mock BundleContext
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		Closeable closeable1 = mockSupport.createMock(Closeable.class);
		Closeable closeable2 = mockSupport.createMock(Closeable.class);
		expect(bundleContextMock.getService(serviceRefMockOne)).andReturn(closeable1).anyTimes();
		expect(bundleContextMock.getService(serviceRefMockTwo)).andReturn(closeable2).anyTimes();
		
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.createFilter("(service.id=2)")).andReturn(filterMock).anyTimes();
		expect(bundleContextMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		mockSupport.replayAll();
		
		// begin test
		OSGiURLParser urlParser = new OSGiURLParser("osgi:servicelist/java.io.Closeable");
		urlParser.parse();
		Context context = 
			new OSGiServiceListContext(bundleContextMock, 
					                   new ServiceReference[]{serviceRefMockOne, serviceRefMockTwo}, 
					                   urlParser);
		
		// call Context.list("") on returned service list context
		try {
			context.listBindings("this-string-is-not-valid");
			fail("OperationNotSupportedException should have been thrown");
		} catch (OperationNotSupportedException namingException) {
			// expected exception
		}
	}
	
	
	/* private test utility methods */
	private static ServiceReference createServiceReferenceMock(EasyMockSupport mockSupport, Bundle bundleMock, int serviceId) {
		ServiceReference serviceRefMockOne = 
			mockSupport.createMock(ServiceReference.class);
		expect(serviceRefMockOne.getProperty(Constants.SERVICE_ID)).andReturn(new Long(serviceId)).anyTimes();
		expect(serviceRefMockOne.getBundle()).andReturn(bundleMock).anyTimes();
		return serviceRefMockOne;
	}
	
}
