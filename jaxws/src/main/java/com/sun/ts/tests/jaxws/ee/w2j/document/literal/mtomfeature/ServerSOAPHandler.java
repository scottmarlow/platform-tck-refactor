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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomfeature;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;

import java.util.Map;
import java.util.List;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  final String PASSED = "PASSED";

  final String FAILED = "FAILED";

  protected void processInboundMessage(SOAPMessageContext context) {
    System.out.println("in ServerSOAPHandler:processInboundMessage");
    JAXWS_Util.dumpHTTPHeaders(context, false);
    // JAXWS_Util.dumpSOAPMessage(context.getMessage(),false);

    String result = FAILED;
    Map<String, List<String>> map = (Map<String, List<String>>) context
        .get(MessageContext.HTTP_REQUEST_HEADERS);
    if (Handler_Util.checkForMsg(context, "ClientEnabled")) {
      // boolean isAboveThreshold = Handler_Util.checkForMsg(context,"GT2000");
      // result =
      // verifyMTOMEnabledThresholdContentTypeHttpHeader(map,isAboveThreshold);
      result = verifyMTOMEnabledContentTypeHttpHeader(map);
    } else {
      result = verifyMTOMDisabledContentTypeHttpHeader(map);
    }
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  protected String verifyMTOMDisabledContentTypeHttpHeader(
      Map<String, List<String>> m) {
    System.out.println(
        "in ServerSOAPHandler:verifyMTOMDisabledContentTypeHttpHeader");
    String result = FAILED;
    Map<String, List<String>> map = JAXWS_Util.convertKeysToLowerCase(m);
    List<String> values = map.get("content-type");
    System.out.println("DEBUG: HTTP header Content-Type=" + values);
    String sValues = values.toString().toLowerCase();
    if (sValues != null) {
      if (sValues.indexOf("text/xml") >= 0) {
        result = PASSED;
      } else {
        result = FAILED + ": INVALID HTTP Content-type [" + sValues
            + "], expected = text/xml";
      }
    } else {
      result = FAILED + ": the HTTP header Content-Type was not found";
    }
    System.out.println("result=" + result);
    return result;
  }

  protected String verifyMTOMEnabledContentTypeHttpHeader(
      Map<String, List<String>> m) {
    System.out
        .println("in ServerSOAPHandler:verifyMTOMEnabledContentTypeHttpHeader");
    String result = FAILED;
    Map<String, List<String>> map = JAXWS_Util.convertKeysToLowerCase(m);
    List<String> values = map.get("content-type");
    System.out.println("DEBUG: HTTP header Content-Type=" + values);
    String sValues = values.toString().toLowerCase();
    if (sValues != null) {
      if ((sValues.indexOf("multipart/related") >= 0)
          && (sValues.indexOf("application/xop+xml") >= 0)) {
        result = PASSED;
      } else {
        result = FAILED + ": INVALID HTTP Content-type [" + sValues
            + "], expected=multipart/related,application/xop+xml";
      }
    } else {
      result = FAILED + ": the HTTP header Content-Type was not found";
    }
    System.out.println("result=" + result);
    return result;
  }

  protected String verifyMTOMEnabledThresholdContentTypeHttpHeader(
      Map<String, List<String>> m, boolean isAboveThreshold) {
    System.out.println("in ServerSOAPHandler:verifyXOPContentTypeHttpHeader");
    String result = FAILED;
    Map<String, List<String>> map = JAXWS_Util.convertKeysToLowerCase(m);
    List<String> values = map.get("content-type");
    System.out.println("DEBUG: HTTP header Content-Type=" + values);
    String sValues = values.toString().toLowerCase();
    if (sValues != null) {
      if (isAboveThreshold) {
        if ((sValues.indexOf("multipart/related") >= 0)
            && (sValues.indexOf("application/xop+xml") >= 0)) {
          result = PASSED;
        } else {
          result = FAILED + ": INVALID HTTP Content-type [" + sValues
              + "], expected=multipart/related,application/xop+xml";
        }
      } else {
        if (sValues.indexOf("text/xml") >= 0) {
          if ((sValues.indexOf("application/xop+xml") < 0)
              && (sValues.indexOf("multipart/related") < 0)) {
            result = PASSED;
          } else {
            result = FAILED + ": INVALID HTTP Content-type [" + sValues
                + "], did not expect: multipart/related,application/xop+xml";
          }
        } else {
          result = FAILED + ": INVALID HTTP Content-type [" + sValues
              + "], expected=text/xml";
        }
      }
    } else {
      result = FAILED + ": the HTTP header Content-Type was not found";
    }
    System.out.println("result=" + result);
    return result;
  }

}
