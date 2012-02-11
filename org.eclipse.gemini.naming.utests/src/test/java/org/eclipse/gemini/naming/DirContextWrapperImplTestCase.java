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

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.easymock.EasyMockSupport;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class DirContextWrapperImplTestCase extends TestCase {

	public void testCreate() throws Exception {
		// setup mocks
		EasyMockSupport mockSupport = new EasyMockSupport();
		DirContext dirContextMock = 
			mockSupport.createMock(DirContext.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		
		mockSupport.replayAll();
		// begin test
		new DirContextWrapperImpl(dirContextMock, factoryManagerMock);
		
		mockSupport.verifyAll();
	}
	
	
	/**
	 * This test verifies that all of the methods on the wrapper for the DirContext interface
	 * will delegate to the internal DirContext implementation.  The test also verifies that the 
	 * parameters passed into the wrapper are passed unchanged to the internal 
	 * implementation.    
	 * 
	 */
	public void testDelegatedMethods() throws Exception { 
		final Name expectedCompositeName = new CompositeName();
		final String[] expectedAttributeParameters = new String[] { "testOne", "testTwo" };
		final ModificationItem[] expectedItems = new ModificationItem[0];
		final Object expectedBindingValue = new Object();
		final SearchControls expectedSearchControls = new SearchControls();
		final Object[] expectedObjArray = new Object[0];
		// mock setup
		EasyMockSupport mockSupport = new EasyMockSupport();
		DirContext dirContextMock = 
			mockSupport.createMock(DirContext.class);
		DirContext subContextMock = 
			mockSupport.createMock(DirContext.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		Attributes attributesMock = 
			mockSupport.createMock(Attributes.class);
		NamingEnumeration namingEnumMock = 
			mockSupport.createMock(NamingEnumeration.class);
		//setup of expected method calls on internal DirContext
		dirContextMock.bind(expectedCompositeName, expectedBindingValue, attributesMock);
		dirContextMock.bind("test-bind-one", expectedBindingValue, attributesMock);
		expect(dirContextMock.createSubcontext(expectedCompositeName, attributesMock)).andReturn(subContextMock);
		expect(dirContextMock.createSubcontext("create-subcontext-one", attributesMock)).andReturn(subContextMock);
		expect(dirContextMock.getAttributes(expectedCompositeName)).andReturn(attributesMock);
		expect(dirContextMock.getAttributes("get-attributes-one")).andReturn(attributesMock);
		expect(dirContextMock.getAttributes(expectedCompositeName, expectedAttributeParameters)).andReturn(attributesMock);
		expect(dirContextMock.getAttributes("get-attributes-with-params", expectedAttributeParameters)).andReturn(attributesMock);
		expect(dirContextMock.getSchema(expectedCompositeName)).andReturn(subContextMock);
		expect(dirContextMock.getSchema("get-schema")).andReturn(subContextMock);
		expect(dirContextMock.getSchemaClassDefinition(expectedCompositeName)).andReturn(subContextMock);
		expect(dirContextMock.getSchemaClassDefinition("get-schema-class-def")).andReturn(subContextMock);
		dirContextMock.modifyAttributes(expectedCompositeName, expectedItems);
		dirContextMock.modifyAttributes("modify-attributes", expectedItems);
		dirContextMock.modifyAttributes(expectedCompositeName, 10, attributesMock);
		dirContextMock.modifyAttributes("modify-attributes-two", 100, attributesMock);
		dirContextMock.rebind(expectedCompositeName, expectedBindingValue, attributesMock);
		dirContextMock.rebind("rebind-one", expectedBindingValue, attributesMock);
		expect(dirContextMock.search(expectedCompositeName, attributesMock)).andReturn(namingEnumMock);		
		expect(dirContextMock.search("search-one", attributesMock)).andReturn(namingEnumMock);
		expect(dirContextMock.search(expectedCompositeName, attributesMock, expectedAttributeParameters)).andReturn(namingEnumMock);
		expect(dirContextMock.search(expectedCompositeName, "*", expectedSearchControls)).andReturn(namingEnumMock);
		expect(dirContextMock.search("search-with-params", attributesMock, expectedAttributeParameters)).andReturn(namingEnumMock);
		expect(dirContextMock.search("search-with-controls", "*", expectedSearchControls)).andReturn(namingEnumMock);
		expect(dirContextMock.search(expectedCompositeName, "*", expectedObjArray, expectedSearchControls)).andReturn(namingEnumMock);
		expect(dirContextMock.search("search-with-controls-and-filter", "*", expectedObjArray, expectedSearchControls)).andReturn(namingEnumMock);
		
		
		mockSupport.replayAll();
		
		// begin test
		DirContext testDirContext = 
			new DirContextWrapperImpl(dirContextMock, factoryManagerMock);
		
		testDirContext.bind(expectedCompositeName, expectedBindingValue, attributesMock);
		testDirContext.bind("test-bind-one", expectedBindingValue, attributesMock);
		assertSame("DirContextWrapperImpl did not return expected subContext",
				   subContextMock, testDirContext.createSubcontext(expectedCompositeName, attributesMock));
		assertSame("DirContextWrapperImpl did not return expected subContext",
				   subContextMock, testDirContext.createSubcontext("create-subcontext-one", attributesMock));
		assertSame("DirContextWrapperImpl did not return expected Attributes",
				   attributesMock, testDirContext.getAttributes(expectedCompositeName));
		assertSame("DirContextWrapperImpl did not return expected Attributes",
				   attributesMock, testDirContext.getAttributes("get-attributes-one"));
		assertSame("DirContextWrapperImpl did not return expected Attributes",
				   attributesMock, testDirContext.getAttributes(expectedCompositeName, expectedAttributeParameters));
		assertSame("DirContextWrapperImpl did not return expected Attributes",
				   attributesMock, testDirContext.getAttributes("get-attributes-with-params", expectedAttributeParameters));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   subContextMock, testDirContext.getSchema(expectedCompositeName));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   subContextMock, testDirContext.getSchema("get-schema"));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   subContextMock, testDirContext.getSchemaClassDefinition(expectedCompositeName));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   subContextMock, testDirContext.getSchemaClassDefinition("get-schema-class-def"));
		testDirContext.modifyAttributes(expectedCompositeName, expectedItems);
		testDirContext.modifyAttributes("modify-attributes", expectedItems);
		testDirContext.modifyAttributes(expectedCompositeName, 10, attributesMock);
		testDirContext.modifyAttributes("modify-attributes-two", 100, attributesMock);
		testDirContext.rebind(expectedCompositeName, expectedBindingValue, attributesMock);
		testDirContext.rebind("rebind-one", expectedBindingValue, attributesMock);
		
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search(expectedCompositeName, attributesMock));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search("search-one", attributesMock));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search(expectedCompositeName, attributesMock, expectedAttributeParameters));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search(expectedCompositeName, "*", expectedSearchControls));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search("search-with-params", attributesMock, expectedAttributeParameters));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search("search-with-controls", "*", expectedSearchControls));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search(expectedCompositeName, "*", expectedObjArray, expectedSearchControls));
		assertSame("DirContextWrapperImpl did not properly call on internal DirContext impl",
				   namingEnumMock, testDirContext.search("search-with-controls-and-filter", "*", expectedObjArray, expectedSearchControls));
		
		mockSupport.verifyAll();
	}

}

