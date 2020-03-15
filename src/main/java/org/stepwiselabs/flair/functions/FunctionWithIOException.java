package org.stepwiselabs.flair.functions;

import java.io.IOException;

@FunctionalInterface
public interface FunctionWithIOException<I,O> {

    O apply(I input) throws IOException;
}
