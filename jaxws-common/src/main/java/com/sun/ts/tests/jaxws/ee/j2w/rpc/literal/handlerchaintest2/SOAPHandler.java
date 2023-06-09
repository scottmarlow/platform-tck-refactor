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

package com.sun.ts.tests.jaxws.ee.j2w.rpc.literal.handlerchaintest2;

import com.sun.ts.lib.util.*;

import jakarta.xml.soap.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.handler.soap.*;
import javax.xml.namespace.QName;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import com.sun.ts.tests.jaxws.common.Handler_Util;

public class SOAPHandler
    implements jakarta.xml.ws.handler.soap.SOAPHandler<SOAPMessageContext> {

  private final String HANDLER_NAME = "ServerSOAPHandler";

  public Set<QName> getHeaders() {
    return new HashSet<QName>();
  }

  public boolean handleMessage(SOAPMessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":handleMessage");

    String direction = Handler_Util.getDirection(context);
    if (Handler_Util.checkForMsg(this, context, "HandlerChainOnSEITest")) {
      HandlerChainOnSEITest(context, direction);
    } else {
      System.out.println(
          "didn't find HandlerChainOnSEITest message, handler will ignore");
    }
    System.out.println("exiting " + HANDLER_NAME + ":handleMessage");
    return true;
  }

  public void HandlerChainOnSEITest(MessageContext context, String direction) {
    System.out.println("in " + HANDLER_NAME + ":HandlerChainOnSEITest");
    try {
      System.out.println("direction=" + direction);
      SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
      SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
      SOAPBody body = env.getBody();
      Iterator it = body.getChildElements();
      while (it.hasNext()) {
        SOAPElement elem = (SOAPElement) it.next();
        Name elemName = elem.getElementName();
        Iterator it2 = ((SOAPElement) elem).getChildElements();
        while (it2.hasNext()) {
          SOAPElement elem2 = (SOAPElement) it2.next();
          String value = elem2.getValue();
          if (value.indexOf("HandlerChainOnSEITest") >= 0) {
            value = value + direction + HANDLER_NAME;
            elem2.setValue(value);
          }
        }
      }
      msg.saveChanges();
      Handler_Util.dumpMsg(context);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      e.printStackTrace();
    }
    System.out.println("exiting " + HANDLER_NAME + ":HandlerChainOnSEITest");
  }

  public void close(MessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":close");
  }

  public boolean handleFault(SOAPMessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":handleFault");
    return true;
  }

}
