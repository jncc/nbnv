package uk.org.nbn.nbnv.domain;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * Convenience for testing
 * if this was java 8 I could use static methods in the interface
 * but its nto so they are nto static
 *
 * @author stephen batty
 *         Date: 14/07/14
 *         Time: 16:38
 */
public interface JsonBean<T> {


    T fromJson(String json) throws IOException;
    T toJson() throws JsonProcessingException;
    T SampleTestData();
}
