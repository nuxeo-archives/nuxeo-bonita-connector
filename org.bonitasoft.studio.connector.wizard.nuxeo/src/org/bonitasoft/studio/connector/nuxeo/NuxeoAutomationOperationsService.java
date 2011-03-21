/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 */
package org.bonitasoft.studio.connector.nuxeo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bonitasoft.studio.connector.wizard.nuxeo.Activator;
import org.eclipse.core.runtime.Status;
import org.nuxeo.ecm.automation.client.jaxrs.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.jaxrs.model.OperationDocumentation;

@SuppressWarnings("restriction")
public class NuxeoAutomationOperationsService {

    private static String automationUrl;

    private static String user;

    private static String password;

    private static HttpAutomationClient client;

    private static Map<String, OperationDocumentation> operations;

    public static Session openRemoteConnection() throws Exception {
        client = new HttpAutomationClient(automationUrl);
        Session session;
        try {
            session = client.getSession(user, password);
        } catch (Exception e) {
            Activator.getDefault().getLog().log(
                    new Status(Status.ERROR, Activator.PLUGIN_ID,
                            e.getMessage(), e));
            throw new Exception(e);
        }
        return session;
    }

    public static Map<String, OperationDocumentation> getOperationsList() {
        try {
            Session session = openRemoteConnection();
            operations = session.getOperations();
            return operations;
        } catch (Exception e) {
            Activator.getDefault().getLog().log(
                    new Status(Status.ERROR, Activator.PLUGIN_ID,
                            e.getMessage(), e));
        }
        return new HashMap<String, OperationDocumentation>();
    }

    public static String getAutomationUrl() {
        return automationUrl;
    }

    public static void setAutomationUrl(String automationUrl) {
        NuxeoAutomationOperationsService.automationUrl = automationUrl;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        NuxeoAutomationOperationsService.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        NuxeoAutomationOperationsService.password = password;
    }

    public static String[] getOperationsInputTypes(String operationName) {
        List<String> acceptedInputs = new ArrayList<String>();
        List<String> signature = Arrays.asList(operations.get(operationName).getSignature());
        for (int i = 0; i < signature.size(); i++) {
            if (i % 2 == 0) {
                acceptedInputs.add(signature.get(i));
            }
        }
        return acceptedInputs.toArray(new String[0]);
    }

}
