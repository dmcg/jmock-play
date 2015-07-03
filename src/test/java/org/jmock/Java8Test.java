package org.jmock;

import org.hamcrest.core.IsAnything;
import org.jmock.api.ExpectationError;
import org.jmock.function.Expec8ions;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ParametersMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class Java8Test {

    @Rule public final JUnitRuleMockery mockery = new JUnitRuleMockery();

    private final Service service = mockery.mock(Service.class);

    public static interface Service {
        public String stringify(Object o);
        public String stringify2(Object o);
        public int write(Object o) throws IOException;
    }

    @Test
    public void matches_matching_parameters() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).with(42).will(() -> "42");
        }});
        assertEquals("42", service.stringify(42));
    }

    @Test
    public void doesnt_match_non_matching_parameters() {
        // JUnitRuleMockery will fail in tearDown
        Mockery localMockery = new Mockery();
        Service localService = localMockery.mock(Service.class);
        localMockery.checking(new Expec8ions() {{
            allowing(callTo(localService)::stringify).with(42).will(() -> "42");
        }});
        try {
            localService.stringify(54);
            fail();
        } catch (ExpectationError e) {
            assertThat(e.toString(), containsString("54"));
        }
    }

    @Test
    public void doesnt_match_non_matching_method() {
        // JUnitRuleMockery will fail in tearDown
        Mockery localMockery = new Mockery();
        Service localService = localMockery.mock(Service.class);
        localMockery.checking(new Expec8ions() {{
            allowing(callTo(localService)::stringify2).with(42).will(() -> "42");
        }});
        try {
            localService.stringify(54);
            fail();
        } catch (ExpectationError e) {
            assertThat(e.toString(), containsString("54"));
        }
        try {
            localMockery.assertIsSatisfied();
            fail();
        } catch (ExpectationError e) {
            assertThat(e.toString(), containsString("42"));
        }
    }

    @Test
    public void does_default_action() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).with(42);
        }});
        assertEquals("", service.stringify(42));
    }

    @Test
    public void allows_anyParameters_matcher() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).with(anyParameters()).will(String::valueOf);
        }});

        assertEquals("42", service.stringify(42));
        assertEquals("54", service.stringify(54));
    }

    @Test
    public void allows_checked_throws() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::write).with(anyParameters()).will(() -> {throw new IOException("whoops");});
        }});
        try {
            service.write(54);
            fail();
        } catch (IOException e) {
            assertThat(e.toString(), containsString("whoops"));
        }
    }

    @Test
    public void allows_unchecked_throws() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).with(anyParameters()).will(() -> {throw new RuntimeException("whoops");});
        }});
        try {
            service.stringify(54);
            fail();
        } catch (RuntimeException e) {
            assertThat(e.toString(), containsString("whoops"));
        }
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
