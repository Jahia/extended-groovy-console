/*
 * ==========================================================================================
 * =                   JAHIA'S DUAL LICENSING - IMPORTANT INFORMATION                       =
 * ==========================================================================================
 *
 *                                 http://www.jahia.com
 *
 *     Copyright (C) 2002-2019 Jahia Solutions Group SA. All rights reserved.
 *
 *     THIS FILE IS AVAILABLE UNDER TWO DIFFERENT LICENSES:
 *     1/GPL OR 2/JSEL
 *
 *     1/ GPL
 *     ==================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE GPL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *     2/ JSEL - Commercial and Supported Versions of the program
 *     ===================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE JSEL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     Alternatively, commercial and supported versions of the program - also known as
 *     Enterprise Distributions - must be used in accordance with the terms and conditions
 *     contained in a separate written agreement between you and Jahia Solutions Group SA.
 *
 *     If you are unsure which license is appropriate for your use,
 *     please contact the sales department at sales@jahia.com.
 */
package org.jahia.modules.extendedgroovyconsole.taglibs;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jahia.api.Constants;
import org.jahia.data.templates.JahiaTemplatesPackage;
import org.jahia.modules.extendedgroovyconsole.osgi.CodeSkeleton;
import org.jahia.modules.extendedgroovyconsole.osgi.CodeSkeletonsLocator;
import org.jahia.osgi.BundleResource;
import org.jahia.registries.ServicesRegistry;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.nodetypes.initializers.ChoiceListInitializer;
import org.jahia.services.content.nodetypes.initializers.ChoiceListInitializerService;
import org.jahia.services.content.nodetypes.initializers.ChoiceListValue;
import org.jahia.settings.SettingsBean;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for the Groovy Console.
 */
public class GroovyConsoleHelper {

    public static final String WARN_MSG = "WARNING: You are about to execute a script, which can manipulate the repository data or execute services in Jahia. Are you sure, you want to continue?";
    private static final Logger logger = LoggerFactory.getLogger(GroovyConsoleHelper.class);
    private static final Map<String, String> ramScripts = new HashMap<>();
    public static final String RAM_SCRIPT_URI_PREFIX = "ramScript://";
    public static final String JAVA_COMMENTS_PREFIX = "//";
    public static final String SCRIPT_CONFIGURATIONS_SECTION_BEGIN = JAVA_COMMENTS_PREFIX + " Script configurations";

