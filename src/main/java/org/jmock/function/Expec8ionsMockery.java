package org.jmock.function;

import org.jmock.Mockery;

public class Expec8ionsMockery extends Mockery {

    public void checking(Expec8ions expectations) {
        expectations.buildExpectations(this);
    }

    public void checking(Proc1<Expec8ions, RuntimeException> consumer) {
        checking(Expec8ions.of(consumer));
    }
}
