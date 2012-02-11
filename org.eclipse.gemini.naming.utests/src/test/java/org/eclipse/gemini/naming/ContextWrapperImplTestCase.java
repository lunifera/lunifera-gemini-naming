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

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

import org.easymock.EasyMockSupport;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class ContextWrapperImplTestCase extends TestCase {

	public void testCreate() throws Exception {
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		
		mockSupport.replayAll();
		
		new ContextWrapperImpl(contextMock, factoryManagerMock);
		
		mockSupport.verifyAll();
	}
	
	public void testDelegatedMethods() throws Exception {
		final Name expectedCompositeName = new CompositeName();
		final Name newCompositeName = new CompositeName();
		final Object expectedBindingValue = new Object();
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		NamingEnumeration namingEnumerationMock = 
			mockSupport.createMock(NamingEnumeration.class);
		Context subContextMock = 
			mockSupport.createMock(Context.class);
		
		// expect every method of the Context interface to delegate to the
		// internal Context implementation, except for the lookup(String) method
		expect(contextMock.lookup(new CompositeName())).andReturn("test1");
		contextMock.bind(expectedCompositeName, expectedBindingValue);
		contextMock.bind("test-binding-one", "just a test");
		contextMock.rebind(expectedCompositeName, "test2");
		contextMock.rebind("test-rebind-one", "another value");
		contextMock.unbind(expectedCompositeName);
		contextMock.unbind("test-binding-one");
		contextMock.rename(expectedCompositeName, newCompositeName);
		contextMock.rename("test-binding-one", "test-binding-two");
		expect(contextMock.list(expectedCompositeName)).andReturn(namingEnumerationMock);
		expect(contextMock.list("test-list-one")).andReturn(namingEnumerationMock);
		expect(contextMock.listBindings(expectedCompositeName)).andReturn(namingEnumerationMock);
		expect(contextMock.listBindings("test-list-bindings-one")).andReturn(namingEnumerationMock);
		contextMock.destroySubcontext(expectedCompositeName);
		contextMock.destroySubcontext("test-destroy-sub-context-one");
		expect(contextMock.createSubcontext(expectedCompositeName)).andReturn(subContextMock);
		expect(contextMock.createSubcontext("test-create-sub-context-one")).andReturn(subContextMock);
		expect(contextMock.lookupLink(expectedCompositeName)).andReturn("link value");
		expect(contextMock.lookupLink("test-lookup-link")).andReturn("another link value");
		expect(contextMock.getNameParser(expectedCompositeName)).andReturn(null);
		expect(contextMock.getNameParser("test-get-name-parser")).andReturn(null);
		expect(contextMock.composeName(expectedCompositeName, expectedCompositeName)).andReturn(null);
		expect(contextMock.composeName("name", "prefix")).andReturn(null);
		expect(contextMock.addToEnvironment("test-property-one", "add-environment-value")).andReturn(null);
		expect(contextMock.removeFromEnvironment("test-remove-property")).andReturn(null);
		expect(contextMock.getEnvironment()).andReturn(null);
		contextMock.close();
		expect(contextMock.getNameInNamespace()).andReturn("name-in-namespace");
		
		
		mockSupport.replayAll();
		
		// begin test
		Context testContext = 
			new ContextWrapperImpl(contextMock, factoryManagerMock);
		
		// exercise all Context methods (except lookup(String), and verify
		// that the underlying context is used in each call
		assertEquals("Context did not delegate to the proper Context implementation",
				     "test1", testContext.lookup(new CompositeName()));
		testContext.bind(expectedCompositeName, expectedBindingValue);
		testContext.bind("test-binding-one", "just a test");
		testContext.rebind(expectedCompositeName, "test2");
		testContext.rebind("test-rebind-one", "another value");
		testContext.unbind(expectedCompositeName);
		testContext.unbind("test-binding-one");
		testContext.rename(expectedCompositeName, newCompositeName);
		testContext.rename("test-binding-one", "test-binding-two");
		assertNotNull("ContextWrapperImpl did not invoke on the internal context",
				      testContext.list(expectedCompositeName));
		assertNotNull("ContextWrapperImpl did not invoke on the internal context",
				      testContext.list("test-list-one"));
		assertNotNull("ContextWrapperImpl did not invoke on the internal context",
			          testContext.listBindings(expectedCompositeName));
		assertNotNull("ContextWrapperImpl did not invoke on the internal context",
			          testContext.listBindings("test-list-bindings-one"));
		testContext.destroySubcontext(expectedCompositeName);
		testContext.destroySubcontext("test-destroy-sub-context-one");
		assertNotNull("ContextWrapperImpl did not invoke on the internal context",
				       testContext.createSubcontext(expectedCompositeName));
		assertNotNull("ContextWrapperImpl did not invoke on the internal context",
				       testContext.createSubcontext("test-create-sub-context-one"));
		assertEquals("ContextWrapperImpl did not invoke on the internal context",
				     "link value", testContext.lookupLink(expectedCompositeName));
		assertEquals("ContextWrapperImpl did not invoke on the internal context",
			         "another link value", testContext.lookupLink("test-lookup-link"));
		assertNull("ContextWrapperImpl did not invoke on the internal context",
				    testContext.getNameParser(expectedCompositeName));
		assertNull("ContextWrapperImpl did not invoke on the internal context",
			       testContext.getNameParser("test-get-name-parser"));
		assertNull("ContextWrapperImpl did not invoke on the internal context",
			       testContext.composeName(expectedCompositeName, expectedCompositeName));
		assertNull("ContextWrapperImpl did not invoke on the internal context",
			       testContext.composeName("name", "prefix"));
		assertNull("ContextWrapperImpl did not invoke on the internal context",
			       testContext.addToEnvironment("test-property-one", "add-environment-value"));
		assertNull("ContextWrapperImpl did not invoke on the internal context", 
				   testContext.removeFromEnvironment("test-remove-property"));
		assertNull("ContextWrapperImpl did not invoke on the internal context", 
				   testContext.getEnvironment());
		testContext.close();
		assertEquals("ContextWrapperImpl did not invoke on the internal context",
				      "name-in-namespace", testContext.getNameInNamespace());
		
		mockSupport.verifyAll();
	}
	
	
	/** 
	 * Verify that the Context.lookup(String) method is intercepted by this wrapper in 
	 * order to support URL Context Factories.  
	 * 
	 */
	public void testURLLookup() throws Exception {
		final String expectedURL = "testURL";
		final String expectedLookupName = expectedURL + ":" + "basicLookupName";
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		ObjectFactory objectFactoryMock = 
			mockSupport.createMock(ObjectFactory.class);
		Context contextMock = 
			mockSupport.createMock(Context.class);
		Context urlContextMock = 
			mockSupport.createMock(Context.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		expect(contextMock.getEnvironment()).andReturn(new Hashtable());
		expect(factoryManagerMock.getURLContextFactory(expectedURL)).andReturn(objectFactoryMock);
		expect(objectFactoryMock.getObjectInstance(null, null, null, new Hashtable())).andReturn(urlContextMock);
		expect(urlContextMock.lookup(expectedLookupName)).andReturn("just a url context factory test");
		
		mockSupport.replayAll();
		
		// begin test
		Context testContext = 
			new ContextWrapperImpl(contextMock, factoryManagerMock);
		
		testContext.lookup(expectedLookupName);
		
		mockSupport.verifyAll();
	}
	
	public void testNonURLLookup() throws Exception {
		final String expectedNonURLName = "lookupOne";
		final String expectedValue = "lookup result";
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		expect(contextMock.lookup(expectedNonURLName)).andReturn(expectedValue);
		
		mockSupport.replayAll();

		// begin test
		Context testContext = 
			new ContextWrapperImpl(contextMock, factoryManagerMock);
	    assertEquals("ContextWrapperImpl did not handle non-url lookup correctly",
	    		     expectedValue, testContext.lookup(expectedNonURLName));
		
		mockSupport.verifyAll();
	}
	
	
	public void testURLLookupWithNoObjectFactory() throws Exception {
		final String expectedURL = "testURL";
		final String expectedLookupName = expectedURL + ":" + "basicLookupName";
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		expect(factoryManagerMock.getURLContextFactory(expectedURL)).andReturn(null);
		
		mockSupport.replayAll();
		
		// begin test
		Context testContext = 
			new ContextWrapperImpl(contextMock, factoryManagerMock);
		
		try {
			testContext.lookup(expectedLookupName);
			fail("NameNotFoundException should have been thrown");
		} catch (NameNotFoundException namingException) {
			// expected exception
		}
		
		
		mockSupport.verifyAll();
	}
	
	
	public void testURLLookupWithNoURLContext() throws Exception {
		final String expectedURL = "testURL";
		final String expectedLookupName = expectedURL + ":" + "basicLookupName";
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		ObjectFactory objectFactoryMock = 
			mockSupport.createMock(ObjectFactory.class);
		Context contextMock = 
			mockSupport.createMock(Context.class);
		
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		expect(contextMock.getEnvironment()).andReturn(new Hashtable());
		expect(factoryManagerMock.getURLContextFactory(expectedURL)).andReturn(objectFactoryMock);
		expect(objectFactoryMock.getObjectInstance(null, null, null, new Hashtable())).andReturn(null);
		
		mockSupport.replayAll();
		
		// begin test
		Context testContext = 
			new ContextWrapperImpl(contextMock, factoryManagerMock);

		try {
			testContext.lookup(expectedLookupName);
			fail("NamingException should have been thrown");
		} catch (NamingException namingException) {
			// expected exception
		}
		
		mockSupport.verifyAll();
	}
	
}
