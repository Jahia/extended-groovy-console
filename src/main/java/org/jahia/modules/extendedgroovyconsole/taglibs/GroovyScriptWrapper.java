package org.jahia.modules.extendedgroovyconsole.taglibs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyScriptWrapper {

    private static final Logger logger = LoggerFactory.getLogger(GroovyScriptWrapper.class);

    private final String uri, filename;

    public GroovyScriptWrapper(String uri, String filename) {
        this.uri = uri;
        this.filename = filename;
    }

    public String getURI() {
        return uri;
    }

    public String getFilename() {
        return filename;
    }
}
