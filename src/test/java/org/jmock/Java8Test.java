package org.jmock;

import org.hamcrest.core.IsAnything;
import org.jmock.api.ExpectationError;
import org.jmock.function.Expec8ions;
import org.jmock.internal.ParametersMatcher;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class Java8Test {

    Mockery mockery = new Mockery();
    Service service = mockery.mock(Service.class);

    public static interface Service {
        public String stringify(Object o);
    }

    @Test
    public void matches_matching_parameters() {
        mockery.checking(new Expec8ions() {{
            allowing(calling(service)::stringify).with(42).will(() -> "42");
        }});
        assertEquals("42", service.stringify(42));
    }

    @Test
    public void doesnt_match_non_matching_parameters() {
        mockery.checking(new Expec8ions() {{
            allowing(calling(service)::stringify).with(42).will(() -> "42");
        }});
        try {
            service.stringify(54);
            fail();
        } catch (ExpectationError e) {
            assertThat(e.toString(), containsString("54"));
        }
    }

    @Test
    public void does_default_action() {
        mockery.checking(new Expec8ions() {{
            allowing(calling(service)::stringify).with(42);
        }});

        assertEquals("", service.stringify(42));
    }

    @Test
    public void allows_a_parameters_matcher() {
        mockery.checking(new Expec8ions() {{
            allowing(calling(service)::stringify).with(anyParameters()).will(String::valueOf);
        }});

        assertEquals("42", service.stringify(42));
        assertEquals("54", service.stringify(54));
    }

    private ParametersMatcher anyParameters() {
        return new AnyParametersMatcher();
    }

    private static class AnyParametersMatcher extends IsAnything<Object[]> implements ParametersMatcher {
        public AnyParametersMatcher() {
            super("(<any parameters>)");
        }

        public boolean isCompatibleWith(Object[] parameters) {
            return true;
        }
    };

}
