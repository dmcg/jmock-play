package org.jmock.function;

import org.hamcrest.Matchers;
import org.jmock.api.ExpectationError;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.jmock.AbstractExpectations.returnValue;
import static org.jmock.function.internal.BaseExpec8ions.anyParameters;
import static org.jmock.internal.Cardinality.atLeast;
import static org.junit.Assert.*;

public class Expec8ionsMockeryTest {

    @Rule
    public final JunitRuleExpec8ionsMockery mockery = new JunitRuleExpec8ionsMockery();

    private final Service service = mockery.mock(Service.class);

    public interface Service {
        String stringify(int o);
        String stringify2(int o);
        String stringifyLong(long o);
        int write(Object o) throws IOException;
        int overload(int o);
        int overload(float f);
        CharSequence echo(CharSequence s);
        void log(Object o);
        long timeNow();
        void thing();
        String concat(String prefix, Object o);
    }

    @Test
    public void expec8ions_uses_method_reference() {
        mockery.checking(new Expec8ions() {{
            once(service::stringify).with(42).will(returnValue("just right"));
            allowing(service::stringify).with(43).will(returnValue("too big"));
            given(atLeast(1), service::stringify).with(41).will(returnValue("too small"));
        }});

        assertEquals("too big", service.stringify(43));
        assertEquals("too small", service.stringify(41));
        assertEquals("just right", service.stringify(42));
    }


    @Test
    public void use_a_lambda_instead_of_subclassing_Expec8ions() {
        mockery.checking(e -> {
            e.allowing(service::stringify).with(42).will(returnValue("just right"));
            e.allowing(service::stringify).with(41).will(returnValue("too small"));
            e.allowing(service::stringify).with(43).will(returnValue("too big"));
        });

        assertEquals("too big", service.stringify(43));
        assertEquals("too small", service.stringify(41));
        assertEquals("just right", service.stringify(42));
    }

    @Test
    public void allows_parameter_matchers() {
        mockery.checking(e -> {
            e.allowing(service::stringify).withMatching(greaterThan(42)).will(returnValue("too big"));
            e.allowing(service::stringify).withMatching(lessThan(42)).will(returnValue("too small"));
            e.allowing(service::stringify).withMatching(equalTo(42)).will(returnValue("just right"));
        });

        assertEquals("too big", service.stringify(43));
        assertEquals("too small", service.stringify(41));
        assertEquals("just right", service.stringify(42));
    }

    @Test
    public void allows_predicates_for_parameters_matcher() {
        mockery.checking(e -> {
            e.allowing(service::stringify).withMatching(i -> i % 2 == 0).will(returnValue("even"));
            e.allowing(service::stringify).withMatching(i -> i % 2 != 0).will(returnValue("odd"));
        });

        assertEquals("even", service.stringify(42));
        assertEquals("odd", service.stringify(43));
    }

    @Test
    public void allows_lambda_for_action() {
        mockery.checking(e -> {
            e.allowing(service::stringify).withMatching(i -> i % 2 == 0).will(() -> "even");
            e.allowing(service::stringify).withMatching(i -> i % 2 != 0).will(() -> "odd");
        });

        assertEquals("even", service.stringify(42));
        assertEquals("odd", service.stringify(43));
    }

    @Test
    public void passes_parameters_to_lambda_for_action() {
        mockery.checking(e -> {
            e.allowing(service::stringify).withMatching(anyParameters()).will((i) -> i % 2 == 0 ? "even" : "odd");
        });

        assertEquals("even", service.stringify(42));
        assertEquals("odd", service.stringify(43));
    }

    @Test
    public void two_parameters() {
        mockery.checking(new Expec8ions() {{
            allowing(service::concat).withMatching(equalTo("prefix"), anything()).will((p, s) -> p + "-" + s);
        }});

        assertEquals("prefix-suffix", service.concat("prefix", "suffix"));
    }

