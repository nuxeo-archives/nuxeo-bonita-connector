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

import org.bonitasoft.studio.connector.nuxeo.NuxeoAutomationOperationsService;
import org.bonitasoft.studio.connector.wizard.sap.i18n.Messages;
import org.bonitasoft.studio.connectors.wizards.DefaultGeneratedConnectorWizardPage;
import org.bonitasoft.studio.connectors.wizards.DefaultSetConnectorFieldsWizard;
import org.bonitasoft.studio.connectors.wizards.extensions.ICustomExtensionWizardPage;
import org.bonitasoft.studio.groovy.providers.ITextOrDataFactory;
import org.bonitasoft.studio.groovy.widgets.TextOrData;
import org.bonitasoft.studio.model.process.Connector;
import org.bonitasoft.studio.model.process.Element;
import org.bonitasoft.studio.pics.Pics;
import org.bonitasoft.studio.repository.connectors.ConnectorRepository;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.nuxeo.ecm.automation.client.jaxrs.Session;
import org.ow2.bonita.connector.core.ConnectorDescription;
import org.ow2.bonita.connector.core.desc.Setter;

public class NuxeoConnectionPage extends DefaultGeneratedConnectorWizardPage
        implements ICustomExtensionWizardPage {

    private static final String SET_USER = "setUserName"; //$NON-NLS-1$

    private static final String SET_PASSWORD = "setUserPassword"; //$NON-NLS-1$

    private static final String SET_HOST = "setUrl"; //$NON-NLS-1$

    private static final GridData WIDGET_GRID_DATA = new GridData(SWT.FILL,
            SWT.TOP, true, false);

    private List<Setter> inputs;

    private Composite parentLayer;

    private Composite configurationComposite;

    private StackLayout parentStackLayout;

    public NuxeoConnectionPage() {
        super(NuxeoConnectionPage.class.getName());
        requiredFields = new ArrayList<IRequirement>();
    }

    public NuxeoConnectionPage(DefaultSetConnectorFieldsWizard wizard,
            String pageId, ConnectorDescription connectorDesc,
            Connector connector) {
        super(wizard, pageId, connectorDesc, connector);
    }

    @Override
    public void setConnectorId(String connectorId) {
        ConnectorDescription connectorDesc = ConnectorRepository.getInstance().getConnectorAPI().getConnector(
                connectorId);
        this.setTitle("Nuxeo connection page");
        this.setDescription("Nuxeo description page");
        this.connector = connectorDesc;
        setImageDescriptor(Pics.getWizban());
    }

    @Override
    public void setModelConnector(Connector arg0) {

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
                2, 1));
        parentStackLayout = new StackLayout();
        parentLayer.setLayout(parentStackLayout);

        configurationComposite = new Composite(parentLayer, SWT.NONE);
        configurationComposite.setLayout(new GridLayout(2, false));
        configurationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, true));

        createUserWidget(getSetter(SET_USER), configurationComposite);
        createPasswordWidget(getSetter(SET_PASSWORD), configurationComposite);
        createHostWidget(getSetter(SET_HOST), configurationComposite);

        parentStackLayout.topControl = configurationComposite;
    }

    private Setter getSetter(String setterName) {
        for (Setter setter : inputs) {
            if (setter.getSetterName().equals(setterName))
                return setter;
        }
        return null;
    }

    private void createUserWidget(final Setter setter, Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.userIdLabel);

        final TextOrData text = new TextOrData(composite, container);
        text.getControl().setLayoutData(WIDGET_GRID_DATA);
        ((Combo) text.getControl()).addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
                setParamValue(setter, text.getText());
                setPageComplete(allRequiredWidgetsSet());
                NuxeoAutomationOperationsService.setUser(text.getText());
            }
        });

        if (currentParameters.get(setter.getSetterName()) != null) {
            text.setText(currentParameters.get(setter.getSetterName()).toString());
        } else if (setter.getParameters().length > 0) {
            text.setText(setter.getParameters()[0].toString());
        }

        if (setter.getParameters().length > 0) {
            ((Combo) text.getControl()).add(setter.getParameters()[0].toString());
        }
    }

    private void createPasswordWidget(final Setter setter, Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.passwordLabel);

        final TextOrData text = new TextOrData(composite, container, true);
        text.getControl().setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, false));

        text.addValueChangedListener(new Listener() {
            public void handleEvent(Event event) {
                setParamValue(setter, text.getText());
                setPageComplete(allRequiredWidgetsSet());
                NuxeoAutomationOperationsService.setPassword(text.getText());
            }
        });

        if (currentParameters.get(setter.getSetterName()) != null) {
            text.setText(currentParameters.get(setter.getSetterName()).toString());
        } else if (setter.getParameters().length > 0) {
            text.setText(setter.getParameters()[0].toString());
        }
    }

    private void createHostWidget(final Setter setter, Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("Server url");

        final TextOrData text = new TextOrData(composite, container);
        text.getControl().setLayoutData(WIDGET_GRID_DATA);

        ((Combo) text.getControl()).addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                setParamValue(setter, text.getText());
                setPageComplete(allRequiredWidgetsSet());
                NuxeoAutomationOperationsService.setAutomationUrl(text.getText());
            }
        });

        if (currentParameters.get(setter.getSetterName()) != null) {
            text.setText(currentParameters.get(setter.getSetterName()).toString());
        } else if (setter.getParameters().length > 0) {
            text.setText(setter.getParameters()[0].toString());
        }

        if (setter.getParameters().length > 0) {
            ((Combo) text.getControl()).add(setter.getParameters()[0].toString());
        }
    }

    @Override
    public boolean canFlipToNextPage() {
        if (!isPageComplete()) {
            return false;
        }
        Session session = null;
        try {
            session = NuxeoAutomationOperationsService.openRemoteConnection();
        } catch (Exception e) {
            return false;
        }
        return session != null;
    }

}
