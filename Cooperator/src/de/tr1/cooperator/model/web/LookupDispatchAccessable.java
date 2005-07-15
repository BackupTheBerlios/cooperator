/*
File:		LookupDispatchAccessable.java
Created:	05-06-07@19:30
Task:		This will call the correct validate method for LookupDispatchActions
Author:		Peter Matjeschk

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
/*
 * Based on
 * $Id: LookupDispatchAccessable.java,v 1.1 2005/07/15 18:30:34 bluecobold Exp $
 * $Id: LookupDispatchAccessable.java,v 1.1 2005/07/15 18:30:34 bluecobold Exp $
 * $Id: LookupDispatchAccessable.java,v 1.1 2005/07/15 18:30:34 bluecobold Exp $
 *
 * Copyright 2000-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tr1.cooperator.model.web;

import de.tr1.cooperator.manager.system.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Iterator;


import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ModuleUtils;


/**
 * This class is a VERY DIRTY hack of Action, DispatchAction and LookupDispatchAction
 * it's just copy and paste to provide the same possibiltys to ActionForms as for the Action itself
 */
public abstract class LookupDispatchAccessable extends Accessable
{
    /**
     * The Class instance of this <code>DispatchAction</code> class.
     */
    protected Class clazz = this.getClass();


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
            MessageResources.getMessageResources
            ("org.apache.struts.actions.LocalStrings");


    /**
     * The set of Method objects we have introspected for this class,
     * keyed by method name.  This collection is populated as different
     * methods are called, so that introspection needs to occur only
     * once per method name.
     */
    protected HashMap methods = new HashMap();


    /**
     * Reverse lookup map from resource value to resource key.
     */
    protected Map localeMap = new HashMap();

    /**
     * Resource key to method name lookup.
     */
    protected Map keyMethodMap = null;


    /**
     * The set of argument type classes for the reflected method call.  These
     * are the same for all calls, so calculate them only once.
     */
    protected Class[] types =
            {
                ActionMapping.class,
                HttpServletRequest.class};


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

	        // Identify the request parameter containing the method name
        String parameter = mapping.getParameter();
        if (parameter == null) {
        	return new ActionErrors();
        }

        // Identify the string to lookup
        String methodName = getMethodName(mapping, request, parameter);
		if (methodName == null) {
			return new ActionErrors();
		}

