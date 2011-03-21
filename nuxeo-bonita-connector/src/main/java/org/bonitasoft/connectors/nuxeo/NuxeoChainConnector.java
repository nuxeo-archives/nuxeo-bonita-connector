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

import java.util.Map;

import org.nuxeo.ecm.automation.client.jaxrs.OperationRequest;
import org.nuxeo.ecm.automation.client.jaxrs.Session;

public class NuxeoChainConnector extends AbstractNuxeoProcessConnector {

    protected String chainId;

    protected Object operationOutput;

    protected Map<String, String> params;

    @Override
    public Object processSession(Session session) throws Exception {
        OperationRequest opReq = session.newRequest(chainId);

        // set params
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                opReq.setContextProperty(param.getKey(), param.getValue());
            }
        }

        return opReq.execute();
    }

}
