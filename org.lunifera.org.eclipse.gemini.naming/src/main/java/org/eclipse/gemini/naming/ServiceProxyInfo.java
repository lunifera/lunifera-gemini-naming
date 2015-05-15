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
 *     Bob Nettleton (Oracle) - Initial Reference Implementation
 ******************************************************************************/  

package org.eclipse.gemini.naming;

import java.lang.reflect.InvocationHandler;

/**
 * Data class to hold the information on a dynamic proxy 
 * for a given OSGi service, including whether the proxy 
 * was actually created.  
 *
 * 
 * @version $Revision$
 */
class ServiceProxyInfo {
	final Object m_service;
	final InvocationHandler m_handler;
	final boolean m_isProxied;
	
	ServiceProxyInfo(Object service, InvocationHandler handler, boolean isProxied) {
		m_service = service;
		m_handler = handler;
		m_isProxied = isProxied;
	}
	
	Object getService() {
		return m_service;
	}
	
	InvocationHandler getHandler() {
		return m_handler;
	}
	
	boolean isProxied() {
		return m_isProxied;
	}
}