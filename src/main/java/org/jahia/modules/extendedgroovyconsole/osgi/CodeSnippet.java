package org.jahia.modules.extendedgroovyconsole.osgi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CodeSnippet extends CodeSkeleton {

    private static final Logger logger = LoggerFactory.getLogger(CodeSnippet.class);

    private final String imports;

    public CodeSnippet(String label, String imports, String code) {
        super(label, code);
        this.imports = imports;
    }

    public String getImports() {
        return imports;
    }
}
