package org.jahia.modules.extendedgroovyconsole.osgi;

import org.jahia.osgi.BundleResource;
import org.jahia.utils.FileUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        scan("groovySkeletons", skeletons, bundle);
        scan("groovySnippets", snippets, bundle);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

    private void scan(String folder, List<CodeSkeleton> list, Bundle bundle) {
        final Enumeration<URL> resourceEnum = bundle.findEntries(String.format("tools/%s", folder), "*.groovy", false);
        if (resourceEnum == null)
            return;
        while (resourceEnum.hasMoreElements()) {
            final BundleResource bundleResource = new BundleResource(resourceEnum.nextElement(), bundle);
            try {
                final String code = FileUtils.getContent(bundleResource);
                list.add(new CodeSkeleton(bundleResource.getFilename(), code));
            } catch (IOException e) {
                logger.error("", e);
            }
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
}
