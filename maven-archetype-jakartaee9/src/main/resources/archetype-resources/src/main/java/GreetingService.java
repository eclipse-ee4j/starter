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

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class GreetingService {

    public GreetingMessage buildGreetingMessage(String name) {
        return GreetingMessage.of("Say Hello to " + name + " at " + LocalDateTime.now());
    }
}
