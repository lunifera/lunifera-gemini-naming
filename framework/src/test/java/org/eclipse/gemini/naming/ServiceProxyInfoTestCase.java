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

import junit.framework.TestCase;

public class ServiceProxyInfoTestCase extends TestCase {
	
	/**
	 * Verifies basic creation of this datatype. 
	 */
	public void testCreate() throws Exception {
		final Object testService = new Object();
		final InvocationHandler testHandler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				return null;
			}
		};
		
		ServiceProxyInfo proxyInfo = 
			new ServiceProxyInfo(testService, testHandler, true);
		
		assertSame("ServiceProxyInfo did not return the correct service",
				   testService, proxyInfo.getService());
		assertSame("ServiceProxyInfo did not return the correct handler",
				   testHandler, proxyInfo.getHandler());
		assertTrue("ServiceProxyInfo did not return the correct proxy status", 
				   proxyInfo.isProxied());
		
		ServiceProxyInfo anotherProxyInfo = 
			new ServiceProxyInfo(testService, testHandler, false);
		assertSame("ServiceProxyInfo did not return the correct service",
				   testService, anotherProxyInfo.getService());
		assertSame("ServiceProxyInfo did not return the correct handler",
				   testHandler, anotherProxyInfo.getHandler());
		assertFalse("ServiceProxyInfo did not return the correct proxy status", 
				    anotherProxyInfo.isProxied());
		
	}
}