    public static StringBuilder generateScriptSkeleton() {

        final StringBuilder code = new StringBuilder(16398);
        code.append("import com.google.common.collect.*\n");
        code.append("import com.google.common.io.*\n");
        code.append("import com.sun.enterprise.web.connector.grizzly.comet.*\n");
        code.append("import com.sun.grizzly.comet.*\n");
        code.append("import com.sun.grizzly.tcp.*\n");
        code.append("import com.sun.grizzly.websockets.*\n");
        code.append("import com.sun.image.codec.jpeg.*\n");
        code.append("import com.sun.medialib.mlib.*\n");
        code.append("import com.sun.net.httpserver.*\n");
        code.append("import com.sun.syndication.feed.synd.*\n");
        code.append("import com.sun.syndication.fetcher.*\n");
        code.append("import com.sun.syndication.fetcher.impl.*\n");
        code.append("import com.sun.syndication.io.*\n");
        code.append("import eu.infomas.annotation.*\n");
        code.append("import groovy.lang.*\n");
        code.append("import groovy.util.*\n");
        code.append("import groovy.util.slurpersupport.*\n");
        code.append("import groovy.xml.*\n");
        code.append("import javax.annotation.security.*\n");
        code.append("import javax.ejb.*\n");
        code.append("import javax.enterprise.context.*\n");
        code.append("import javax.enterprise.context.spi.*\n");
        code.append("import javax.enterprise.event.*\n");
        code.append("import javax.enterprise.inject.*\n");
        code.append("import javax.enterprise.inject.spi.*\n");
        code.append("import javax.enterprise.util.*\n");
        code.append("import javax.inject.*\n");
        code.append("import javax.interceptor.*\n");
        code.append("import javax.jcr.*\n");
        code.append("import javax.jcr.nodetype.*\n");
        code.append("import javax.jcr.observation.*\n");
        code.append("import javax.jcr.query.*\n");
        code.append("import javax.jcr.query.qom.*\n");
        code.append("import javax.jcr.version.*\n");
        code.append("import javax.mail.*\n");
        code.append("import javax.mail.internet.*\n");
        code.append("import javax.mail.util.*\n");
        code.append("import javax.persistence.*\n");
        code.append("import javax.servlet.*\n");
        code.append("import javax.servlet.annotation.*\n");
        code.append("import javax.servlet.http.*\n");
        code.append("import javax.servlet.resources.*\n");
        code.append("import javax.validation.*\n");
        code.append("import name.fraser.neil.plaintext.*\n");
        code.append("import net.htmlparser.jericho.*\n");
        code.append("import nu.xom.*\n");
        code.append("import oauth.signpost.*\n");
        code.append("import oauth.signpost.basic.*\n");
        code.append("import oauth.signpost.commonshttp.*\n");
        code.append("import oauth.signpost.exception.*\n");
        code.append("import oauth.signpost.http.*\n");
        code.append("import oracle.xml.parser.*\n");
        code.append("import oracle.xml.parser.v2.*\n");
        code.append("import org.aopalliance.aop.*\n");
        code.append("import org.aopalliance.intercept.*\n");
        code.append("import org.apache.camel.*\n");
        code.append("import org.apache.camel.builder.*\n");
        code.append("import org.apache.camel.component.mail.*\n");
        code.append("import org.apache.camel.impl.*\n");
        code.append("import org.apache.camel.model.*\n");
        code.append("import org.apache.camel.spring.*\n");
        code.append("import org.apache.camel.util.*\n");
        code.append("import org.apache.catalina.connector.*\n");
        code.append("import org.apache.catalina.util.*\n");
        code.append("import org.apache.catalina.websocket.*\n");
        code.append("import org.apache.commons.beanutils.*\n");
        code.append("import org.apache.commons.codec.binary.*\n");
        code.append("import org.apache.commons.codec.digest.*\n");
        code.append("import org.apache.commons.collections.*\n");
        code.append("import org.apache.commons.collections.iterators.*\n");
        code.append("import org.apache.commons.collections.keyvalue.*\n");
        code.append("import org.apache.commons.collections.list.*\n");
        code.append("import org.apache.commons.collections.map.*\n");
        code.append("import org.apache.commons.httpclient.*\n");
        code.append("import org.apache.commons.httpclient.auth.*\n");
        code.append("import org.apache.commons.httpclient.methods.*\n");
        code.append("import org.apache.commons.httpclient.methods.multipart.*\n");
        code.append("import org.apache.commons.httpclient.params.*\n");
        code.append("import org.apache.commons.httpclient.protocol.*\n");
        code.append("import org.apache.commons.id.*\n");
        code.append("import org.apache.commons.lang.*\n");
        code.append("import org.apache.commons.lang.builder.*\n");
        code.append("import org.apache.commons.lang.exception.*\n");
        code.append("import org.apache.commons.lang.math.*\n");
        code.append("import org.apache.commons.lang.time.*\n");
        code.append("import org.apache.commons.logging.*\n");
        code.append("import org.apache.coyote.http11.upgrade.*\n");
        code.append("import org.apache.jackrabbit.commons.query.*\n");
        code.append("import org.apache.jackrabbit.util.*\n");
        code.append("import org.apache.jackrabbit.value.*\n");
        code.append("import org.apache.log4j.*\n");
        code.append("import org.apache.oro.text.regex.*\n");
        code.append("import org.apache.pdfbox.pdmodel.*\n");
        code.append("import org.apache.pluto.container.*\n");
        code.append("import org.apache.regexp.*\n");
        code.append("import org.apache.solr.client.solrj.response.*\n");
        code.append("import org.apache.tika.io.*\n");
        code.append("import org.apache.tomcat.util.http.mapper.*\n");
        code.append("import org.apache.tools.ant.*\n");
        code.append("import org.apache.velocity.tools.generic.*\n");
        code.append("import org.apache.xerces.dom.*\n");
        code.append("import org.apache.xerces.jaxp.*\n");
        code.append("import org.apache.xerces.parsers.*\n");
        code.append("import org.artofsolving.jodconverter.document.*\n");
        code.append("import org.artofsolving.jodconverter.office.*\n");
        code.append("import org.codehaus.groovy.runtime.*\n");
        code.append("import org.codehaus.groovy.runtime.typehandling.*\n");
        code.append("import org.cyberneko.html.parsers.*\n");
        code.append("import org.dom4j.*\n");
        code.append("import org.dom4j.io.*\n");
        code.append("import org.dom4j.tree.*\n");
        code.append("import org.drools.*\n");
        code.append("import org.drools.spi.*\n");
        code.append("import org.drools.util.*\n");
        code.append("import org.eclipse.jetty.continuation.*\n");
        code.append("import org.eclipse.jetty.websocket.*\n");
        code.append("import org.glassfish.grizzly.*\n");
        code.append("import org.glassfish.grizzly.comet.*\n");
        code.append("import org.glassfish.grizzly.filterchain.*\n");
        code.append("import org.glassfish.grizzly.http.*\n");
        code.append("import org.glassfish.grizzly.http.server.*\n");
        code.append("import org.glassfish.grizzly.http.server.util.*\n");
        code.append("import org.glassfish.grizzly.http.util.*\n");
        code.append("import org.glassfish.grizzly.servlet.*\n");
        code.append("import org.glassfish.grizzly.utils.*\n");
        code.append("import org.glassfish.grizzly.websockets.*\n");
        code.append("import org.hibernate.*\n");
        code.append("import org.hibernate.cfg.*\n");
        code.append("import org.hibernate.classic.*\n");
        code.append("import org.hibernate.criterion.*\n");
        code.append("import org.jahia.admin.*\n");
        code.append("import org.jahia.admin.sites.*\n");
        code.append("import org.jahia.ajax.gwt.client.widget.contentengine.*\n");
        code.append("import org.jahia.ajax.gwt.client.widget.edit.sidepanel.*\n");
        code.append("import org.jahia.ajax.gwt.client.widget.publication.*\n");
        code.append("import org.jahia.ajax.gwt.client.widget.subscription.*\n");
        code.append("import org.jahia.ajax.gwt.client.widget.toolbar.action.*\n");
        code.append("import org.jahia.ajax.gwt.helper.*\n");
        code.append("import org.jahia.ajax.gwt.utils.*\n");
        code.append("import org.jahia.api.*\n");
        code.append("import org.jahia.bin.*\n");
        code.append("import org.jahia.bin.errors.*\n");
        code.append("import org.jahia.data.*\n");
        code.append("import org.jahia.data.applications.*\n");
        code.append("import org.jahia.data.beans.portlets.*\n");
        code.append("import org.jahia.data.templates.*\n");
        code.append("import org.jahia.data.viewhelper.principal.*\n");
        code.append("import org.jahia.defaults.config.spring.*\n");
        code.append("import org.jahia.engines.*\n");
        code.append("import org.jahia.exceptions.*\n");
        code.append("import org.jahia.modules.visibility.rules.*\n");
        code.append("import org.jahia.params.*\n");
        code.append("import org.jahia.params.valves.*\n");
        code.append("import org.jahia.pipelines.*\n");
        code.append("import org.jahia.pipelines.valves.*\n");
        code.append("import org.jahia.registries.*\n");
        code.append("import org.jahia.security.license.*\n");
        code.append("import org.jahia.services.*\n");
        code.append("import org.jahia.services.applications.*\n");
        code.append("import org.jahia.services.atmosphere.*\n");
        code.append("import org.jahia.services.cache.*\n");
        code.append("import org.jahia.services.channels.*\n");
        code.append("import org.jahia.services.channels.providers.*\n");
        code.append("import org.jahia.services.content.*\n");
        code.append("import org.jahia.services.content.decorator.*\n");
        code.append("import org.jahia.services.content.nodetypes.*\n");
        code.append("import org.jahia.services.content.nodetypes.initializers.*\n");
        code.append("import org.jahia.services.content.nodetypes.renderer.*\n");
        code.append("import org.jahia.services.content.rules.*\n");
        code.append("import org.jahia.services.image.*\n");
        code.append("import org.jahia.services.importexport.*\n");
        code.append("import org.jahia.services.logging.*\n");
        code.append("import org.jahia.services.mail.*\n");
        code.append("import org.jahia.services.notification.*\n");
        code.append("import org.jahia.services.preferences.user.*\n");
        code.append("import org.jahia.services.pwdpolicy.*\n");
        code.append("import org.jahia.services.query.*\n");
        code.append("import org.jahia.services.render.*\n");
        code.append("import org.jahia.services.render.filter.*\n");
        code.append("import org.jahia.services.render.filter.cache.*\n");
        code.append("import org.jahia.services.render.scripting.*\n");
        code.append("import org.jahia.services.scheduler.*\n");
        code.append("import org.jahia.services.search.*\n");
        code.append("import org.jahia.services.seo.*\n");
        code.append("import org.jahia.services.seo.jcr.*\n");
        code.append("import org.jahia.services.seo.urlrewrite.*\n");
        code.append("import org.jahia.services.sites.*\n");
        code.append("import org.jahia.services.tags.*\n");
        code.append("import org.jahia.services.tasks.*\n");
        code.append("import org.jahia.services.templates.*\n");
        code.append("import org.jahia.services.transform.*\n");
        code.append("import org.jahia.services.translation.*\n");
        code.append("import org.jahia.services.uicomponents.bean.*\n");
        code.append("import org.jahia.services.uicomponents.bean.contentmanager.*\n");
        code.append("import org.jahia.services.uicomponents.bean.editmode.*\n");
        code.append("import org.jahia.services.uicomponents.bean.toolbar.*\n");
        code.append("import org.jahia.services.usermanager.*\n");
        code.append("import org.jahia.services.usermanager.jcr.*\n");
        code.append("import org.jahia.services.visibility.*\n");
        code.append("import org.jahia.services.workflow.*\n");
        code.append("import org.jahia.settings.*\n");
        code.append("import org.jahia.tools.files.*\n");
        code.append("import org.jahia.tools.jvm.*\n");
        code.append("import org.jahia.utils.*\n");
        code.append("import org.jahia.utils.comparator.*\n");
        code.append("import org.jahia.utils.i18n.*\n");
        code.append("import org.jahia.utils.zip.*\n");
        code.append("import org.jaxen.*\n");
        code.append("import org.jaxen.jdom.*\n");
        code.append("import org.jbpm.api.activity.*\n");
        code.append("import org.jbpm.api.model.*\n");
        code.append("import org.jbpm.api.task.*\n");
        code.append("import org.joda.time.*\n");
        code.append("import org.joda.time.format.*\n");
        code.append("import org.mortbay.util.ajax.*\n");
        code.append("import org.quartz.*\n");
        code.append("import org.springframework.aop.*\n");
        code.append("import org.springframework.aop.framework.*\n");
        code.append("import org.springframework.aop.support.*\n");
        code.append("import org.springframework.beans.*\n");
        code.append("import org.springframework.beans.factory.*\n");
        code.append("import org.springframework.beans.factory.annotation.*\n");
        code.append("import org.springframework.beans.factory.config.*\n");
        code.append("import org.springframework.beans.factory.support.*\n");
        code.append("import org.springframework.beans.factory.xml.*\n");
        code.append("import org.springframework.beans.propertyeditors.*\n");
        code.append("import org.springframework.context.*\n");
        code.append("import org.springframework.context.event.*\n");
        code.append("import org.springframework.context.support.*\n");
        code.append("import org.springframework.core.*\n");
        code.append("import org.springframework.core.enums.*\n");
        code.append("import org.springframework.core.io.*\n");
        code.append("import org.springframework.core.io.support.*\n");
        code.append("import org.springframework.dao.*\n");
        code.append("import org.springframework.jdbc.core.*\n");
        code.append("import org.springframework.orm.*\n");
        code.append("import org.springframework.orm.hibernate3.*\n");
        code.append("import org.springframework.orm.hibernate3.annotation.*\n");
        code.append("import org.springframework.orm.hibernate3.support.*\n");
        code.append("import org.springframework.scheduling.quartz.*\n");
        code.append("import org.springframework.ui.context.*\n");
        code.append("import org.springframework.ui.context.support.*\n");
        code.append("import org.springframework.util.*\n");
        code.append("import org.springframework.util.xml.*\n");
        code.append("import org.springframework.web.context.*\n");
        code.append("import org.springframework.web.context.support.*\n");
        code.append("import org.springframework.web.servlet.*\n");
        code.append("import org.springframework.web.servlet.mvc.*\n");
        code.append("import org.springframework.webflow.core.collection.*\n");
        code.append("import sun.awt.image.*\n");
        code.append("import sun.awt.image.codec.*\n");
        code.append("import sun.security.action.*\n");
        code.append("import ucar.nc2.util.net.*\n");
        code.append("\n");
        return code;
    }

