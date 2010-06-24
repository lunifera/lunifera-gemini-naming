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

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

class DirContextWrapperImpl extends ContextWrapperImpl implements DirContext {

	private final DirContext m_dirContext;
	
	DirContextWrapperImpl(DirContext dirContext, FactoryManager factoryManager) {
		super(dirContext, factoryManager);
		m_dirContext = dirContext;
	}
	
	public void bind(String name, Object obj, Attributes attributes)
			throws NamingException {
		m_dirContext.bind(name, obj, attributes);

	}

	public void bind(Name name, Object obj, Attributes attributes)
			throws NamingException {
		m_dirContext.bind(name, obj, attributes);
	}

	public DirContext createSubcontext(String name, Attributes attributes)
			throws NamingException {
		return m_dirContext.createSubcontext(name, attributes);
	}

	public DirContext createSubcontext(Name name, Attributes attributes)
			throws NamingException {
		return m_dirContext.createSubcontext(name, attributes);
	}

	public Attributes getAttributes(String name) throws NamingException {
		return m_dirContext.getAttributes(name);
	}

	public Attributes getAttributes(Name name) throws NamingException {
		return m_dirContext.getAttributes(name);
	}

	public Attributes getAttributes(String name, String[] values)
			throws NamingException {
		return m_dirContext.getAttributes(name, values);
	}

	public Attributes getAttributes(Name name, String[] values)
			throws NamingException {
		return m_dirContext.getAttributes(name, values);
	}

	public DirContext getSchema(String name) throws NamingException {
		return m_dirContext.getSchema(name);
	}

	public DirContext getSchema(Name name) throws NamingException {
		return m_dirContext.getSchema(name);
	}

	public DirContext getSchemaClassDefinition(String name)
			throws NamingException {
		return m_dirContext.getSchemaClassDefinition(name);
	}

	public DirContext getSchemaClassDefinition(Name name)
			throws NamingException {
		return m_dirContext.getSchemaClassDefinition(name);
	}

	public void modifyAttributes(String name, ModificationItem[] values)
			throws NamingException {
		m_dirContext.modifyAttributes(name, values);
	}

	public void modifyAttributes(Name name, ModificationItem[] values)
			throws NamingException {
		m_dirContext.modifyAttributes(name, values);
	}

	public void modifyAttributes(String name, int index, Attributes attributes)
			throws NamingException {
		m_dirContext.modifyAttributes(name, index, attributes);
	}

	public void modifyAttributes(Name name, int index, Attributes attributes)
			throws NamingException {
		m_dirContext.modifyAttributes(name, index, attributes);
	}

	public void rebind(String name, Object obj, Attributes attributes)
			throws NamingException {
		m_dirContext.rebind(name, obj, attributes);
	}

	public void rebind(Name name, Object obj, Attributes attributes)
			throws NamingException {
		m_dirContext.rebind(name, obj, attributes);
	}

	public NamingEnumeration search(String name, Attributes attributes)
			throws NamingException {
		return m_dirContext.search(name, attributes);
	}

	public NamingEnumeration search(Name name, Attributes attributes)
			throws NamingException {
		return m_dirContext.search(name, attributes);
	}

	public NamingEnumeration search(String name, String filter, SearchControls searchControls) throws NamingException {
		return m_dirContext.search(name, filter, searchControls);
	}

	public NamingEnumeration search(String name, Attributes attributes, String[] attributesToReturn)
			throws NamingException {
		return m_dirContext.search(name, attributes, attributesToReturn);
	}

	public NamingEnumeration search(Name name, String filter, SearchControls searchControls)
			throws NamingException {
		return m_dirContext.search(name, filter, searchControls);
	}

	public NamingEnumeration search(Name name, Attributes attributes, String[] attributesToReturn) throws NamingException {
		return m_dirContext.search(name, attributes, attributesToReturn);
	}

	public NamingEnumeration search(String name, String filter, Object[] filterArgs, SearchControls searchControls) throws NamingException {
		return m_dirContext.search(name, filter, filterArgs, searchControls);
	}

	public NamingEnumeration search(Name name, String filter, Object[] filterArgs, SearchControls searchControls) throws NamingException {
		return m_dirContext.search(name, filter, filterArgs, searchControls);
	}

}
