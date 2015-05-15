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

import javax.naming.Context;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.ObjectFactory;


/**
 * Interface that defines the basic usage of the Factory Manager in Gemini Naming.
 * 
 * The FactoryManager interface can be used to abstract the details of OSGi
 * service access from certain portions of the implementation.
 * 
 */
public interface FactoryManager extends InitialContextFactoryBuilder {

	/**
	 * Returns a javax.naming.spi.ObjectFactory that is published in the OSGi
	 * service registry. The ObjectFactory returned must support the specified
	 * urlScheme.
	 * 
	 * @param urlScheme the requested URL scheme
	 * @return a javax.naming.spi.ObjectFactory that supports the given URL
	 *         scheme
	 */
	public ObjectFactory getURLContextFactory(String urlScheme);
	
	/**
	 * Associates a given OSGi JNDI Factory service to a Context that 
	 * was created with the given service.  
	 * 
	 * @param factory the JNDI factory service used to create the Context
	 * @param createdContext the Context created with this factory service  
	 */
	public void associateFactoryService(Object factory, Context createdContext);
	
	
	/**
	 * Checks to see if a given OSGi JNDI Factory Service is still active in 
	 * the service registry.  
	 * @param factory the JNDI Factory service
	 * @return true if the service is still available
	 *         false if the service is no longer available
	 */
	public boolean isFactoryServiceActive(Object factory);
}
