package com.mytaxi.android_demo.activities;

/**
 * Contains a static reference IdlingResource, and should be available only in a mock build type.
 */
public class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        SimpleCountingIdlingResource.increment();
    }

    public static void decrement() {
        SimpleCountingIdlingResource.decrement();
    }

    public static SimpleCountingIdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}