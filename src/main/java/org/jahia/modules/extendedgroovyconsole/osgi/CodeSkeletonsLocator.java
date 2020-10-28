package org.jahia.modules.extendedgroovyconsole.osgi;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jahia.osgi.BundleResource;
import org.jahia.utils.FileUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CodeSkeletonsLocator implements BundleActivator {

    private static final Logger logger = LoggerFactory.getLogger(CodeSkeletonsLocator.class);

    private static final List<CodeSkeleton> skeletons = new ArrayList<>();
    private static final List<CodeSkeleton> snippets = new ArrayList<>();

    public static List<CodeSkeleton> getSkeletons() {
        return skeletons;
    }

    public static List<CodeSkeleton> getSnippets() {
        return snippets;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        final Bundle bundle = context.getBundle();
        scan("groovySkeletons", skeletons, bundle, new CodeParserCallback());
        scan("groovySnippets", snippets, bundle, new CodeParserCallback() {
            @Override
            public CodeSkeleton getCode(BundleResource br) {
                String content = null;
                InputStream is = null;
                try {
                    is = br.getInputStream();
                    final List<String> lines = IOUtils.readLines(is);
                    final StringBuilder imports = new StringBuilder();
                    final StringBuilder code = new StringBuilder();
                    boolean inImports = true;
                    for (String line : lines) {
                        if (inImports) {
                            if (line.trim().startsWith("import ") || StringUtils.isBlank(line)) {
                                imports.append(line).append("\n");
                            } else {
                                inImports = false;
                            }
                        }
                        if (!inImports) {
                            code.append(line).append("\n");
                        }
                    }

                    return new CodeSnippet(br.getFilename(), imports.toString(), code.toString());
                } catch (IOException e) {
                    logger.error("", e);
                } finally {
                    IOUtils.closeQuietly(is);
                }
                return null;
            }
        });
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

    private void scan(String folder, List<CodeSkeleton> list, Bundle bundle, CodeParserCallback cb) {
        final Enumeration<URL> resourceEnum = bundle.findEntries(String.format("tools/%s", folder), "*.groovy", false);
        if (resourceEnum == null)
            return;
        while (resourceEnum.hasMoreElements()) {
            final BundleResource bundleResource = new BundleResource(resourceEnum.nextElement(), bundle);
            list.add(cb.getCode(bundleResource));
        }
    }

    public class CodeSkeleton {

        private String label;
        private String code;

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

    public class CodeSnippet extends CodeSkeleton {

        private String imports;

        public CodeSnippet(String label, String imports, String code) {
            super(label, code);
            this.imports = imports;
        }

        public String getImports() {
            return imports;
        }
    }

    private class CodeParserCallback {

        public CodeSkeleton getCode(BundleResource br) {
            try {
                final String code = FileUtils.getContent(br);
                return new CodeSkeleton(br.getFilename(), code);
            } catch (IOException e) {
                logger.error("", e);
                return null;
            }
        }
    }
}
