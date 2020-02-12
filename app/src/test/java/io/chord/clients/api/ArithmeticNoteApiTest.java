package io.chord.clients.api;

import io.chord.clients.ApiClient;
import io.chord.clients.models.NoteDto;
import io.chord.clients.models.ValidationProblemDetails;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ArithmeticNoteApi
 */
public class ArithmeticNoteApiTest {

    private ArithmeticNoteApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(ArithmeticNoteApi.class);
    }


    /**
     * 
     *
     * 
     */
    @Test
    public void alterDownwardTest() {
        Integer semitones = null;
        NoteDto body = null;
        // NoteDto response = api.alterDownward(semitones, body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void alterUpwardTest() {
        Integer semitones = null;
        NoteDto body = null;
        // NoteDto response = api.alterUpward(semitones, body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void createTest() {
        NoteDto body = null;
        // NoteDto response = api.create(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void fromIntegralTest() {
        Integer index = null;
        // NoteDto response = api.fromIntegral(index);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void fromMidiTest() {
        Integer index = null;
        // NoteDto response = api.fromMidi(index);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void fromStringTest() {
        String note = null;
        // NoteDto response = api.fromString(note);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void getIntervalTest() {
        Integer degree = null;
        NoteDto body = null;
        // NoteDto response = api.getInterval(degree, body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void shiftToLeftTest() {
        Integer distance = null;
        NoteDto body = null;
        // NoteDto response = api.shiftToLeft(distance, body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void shiftToRightTest() {
        Integer distance = null;
        NoteDto body = null;
        // NoteDto response = api.shiftToRight(distance, body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void simplifyTest() {
        NoteDto body = null;
        // NoteDto response = api.simplify(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void toIntegralTest() {
        NoteDto body = null;
        // Integer response = api.toIntegral(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void toMidiTest() {
        NoteDto body = null;
        // Integer response = api.toMidi(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void toStringTest() {
        NoteDto body = null;
        // String response = api.toString(body);

        // TODO: test validations
    }
}
