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

package com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice;

import com.sun.ts.lib.util.*;
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.handler.LogicalMessageContext;
import jakarta.xml.ws.soap.SOAPFaultException;

import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.Name;

import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;

public class ServerLogicalHandler4 extends LogicalHandlerBase {
  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private static final String WHICHHANDLERTYPE = "Server";

  private static final String HANDLERNAME = "ServerLogicalHandler4";

  private final QName FAULTCODE = new QName(NAMESPACEURI, "ItsASoapFault",
      "tns");

  private static final String FAULTACTOR = "faultActor";

  private Name name = null;

  private jakarta.xml.soap.SOAPFault sf;

  public ServerLogicalHandler4() {
    super();
    super.setWhichHandlerType(WHICHHANDLERTYPE);
    super.setHandlerName(HANDLERNAME);
  }

  public boolean handleMessage(LogicalMessageContext context) {

    System.out.println("in " + this + ":handleMessage");
    TestUtil.logTrace("in " + this + ":handleMessage");
    Handler_Util.setTraceFlag(
        Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

    Handler_Util.initTestUtil(this,
        Handler_Util.getValueFromMsg(this, context, "harnessloghost"),
        Handler_Util.getValueFromMsg(this, context, "harnesslogport"),
        Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

    String direction = Handler_Util.getDirection(context);
    if (!Handler_Util.checkForMsg(this, context, "GetTrackerData")) {
      HandlerTracker.reportHandleMessage(this,
          Handler_Util.getDirection(context));
      if (direction.equals(Constants.INBOUND)) {
        if (Handler_Util.checkForMsg(this, context,
            "ServerLogicalInboundHandleMessageThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound RuntimeException");
          throw new RuntimeException(HANDLERNAME
              + ".handleMessage throwing an inbound RuntimeException");
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerLogicalInboundHandleMessageFalseTest")) {
          String tmp = Handler_Util.getMessageAsString(context);
          String response = tmp.replaceAll("MyAction", "MyResult");
          context.getMessage()
              .setPayload(new StreamSource(new StringReader(response)));
          return false;
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerLogicalInboundHandleMessageThrowsSOAPFaultTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound SOAPFaultException");
          String faultString = "ServerLogicalHandler4.handleMessage throwing an inbound SOAPFaultException";
          try {
            name = SOAPFactory.newInstance().createName("somefaultentry");
            sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                faultString, name);
          } catch (Exception e) {
            HandlerTracker.reportThrowable(this,
                new Exception(
                    "Unexpected error in handleMessage for an inbound message"
                        + e));
          }
          throw new SOAPFaultException(sf);
        }
      } else if (direction.equals(Constants.OUTBOUND)) {

        if (Handler_Util.checkForMsg(this, context,
            "ServerLogicalOutboundHandleMessageThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound RuntimeException");
          throw new RuntimeException(HANDLERNAME
              + ".handleMessage throwing an outbound RuntimeException");
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerLogicalOutboundHandleMessageFalseTest")) {
          return false;
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerLogicalOutboundHandleMessageThrowsSOAPFaultTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound SOAPFaultException");
          String faultString = "ServerLogicalHandler4.handleMessage throwing an outbound SOAPFaultException";
          try {
            name = SOAPFactory.newInstance().createName("somefaultentry");
            sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                faultString, name);
          } catch (Exception e) {
            HandlerTracker.reportThrowable(this,
                new Exception(
                    "Unexpected error in handleMessage for an outbound message"
                        + e));
          }
          throw new SOAPFaultException(sf);
        }
      }
    }

    System.out.println("exiting " + this + ":handleMessage");
    TestUtil.logTrace("exiting " + this + ":handleMessage");
    return true;
  }

  public boolean handleFault(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    TestUtil.logTrace("in " + this + ":handleFault");
    HandlerTracker.reportHandleFault(this);
    String direction = Handler_Util.getDirection(context);
    if (direction.equals(Constants.OUTBOUND)) {
      if (Handler_Util.checkForMsg(this, context,
          "ServerLogicalHandler5.handleMessage throws SOAPFaultException for ServerLogicalInboundHandleFaultFalseTest")) {
        return false;
      } else if (Handler_Util.checkForMsg(this, context,
          "ServerLogicalHandler6.handleMessage throws SOAPFaultException for ServerLogicalInboundHandleFaultThrowsRuntimeExceptionTest")) {
        HandlerTracker.reportComment(this,
            "Throwing an outbound RuntimeException");
        throw new RuntimeException(
            HANDLERNAME + ".handleFault throwing an outbound RuntimeException");
      } else if (Handler_Util.checkForMsg(this, context,
          "ServerLogicalHandler6.handleMessage throws SOAPFaultException for ServerLogicalInboundHandleFaultThrowsSOAPFaultExceptionTest")) {
        HandlerTracker.reportComment(this,
            "Throwing an outbound SOAPFaultException");
        String faultString = "ServerLogicalHandler4.handleFault throwing an outbound SOAPFaultException";
        try {
          name = SOAPFactory.newInstance().createName("somefaultentry");
          sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
              faultString, name);
        } catch (Exception e) {
          HandlerTracker.reportThrowable(this, new Exception(
              "Unexpected error occurred in handleFault for an outbound message"
                  + e));
        }
        throw new SOAPFaultException(sf);
      }
    }

    System.out.println("exiting " + this + ":handleFault");
    TestUtil.logTrace("exiting " + this + ":handleFault");
    return true;
  }

}
