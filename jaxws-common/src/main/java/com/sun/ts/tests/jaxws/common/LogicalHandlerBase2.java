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
 * $Id$
 */

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import jakarta.xml.ws.handler.*;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.PostConstruct;

public class LogicalHandlerBase2
    implements jakarta.xml.ws.handler.LogicalHandler<LogicalMessageContext> {
  private int destroyCalled = 0;

  private int doingHandlerWork = 0;

  private String whichHandlerType = null;

  private String handlerName = null;

  public void setWhichHandlerType(String w) {
    this.whichHandlerType = w;
  }

  public String getWhichHandlerType() {
    return this.whichHandlerType;
  }

  public void setHandlerName(String h) {
    this.handlerName = h;
  }

  public String getHandlerName() {
    return this.handlerName;
  }

  public void preinvoke() {
    doingHandlerWork++;
    if (destroyCalled > 0)
      HandlerTracker.reportThrowable(this, new Exception(
          "Violation of Handler Lifecycle - Handler used after destroy called"));
  }

  public void postinvoke() {
    doingHandlerWork = 0;
  }

  @PostConstruct
  public void myInit() {
    System.out.println("in " + this + ":myInit");
    HandlerTracker.reportInit(this, "myInit");
  }

  @PreDestroy
  public void myDestroy() {
    System.out.println("in " + this + ":myDestroy");
    if (doingHandlerWork > 0)
      HandlerTracker.reportThrowable(this, new Exception(
          "Violation of Handler Lifecycle - destroy called during handler usage"));

    destroyCalled++;
    HandlerTracker.reportDestroy(this, "myDestroy");
  }

  public boolean handleMessage(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleMessage");
    TestUtil.logTrace("in " + this + ":handleMessage");
    try {
      preinvoke();
      Handler_Util.setTraceFlag(
          Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

      Handler_Util.initTestUtil(this,
          Handler_Util.getValueFromMsg(this, context, "harnessloghost"),
          Handler_Util.getValueFromMsg(this, context, "harnesslogport"),
          Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

      if (!Handler_Util.checkForMsg(this, context, "GetTrackerData")) {
        String direction = Handler_Util.getDirection(context);
        HandlerTracker.reportHandleMessage(this, direction);
      } else {
        TestUtil.logTrace("found GetTrackerData message, handler will ignore");
      }
    } finally {
      postinvoke();
    }
    System.out.println("exiting " + this + ":handleMessage");
    TestUtil.logTrace("exiting " + this + ":handleMessage");
    return true;
  }

  public void close(MessageContext context) {
    TestUtil.logTrace("in " + this + ":close");
    try {
      preinvoke();
      HandlerTracker.reportClose(this);
    } finally {
      postinvoke();
    }
  }

  public boolean handleFault(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    TestUtil.logTrace("in " + this + ":handleFault");
    try {
      preinvoke();
      HandlerTracker.reportHandleFault(this);
    } finally {
      postinvoke();
    }
    TestUtil.logTrace("exiting " + this + ":handleFault");
    System.out.println("exiting " + this + ":handleFault");
    return true;
  }

}
