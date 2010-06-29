6/29/2010

Eclipse Gemini Naming Build instructions:

As a temporary build workaround, please set the following environment variable:

GEMINI_NAMING_HOME

to the top-level directory of the Gemini Naming source tree.  

If your top-level directory is $SRC_ROOT, then set GEMINI_NAMING_HOME to:

$SRC_ROOT/org.eclipse.gemini.naming

This workaround is currently required due to the fact that the OSGi Enterprise API jar is not available as a maven bundle.  Once the API jar is available from a maven repository, this build workaround will not be necessary.  