# Nuxeo Bonita Connector

This addon provides a Bonita connector (http://www.bonitasoft.com/products/BPM_download.php) that allows to invoke an Automation Operation on a Nuxeo server.

Here is the list of  modules you can find inside this addon:

* nuxeo-bonita-connector: (maven project) this module contains the generic connector definition which allows invoking an operation on the server

*org.bonitasoft.studio.connector.wizard.nuxeo (Eclipe plugin): contains the wizard allowing to configure the operation to be invoked on the server

## How to build

Using maven 2.2.1 or later, from root of the `nuxeo-bonita-connector'  folder:

    $ mvn clean install

This will build  the nuxeo-bonita-connector-1.0-SNAPSHOT.jar

    $    run mvn assembly:assembly in the nuxeo-bonita-connector folder
This will build an nuxeo-bonita-connector-1.0-SNAPSHOT-connector.zip

Unzip this and copy the nuxeo folder in  $BONITA-HOME/studio/workspace/local_default_My Extensions/provided-libs.
Then export  org.bonitasoft.studio.connector.wizard.nuxeo as a deployable plugin form Eclipse into $BONITA-HOME/studio


## Licence

This is an addon for Bonita Open Solution and  therefore is distributed under the same license,  GNU General Public License .
 
## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>

