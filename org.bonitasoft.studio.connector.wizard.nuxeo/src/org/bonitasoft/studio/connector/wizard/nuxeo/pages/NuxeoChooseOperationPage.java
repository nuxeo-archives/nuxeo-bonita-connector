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
package org.bonitasoft.studio.connector.wizard.nuxeo.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bonitasoft.studio.connector.nuxeo.NuxeoAutomationOperationsService;
import org.bonitasoft.studio.connector.wizard.nuxeo.operation.model.NuxeoOperationDataDescriptor;
import org.bonitasoft.studio.connectors.wizards.DefaultGeneratedConnectorWizardPage;
import org.bonitasoft.studio.connectors.wizards.DefaultSetConnectorFieldsWizard;
import org.bonitasoft.studio.connectors.wizards.extensions.ICustomExtensionWizardPage;
import org.bonitasoft.studio.groovy.providers.ITextOrDataFactory;
import org.bonitasoft.studio.model.process.Connector;
import org.bonitasoft.studio.model.process.Element;
import org.bonitasoft.studio.repository.connectors.ConnectorRepository;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.nuxeo.ecm.automation.client.jaxrs.model.DocRef;
import org.ow2.bonita.connector.core.ConnectorDescription;
import org.ow2.bonita.connector.core.desc.Array;
import org.ow2.bonita.connector.core.desc.Setter;
import org.ow2.bonita.connector.core.desc.Text;
import org.ow2.bonita.connector.core.desc.Widget;

