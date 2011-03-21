package org.bonitasoft.studio.connector.nuxeo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.nuxeo.ecm.automation.client.jaxrs.model.OperationDocumentation;
import org.nuxeo.ecm.automation.client.jaxrs.spi.JsonMarshalling;

public class NuxeoAutomationOperationRegistry {

	private static final int BUFFER_SIZE = 1024 * 64; // 64K
	private static final int MAX_BUFFER_SIZE = 1024 * 1024; // 64K
	private static final int MIN_BUFFER_SIZE = 1024 * 8; // 64K

	public static Map<String, OperationDocumentation> getOperationsList() {
		try {
			return JsonMarshalling.readRegistry(
					getFilePathFromUrl(Activator.getContext().getBundle().getResource("/automation")))
					.getOperations();
		} catch (Exception e) {
			return null;
		}
	}


	public static String getFilePathFromUrl(URL url) {
		String path = "";
		if (url.getProtocol().equals("file")) {
			try {
				path = URLDecoder.decode(url.getPath(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return path;
	}

	public static String readFile(File file) throws FileNotFoundException,
			IOException {
		return read(new FileInputStream(file));
	}

	private static String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] buffer = createBuffer(in.available());
		try {
			int read;
			while ((read = in.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, read));
			}
		} finally {
			in.close();
		}
		return sb.toString();
	}

	private static byte[] createBuffer(int preferredSize) {
		if (preferredSize < 1) {
			preferredSize = BUFFER_SIZE;
		}
		if (preferredSize > MAX_BUFFER_SIZE) {
			preferredSize = MAX_BUFFER_SIZE;
		} else if (preferredSize < MIN_BUFFER_SIZE) {
			preferredSize = MIN_BUFFER_SIZE;
		}
		return new byte[preferredSize];
	}

}
