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
import javax.naming.Context;
import javax.naming.OperationNotSupportedException;

import junit.framework.TestCase;

public class NotSupportedContextTestCase extends TestCase {
	
	public void testCreate() throws Exception {
		new NotSupportedContext("just a test");
	}
	
	/**
	 * Verify that all Context methods supported by this class 
	 * throw an OperationNotSupportedException. 
	 */
	public void testMethods() throws Exception {
		final String expectedMessage = "just a test";
		Context context = new NotSupportedContext(expectedMessage);
		
		try {
			context.addToEnvironment("test", "test1");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
					      expectedMessage, namingException.getMessage());
		}
		
		try {
			context.bind(new CompositeName(), "bind test");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
				         expectedMessage, namingException.getMessage());
		}
		
		try {
			context.bind("bind-name", "bind-value");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
				         expectedMessage, namingException.getMessage());
		}
		
		try {
			context.close();
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
				         expectedMessage, namingException.getMessage());
		}
		
		try {
			context.composeName(new CompositeName(), new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
				          expectedMessage, namingException.getMessage());
		}
		
		try {
			context.composeName("name", "prefix");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
				         expectedMessage, namingException.getMessage());
		}
		
		try {
			context.createSubcontext(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
			             expectedMessage, namingException.getMessage());
		}
		
		try {
			context.createSubcontext("name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
			             expectedMessage, namingException.getMessage());
		}
	
		try {
			context.destroySubcontext(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
			             expectedMessage, namingException.getMessage());
		}
		
		try {
			context.destroySubcontext("name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.getEnvironment();
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.getNameInNamespace();
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.getNameParser(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.getNameParser("name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.list(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.list("name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.listBindings(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.listBindings("name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.lookup(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.lookup("name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.lookupLink(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.lookupLink("name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.rebind(new CompositeName(), "just a test");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.rebind("name", "just a rebind test");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.removeFromEnvironment("property-name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.rename(new CompositeName(), new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.rename("old-name", "new-name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.unbind(new CompositeName());
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
		
		try {
			context.unbind("unbind-name");
			fail("OperationNotSupportedException should have been thrown");
		} catch(OperationNotSupportedException namingException) {
			// expected exception
			assertEquals("Context did not include expected message with exception",  
		                 expectedMessage, namingException.getMessage());
		}
	}
}
