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
package org.bonitasoft.studio.connector.wizard.nuxeo.operation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ow2.bonita.connector.core.desc.Array;
import org.ow2.bonita.connector.core.desc.Setter;
import org.ow2.bonita.connector.core.desc.Text;
import org.ow2.bonita.connector.core.desc.Widget;

public class NuxeoOperationDataDescriptor {

    /**
     * The operation input can be a document ( or a list of docs) ; a blob ( or
     * a list of blobs) ; if the input is a document, then the input is actually
     * a docRef or the path of the document
     * */

    public static final Map<String, Class<? extends Widget>> OP_DATA_TYPE_TO_WIDGET = new HashMap<String, Class<? extends Widget>>();
    static {
        // string
        OP_DATA_TYPE_TO_WIDGET.put("document", Text.class);
        // list of strings
        OP_DATA_TYPE_TO_WIDGET.put("documents", Array.class);
        // file upload
        // OP_DATA_TYPE_TO_WIDGET.put("blob", );
        // list of file
        // OP_DATA_TYPE_TO_WIDGET.put("blobs", Composite.class);
    }

    public static Widget inputTypeToWidget(String inputType, Setter setter) {
        Class<?> widgetClass = OP_DATA_TYPE_TO_WIDGET.get(inputType);
        if (widgetClass.equals(Text.class)) {
            return new Text(inputType, setter, 10, 20);
        }
        if (widgetClass.equals(Array.class)) {
            return new Array(inputType, setter, 1, 2, true, false,
                    new ArrayList<String>());
        }
        return null;
    }
}
