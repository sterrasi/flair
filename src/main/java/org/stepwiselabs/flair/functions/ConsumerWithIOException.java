package org.stepwiselabs.flair.functions;

import java.io.IOException;

@FunctionalInterface
public interface ConsumerWithIOException<I> {

    void accept(I input) throws IOException;
}
