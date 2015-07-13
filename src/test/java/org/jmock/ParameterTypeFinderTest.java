package org.jmock;

import org.hamcrest.Matchers;
import org.jmock.function.Func1;
import org.jmock.function.Func2;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jmock.function.internal.ParameterTypeFinder.findParameterTypes;

public class ParameterTypeFinderTest {

    @Test
    public void one_object() {
        Func1<String, String, RuntimeException> f = this::stringMethod;
        assertThat(findParameterTypes(f::applyArgs, 1), Matchers.arrayContaining(String.class));
    }

    @Test
    public void one_primitive() {
        Func1<Integer, String, RuntimeException> f = this::intMethod;
        assertThat(findParameterTypes(f::applyArgs, 1), Matchers.arrayContaining(Integer.class));
    }

    @Test
    public void object_primitive() {
        Func2<String, Integer, String, RuntimeException> f = this::stringIntMethod;
        assertThat(findParameterTypes(f::applyArgs, 2), Matchers.arrayContaining(String.class, Integer.class));
    }

    @Test
    public void primitive_object() {
        Func2<Integer, String, String, RuntimeException> f = this::intStringMethod;
        assertThat(findParameterTypes(f::applyArgs, 2), Matchers.arrayContaining(Integer.class, String.class));
    }

    public String stringMethod(String s) {
        return "hello";
    }

    public String intMethod(int i) {
        return "hello";
    }

    public String stringIntMethod(String s, int i) {
        return "hello";
    }

    public String intStringMethod(int i, String s) {
        return "hello";
    }

}