    @Test
    public void long_version() {
        mockery.checking(new Expec8ions() {{
            allowing(service::stringifyLong).with(42L).will(() -> "42");
        }});
        assertEquals("42", service.stringifyLong(42));
    }

    @Test
    public void matches_the_correct_mock() {
        Service service2 = mockery.mock(Service.class, "service2");
        mockery.checking(new Expec8ions() {{
            allowing(service::stringify).with(42).will(() -> "42");
            allowing(service2::stringify).with(42).will(() -> "422");
        }});
        assertEquals("42", service.stringify(42));
        assertEquals("422", service2.stringify(42));
    }

    @Test
    public void doesnt_match_non_matching_parameters() {
        withLocalMockery((localMockery, localService) -> {
            localMockery.checking(new Expec8ions() {{
                allowing(localService::stringify).with(42).will(() -> "42");
            }});
            try {
                localService.stringify(54);
                fail();
            } catch (ExpectationError e) {
                assertThat(e.toString(), containsString("54"));
            }
        });
    }

    @Test
    public void doesnt_match_non_matching_method() {
        withLocalMockery((localMockery, localService) -> {
            localMockery.checking(new Expec8ions() {{
                allowing(localService::stringify).with(42).will(() -> "42");
            }});
            try {
                localService.stringify2(42);
                fail();
            } catch (ExpectationError e) {
                assertThat(e.toString(), containsString("42"));
            }
        });
    }

    @Test
    public void does_default_action() {
        mockery.checking(new Expec8ions() {{
            allowing(service::stringify).with(42);
        }});
        assertEquals("", service.stringify(42));
    }

    @Test
    public void allows_checked_throws() {
        mockery.checking(new Expec8ions() {{
            allowing(service::write).withMatching(anyParameters()).will(() -> {throw new IOException("whoops");});
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
            allowing(service::stringify).withMatching(anyParameters()).will(() -> {throw new RuntimeException("whoops");});
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
            allowing((Func1<Integer, Integer, RuntimeException>) service::overload).with(7).will(() -> 7);
            allowing((Func1<Float, Integer, RuntimeException>) service::overload).with(7.7F).will(() -> 8);
        }});
        assertEquals(7, service.overload(7));
        assertEquals(8, service.overload(7.7F));
    }

    @Test
    public void allows_variance() {
        mockery.checking(new Expec8ions() {{
            allowing(service::echo).with("hello").will(() -> "hello");
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
            allowing(service::log).withMatching(anyParameters()).will((p) -> {
                param[0] = p;
            });
            // allowing(callTo(service)::log).withMatching(anyParameters()).will((p) -> { return p; }}); // doesn't compile :-)
        }});
        service.log("hello");
        assertEquals("hello", param[0]);
    }

    @Test
    public void function0_has_no_with() {
        mockery.checking(new Expec8ions() {{
            allowing(service::timeNow).will(System::currentTimeMillis);
        }});
        assertThat(service.timeNow(), greaterThan(0L));
    }

    @Test
    public void proc0_has_no_with() {
        final boolean[] called = new boolean[1];
        mockery.checking(new Expec8ions() {{
            allowing(service::thing).will(() -> called[0] = true);
        }});
        assertFalse(called[0]);

        service.thing();
        assertTrue(called[0]);
    }

    @Test
    public void cardinality_works() {
        withLocalMockery((localMockery, localService) -> {
            localMockery.checking(new Expec8ions() {{
                once(localService::stringify).withMatching(anyParameters()).will(String::valueOf);
            }});
            assertEquals("42", localService.stringify(42));

            try {
                localService.stringify(42);
                fail();
            } catch (ExpectationError e) {
                assertThat(e.toString(), containsString("42"));
            }
        });
    }

    /** prevents mockery field from reporting in tearDown */
    private static void withLocalMockery(Proc2<Expec8ionsMockery, Service, RuntimeException> f) {
        Expec8ionsMockery localMockery = new Expec8ionsMockery();
        Service localService = localMockery.mock(Service.class);
        f.call(localMockery, localService);
    }
}
