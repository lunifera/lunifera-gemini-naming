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

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * Utility class for security-related operations in the Gemini Naming implementation.  
 *
 * 
 * @version $Revision$
 */
class SecurityUtils {

	private SecurityUtils() {
		// construction of this object is not allowed
	}
	
	
	/**
	 * Invokes the specified action in a doPrivileged() block, and 
	 * returns the result.  
	 * 
	 * @param action the PrivilegedExceptionAction to execute
	 * @return the resulting Object of the operation
	 * @throws Exception the exception thrown (if any) by the action itself
	 */
	static Object invokePrivilegedAction(final PrivilegedExceptionAction action) throws Exception {
		try {
			return AccessController.doPrivileged(action);
		}
		catch (PrivilegedActionException e) {
			throw e.getException();
		}
	}
	
	
	/**
	 * Invokes the specified action, which does not require a return
	 * @param action the PrivilegedExceptionAction to execute
	 * @throws Exception the exception thrown (if any) by the action itself
	 */
	static void invokePrivilegedActionNoReturn(final PrivilegedExceptionAction action) throws Exception {
		try {
			AccessController.doPrivileged(action);
		}
		catch (PrivilegedActionException e) {
			throw e.getException();
		}
	}
}
