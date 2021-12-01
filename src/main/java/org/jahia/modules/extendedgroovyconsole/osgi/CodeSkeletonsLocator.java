package org.jahia.modules.extendedgroovyconsole.osgi;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jahia.modules.extendedgroovyconsole.taglibs.GroovyConsoleHelper;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
        scan("groovySnippets", snippets, bundle, new SnippetParserCallback());
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
            final CodeSkeleton code = cb.getCode(bundleResource);
            if (code != null) list.add(code);
        }
    }

    private static class CodeParserCallback {

        public CodeSkeleton getCode(BundleResource br) {
            try {
                final String code = GroovyConsoleHelper.getContent(br, StandardCharsets.UTF_8);
                return new CodeSkeleton(br.getFilename(), code);
            } catch (IOException e) {
                logger.error("", e);
                return null;
            }
        }
    }

    private static class SnippetParserCallback extends CodeParserCallback {
        @Override
        public CodeSkeleton getCode(BundleResource br) {
            InputStream is = null;
            try {
                is = br.getInputStream();
                final List<String> lines = IOUtils.readLines(is, StandardCharsets.UTF_8);
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
    }
}
