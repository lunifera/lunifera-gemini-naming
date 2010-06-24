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
import javax.naming.directory.DirContext;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;

import org.easymock.EasyMockSupport;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;



public class InitialContextFactoryWrapperTestCase extends TestCase {

	public void testCreateWrapper() throws Exception {
		// setup mocks
		EasyMockSupport mockSupport = new EasyMockSupport();
		InitialContextFactory factoryMock = 
			mockSupport.createMock(InitialContextFactory.class);
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		
		mockSupport.replayAll();
		
		// create wrapper
		new InitialContextFactoryWrapper(factoryMock, factoryManagerMock);
		
		mockSupport.verifyAll();
	}
	
	public void testCreateContextDefault() throws Exception {
		// setup mocks
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		
		InitialContextFactory factoryMock = 
			mockSupport.createMock(InitialContextFactory.class);
		expect(factoryMock.getInitialContext(new Hashtable<String, Object>())).andReturn(contextMock);
		
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		factoryManagerMock.associateFactoryService(same(factoryMock), isA(Context.class));
		
		mockSupport.replayAll();
		
		// create wrapper
		InitialContextFactoryWrapper wrapper = 
			new InitialContextFactoryWrapper(factoryMock, factoryManagerMock);
		
		Context resultContext = 
			wrapper.getInitialContext(new Hashtable<String, Object>());
		assertNotNull("Wrapper returned a null Context", resultContext);
		
		mockSupport.verifyAll();
	}
	
	public void testCreateDirContextDefault() throws Exception {
		// setup mocks
		EasyMockSupport mockSupport = new EasyMockSupport();
		DirContext dirContextMock = 
			mockSupport.createMock(DirContext.class);
		
		InitialContextFactory factoryMock = 
			mockSupport.createMock(InitialContextFactory.class);
		expect(factoryMock.getInitialContext(new Hashtable<String, Object>())).andReturn(dirContextMock);
		
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		factoryManagerMock.associateFactoryService(same(factoryMock), isA(Context.class));
		
		mockSupport.replayAll();
		
		// create wrapper
		InitialContextFactoryWrapper wrapper = 
			new InitialContextFactoryWrapper(factoryMock, factoryManagerMock);
		
		DirContext resultContext = 
			(DirContext)wrapper.getInitialContext(new Hashtable<String, Object>());
		assertNotNull("Wrapper returned a null Context", resultContext);

		mockSupport.verifyAll();
	}
	
	public void testCreateBuilderSupportedInitialContextFactory() throws Exception {
		// setup mocks
		EasyMockSupport mockSupport = new EasyMockSupport();
		Context contextMock = 
			mockSupport.createMock(Context.class);
		
		InitialContextFactoryBuilder builderMock = 
			mockSupport.createMock(InitialContextFactoryBuilder.class);
		
		BuilderSupportedInitialContextFactory initialContextFactoryMock = 
			mockSupport.createMock(BuilderSupportedInitialContextFactory.class);
		expect(initialContextFactoryMock.getInitialContext(new Hashtable<String, Object>())).andReturn(contextMock);
		expect(initialContextFactoryMock.getBuilder()).andReturn(builderMock);
		
		FactoryManager factoryManagerMock = 
			mockSupport.createMock(FactoryManager.class);
		factoryManagerMock.associateFactoryService(same(builderMock), isA(Context.class));
		
		mockSupport.replayAll();
		
		// create wrapper
		InitialContextFactoryWrapper wrapper = 
			new InitialContextFactoryWrapper(initialContextFactoryMock, factoryManagerMock);
		
		Context resultContext = 
			wrapper.getInitialContext(new Hashtable<String, Object>());
		assertNotNull("Wrapper returned a null Context", resultContext);
		
		mockSupport.verifyAll();
	}
	
	
}