        return dispatchMethod(mapping, request, methodName);
	}


    /**
     * Dispatch to the specified method.
     * @since Struts 1.1
     */
    protected ActionErrors dispatchMethod(ActionMapping mapping,
                                           HttpServletRequest request,
                                           String name) {

        // Make sure we have a valid method name to call.
        // This may be null if the user hacks the query string.
        if (name == null) {
            return this.unspecified(mapping, request);
        }

        // Identify the method object to be dispatched to
        Method method = null;
        try {
            method = getMethod(name);

        } catch(NoSuchMethodException e) {
        	return new ActionErrors();
        }

        ActionErrors error = null;
        try {
            Object args[] = {mapping, request};
            error = (ActionErrors) method.invoke(this, args);

        } catch(ClassCastException e) {
        	return new ActionErrors();
        } catch(IllegalAccessException e) {
        	return new ActionErrors();
        } catch(InvocationTargetException e) {
        	return new ActionErrors();
        }

        // Return the returned ActionForward instance
        return (error);
    }


    /**
     * Introspect the current class to identify a method of the specified
     * name that accepts the same parameter types as the <code>execute</code>
     * method does.
     *
     * @param name Name of the method to be introspected
     *
     * @exception NoSuchMethodException if no such method can be found
     */
    protected Method getMethod(String name)
            throws NoSuchMethodException {

        synchronized(methods) {
            Method method = (Method) methods.get(name);
            if (method == null) {
                method = clazz.getMethod(name, types);
                methods.put(name, method);
            }
            return (method);
        }

    }


    /**
     * Method which is dispatched to when there is no value for specified
     * request parameter included in the request.  Subclasses of
     * <code>DispatchAction</code> should override this method if they wish
     * to provide default behavior different than throwing a ServletException.
     */
    protected ActionErrors unspecified(
            ActionMapping mapping,
            HttpServletRequest request){

        return new ActionErrors();
    }

    /**
     * Returns the method name, given a parameter's value.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @param parameter The <code>ActionMapping</code> parameter's name
     *
     * @return The method's name.
     * @since Struts 1.2.0
     */
    protected String getMethodName(
        ActionMapping mapping,
        HttpServletRequest request,
        String parameter){

        // Identify the method name to be dispatched to.
        // dispatchMethod() will call unspecified() if name is null
        String keyName = request.getParameter(parameter);
        if (keyName == null || keyName.length() == 0) {
            return null;
        }

        String methodName = getLookupMapName(request, keyName, mapping);

        return methodName;
    }

    /**
     * Lookup the method name corresponding to the client request's locale.
     *
     * @param request The HTTP request we are processing
     * @param keyName The parameter name to use as the properties key
     * @param mapping The ActionMapping used to select this instance
     *
     * @return The method's localized name.
     * @throws ServletException if keyName cannot be resolved
     * @since Struts 1.2.0
     */
    protected String getLookupMapName(
        HttpServletRequest request,
        String keyName,
        ActionMapping mapping) {

        // Based on this request's Locale get the lookupMap
        Map lookupMap = null;

        synchronized(localeMap) {
            Locale userLocale = this.getLocale(request);
            lookupMap = (Map) this.localeMap.get(userLocale);

            if (lookupMap == null) {
                lookupMap = this.initLookupMap(request, userLocale);
                this.localeMap.put(userLocale, lookupMap);
            }
        }

        // Find the key for the resource
        String key = (String) lookupMap.get(keyName);
        if (key == null) {

            return null;
        }

        // Find the method name
        String methodName = (String) keyMethodMap.get(key);
        if (methodName == null) {
        	return null;
        }

        return methodName;
    }

    /**
     * <p>Return the user's currently selected Locale.</p>
     *
     * @param request The request we are processing
     */
    protected Locale getLocale(HttpServletRequest request) {

        return RequestUtils.getUserLocale(request, null);

    }

    /**
     * This is the first time this Locale is used so build the reverse lookup Map.
     * Search for message keys in all configured MessageResources for
     * the current module.
     */
    private Map initLookupMap(HttpServletRequest request, Locale userLocale) {
        Map lookupMap = new HashMap();
        this.keyMethodMap = this.getKeyMethodMap();

        ModuleConfig moduleConfig =
                (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);

        MessageResourcesConfig[] mrc = moduleConfig.findMessageResourcesConfigs();

        // Look through all module's MessageResources
        for (int i = 0; i < mrc.length; i++) {
            MessageResources resources = this.getResources(request, mrc[i].getKey());

            // Look for key in MessageResources
            Iterator iter = this.keyMethodMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String text = resources.getMessage(userLocale, key);

                // Found key and haven't added to Map yet, so add the text
                if ((text != null) && !lookupMap.containsKey(text)) {
                    lookupMap.put(text, key);
                }
            }
        }

        return lookupMap;
    }

    /**
     * <p>Return the default message resources for the current module.</p>
     *
     * @param request The servlet request we are processing
     * @since Struts 1.1
     */
    protected MessageResources getResources(HttpServletRequest request) {

        return ((MessageResources) request.getAttribute(Globals.MESSAGES_KEY));

    }
    /**
     * <p>Return the specified message resources for the current module.</p>
     *
     * @param request The servlet request we are processing
     * @param key The key specified in the
     *  <code>&lt;message-resources&gt;</code> element for the
     *  requested bundle
     *
     * @since Struts 1.1
     */
    protected MessageResources getResources(
        HttpServletRequest request,
        String key) {

        // Identify the current module
        ServletContext context = getServlet().getServletContext();
        ModuleConfig moduleConfig =
            ModuleUtils.getInstance().getModuleConfig(request, context);

        // Return the requested message resources instance
        return (MessageResources) context.getAttribute(
            key + moduleConfig.getPrefix());

    }

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected abstract Map getKeyMethodMap();


}