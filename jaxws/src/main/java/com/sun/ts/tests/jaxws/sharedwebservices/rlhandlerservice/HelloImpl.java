/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * @(#)HelloImpl.java	1.16 05/09/14
 */

package com.sun.ts.tests.jaxws.sharedwebservices.rlhandlerservice;

import com.sun.ts.lib.util.*;

import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.*;

import jakarta.xml.ws.WebServiceException;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;
import jakarta.annotation.Resource;

@WebService(portName = "HelloPort", targetNamespace = "http://rlhandlerservice.org/wsdl", serviceName = "RLHandlerService", wsdlLocation = "WEB-INF/wsdl/RLHandlerService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.rlhandlerservice.Hello")

public class HelloImpl implements Hello {

  @Resource
  WebServiceContext wscontext;

  public com.sun.ts.tests.jaxws.sharedwebservices.rlhandlerservice.MyResultType doHandlerTest1(
      com.sun.ts.tests.jaxws.sharedwebservices.rlhandlerservice.MyActionType action) {

    Handler_Util.setTraceFlag(action.getHarnesslogtraceflag());

    Handler_Util.initTestUtil("HelloImpl", action.getHarnessloghost(),
        action.getHarnesslogport(), action.getHarnesslogtraceflag());

    TestUtil.logTrace("*** in HelloImpl:doHandlerTest1 ***");
    String theAction = action.getAction();
    TestUtil.logTrace("*** action = " + theAction + " ***");
    String testType = action.getTestType();
    TestUtil.logTrace("*** testType = " + testType + " ***");
    TestUtil.logTrace("*** wscontext = " + wscontext + " ***");

    String errors = "";

    MyResultType r = null;
    try {
      TestUtil.logTrace("The endpoint is sending back the following data:");
      TestUtil.logTrace("action=" + action.getAction());
      TestUtil.logTrace("getTestType=" + action.getTestType());
      TestUtil.logTrace("harnessloghost=" + action.getHarnessloghost());
      TestUtil.logTrace("harnesslogport=" + action.getHarnesslogport());
      TestUtil
          .logTrace("harnesslogtraceflag=" + action.getHarnesslogtraceflag());
      TestUtil.logTrace("errors:" + errors);

      r = new MyResultType();
      r.setAction(action.getAction());
      r.setTestType(action.getTestType());
      r.setErrors(errors);
      r.setHarnessloghost(action.getHarnessloghost());
      r.setHarnesslogport(action.getHarnesslogport());
      r.setHarnesslogtraceflag(action.getHarnesslogtraceflag());
    } catch (Exception e) {
      throw new WebServiceException(e.getMessage(), e);
    }
    return r;
  }
}
