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
package org.jahia.modules.extendedgroovyconsole;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.Writer;

import static org.apache.taglibs.standard.functions.Functions.escapeXml;

/**
 * SLF4J logger wrapper to also log into the provided instance of {@link PrintWriter}.
 *
 * @author Sergiy Shyrkov
 */
public class LoggerWrapper extends org.slf4j.ext.LoggerWrapper {

    private PrintWriter out;
    final boolean useColors;

    private enum LOG_LEVEL {
        ERROR, WARN, INFO, DEBUG
    }

    /**
     * Initializes an instance of this class.
     *
     * @param logger
     */
    public LoggerWrapper(Logger logger, Writer out) {
        this(logger, out, true);
    }

    public LoggerWrapper(Logger logger, Writer out, boolean useColors) {
        super(logger, logger.getName());
        this.out = new PrintWriter(out, true);
        this.useColors = useColors;
    }

    @Override
    public void error(String msg) {
        if (!isErrorEnabled()) return;
        out(msg, null, LOG_LEVEL.ERROR, null);
        super.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        if (!isErrorEnabled()) return;
        out(format, new Object[] { arg }, LOG_LEVEL.ERROR, null);
        super.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (!isErrorEnabled()) return;
        out(format, new Object[] { arg1, arg2 }, LOG_LEVEL.ERROR, null);
        super.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object[] argArray) {
        if (!isErrorEnabled()) return;
        out(format, argArray, LOG_LEVEL.ERROR, null);
        super.error(format, argArray);
    }

    @Override
    public void error(String msg, Throwable t) {
        if (!isErrorEnabled()) return;
        out(msg, null, LOG_LEVEL.ERROR, t);
        super.error(msg, t);
    }

    public void info(Object msg) {
        info(String.valueOf(msg));
    }

    @Override
    public void info(String msg) {
        if (!isInfoEnabled()) return;
        out(msg, null, LOG_LEVEL.INFO, null);
        super.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        if (!isInfoEnabled()) return;
        out(format, new Object[] { arg }, LOG_LEVEL.INFO, null);
        super.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (!isInfoEnabled()) return;
        out(format, new Object[] { arg1, arg2 }, LOG_LEVEL.INFO, null);
        super.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object[] argArray) {
        if (!isInfoEnabled()) return;
        out(format, argArray, LOG_LEVEL.INFO, null);
        super.info(format, argArray);
    }

    @Override
    public void info(String msg, Throwable t) {
        if (!isInfoEnabled()) return;
        out(msg, null, LOG_LEVEL.INFO, t);
        super.info(msg, t);
    }

    private void out(String format, Object[] argArray, LOG_LEVEL logLevel, Throwable t) {
        String msg = argArray != null ? MessageFormatter.arrayFormat(format, argArray).getMessage() : format;
        msg = escapeXml(msg);
        out.println(formatLog(msg, logLevel));

        if (t != null) {
            out.print(getOpeningFormatting("stacktrace", logLevel));
            t.printStackTrace(out);
            out.print(getClosingFormatting(logLevel));
        }
    }

    private String formatLog(String msg, LOG_LEVEL logLevel) {
        if (!useColors) {
            return msg;
        }
        return getOpeningFormatting("log", logLevel) + msg + getClosingFormatting(logLevel);
    }

    private String getOpeningFormatting(String type, LOG_LEVEL logLevel) {
        return "<span class=\"" + type + " " + logLevel.name().toLowerCase() + "\">";
    }

    private String getClosingFormatting(LOG_LEVEL logLevel) {
        return "</span>";
    }

    @Override
    public void warn(String msg) {
        if (!isWarnEnabled()) return;
        out(msg, null, LOG_LEVEL.WARN, null);
        super.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        if (!isWarnEnabled()) return;
        out(format, new Object[] { arg }, LOG_LEVEL.WARN, null);
        super.warn(format, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (!isWarnEnabled()) return;
        out(format, new Object[] { arg1, arg2 }, LOG_LEVEL.WARN, null);
        super.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object[] argArray) {
        if (!isWarnEnabled()) return;
        out(format, argArray, LOG_LEVEL.WARN, null);
        super.warn(format, argArray);
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (!isWarnEnabled()) return;
        out(msg, null, LOG_LEVEL.WARN, t);
        super.warn(msg, t);
    }

    @Override
    public void debug(String msg) {
        if (!isDebugEnabled()) return;
        out(msg, null, LOG_LEVEL.DEBUG, null);
        super.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        if (!isDebugEnabled()) return;
        out(format, new Object[] { arg }, LOG_LEVEL.DEBUG, null);
        super.debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (!isDebugEnabled()) return;
        out(format, new Object[] { arg1, arg2 }, LOG_LEVEL.DEBUG, null);
        super.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object[] argArray) {
        if (!isDebugEnabled()) return;
        out(format, argArray, LOG_LEVEL.DEBUG, null);
        super.debug(format, argArray);
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (!isDebugEnabled()) return;
        out(msg, null, LOG_LEVEL.DEBUG, t);
        super.debug(msg, t);
    }
}
