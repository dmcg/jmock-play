package org.jmock;

import org.hamcrest.Matcher;
import org.jmock.api.Action;
import org.jmock.function.*;
import org.jmock.internal.Cardinality;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.action.ThrowAction;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.jmock.internal.Cardinality.exactly;

public class Java8ExampleTest {

    Mockery8 mockery = new Mockery8();
    Service service = mockery.mock(Service.class);


    public static interface Service {
        public int add(int a, int b) throws IOException;
        public String stringify(Object o);
        public void beep();
        public void log(String s);

        public int thing(int a, int b) throws IOException;
        public int thing(int a, int b, int c) throws IOException;
    }


    @Test public void methodReference_syntax() {
        // I think I can make this work - it compiles but isn't wired up correctly
        mockery.allowing(service::add).with(1, 2).will(() -> 3);
        mockery.allowing(service::add).with(equalTo(1), equalTo(2)).will(() -> 3);
        mockery.allowing(service::add).with(any(Integer.class), any(Integer.class)).will((p1, p2) -> p1 + p1);
        mockery.allowing(service::add).with(1, 3).will(() -> { throw new IOException(); });
        mockery.allowing(service::add).with(1, 3).will(new ReturnValueAction(4));
        mockery.given(exactly(2), service::add).with(1, 2).will(() -> 3);

        mockery.allowing(service::stringify).with(42).will(String::valueOf);

        mockery.allowing(service::beep).with().will(new ThrowAction(new RuntimeException()));
        mockery.allowing(service::log).with("fred").will((s) -> null); // TODO

        mockery.allowing((IIIIOException) service::thing).with(1, 2).will((p1, p2) -> p1 + p2);
        mockery.allowing((IIIIIOException) service::thing).with(1, 2, 3).will((p1, p2, p3) -> p1 + p2 + p3);
    }

    @Test public void expectations_extension() throws IOException {
        // Not typesafe or parameter number safe, but a quick win
        mockery.checking(new Expectations8() {{
            allowing(service).add(1, 2);
            __will(() -> 3);

            allowing(service).add(1, 3);
            __will(() -> {
                throw new IOException();
            });

            allowing(service).add(with(1), with(3));
            __will((Integer p1, Integer p2) -> p1 + p2);
        }});
    }

    private interface IIIIOException extends Function2<Integer, Integer, Integer, IOException> {}
    private interface IIIIIOException extends Function3<Integer, Integer, Integer, Integer, IOException> {}


    public static class Mockery8 extends Mockery {

        public <X extends Throwable> MethodCapture0<Void, X> allowing(Proc0<X> method) {
            return given(Cardinality.ALLOWING, method);
        }

        public <R, X extends Throwable> MethodCapture0<Void, X> given(Cardinality times, Proc0<X> method) {
            return new MethodCapture0<>(() -> {method.apply(); return null;}, times);
        }

        public <R, X extends Throwable> MethodCapture0<R, X> allowing(Function0<R, X> method) {
            return given(Cardinality.ALLOWING, method);
        }

        public <R, X extends Throwable> MethodCapture0<R, X> given(Cardinality times, Function0<R, X> method) {
            return new MethodCapture0<>(method, times);
        }

        private class MethodCapture0<R, X extends Throwable> {
            private final Function0<R, X> method;
            private final Cardinality cardinality;

            public MethodCapture0(Function0<R, X> method, Cardinality cardinality) {
                this.method = method;
                this.cardinality = cardinality;
            }

            public Will<R> with() {
                return new Will<R>();
            }

            private class Will<R> {
                public void will(FallibleSupplier<R, X> supplier) {
                    will(new SupplierAction<>(supplier));
                }

                public void will(Action action) {
                }

                public void will(Function0<R, X> f) {
                    will(new Function0Action<>(f));
                }
            }
        }

        public <P1, X extends Throwable> MethodCapture1<P1, Void, X> allowing(Proc1<P1, X> method) {
            return given(Cardinality.ALLOWING, method);
        }

        public <P1, X extends Throwable> MethodCapture1<P1, Void, X> given(Cardinality times, Proc1<P1, X> method) {
            return new MethodCapture1<>((p) -> {method.apply(p); return null;}, times);
        }

