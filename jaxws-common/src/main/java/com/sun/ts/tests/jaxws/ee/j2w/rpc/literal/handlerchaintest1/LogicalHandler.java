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

package com.sun.ts.tests.jaxws.ee.j2w.rpc.literal.handlerchaintest1;

import jakarta.xml.ws.handler.*;

import com.sun.ts.tests.jaxws.common.Handler_Util;

import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;

public class LogicalHandler
    implements jakarta.xml.ws.handler.LogicalHandler<LogicalMessageContext> {
  private final String HANDLER_NAME = "ServerLogicalHandler";

  public boolean handleMessage(LogicalMessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":handleMessage");

    String direction = Handler_Util.getDirection(context);
    if (Handler_Util.checkForMsg(this, context, "HandlerChainOnImplTest")) {
      HandlerChainOnImplTest(context, direction);
    } else {
      System.out.println(
          "didn't find HandlerChainOnImplTest message, handler will ignore");
    }
    System.out.println("exiting " + HANDLER_NAME + ":handleMessage");
    return true;
  }

  public void HandlerChainOnImplTest(LogicalMessageContext context,
      String direction) {
    System.out.println("in " + HANDLER_NAME + ":HandlerChainOnImplTest");

    Handler_Util.dumpMsg(context);

    String tmp = Handler_Util.getMessageAsString(context);
    String newTmp = tmp.replaceAll("HandlerChainOnImplTest",
        "HandlerChainOnImplTest" + direction + HANDLER_NAME);
    context.getMessage().setPayload(new StreamSource(new StringReader(newTmp)));
    Handler_Util.dumpMsg(context);

    System.out.println("exiting " + HANDLER_NAME + ":HandlerChainOnImplTest");
  }

  public void close(MessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":close");
  }

  public boolean handleFault(LogicalMessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":handleFault");
    return true;
  }

}
