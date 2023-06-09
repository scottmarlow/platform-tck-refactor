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

import com.sun.ts.tests.jaxws.common.SOAPHandlerBase;
import jakarta.xml.ws.soap.SOAPFaultException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.namespace.QName;
import jakarta.xml.soap.*;

public class ServerSOAPHandler5 extends SOAPHandlerBase {
  private static final String WHICHHANDLERTYPE = "Server";

  private static final String HANDLERNAME = "ServerSOAPHandler5";

  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private final QName FAULTCODE = new QName(NAMESPACEURI, "ItsASoapFault",
      "tns");

  private static final String FAULTACTOR = "faultActor";

  private Name name = null;

  private jakarta.xml.soap.SOAPFault sf;

  public ServerSOAPHandler5() {
    super();
    super.setWhichHandlerType(WHICHHANDLERTYPE);
    super.setHandlerName(HANDLERNAME);
  }

  public boolean handleMessage(SOAPMessageContext context) {
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
      HandlerTracker.reportHandleMessage(this, direction);
      if (direction.equals(Constants.INBOUND)) {
        if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPInboundHandleFaultFalseTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound SOAPFaultException");
          String faultString = "ServerSOAPHandler5.handleMessage throws SOAPFaultException for ServerSOAPInboundHandleFaultFalseTest";
          try {
            name = SOAPFactory.newInstance().createName("somefaultentry");
            sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                faultString, name);
          } catch (Exception e) {
            HandlerTracker.reportThrowable(this, new Exception(
                "Unexpected error occurred in handleMessage for an outbound message"
                    + e));
          }
          throw new SOAPFaultException(sf);
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound SOAPFaultException");
          String faultString = "ServerSOAPHandler6.handleMessage throws SOAPFaultException for ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest";
          try {
            name = SOAPFactory.newInstance().createName("somefaultentry");
            sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                faultString, name);
          } catch (Exception e) {
            HandlerTracker.reportThrowable(this, new Exception(
                "Unexpected error occurred in ServerSOAPHandler6.handleMessage for an inbound message"
                    + e));
          }
          throw new SOAPFaultException(sf);
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound SOAPFaultException");
          String faultString = "ServerSOAPHandler6.handleMessage throws SOAPFaultException for ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest";
          try {
            name = SOAPFactory.newInstance().createName("somefaultentry");
            sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                faultString, name);
          } catch (Exception e) {
            HandlerTracker.reportThrowable(this, new Exception(
                "Unexpected error occurred in handleMessage for an inbound message"
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

}
