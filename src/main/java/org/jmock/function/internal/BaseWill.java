package org.jmock.function.internal;

import org.jmock.api.Action;

public class BaseWill {

    private final BaseMethodCapture<?> methodCapture;

    protected BaseWill(BaseMethodCapture<?> methodCapture) {
        this.methodCapture = methodCapture;
    }

    public void will(Action action) {
        methodCapture.setAction(action);
    }
}
