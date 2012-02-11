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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.naming.Context;

import org.easymock.EasyMockSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class ReflectionUtilsTestCase extends TestCase {

	
	public void testInvokeMethodOnContext() throws Throwable {
		final String expectedName = "test-binding-name-one";
		final String expectedValue = "invoke-method-on-context";
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		expect(contextMock.lookup(expectedName)).andReturn(expectedValue);
		
		mockSupport.replayAll();

		// begin test
		Method lookupMethod = Context.class.getMethod("lookup", String.class);
		Object result = ReflectionUtils.invokeMethodOnContext(lookupMethod, contextMock, new Object[] {expectedName});
		assertNotNull("ReflectionUtils returned null", result);
		assertTrue("ReflectionUtils returned an incorrect type",
				   result instanceof String);
		assertEquals("ReflectionUtils returned an incorrect string value",
					 expectedValue, result);

		mockSupport.verifyAll();
	}
	
	public void testInvokeMethodOnObject() throws Throwable {
		final String expectedName = "test-binding-name-one";
		final String expectedValue = "invoke-method-on-object";
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		expect(contextMock.lookup(expectedName)).andReturn(expectedValue);
		
		mockSupport.replayAll();

		// begin test
		Method lookupMethod = Context.class.getMethod("lookup", String.class);
		Object result = ReflectionUtils.invokeMethodOnObject(lookupMethod, contextMock, new Object[] {expectedName});
		assertNotNull("ReflectionUtils returned null", result);
		assertTrue("ReflectionUtils returned an incorrect type",
				   result instanceof String);
		assertEquals("ReflectionUtils returned an incorrect string value",
					 expectedValue, result);
		
		mockSupport.verifyAll();
	}
	
	public void testGetProxyForSingleService() throws Exception {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		TestService serviceMock = 
			mockSupport.createMock(TestService.class);
		expect(serviceMock.getValue()).andReturn("just a test");
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		Bundle bundleMock = 
			mockSupport.createMock(Bundle.class);
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		OSGiURLParser urlParser = new OSGiURLParser("osgi:service/" + TestService.class.getName());
		urlParser.parse();
		
		ServiceReference serviceReferenceMock = 
			mockSupport.createMock(ServiceReference.class);
		expect(serviceReferenceMock.getProperty(Constants.SERVICE_ID)).andReturn(new Long(1));
		expect(serviceReferenceMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		expect(bundleContextMock.getService(serviceReferenceMock)).andReturn(serviceMock).times(2);
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock);
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		expectLastCall().anyTimes();
		
		mockSupport.replayAll();
		
		// begin test
		ServiceProxyInfo result = 
			ReflectionUtils.getProxyForSingleService(bundleContextMock, urlParser, serviceReferenceMock);
		assertNotNull("ReflectionUtils did not return a service proxy info",
				      result);
		assertTrue("ReflectionUtils did not proxy the object as expected",
				   result.isProxied());
		assertNotSame("ReflectionUtils did not return the expected proxy", 
				      result.getService(), serviceMock);
		assertNotNull("ReflectionUtils did not set the InvocationHandler type", 
				      result.getHandler());
		assertEquals("ReflectionUtils returned a proxy for an unexpected object",
				     "just a test", ((TestService)result.getService()).getValue());
		
		
		mockSupport.verifyAll();
	}
	
	
	public void testGetProxyForSingleServiceUsingJNDIServiceName() throws Exception {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		TestService serviceMock = 
			mockSupport.createMock(TestService.class);
		expect(serviceMock.getValue()).andReturn("just a test");
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		Bundle bundleMock = 
			mockSupport.createMock(Bundle.class);
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		OSGiURLParser urlParser = new OSGiURLParser("osgi:service/" + "anotherName");

		urlParser.parse();
		
		ServiceReference serviceReferenceMock = 
			mockSupport.createMock(ServiceReference.class);
		expect(serviceReferenceMock.getProperty(Constants.SERVICE_ID)).andReturn(new Long(1));
		expect(serviceReferenceMock.getProperty(Constants.OBJECTCLASS)).andReturn(new String[] {TestService.class.getName()});
		expect(serviceReferenceMock.getBundle()).andReturn(bundleMock).anyTimes();
		expect(serviceReferenceMock.isAssignableTo(bundleMock, TestService.class.getName())).andReturn(true);
		
		expect(bundleContextMock.getService(serviceReferenceMock)).andReturn(serviceMock).times(2);
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock);
		expect(bundleContextMock.getBundle()).andReturn(bundleMock).anyTimes();
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		expectLastCall().anyTimes();
		
		mockSupport.replayAll();
		
		// begin test
		ServiceProxyInfo result = 
			ReflectionUtils.getProxyForSingleService(bundleContextMock, urlParser, serviceReferenceMock);
		assertNotNull("ReflectionUtils did not return a service proxy info",
				      result);
		assertTrue("ReflectionUtils did not proxy the object as expected",
				   result.isProxied());
		assertNotSame("ReflectionUtils did not return the expected proxy", 
				      result.getService(), serviceMock);
		assertNotNull("ReflectionUtils did not set the InvocationHandler type", 
				      result.getHandler());
		assertEquals("ReflectionUtils returned a proxy for an unexpected object",
				     "just a test", ((TestService)result.getService()).getValue());
		
		
		mockSupport.verifyAll();
	}
	
	
	public void testGetProxyForSingleServiceUsingJNDIServiceNameNoInterface() throws Exception {
		final OSGiURLParser urlParser = new OSGiURLParser("osgi:service/" + "anotherName");
		urlParser.parse();
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		TestService serviceMock = 
			mockSupport.createMock(TestService.class);
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		ServiceReference serviceReferenceMock = 
			mockSupport.createMock(ServiceReference.class);
		// return a service class name that does not exist in the classpath
		expect(serviceReferenceMock.getProperty(Constants.OBJECTCLASS)).andReturn(new String[] {"com.oracle.does.not.exist.TestService"});
		expect(bundleContextMock.getService(serviceReferenceMock)).andReturn(serviceMock);
		
		mockSupport.replayAll();
		
		// begin test
		try {
			ReflectionUtils.getProxyForSingleService(bundleContextMock, urlParser, serviceReferenceMock);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException illegalArgumentException) {
			// expected exception
		}
		
		
		
		mockSupport.verifyAll();
	}
	
	public void testGetProxyForSingleServiceWithInvocationHandlerFactory() throws Throwable {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		TestService serviceMock = 
			mockSupport.createMock(TestService.class);
		expect(serviceMock.getValue()).andReturn("just a test");
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		Bundle bundleMock = 
			mockSupport.createMock(Bundle.class);
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		OSGiURLParser urlParser = new OSGiURLParser("osgi:service/" + TestService.class.getName());
		urlParser.parse();
		
		ServiceReference serviceReferenceMock = 
			mockSupport.createMock(ServiceReference.class);
		expect(serviceReferenceMock.getProperty(Constants.SERVICE_ID)).andReturn(new Long(1));
		expect(serviceReferenceMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		expect(bundleContextMock.getService(serviceReferenceMock)).andReturn(serviceMock).times(2);
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock);
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		expectLastCall().anyTimes();
		
		InvocationHandlerFactory handlerFactoryMock = 
			mockSupport.createMock(InvocationHandlerFactory.class);
		InvocationHandler handlerMock = 
			mockSupport.createMock(InvocationHandler.class);
		
		expect(handlerMock.invoke(isA(Object.class), isA(Method.class), (Object[])anyObject())).andReturn("just a test");
		expect(handlerFactoryMock.create(bundleContextMock, serviceReferenceMock, urlParser, serviceMock)).andReturn(handlerMock);
		
		mockSupport.replayAll();
		
		// begin test
		ServiceProxyInfo result = 
			ReflectionUtils.getProxyForSingleService(bundleContextMock, 
					                                 urlParser, 
					                                 serviceReferenceMock, 
					                                 handlerFactoryMock);
		
		
		assertNotNull("ReflectionUtils did not return a service proxy info",
			          result);
		assertTrue("ReflectionUtils did not proxy the object as expected",
		  	       result.isProxied());
		assertNotSame("ReflectionUtils did not return the expected proxy", 
			          result.getService(), serviceMock);
		assertNotNull("ReflectionUtils did not set the InvocationHandler type", 
			          result.getHandler());
		assertSame("ReflectionUtils did not return the expected InvocationHandler",
				   handlerMock, result.getHandler());
		assertEquals("ReflectionUtils returned a proxy for an unexpected object",
			         "just a test", ((TestService)result.getService()).getValue());
		
	}
	
	
	public void testGetProxyForSingleServiceWithInvocationHandlerFactoryClassType() throws Throwable {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		TestAnotherService serviceStub = 
			new TestAnotherService("a value from a class");
		
		Filter filterMock = 
			mockSupport.createMock(Filter.class);
		Bundle bundleMock = 
			mockSupport.createMock(Bundle.class);
		BundleContext bundleContextMock = 
			mockSupport.createMock(BundleContext.class);
		OSGiURLParser urlParser = new OSGiURLParser("osgi:service/" + TestAnotherService.class.getName());
		urlParser.parse();
		
		ServiceReference serviceReferenceMock = 
			mockSupport.createMock(ServiceReference.class);
		expect(serviceReferenceMock.getProperty(Constants.SERVICE_ID)).andReturn(new Long(1));
		expect(serviceReferenceMock.getBundle()).andReturn(bundleMock).anyTimes();
		
		expect(bundleContextMock.getService(serviceReferenceMock)).andReturn(serviceStub).times(2);
		expect(bundleContextMock.createFilter("(service.id=1)")).andReturn(filterMock);
		bundleContextMock.addServiceListener(isA(ServiceListener.class), isA(String.class));
		expectLastCall().anyTimes();
		
		InvocationHandlerFactory handlerFactoryMock = 
			mockSupport.createMock(InvocationHandlerFactory.class);
		InvocationHandler handlerMock = 
			mockSupport.createMock(InvocationHandler.class);
		
		expect(handlerMock.invoke(isA(Object.class), isA(Method.class), (Object[])anyObject())).andReturn("just a test");
		expect(handlerFactoryMock.create(bundleContextMock, serviceReferenceMock, urlParser, serviceStub)).andReturn(handlerMock);
		
		mockSupport.replayAll();
		
		// begin test
		ServiceProxyInfo result = 
			ReflectionUtils.getProxyForSingleService(bundleContextMock, 
					                                 urlParser, 
					                                 serviceReferenceMock, 
					                                 handlerFactoryMock);
		
		
		assertNotNull("ReflectionUtils did not return a service proxy info",
			          result);
		assertFalse("ReflectionUtils should not have proxied this object (class type)",
		  	        result.isProxied());
		assertSame("ReflectionUtils did not return the expected service", 
			          result.getService(), serviceStub);
		assertNull("ReflectionUtils incorrect set the InvocationHandler type", 
			          result.getHandler());
		assertEquals("ReflectionUtils returned a proxy for an unexpected object",
			         "a value from a class", ((TestAnotherService)result.getService()).getValue());
		
	}
	
	public interface TestService {
		public String getValue();
	}
	
	public class TestAnotherService {
		private final String m_value;
		
		TestAnotherService(String value) {
			m_value = value;
		}
		
		public String getValue() {
			return m_value;
		}
	}
}
