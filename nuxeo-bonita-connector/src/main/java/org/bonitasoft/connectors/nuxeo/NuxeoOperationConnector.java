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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.automation.client.jaxrs.OperationRequest;
import org.nuxeo.ecm.automation.client.jaxrs.Session;
import org.nuxeo.ecm.automation.client.jaxrs.model.OperationInput;
import org.nuxeo.ecm.automation.client.jaxrs.model.PathRef;
import org.nuxeo.ecm.automation.client.jaxrs.model.DocRef;
import org.nuxeo.ecm.automation.client.jaxrs.model.DocRefs;
import org.ow2.bonita.connector.core.ConnectorError;

public class NuxeoOperationConnector extends AbstractNuxeoProcessConnector {

    protected OperationInput operationInput;

    protected String inputType;

    protected String operationId;

    protected Map<String, Object> params;

    @Override
    public Object processSession(Session session) throws Exception {
        OperationRequest opReq = session.newRequest(operationId);

        if (!"void".equals(inputType)) {
            opReq.setInput(operationInput);
        }

        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                opReq.set(param.getKey(), param.getValue());
            }
        }
        return opReq.execute();
    }

    public void setOperationInput(OperationInput operationInput) {
        this.operationInput = operationInput;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**
     * Add methods to match all possibe input types: document - string
     * documents- string list
     * 
     * blob - file blobs - file list
     * */
    public void setOperationInput(String docRef) {
        setOperationInput(new DocRef(docRef));
    }

    public void setOperationInput(List<List<String>> documents) {
        DocRefs docRefs = new DocRefs();
        for (int i = 0; i < documents.size(); i++) {
            String string = documents.get(0).get(0);
            if (string != null && !string.equals("")) {
                docRefs.add(new DocRef(string));
            }
        }
        setOperationInput(docRefs);
    }

    public void setOperationInput(File filename) {
        throw new UnsupportedOperationException();
    }

    // TODO; implement this
    @Override
    protected List<ConnectorError> validateValues() {
        return null;
    }

}