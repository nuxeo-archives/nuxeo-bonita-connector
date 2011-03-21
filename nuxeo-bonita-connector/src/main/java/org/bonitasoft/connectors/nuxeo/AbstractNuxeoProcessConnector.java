/**
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.nuxeo;

import java.util.List;

import org.nuxeo.ecm.automation.client.jaxrs.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 *
 *
 */
public abstract class AbstractNuxeoProcessConnector extends ProcessConnector {

    protected String userName;

    protected String userPassword;

    protected String url;

    protected String repositoryName;

    protected Object operationOutput;

    public abstract Object processSession(Session session) throws Exception;

    @Override
    protected void executeConnector() throws Exception {
        HttpAutomationClient client = new HttpAutomationClient(url);
        Session session = client.getSession(userName, userPassword);
        if (repositoryName != null) {
            // TODO: add optional repo conf to the session
        }
        operationOutput = processSession(session);
        client.shutdown();
    }

    @Override
    protected List<ConnectorError> validateValues() {
        throw new UnsupportedOperationException();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public Object getOperationOutput() {
        return operationOutput;
    }

}