public class NuxeoChooseOperationPage extends
        DefaultGeneratedConnectorWizardPage implements
        ICustomExtensionWizardPage {

    private static final String SET_OPERATION_NAME = "setOperationId"; //$NON-NLS-1$

    private static final String SET_INPUT_TYPE = "setInputType";

    private static final String SET_INPUT = "setOperationInput";

    private static final GridData WIDGET_GRID_DATA = new GridData(SWT.FILL,
            SWT.TOP, true, false);

    private List<Setter> inputs;

    private Composite opInputTypeComposite;

    private Composite opInputComposite;

    private Composite parentLayer;

    private GridLayout parentLayout;

    private StackLayout stackLayout;

    private Combo opInputTypeCombo;

    private Control opInputDataControl;

    public NuxeoChooseOperationPage() {
        super(NuxeoChooseOperationPage.class.getName());
        requiredFields = new ArrayList<IRequirement>();
    }

    public NuxeoChooseOperationPage(DefaultSetConnectorFieldsWizard wizard,
            String pageId, ConnectorDescription connectorDesc,
            Connector connector) {
        super(wizard, pageId, connectorDesc, connector);
    }

    public NuxeoChooseOperationPage(String wizardPageName) {
        super(wizardPageName);
        requiredFields = new ArrayList<IRequirement>();
    }

    @Override
    public void setConnectorId(String connectorId) {
        this.setTitle("Choose operationId");
        this.setDescription("Nuxeo description page");
        ConnectorDescription connectorDesc = ConnectorRepository.getInstance().getConnectorAPI().getConnector(
                connectorId);
        this.connector = connectorDesc;
    }

    @Override
    public void setModelConnector(Connector modelConnector) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setModelContainer(Element modelContainer) {
        this.container = modelContainer;
    }

    @Override
    public void setSetConnectorFieldsWizard(
            DefaultSetConnectorFieldsWizard wizard) {
        this.setWizard(wizard);
    }

    @Override
    public void setTextOrDataFactory(ITextOrDataFactory textOrDataFactory) {
        this.textOrDataFactory = textOrDataFactory;
    }

    @Override
    protected void createMainCompositeContent() {
        inputs = connector.getInputs();
        currentParameters = ((DefaultSetConnectorFieldsWizard) getWizard()).getInputParameters();

        parentLayer = new Composite(mainComposite, SWT.NONE);
        parentLayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                4, 1));
        parentLayout = new GridLayout(1, false);
        parentLayer.setLayout(parentLayout);

        opInputTypeComposite = new Composite(parentLayer, SWT.BORDER_DASH);
        opInputTypeComposite.setLayout(new GridLayout(2, false));
        opInputTypeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, true));
        createOperationNameWidget(getSetter(SET_OPERATION_NAME),
                opInputTypeComposite);
        createChooseInputTypeWidget(new String[0], getSetter(SET_INPUT_TYPE),
                opInputTypeComposite);

        stackLayout = new StackLayout();
        opInputComposite = new Composite(parentLayer, SWT.BORDER_DASH);
        opInputComposite.setLayout(stackLayout);
        opInputComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true));

    }

    private Setter getSetter(String setterName) {
        for (Setter setter : inputs) {
            if (setter.getSetterName().equals(setterName))
                return setter;
        }
        return null;
    }

    private void createOperationNameWidget(final Setter setter,
            Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("Operation label");

        final Combo typeCombo = new Combo(composite, SWT.READ_ONLY);
        typeCombo.setLayoutData(WIDGET_GRID_DATA);

        Set<String> opIds = NuxeoAutomationOperationsService.getOperationsList().keySet();
        for (String string : opIds) {
            typeCombo.add(string);
        }
        typeCombo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                setParamValue(setter, typeCombo.getText());
                updateOperationInputTypeWidget(typeCombo.getText());
                setPageComplete(allRequiredWidgetsSet());
            }
        });
        if (currentParameters.get(setter.getSetterName()) != null) {
            typeCombo.setText(currentParameters.get(setter.getSetterName()).toString());
        } else if (setter.getParameters().length > 0) {
            typeCombo.setText(setter.getParameters()[0].toString());
        }
    }

    private void updateOperationInputTypeWidget(String opName) {
        String[] opSignature = NuxeoAutomationOperationsService.getOperationsInputTypes(opName);
        updateChooseInputTypeWidget(opSignature);

    }

    private void updateChooseInputTypeWidget(String[] inputTypes) {
        if (opInputTypeCombo == null) {
            return;
        }
        opInputTypeCombo.removeAll();
        for (String string : inputTypes) {
            opInputTypeCombo.add(string);
        }
        opInputTypeCombo.layout(true);
        opInputTypeComposite.layout(true);
    }

    private void createChooseInputTypeWidget(String[] inputTypes,
            final Setter setter, final Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("Operation input type");
        opInputTypeCombo = new Combo(composite, SWT.READ_ONLY);
        opInputTypeCombo.setLayoutData(WIDGET_GRID_DATA);
        for (String string : inputTypes) {
            opInputTypeCombo.add(string);
        }
        opInputTypeCombo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                setParamValue(setter, opInputTypeCombo.getText());
                updateInputDataWidget(opInputTypeCombo.getText(),
                        getSetter(SET_INPUT));
                setPageComplete(allRequiredWidgetsSet());
            }
        });
        if (currentParameters.get(setter.getSetterName()) != null) {
            opInputTypeCombo.setText(currentParameters.get(
                    setter.getSetterName()).toString());
        } else if (setter.getParameters().length > 0) {
            opInputTypeCombo.setText(setter.getParameters()[0].toString());
        }
    }

    // TODO : change implementation;
    // use dummy widgets to call createSWTWidget
    private void updateInputDataWidget(String opInputType, final Setter setter) {
        if (opInputType == null || "".equals(opInputType)) {
            return;
        }

        Widget widget = NuxeoOperationDataDescriptor.inputTypeToWidget(
                opInputType, setter);
        if (widget == null) {
            return;
        }
        opInputDataControl = createInputOpSWTWidget(widget, opInputComposite);
        if (currentParameters.get(setter.getSetterName()) != null) {
            opInputDataControl.setData(currentParameters.get(
                    setter.getSetterName()).toString());
        } else if (setter.getParameters().length > 0) {
            opInputDataControl.setData(setter.getParameters()[0].toString());
        }
        stackLayout.topControl = opInputDataControl;
        opInputComposite.layout(true);
    }

    protected Control createInputOpSWTWidget(final Widget widget,
            Composite composite) {
        for (Control c : composite.getChildren()) {
            c.dispose();
        }
        composite.layout(true);
        createSWTWidget(widget, composite);
        return composite.getChildren()[0];
    }
}