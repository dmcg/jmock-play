package org.jmock.function;

import org.jmock.Mockery;

public class Expec8ionsMockery extends Mockery {

    public void checking(Expec8ions expectations) {
        expectations.buildExpectations(this);
    }
}
