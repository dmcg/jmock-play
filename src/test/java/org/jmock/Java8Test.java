package org.jmock;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsAnything;
import org.jmock.api.ExpectationError;
import org.jmock.function.Expec8ions;
import org.jmock.function.Function1;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ParametersMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Java8Test {

    @Rule public final JUnitRuleMockery mockery = new JUnitRuleMockery();

    private final Service service = mockery.mock(Service.class);

    public interface Service {
        String stringify(int o);
        String stringify2(int o);
        int write(Object o) throws IOException;
        int overload(int o);
        int overload(float f);
        CharSequence echo(CharSequence s);
        void log(Object o);
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
    public void respects_parameters_matcher() {
        // JUnitRuleMockery will fail in tearDown
        Mockery localMockery = new Mockery();
        Service localService = localMockery.mock(Service.class);

        localMockery.checking(new Expec8ions() {{
            allowing(callTo(localService)::stringify).withMatching(greaterThan(42)).will(String::valueOf);
        }});
        assertEquals("54", localService.stringify(54));

        try {
            localService.stringify(42);
            fail();
        } catch (ExpectationError e) {
            assertThat(e.toString(), containsString("42"));
        }
    }

    @Test
    public void allows_anyParameters_matcher() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).withMatching(anyParameters()).will(String::valueOf);
        }});

        assertEquals("42", service.stringify(42));
        assertEquals("54", service.stringify(54));
    }

    @Test
    public void can_use_predicates_for_parameters_matcher() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).withMatching(i -> i % 2 == 0).will(() -> "even");
            allowing(callTo(service)::stringify).withMatching(i -> i % 2 != 0).will(() -> "odd");
        }});

        assertEquals("even", service.stringify(42));
        assertEquals("odd", service.stringify(43));
    }

    @Test
    public void allows_checked_throws() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::write).withMatching(anyParameters()).will(() -> {throw new IOException("whoops");});
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
            allowing(callTo(service)::stringify).withMatching(anyParameters()).will(() -> {throw new RuntimeException("whoops");});
        }});
        try {
            service.stringify(54);
            fail();
        } catch (RuntimeException e) {
            assertThat(e.toString(), containsString("whoops"));
        }
    }

    @Test
    public void allows_overrides() {
        mockery.checking(new Expec8ions() {{
            allowing((Function1<Integer, Integer, RuntimeException>) callTo(service)::overload).with(7).will(() -> 7);
            allowing((Function1<Float, Integer, RuntimeException>) callTo(service)::overload).with(7.7F).will(() -> 8);
        }});
        assertEquals(7, service.overload(7));
        assertEquals(8, service.overload(7.7F));
    }

    @Test
    public void allows_variance() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::echo).with("hello").will(() -> "hello");
        }});
        assertEquals("hello", service.echo("hello"));

        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::echo).withMatching(Matchers.equalTo("helloSequence")).will(() -> "helloSequence");
        }});
        assertEquals("helloSequence", service.echo("helloSequence"));
    }

    @Test
    public void allows_void_return() {
        final Object[] param = new Object[1];
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::log).withMatching(anyParameters()).will((p) -> { param[0] = p; });
            // allowing(callTo(service)::log).withMatching(anyParameters()).will((p) -> { return p; }); // doesn't compile :-)
        }});
        service.log("hello");
        assertEquals("hello", param[0]);
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
    }

}
