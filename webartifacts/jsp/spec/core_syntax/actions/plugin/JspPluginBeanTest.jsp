<%--

    Copyright (c) 2003, 2022 Oracle and/or its affiliates and others.
    All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page contentType="text/plain" %>
<jsp:plugin type="bean" code="foo.class" codebase="/"
            align="middle" archive="test.jar" height="10"
            hspace="1" jreversion="1.3.1" name="test"
            vspace="1" width="10"
            nspluginurl="http://www.nowaythiswebsitecouldpossiblyexist.com"
            iepluginurl="http://www.nowaythiswebsitecouldpossiblyexist.com">
            <jsp:params>
                <jsp:param name="test" value="testvalue"/>
            </jsp:params>
            <jsp:fallback>fallback_text</jsp:fallback>
</jsp:plugin>
expected_text