        public <P1, R, X extends Throwable> MethodCapture1<P1, R, X> allowing(Function1<P1, R, X> method) {
            return given(Cardinality.ALLOWING, method);
        }

        public <P1, R, X extends Throwable> MethodCapture1<P1, R, X> given(Cardinality times, Function1<P1, R, X> method) {
            return new MethodCapture1<>(method, times);
        }

        private class MethodCapture1<P1, R, X extends Throwable> {
            private final Function1<P1, R, X> method;
            private final Cardinality cardinality;

            public MethodCapture1(Function1<P1, R, X> method, Cardinality cardinality) {
                this.method = method;
                this.cardinality = cardinality;
            }

            public Will with(P1 p1) {
                return new Will();
            }

            public Will with(Matcher<P1> p1) {
                return new Will();
            }

            private class Will {
                public void will(FallibleSupplier<R, X> supplier) {
                    will(new SupplierAction<>(supplier));
                }

                public void will(Action action) {
                }

                public void will(Function1<P1, R, X> f) {
                    will(new Function1Action<>(f));
                }
            }
        }


        public <P1, P2, R, X extends Throwable> MethodCapture2<P1, P2, R, X> allowing(Function2<P1,  P2,  R, X> method) {
            return given(Cardinality.ALLOWING, method);
        }

        public <P1, P2, R, X extends Throwable> MethodCapture2<P1, P2, R, X> given(Cardinality times, Function2<P1, P2, R, X> method) {
            return new MethodCapture2<>(method, times);
        }

        private class MethodCapture2<P1, P2, R, X extends Throwable> {
            private final Function2<P1, P2, R, X> method;
            private final Cardinality cardinality;

            public MethodCapture2(Function2<P1, P2, R, X> method, Cardinality cardinality) {
                this.method = method;
                this.cardinality = cardinality;
            }

            public Will<R> with(P1 p1, P2 p2) {
                return new Will<R>();
            }

            public Will<R> with(Matcher<P1> p1, Matcher<P2> p2) {
                return new Will<R>();
            }

            private class Will<R> {
                public void will(FallibleSupplier<R, X> supplier) {
                    will(new SupplierAction<>(supplier));
                }

                public void will(Action action) {
                }

                public void will(Function2<P1, P2, R, X> f) {
                    will(new Function2Action<>(f));
                }
            }
        }

        public <P1, P2, P3, R, X extends Throwable> MethodCapture3<P1, P2, P3, R, X> allowing(Function3<P1, P2, P3, R, X> method) {
            return given(Cardinality.ALLOWING, method);
        }

        public <P1, P2, P3, R, X extends Throwable> MethodCapture3<P1, P2, P3, R, X> given(Cardinality times, Function3<P1, P2, P3, R, X> method) {
            return new MethodCapture3<>(method, times);
        }

        private class MethodCapture3<P1, P2, P3, R, X extends Throwable> {
            private final Function3<P1, P2, P3, R, X> method;
            private final Cardinality cardinality;

            public MethodCapture3(Function3<P1, P2, P3, R, X> method, Cardinality cardinality) {
                this.method = method;
                this.cardinality = cardinality;
            }

            public Will<R> with(P1 p1, P2 p2, P3 p3) {
                return new Will<R>();
            }

            public Will<R> with(Matcher<P1> p1, Matcher<P2> p2, Matcher<P3> p3) {
                return new Will<R>();
            }

            private class Will<R> {
                public void will(FallibleSupplier<R, X> supplier) {
                    will(new SupplierAction<>(supplier));
                }

                public void will(Action action) {
                }

                public void will(Function3<P1, P2, P3, R, X> f) {
                    will(new Function3Action<>(f));
                }
            }
        }
    }

    private class Expectations8 extends Expectations {
        public <R, X extends Throwable> void __will(FallibleSupplier<R, X> supplier) {
            will(new SupplierAction<>(supplier));
        }

        public <P1, P2, R, X extends Throwable> void __will(Function2<P1, P2, R, X> f) {
            will(new Function2Action<>(f));
        }
        public <P1, P2, P3, R, X extends Throwable> void __will(Function3<P1, P2, P3, R, X> f) {
            will(new Function3Action<>(f));
        }
    }

}
