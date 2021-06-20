/********************************************************************************
 * Copyright (c) 2020 Hantsy Bai and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Hantsy Bai - initial version.
 ********************************************************************************/
package $package;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GreetingMessage implements Serializable {

    private String message;

    public static GreetingMessage of(String s) {
        final GreetingMessage message = new GreetingMessage();
        message.setMessage(s);
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
