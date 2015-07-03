package org.jmock.function;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jmock.internal.ParametersMatcher;

import java.util.function.Predicate;

public class PredicateMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final Predicate<T> predicate;

    public PredicateMatcher(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected boolean matchesSafely(T t, Description description) {
        return predicate.test(t);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches a predicate");
    }
}
