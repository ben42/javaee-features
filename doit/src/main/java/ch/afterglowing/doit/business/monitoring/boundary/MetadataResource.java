package ch.afterglowing.doit.business.monitoring.boundary;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Created by ben on 09.10.15.
 */
@WebServlet(urlPatterns = "api/metadata")
public class MetadataResource extends HttpServlet {

    @Inject
    ServletContext servletContext;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println(getMetadata());
        }
    }

    public String getMetadata() {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try {
            Map<String, String> stringStringMap = readMetadataFromManifest("/META-INF/MANIFEST.MF");
            stringStringMap.forEach((key, value) -> objectBuilder.add(key, value));
        } catch (IOException e) {
            System.err.println("Could not read metadata from MANIFEST.MF: " + e.getMessage());
        }

        return objectBuilder.build().toString();
    }

    public Map<String, String> readMetadataFromManifest(String path) throws IOException {
        Map<String, String> result = new HashMap<>();
        try (InputStream is = servletContext.getResourceAsStream(path)) {
            if (is != null) {
                Manifest manifest = new Manifest(is);
                Attributes attributes = manifest.getMainAttributes();
                if (attributes != null) {
                    attributes.forEach((key, value) -> result.put(key.toString(), value.toString()));
                }
            }
        }
        return result;
    }
}
