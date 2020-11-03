<%@ page contentType="text/html; charset=UTF-8" language="java"
%><?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ page import="org.jahia.utils.ScriptEngineUtils" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="javax.script.*" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="org.jahia.services.scheduler.JSR223ScriptJob" %>
<%@ page import="org.jahia.services.scheduler.BackgroundJob" %>
<%@ page import="org.quartz.JobDetail" %>
<%@ page import="org.quartz.JobDataMap" %>
<%@ page import="org.jahia.registries.ServicesRegistry" %>
<%@ page import="org.quartz.SchedulerException" %>
<%@ page import="org.springframework.core.io.UrlResource" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.jahia.modules.extendedgroovyconsole.LoggerWrapper" %>
<%@ page import="org.jahia.modules.extendedgroovyconsole.taglibs.GroovyConsoleHelper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="tools" uri="http://www.jahia.org/tags/tools/extendedgroovyconsole" %>
<c:set var="pageTitle" value="Extended Groovy Console" />
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>${pageTitle}</title>
    <%@ include file="css.jspf" %>
    <link type="text/css" href="<c:url value='/modules/assets/css/jquery.fancybox.css'/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value='/modules/assets/css/jquery-ui.smoothness.css'/>" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/modules/jquery/javascript/jquery.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/modules/assets/javascript/jquery.fancybox.pack.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/modules/assets/javascript/jquery-ui.min.js'/>"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".codeAreaContent").accordion({
                collapsible: true,
                active: false,
                heightStyle: 'content'
            });
            $("#snippetsAreaWrapper").hide();
            $('a.fancybox-link').fancybox({
                'hideOnContentClick': false,
                'titleShow': false,
                'transitionOut': 'none'
            });
        });

        function injectSkeleton(id) {
            document.getElementById('text').value = document.getElementById(id).innerText;
            $.fancybox.close()
        }

        function copySnippet(id) {
            let copyText = document.getElementById(id);
            navigator.clipboard.writeText(copyText.innerText);
            $.fancybox.close();
        }
    </script>
</head>
<body>
<h1>${pageTitle}&nbsp;<a class="fancybox-link" title="Help" href="#helpArea"><img
        src="<c:url value='/icons/help.png'/>" width="16" height="16" alt="help" title="Help"></a></h1>
<%
    long timer = System.currentTimeMillis();
    ScriptEngine engine = null;
    try {
        engine = ScriptEngineUtils.getInstance().scriptEngine("groovy");
%>
<c:if test="${not empty param.scriptURI && param.scriptURI != 'custom'}">
    <%
        pageContext.setAttribute("scriptContent", org.jahia.utils.FileUtils.getContent(new UrlResource(request.getParameter("scriptURI"))));
    %>
</c:if>
<c:if test="${not empty param.runScript and param.runScript eq 'true'}">
    <%
        final StringBuilder code = GroovyConsoleHelper.generateScriptSkeleton();

        /* TODO QUARTZ
        boolean executeInBackground = request.getParameter("background") != null;
        if (executeInBackground) {
            code.append("\n");
            code.append("def log = ").append(LoggerFactory.class.getName()).append(".getLogger(\"org.jahia.tools.groovyConsole\");\n");
            code.append("def logger = log;\n");
            code.append("\n");
        }
         */

        final String scriptURL = request.getParameter("scriptURI");
        boolean isPredefinedScript = false;
        if (StringUtils.isBlank(scriptURL) || "custom".equals(scriptURL)) {
            code.append(request.getParameter("script"));
        } else {
            code.append((String) pageContext.getAttribute("scriptContent"));
            isPredefinedScript = true;
        }
        /* TODO QUARTZ
        if (executeInBackground) {
            File groovyConsole = File.createTempFile("groovyConsole", ".groovy");
            FileUtils.write(groovyConsole, code);
            JobDetail jahiaJob = BackgroundJob.createJahiaJob("Groovy console script", JSR223ScriptJob.class);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(JSR223ScriptJob.JOB_SCRIPT_ABSOLUTE_PATH, groovyConsole.getAbsolutePath());
            jobDataMap.put("userkey","root");
            jahiaJob.setJobDataMap(jobDataMap);
            try {
                ServicesRegistry.getInstance().getSchedulerService().scheduleJobNow(jahiaJob);
            } catch (SchedulerException e) {
                pageContext.setAttribute("error",e);
            }
            pageContext.setAttribute("result", "Being executed in background look at your console");
            pageContext.setAttribute("took", System.currentTimeMillis() - timer);
        } else {

         */
            ScriptContext ctx = new SimpleScriptContext();
            ctx.setWriter(new StringWriter());
            Bindings bindings = engine.createBindings();
            LoggerWrapper lw = new LoggerWrapper(LoggerFactory.getLogger("org.jahia.tools.extendedgroovyConsole"),
                    "org.jahia.tools.groovyConsole", ctx.getWriter());
            bindings.put("log", lw);
            bindings.put("logger", lw);
            if (isPredefinedScript) {
                final String[] paramNames = GroovyConsoleHelper.getScriptParamNames(scriptURL);
                if (paramNames != null)
                    for (String paramName : paramNames) {
                        bindings.put(paramName, request.getParameter("scriptParam_" + paramName));
                    }
            }
            ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
            Object result = engine.eval(code.toString(), ctx);
            pageContext.setAttribute("result", result == null ? ((StringWriter) ctx.getWriter()).getBuffer().toString() : result);
            pageContext.setAttribute("took", System.currentTimeMillis() - timer);
        //}
    %>
    <fieldset>
        <legend style="color: blue">Successfully executed in ${took} ms</legend>
        <p><strong>Result:</strong><br/>
        <pre>${not empty result ? fn:escapeXml(result) : '<empty>'}</pre>
        </p>
    </fieldset>
</c:if>
<%
} catch (ScriptException e) {
    if (e instanceof ScriptException && e.getMessage() != null && e.getMessage().startsWith(
            "Script engine not found for extension")) {
%><p>Groovy engine is not available.</p><%
} else {
    Throwable ex = e.getCause() != null ? e.getCause() : e;
    if (ex instanceof ScriptException && e.getCause() != null) {
        ex = ex.getCause();
    }
    pageContext.setAttribute("error", ex);
    StringWriter sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));
    sw.flush();
    pageContext.setAttribute("stackTrace", sw.getBuffer().toString());
