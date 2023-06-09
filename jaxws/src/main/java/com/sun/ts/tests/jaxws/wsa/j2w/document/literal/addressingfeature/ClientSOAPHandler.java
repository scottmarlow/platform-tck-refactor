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
package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.addressingfeature;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.soap.SOAPException;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {
  String testName = null;

  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    // figure out which testname i'm checking for and call appropropriate
    // verify*(...)
    if (Handler_Util.checkForMsg(context,
        "ClientEnabledNotREQServerNotEnabled")) {
      verifyClientEnabledNotREQServerNotEnabled(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientNotEnabledServerEnabledNotREQ")) {
      verifyClientNotEnabledServerEnabledNotREQ(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientEnabledNotREQServerEnabledREQ")) {
      verifyClientEnabledNotREQServerEnabledREQ(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientEnabledNotREQServerEnabledNotREQ")) {
      verifyClientEnabledNotREQServerEnabledNotREQ(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientEnabledNotREQServerUsingDefaults")) {
      verifyClientEnabledNotREQServerUsingDefaults(context, action);
    }
  }

  private void verifyClientNotEnabledServerEnabledNotREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientNotEnabledServerEnabledNotREQ here...
    checkAddressingHeadersDoNotExist(context, action);
  }

  private void verifyClientEnabledNotREQServerNotEnabled(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledNotREQServerNotEnabled here...
    checkAddressingHeadersDoNotExist(context, action);
  }

  private void verifyClientEnabledNotREQServerEnabledREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledNotREQServerEnabledREQ here...
    checkAddressingHeadersMayExist(context, action);
  }

  private void verifyClientEnabledNotREQServerEnabledNotREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledNotREQServerEnabledNotREQ
    // here...
    checkAddressingHeadersMayExist(context, action);
  }

  private void verifyClientEnabledNotREQServerUsingDefaults(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledNotREQServerUsingDefaults
    // here...
    checkAddressingHeadersMayExist(context, action);
  }

  private void verifyAction(String action) {
    TestUtil.logMsg("ClientSOAPHandler.verifyAction: [action=" + action + "]");
    if (!TestConstants.ADD_NUMBERS_OUT_ACTION.equals(action)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.ADD_NUMBERS_OUT_ACTION + ", Actual:" + action);
    }
  }

  private void checkAddressingHeadersMayExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkAddressingHeadersMayExist");
    // If Addressing headers exist then check them otherwise don't
    if (action != null) {
      verifyAction(action);
      checkInboundToExist(context);
      checkInboundRelatesToExist(context);
    }
  }

  private void checkAddressingHeadersExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkAddressingHeadersExist");
    verifyAction(action);
    checkInboundToExist(context);
    checkInboundRelatesToExist(context);
  }

  private void checkAddressingHeadersDoNotExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkAddressingHeadersDoNotExist");
    checkActionDoesNotExist(action);
    checkInboundToDoesNotExist(context);
    checkInboundRelatesToDoesNotExist(context);
  }

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    testName = (String) context.get("test.name");
    TestUtil.logMsg("ClientSOAPHandler.getAction(): testName=" + testName);
    if (testName == null) {
      return super.getAction(context);
    }
    /* Headers MAY be present on SOAPResponse */
    else if (testName.equals("ClientEnabledNotREQServerEnabledNotREQ")
        || testName.equals("ClientEnabledNotREQServerEnabledREQ")
        || testName.equals("ClientEnabledNotREQServerUsingDefaults")) {
      try {
        return super.getAction(context);
      } catch (Exception e) {
        return null;
      }
    }
    /* Headers MUST NOT be present on SOAPResponse */
    else if (testName.equals("ClientEnabledNotREQServerNotEnabled")
        || testName.equals("ClientNotEnabledServerEnabledNotREQ")) {
      return super.getActionDoesNotExist(context);
    }
    /* Fault case just return null */
    else
      return null;
  }

  protected String whichHandler() {
    return "ClientSOAPHandler";
  }
}
