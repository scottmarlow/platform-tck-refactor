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
 * $Id: ServerLogicalHandler.java 52501 2009-01-24 02:29:49Z adf $
 */

package com.sun.ts.tests.jaxws.sharedwebservices.doclithelloservice;

import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.LogicalMessage;

import jakarta.xml.ws.WebServiceException;

import javax.xml.transform.Source;
import javax.xml.transform.dom.*;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import com.sun.ts.tests.jaxws.common.Constants;

import org.w3c.dom.Node;

public class ServerLogicalHandler
    implements jakarta.xml.ws.handler.LogicalHandler<LogicalMessageContext> {

  private static final jakarta.xml.bind.JAXBContext jaxbContext = createJAXBContext();

  private static jakarta.xml.bind.JAXBContext createJAXBContext() {
    try {
      return jakarta.xml.bind.JAXBContext.newInstance(
          com.sun.ts.tests.jaxws.sharedwebservices.doclithelloservice.ObjectFactory.class);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  public boolean handleMessage(LogicalMessageContext context) {

    System.out.println("in " + this + ":handleMessage");
    String direction = null;
    if (Handler_Util.getDirection(context).equals(Constants.INBOUND)) {
      direction = Constants.INBOUND;
    } else {
      direction = Constants.OUTBOUND;
    }
    System.out.println("direction=" + direction);

    LogicalMessage lm = context.getMessage();
    if (lm != null) {
      if (Handler_Util.checkForMsg(this, context, "setgetPayloadSourceTest")) {
        Source source = lm.getPayload();
        if (source != null) {
          try {
            DOMResult dr = JAXWS_Util.getSourceAsDOMResult(source);
            System.out.println("msg=" + JAXWS_Util.getDOMResultAsString(dr));

            Node documentNode = dr.getNode();
            Node requestResponseNode = documentNode.getFirstChild();

            System.out
                .println("localname=" + requestResponseNode.getLocalName());
            if ((!requestResponseNode.getLocalName()
                .startsWith("Hello3Request"))
                && (!requestResponseNode.getLocalName()
                    .startsWith("Hello3Response"))) {

              System.out.println("The expected messages were not received");
              return true;
            }
            // The first child is the test name the second(last) is the argument
            Node textNode = requestResponseNode.getLastChild().getFirstChild();

            String item = textNode.getNodeValue();
            System.out.println("orig value = " + item);

            textNode.setNodeValue(item + ":" + direction
                + "ServerLogicalHandler_getsetPayloadSource");
            System.out.println("new value = " + textNode.getNodeValue());
            source = new DOMSource(documentNode);
            lm.setPayload(source);
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          System.out.println("LogicalMessage.getPayload returned null");
        }
      } else if (Handler_Util.checkForMsg(this, context,
          "setgetPayloadJAXBContextTest")) {
        try {
          if (direction.equals(Constants.INBOUND)) {
            Hello3Request hreq = (Hello3Request) lm.getPayload(jaxbContext);
            String item = hreq.getArgument();
            item += ":" + direction
                + "ServerLogicalHandler_getsetPayloadJAXBContext";
            hreq.setArgument(item);
            lm.setPayload(hreq, jaxbContext);
          } else {
            Hello3Response hresp = (Hello3Response) lm.getPayload(jaxbContext);
            String item = hresp.getArgument();
            item += ":" + direction
                + "ServerLogicalHandler_getsetPayloadJAXBContext";
            hresp.setArgument(item);
            lm.setPayload(hresp, jaxbContext);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else {
      System.out.println("LogicalMessageContext.getMessage() returned null");
    }
    return true;
  }

  public void close(MessageContext context) {
    System.out.println("in " + this + ":close");
  }

  public boolean handleFault(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    return true;
  }

}
