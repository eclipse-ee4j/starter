# Contributing to Eclipse Starter for Jakarta EE

Thanks for your interest in this project!

## Developer resources

Information regarding source code management, builds, coding standards, and
more.

* https://projects.eclipse.org/projects/ee4j.starter/developer

The project maintains the following source code repositories

* https://github.com/eclipse-ee4j/starter

## Development Standards/Style

We use four spaces for indentation. All files must end with a new line. Velocity code must be indented at a 
separate level from the generated code.

Note that the project performs pre-processing in Maven to make Velocity indentation in Archetypes possible.
Only certain files (generated XML, Dockerfile, and README.md) is processed and only certain Velocity code
is processed (inline-comments, if, else, elseif, end, and set). If you introduce Velocity in other files (for 
example Java) or need other Velocity statements processed, the Maven pre-processing needs to be adjusted 
accordingly.

## Eclipse Development Process

This Eclipse Foundation open project is governed by the Eclipse Foundation
Development Process and operates under the terms of the Eclipse IP Policy.

The Jakarta EE Specification Committee has adopted the Jakarta EE Specification
Process (JESP) in accordance with the Eclipse Foundation Specification Process
v1.2 (EFSP) to ensure that the specification process is complied with by all
Jakarta EE specification projects.

* https://eclipse.org/projects/dev_process
* https://www.eclipse.org/org/documents/Eclipse_IP_Policy.pdf
* https://jakarta.ee/about/jesp/
* https://www.eclipse.org/legal/efsp_non_assert.php

## Eclipse Contributor Agreement

Before your contribution can be accepted by the project team contributors must
electronically sign the Eclipse Contributor Agreement (ECA).

* http://www.eclipse.org/legal/ECA.php

Commits that are provided by non-committers must have a Signed-off-by field in
the footer indicating that the author is aware of the terms by which the
contribution has been provided to the project. The non-committer must
additionally have an Eclipse Foundation account and must have a signed Eclipse
Contributor Agreement (ECA) on file.

For more information, please see the Eclipse Committer Handbook:
https://www.eclipse.org/projects/handbook/#resources-commit

## Contact

Contact the project developers via the project's "dev" list.

* https://accounts.eclipse.org/mailing-list/starter-dev