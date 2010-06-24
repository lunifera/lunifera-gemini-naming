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
 *     Bob Nettleton - Initial Developer tests for Reference Implementation
 ******************************************************************************/


/**
 * This class is a common base class for the developer tests for the Gemini 
 * Naming project.  
 */

package org.eclipse.gemini.naming.test;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

public abstract class NamingTestCase extends AbstractConfigurableBundleCreatorTests {

	private Map m_mapOfServicesToRegistrations = 
		new HashMap();

	protected void onSetUp() throws Exception {
		m_mapOfServicesToRegistrations = new HashMap();
	}
	
	protected void onTearDown() throws Exception {
		super.onTearDown();
        unregisterAllServices();
	}
	
	protected void registerService(String serviceType, Object service, Dictionary properties) {
    	ServiceRegistration registration = 
    		bundleContext.registerService(serviceType, service, properties);
    	m_mapOfServicesToRegistrations.put(service, registration);
    	
    }
    
    protected void unregisterService(Object service) {
    	if(m_mapOfServicesToRegistrations.containsKey(service)) {
    		ServiceRegistration registration = 
    			(ServiceRegistration)m_mapOfServicesToRegistrations.get(service);
    		registration.unregister();
    	}
    }
    
    protected void unregisterAllServices() {
    	Set keySet = m_mapOfServicesToRegistrations.keySet();
    	Iterator iterator = keySet.iterator();
    	while(iterator.hasNext()) {
    		unregisterService(iterator.next());
    	}
    }
    
    protected BundleContext getContext() {
   	  	return bundleContext;
    }
	
}