%>
<fieldset>
    <legend style="color: red">Error</legend>
    <p style="color: red">${fn:escapeXml(error)}</p>
    <a href="#show-stacktrace" onclick="var st=document.getElementById('stacktrace').style; st.display=st.display == 'none' ? '' : 'none'; return false;">show stacktrace</a>
    <pre id="stacktrace" style="display:none">${stackTrace}</pre>
</fieldset>
<%
        }
    }
%>
<form id="groovyForm" action="?" method="post">
    <input type="hidden" name="toolAccessToken" value="${toolAccessToken}"/>
    <input type="hidden" id="runScript" name="runScript" value="true" />
    <c:if test="${empty param.scriptURI or param.scriptURI eq 'custom'}">
        <input type="checkbox" value="background" name="background" id="background"/>&nbsp;<label for="background" title="Execute the script as a background job (separate thread)">Execute as a background job</label>
    </c:if>
    <c:set var="scripts" value="${tools:getGroovyConsoleScripts()}" />
    <c:if test="${not empty scripts}">
        <p>
            Chose a pre-defined script to be executed:
            <select name="scriptURI" onchange="document.getElementById('runScript').setAttribute('value','false'); document.getElementById('groovyForm').submit();">
                <option value="custom" class="scriptURISelection">---</option>
                <c:forEach items="${scripts}" var="bundle">
                    <optgroup label="${bundle.key}">
                        <c:forEach items="${bundle.value}" var="script">
                            <%--@elvariable id="script" type="org.jahia.osgi.BundleResource"--%>
                            <c:remove var="currentScriptIsSelected" />
                            <c:if test="${script.URI eq param.scriptURI}"><c:set var="currentScriptIsSelected">selected='selected'</c:set><c:set var="currentScriptFilename" value="${script.filename}"/></c:if>
                            <option value="${script.URI}" class="scriptURISelection" ${currentScriptIsSelected}><c:out value="${script.filename}" /></option>
                        </c:forEach>
                    </optgroup>
                </c:forEach>
            </select>
            <c:if test="${not empty scriptContent}">
                <a class="fancybox-link" title="${fn:escapeXml(currentScriptFilename)}" href="#viewArea"><img
                        src="<c:url value='/icons/filePreview.png'/>" width="16" height="16" alt="view" title="View"></a>
            </c:if>
        </p>
    </c:if>
    <c:choose>
        <c:when test="${empty param.scriptURI or param.scriptURI eq 'custom'}">
            <p>${not empty scripts ? 'Or paste' : 'Paste'} here the Groovy code you would like to execute against Jahia:</p>

            <p>
                    <textarea rows="25" style="width: 100%" id="text" name="script"
                              onkeyup="if ((event || window.event).keyCode == 13 && (event || window.event).ctrlKey && confirm(<%=GroovyConsoleHelper.WARN_MSG%>')) document.getElementById('groovyForm').submit();">${param.script}</textarea>
            </p>
            <a class="fancybox-link" href="#snippetsArea">Code snippets</a>
        </c:when>
        <c:otherwise>
            ${tools:getScriptCustomFormElements(param.scriptURI, pageContext.request)}
        </c:otherwise>
    </c:choose>
    <p>
        <input type="submit" value="Execute ([Ctrl+Enter])" onclick="if (!confirm('<%=GroovyConsoleHelper.WARN_MSG%>')) { return false; }"/>
    </p>
</form>
<%@ include file="gotoIndex.jspf" %>
<div style="display: none;">
    <div id="helpArea">
        <h3>How to use the Groovy console</h3>

        <h4>Using a custom script</h4>
        <p>You can run a custom script here, writing or pasting it into the textarea. If you write your script directly in the console, some code
        skeletons &amp; snippets are available to help you.</p>
        <p>If your script has to generate some output, you can use the built in logger: <em>log.info("Some output")</em></p>

        <h4>Using a predefined script</h4>
        <p>You can as well package in any of your modules a predefined script, which can then be conveniently run from the console
            without you have to copy and paste it. You still have the possibility to write or paste a custom script.</p>
        <p>Full documentation available on GitHub:
            <a href="https://github.com/Jahia/extended-groovy-console#extended-groovy-console" 
               target="_blank">https://github.com/Jahia/extended-groovy-console</a></p>
    </div>
</div>
<c:if test="${not empty scriptContent}">
    <div style="display: none;">
        <div id="viewArea">
            <pre>${scriptContent}</pre>
        </div>
    </div>
</c:if>
<%@include file="snippetsArea.jspf"%>
</body>
</html>