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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1012;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;

import java.util.Map;
import java.util.List;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  final String PASSED = "PASSED";

  final String FAILED = "FAILED";

  protected void processInboundMessage(SOAPMessageContext context) {
    System.out.println("in ServerSOAPHandler:processInboundMessage");
    JAXWS_Util.dumpHTTPHeaders(context);
    Map<String, List<String>> map = (Map<String, List<String>>) context
        .get(MessageContext.HTTP_REQUEST_HEADERS);
    String result = verifyContentTypeHttpHeader(map);
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  /**
   * Verifies the contents of the Content-Type HTTP header
   *
   * @param request
   *          the HTTP servlet request.
   */
  protected String verifyContentTypeHttpHeader(Map<String, List<String>> m) {
    System.out.println("in ServerSOAPHandler:verifyContentTypeHttpHeader");
    String result = FAILED;
    Map<String, List<String>> map = JAXWS_Util.convertKeysToLowerCase(m);
    List<String> values = map.get("content-type");
    System.out.println("HTTP header Content-Type=" + values);
    String sValues = values.toString().toLowerCase();
    if (sValues != null) {
      int index = sValues.indexOf("charset=");
      if (index > 0) {
        if ((sValues.indexOf("utf-8") >= 0)
            || (sValues.indexOf("utf-16") >= 0)) {
          result = PASSED;
        } else {
          result = FAILED + ":charset did not equal utf-8 or utf-16, it was |"
              + sValues + "|";
        }
      } else {
        result = FAILED + ": charset not found in HTTP Content-Type [" + sValues
            + "]";
      }
    } else {
      result = FAILED + ": the HTTP header Content-Type was not found";
    }
    System.out.println("result=" + result);
    return result;
  }
}
