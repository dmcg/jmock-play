package org.jmock.function;

import org.jmock.function.internal.Expec8ions1;

public class Expec8ions extends Expec8ions1 {

    public static Expec8ions of(Proc1<Expec8ions, RuntimeException> consumer) {
        Expec8ions result = new Expec8ions();
        consumer.call(result);
        return result;
    }

    public static Expec8ions expec8ions(Proc1<Expec8ions, RuntimeException> consumer) {
        return of(consumer);
    }

}