    /**
     * Returns a collection of BundleResource, representing scripts, which are found in all active module bundles.
     *
     * @return a collection of BundleResource, representing scripts, which are found in all active module bundles
     */
    public static Map<String, Collection<GroovyScriptWrapper>> getGroovyConsoleScripts() {
        final Map<String, Collection<GroovyScriptWrapper>> scripts = new TreeMap<>();
        for (final JahiaTemplatesPackage aPackage : ServicesRegistry.getInstance().getJahiaTemplateManagerService()
                .getAvailableTemplatePackages()) {
            final Bundle bundle = aPackage.getBundle();
            if (bundle != null) {
                final TreeSet<GroovyScriptWrapper> bundleScripts = new TreeSet<>(Comparator.comparing(GroovyScriptWrapper::getFilename));
                scanGroovyConsoleScripts(bundle, "groovyConsole", bundleScripts);
                scanGroovyConsoleScripts(bundle, "extendedGroovyConsole", bundleScripts);
                if (CollectionUtils.isNotEmpty(bundleScripts)) {
                    scripts.put(String.format("%s (%s)", bundle.getSymbolicName(), bundle.getVersion()), bundleScripts);
                }
            }
        }
        if (!ramScripts.isEmpty()) {
            final Collection<GroovyScriptWrapper> ramScriptsList = ramScripts.keySet().stream().map(key -> new GroovyScriptWrapper(RAM_SCRIPT_URI_PREFIX + key, key)).collect(Collectors.toList());
            scripts.put("RAM scripts", ramScriptsList);
        }
        return scripts;
    }

