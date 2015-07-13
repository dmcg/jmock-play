package org.jmock;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class Java8Test2 {

    @Rule
    public final Expec8ionsMockery mockery = new Expec8ionsMockery();

    private final Service service = mockery.mock(Service.class);

    public interface Service {
        String stringify(int o);
        String stringify2(int o);
    }

    @Test
    public void test() {
        mockery.checking(expect -> {
            expect.allowing(service::stringify).with(42).will(() -> "42");
            expect.allowing(service::stringify2).with(42).will(() -> "422");
        });

        assertEquals("42", service.stringify(42));
        assertEquals("422", service.stringify2(42));
    }


}
