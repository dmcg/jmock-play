package org.jmock;

import org.hamcrest.Matchers;
import org.jmock.api.ExpectationError;
import org.jmock.function.Expec8ions;
import org.jmock.function.Func1;
import org.jmock.function.Proc2;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.jmock.function.internal.BaseExpec8ions.anyParameters;
import static org.junit.Assert.*;

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
        long timeNow();
        void thing();
        String concat(String prefix, Object o);
    }

    @Test
    public void matches_matching_parameters() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).with(42).will(() -> "42");
        }});
        assertEquals("42", service.stringify(42));
    }

    @Test
    public void matches_the_correct_mock() {
        Service service2 = mockery.mock(Service.class, "service2");
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).with(42).will(() -> "42");
            allowing(callTo(service2)::stringify).with(42).will(() -> "422");
        }});
        assertEquals("422", service2.stringify(42));
        assertEquals("42", service.stringify(42));
    }

    @Test
    public void doesnt_match_non_matching_parameters() {
        withLocalMockery((localMockery, localService) -> {
            localMockery.checking(new Expec8ions() {{
                allowing(callTo(localService)::stringify).with(42).will(() -> "42");
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
        });
    }

    @Test
    public void does_default_action() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify).with(42);
        }});
        assertEquals("", service.stringify(42));
    }

    @Test
    public void default_parameters_works() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::stringify);
        }});
        assertEquals("", service.stringify(42));
    }

    @Test
    public void respects_parameters_matcher() {
        withLocalMockery((localMockery, localService) -> {
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
        });
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
            allowing((Func1<Integer, Integer, RuntimeException>) callTo(service)::overload).with(7).will(() -> 7);
            allowing((Func1<Float, Integer, RuntimeException>) callTo(service)::overload).with(7.7F).will(() -> 8);
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
            allowing(callTo(service)::log).withMatching(anyParameters()).will((p) -> {
                param[0] = p;
            });
            // allowing(callTo(service)::log).withMatching(anyParameters()).will((p) -> { return p; }); // doesn't compile :-)
        }});
        service.log("hello");
        assertEquals("hello", param[0]);
    }

    @Test
    public void function0_has_no_with() {
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::timeNow).will(System::currentTimeMillis);
        }});
        assertThat(service.timeNow(), greaterThan(0L));
    }

    @Test
    public void proc0_has_no_with() {
        final boolean[] called = new boolean[1];
        mockery.checking(new Expec8ions() {{
            allowing(callTo(service)::thing).will(() -> called[0] = true);
        }});
        assertFalse(called[0]);

        service.thing();
        assertTrue(called[0]);
    }

    @Test
    public void lambda_for_expectations_is_a_bit() {
        mockery.checking(Expec8ions.of(e -> {
            e.allowing(e.callTo(service)::stringify).withMatching(anyParameters()).will(String::valueOf);
        }));

        assertEquals("42", service.stringify(42));
    }

    @Test
    public void two_parameters() {
        mockery.checking(Expec8ions.of(e -> {
            e.allowing(e.callTo(service)::concat).withMatching(equalTo("prefix"), anything()).will((p, s) -> p + "-" + s);
        }));

        assertEquals("prefix-suffix", service.concat("prefix", "suffix"));
    }

    @Test
    public void cardinality_works() {
        withLocalMockery((localMockery, localService) -> {
            localMockery.checking(new Expec8ions() {{
                once(callTo(localService)::stringify).withMatching(anyParameters()).will(String::valueOf);
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
    private static void withLocalMockery(Proc2<Mockery, Service, RuntimeException> f) {
        Mockery localMockery = new Mockery();
        Service localService = localMockery.mock(Service.class);
        f.call(localMockery, localService);
    }

// Other syntax ideas
//    @Test
//    public void matches_matching_parameters_nat() {
//        mockery.allowing(service::stringify).with(equalTo(42)).will();
//        mockery.allowing(() -> service.stringify(with(equalTo(42));).will();
//        mockery.allowing((with) -> service.stringify(with.anInt(equalTo(42));).will();
//
//        mockery.allowing((e) -> oneOf(() -> service.stringify(42))).will());
//
//        assertEquals("42", service.stringify(42));
//    }

}
