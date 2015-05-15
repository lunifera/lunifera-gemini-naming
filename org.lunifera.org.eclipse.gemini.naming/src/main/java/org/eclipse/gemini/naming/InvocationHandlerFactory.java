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

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Factory interface for creating InvocationHandler instances
 * for the purpose of proxying OSGi services.  
 *
 * 
 * @version $Revision$
 */
interface InvocationHandlerFactory {
	/**
	 * Create an InvocationHandler for the specified OSGi service.  
	 * 
	 * @param bundleContext the BundleContext used to obtain the OSGi service
	 * @param serviceReference the ServiceReference that represents the service
	 * @param urlParser the OSGiURLParser associated with this service request
	 * @param osgiService the initial OSGi service to be proxied. 
	 * 
	 * @return an InvocationHandler that can be associated with a dynamic proxy
	 *         for the specified service.  
	 */
	InvocationHandler create(BundleContext bundleContext, ServiceReference serviceReference, OSGiURLParser urlParser, Object osgiService);
}