    private static void scanGroovyConsoleScripts(Bundle bundle, String folder, Collection<GroovyScriptWrapper> scripts) {
        final Enumeration<URL> resourceEnum = bundle.findEntries(String.format("META-INF/%s", folder), "*.groovy", false);
        if (resourceEnum == null)
            return;
        while (resourceEnum.hasMoreElements()) {
            final BundleResource bundleResource = new BundleResource(resourceEnum.nextElement(), bundle);
            try {
                final Properties configurations = getScriptConfigurations(bundleResource.getURI().toString());
                final String visibilityKey = "script.visibilityCondition";
                final boolean scriptVisible;
                if (configurations == null || !configurations.containsKey(visibilityKey)) {
                    scriptVisible = true;
                } else {
                    final String visibilityCondition = configurations.getProperty(visibilityKey);
                    final Matcher matcher = Pattern.compile("\\{(.*)=(.*)}").matcher(visibilityCondition);
                    if (matcher.matches()) {
                        final String property = getProperty(matcher.group(1));
                        scriptVisible = StringUtils.equals(property, matcher.group(2));
                    } else {
                        scriptVisible = Boolean.parseBoolean(getProperty(configurations.getProperty(visibilityKey)));
                    }
                }
                if (scriptVisible)
                    scripts.add(new GroovyScriptWrapper(bundleResource.getURI().toString(), bundleResource.getFilename()));
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public static String getGroovyConsoleScript(String uri) {
        try {
            if (StringUtils.startsWith(uri, RAM_SCRIPT_URI_PREFIX)) {
                final String content = ramScripts.get(uri.substring(RAM_SCRIPT_URI_PREFIX.length()));
                if (StringUtils.startsWith(content, "localfs:")) {
                    final File file = new File(content.trim().substring("localfs:".length()));
                    return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                }
                return content;
            } else {
                return getContent(new UrlResource(uri), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            logger.error("Impossible to load the script", e);
            return null;
        }
    }

    public static String getContent(Resource resource, Charset charset) throws IOException {
        String content = null;
        InputStream is = null;
        try {
            is = resource.getInputStream();
            content = IOUtils.toString(is, charset);
        } finally {
            IOUtils.closeQuietly(is);
        }

        return content;
    }

    private static String getProperty(String name) {
        final String property = SettingsBean.getInstance().getPropertiesFile().getProperty(name);
        return property != null ? property : System.getProperty(name);
    }

    private static Properties getScriptConfigurations(String scriptURI) {
        if (StringUtils.isBlank(scriptURI)) {
            return null;
        }

        final Properties inlineConfigurations = getScriptInlineConfigurations(scriptURI);
        if (inlineConfigurations != null) return inlineConfigurations;

        if (StringUtils.startsWith(scriptURI, RAM_SCRIPT_URI_PREFIX)) return null;

        try {
            final UrlResource resource = new UrlResource(
                    String.format("%s.properties", StringUtils.substringBeforeLast(scriptURI, ".groovy")));
            if (!resource.exists()) return null;
            final Properties confs = new Properties();
            confs.load(resource.getInputStream());
            return confs;
        } catch (IOException ioe) {
            logger.error("An error occured while reading the configurations for the script " + scriptURI, ioe);
        }
        return null;
    }

    private static Properties getScriptInlineConfigurations(String scriptURI) {
        final String script = getGroovyConsoleScript(scriptURI);
        if (StringUtils.isBlank(script)) return null;

        final Properties properties = new Properties();
        boolean isConfigSection = false;
        for (String line : script.split("\n")) {
            if (isConfigSection) {
                if (StringUtils.startsWith(line.trim(), JAVA_COMMENTS_PREFIX)) {
                    final String[] prop = StringUtils.split(line.trim().substring(2), "=", 2);
                    if (prop.length != 2) continue;
                    properties.put(prop[0].trim(), prop[1].trim());
                }
            } else if (StringUtils.startsWith(line.trim(), SCRIPT_CONFIGURATIONS_SECTION_BEGIN)) {
                isConfigSection = true;
            }
        }

        if (properties.isEmpty()) return null;
        return properties;
    }

    /**
     * Returns a generated HTML with form elements for the script parameters.
     *
     * @param scriptURI
     * @param request
     * @return
     */
    public static String getScriptCustomFormElements(String scriptURI, HttpServletRequest request) {
        if (StringUtils.isBlank(scriptURI)) {
            return StringUtils.EMPTY;
        }

        final Properties confs = getScriptConfigurations(scriptURI);
        if (confs == null) {
            return StringUtils.EMPTY;
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("<fieldset><legend>Script configuration</legend><div class=\"scriptConfig\">");
        final String title = confs.getProperty("script.title");
        if (StringUtils.isNotBlank(title))
            sb.append("<h2>").append(title).append("</h2>");
        final String desc = confs.getProperty("script.description");
        if (StringUtils.isNotBlank(desc))
            sb.append("<div class=\"description\">").append(desc).append("</div>");
        final String[] paramNames = StringUtils
                .split(confs.getProperty("script.parameters.names", "").replaceAll("\\s", ""), ",");
        if (ArrayUtils.isNotEmpty(paramNames)) {
            final String width = confs.getProperty("script.parameters.display.width", "400px");
            sb.append("<table><colgroup><col width=\"").append(width).append("\"/><col/></colgroup>");
            for (String paramName : paramNames) {
                final StringBuilder label = new StringBuilder();
                final StringBuilder input = new StringBuilder();
                if (generateFormElement(paramName.trim(), input, confs, request)) {
                    generateFormElementLabel(paramName.trim(), label, confs);
                    sb.append("<tr><td>").append(label.toString()).append("</td><td>").append(input.toString()).append("</td></tr>");
                }
            }
            sb.append("</table>");
        }
        sb.append("</div></fieldset>");
        return sb.toString();
    }

    /**
     * Returns an array of parameter names for the specified script or <code>null</code> if the script has no parameters.
     *
     * @param scriptURI the script URI to get parameter names for
     * @return a Map where the keys are the parameter names for the specified script, and the values their related type, or <code>null</code> if the script has no parameters
     */
    public static Map<String,String> getScriptParamNames(String scriptURI) {
        final Properties confs = getScriptConfigurations(scriptURI);
        if (confs == null) return null;
        return Arrays.stream(StringUtils.split(confs.getProperty("script.parameters.names", "").replaceAll("\\s", ""), ","))
                .collect(Collectors.toMap(name -> name, name -> confs.getProperty(String.format("script.param.%s.type", name), "checkbox")));
    }

    private static boolean generateFormElement(String paramName, StringBuilder input, Properties confs,
                                               HttpServletRequest request) {
        final String paramType = confs.getProperty(String.format("script.param.%s.type", paramName), "checkbox").trim();
        if ("checkbox".equals(paramType)) {
            generateCbFormElement(paramName, input, confs, request);
        } else if ("text".equals(paramType)) {
            generateTextFormElement(paramName, input, confs, request);
        } else if ("textarea".equals(paramType)) {
            generateTextareaFormElement(paramName, input, confs, request);
        } else if ("choicelist".equalsIgnoreCase(paramType)) {
            generateChoiceListFormElement(paramName, input, confs, request);
        } else {
            logger.error(
                    String.format("Unsupported form element type for the parameter %s: %s", paramName, paramType));
            return false;
        }
        return true;
    }

    private static void generateFormElementLabel(String paramName, StringBuilder sb, Properties confs) {
        sb.append("<label for=\"scriptParam_").append(paramName).append("\">");
        sb.append(confs.getProperty(String.format("script.param.%s.label", paramName), paramName)).append("</label> ");
    }

    private static void generateCbFormElement(String paramName, StringBuilder sb, Properties confs,
                                              HttpServletRequest request) {
        sb.append("<input type=\"checkbox\" name=\"scriptParam_").append(paramName).append("\" id=\"scriptParam_")
                .append(paramName);
        final String paramVal = getElementDefaultValue(paramName, confs, request);
        if (StringUtils.isNotBlank(paramVal)
                && ("on".equalsIgnoreCase(paramVal.trim()) || "true".equalsIgnoreCase(paramVal.trim())))
            sb.append("\" checked=\"true");
        sb.append("\" />");
    }

    private static void generateTextFormElement(String paramName, StringBuilder sb, Properties confs,
                                                HttpServletRequest request) {
        sb.append("<input type=\"text\" ");
        sb.append("name=\"scriptParam_").append(paramName).append("\" id=\"scriptParam_").append(paramName);
        final String paramVal = getElementDefaultValue(paramName, confs, request);
        if (StringUtils.isNotBlank(paramVal))
            sb.append("\" value=\"").append(paramVal);
        sb.append("\" />");
    }

    private static void generateTextareaFormElement(String paramName, StringBuilder sb, Properties confs,
                                                    HttpServletRequest request) {
        sb.append("<textarea ");
        sb.append("name=\"scriptParam_").append(paramName).append("\" id=\"scriptParam_").append(paramName).append("\">");
        final String paramVal = getElementDefaultValue(paramName, confs, request);
        if (StringUtils.isNotBlank(paramVal))
            sb.append(paramVal);
        sb.append("</textarea>");
    }

    /*
    private static void generateSimpleFormElement(String markupName, boolean withBody, String additionalAttributes,
                                                  String paramName, StringBuilder sb, Properties confs,
                                                  HttpServletRequest request) {
        sb.append("<").append(markupName);
        if (StringUtils.isNotBlank(additionalAttributes)) sb.append(" ").append(additionalAttributes.trim());
        sb.append(" name=\"scriptParam_").append(paramName).append("\" id=\"scriptParam_").append(paramName);
        final String paramVal = getElementDefaultValue(paramName, confs, request);
        if (StringUtils.isNotBlank(paramVal))
            sb.append("\" value=\"").append(paramVal);
        if (withBody)
            sb.append("\"></").append(markupName).append(">");
        else
            sb.append("\" />");
    }
    */

    private static void generateChoiceListFormElement(String paramName, StringBuilder sb, Properties confs,
                                                      HttpServletRequest request) {
        sb.append("<select name=\"scriptParam_").append(paramName).append("\" id=\"scriptParam_")
                .append(paramName).append("\">");
        final String paramVal = getElementDefaultValue(paramName, confs, request);
        final String values = confs.getProperty(String.format("script.param.%s.values", paramName));
        generateStaticOptions(values, paramVal, sb);
        final String dynamicvalues = confs.getProperty(String.format("script.param.%s.dynamicvalues", paramName));
        generateDynamicOptions(dynamicvalues, paramVal, sb);
        sb.append("</select>");
    }

    private static void generateStaticOptions(String values, String currentValue, StringBuilder sb) {
        if (StringUtils.isBlank(values)) return;

        for (String v : StringUtils.split(values, ',')) {
            final String[] split = StringUtils.split(v, ":", 2);
            final String label = split.length > 1 ? split[1] : split[0];
            final String value = split[0].trim();
            sb.append("<option value=\"").append(value);
            if (StringUtils.equals(value, currentValue))
                sb.append("\" selected=\"selected");
            sb.append("\">").append(label.trim()).append("</option>");
        }
    }

    private static void generateDynamicOptions(String values, String currentValue, StringBuilder sb) {
        if (StringUtils.isBlank(values)) return;

        final Map<String, ChoiceListInitializer> initializerMap = ChoiceListInitializerService.getInstance().getInitializers();
        final String[] initializers = StringUtils.split(values, ',');
        List<ChoiceListValue> clValues = null;
        for (String initializer : initializers) {
            final String[] parts = StringUtils.split(initializer, "='");
            final ChoiceListInitializer choiceListInitializer = initializerMap.get(parts[0].trim());
            if (choiceListInitializer == null) continue;
            final String param = parts.length > 1 ? parts[1].trim() : null;
            final Map<String, Object> context = new HashMap<String, Object>();
            try {
                final JCRNodeWrapper systemsite = JCRSessionFactory.getInstance().getCurrentSystemSession(Constants.EDIT_WORKSPACE, null, null).getNode("/sites/systemsite");
                context.put("contextParent", systemsite);
                clValues = choiceListInitializer.getChoiceListValues(null, param, clValues, Locale.ENGLISH, context);
            } catch (RepositoryException e) {
                logger.error("", e);
            }
        }
        for (ChoiceListValue clValue : clValues) {
            try {
                final String value = clValue.getValue().getString().trim();
                sb.append("<option value=\"").append(value);
                if (StringUtils.equals(value, currentValue))
                    sb.append("\" selected=\"selected");
                sb.append("\">").append(clValue.getDisplayName()).append("</option>");
            } catch (RepositoryException e) {
                logger.error("", e);
            }
        }
    }

    public static Collection<CodeSkeleton> getCodeSkeletons() {
        return CodeSkeletonsLocator.getSkeletons();
    }

    public static Collection<CodeSkeleton> getCodeSnippets() {
        return CodeSkeletonsLocator.getSnippets();
    }

    private static String getElementDefaultValue(String paramName, Properties confs, HttpServletRequest request) {
        final String keepValueAfterSubmit = confs.getProperty(String.format("script.param.%s.keepValueAfterSubmit", paramName));
        final boolean resetValue = Boolean.FALSE.toString().equalsIgnoreCase(StringUtils.trim(keepValueAfterSubmit));
        if (!resetValue && "true".equals(request.getParameter("runScript"))) {
            return request.getParameter("scriptParam_" + paramName);
        }
        return confs.getProperty(String.format("script.param.%s.default", paramName), "").trim();
    }

    public static String saveRamScript(HttpServletRequest request) {
        final String scriptID = request.getParameter("ramScriptID");
        if (StringUtils.isBlank(scriptID)) return "The script ID is missing";
        final String scriptContent = request.getParameter("script");
        if (StringUtils.isBlank(scriptContent)) return "The script is empty";

        if (ramScripts.put(scriptID, scriptContent) == null) {
            logger.info("Saved the script [{}] in memory", scriptID);
        } else {
            logger.info("Updated the script [{}] in memory", scriptID);
        }

        return StringUtils.EMPTY;
    }
}
