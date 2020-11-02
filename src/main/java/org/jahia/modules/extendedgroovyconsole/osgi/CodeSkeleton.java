package org.jahia.modules.extendedgroovyconsole.osgi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeSkeleton {

    private static final Logger logger = LoggerFactory.getLogger(CodeSkeleton.class);

    private final String label;
    private final String code;

    public CodeSkeleton(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }
}
