package io.chord.clients.api;

import io.chord.clients.ApiClient;
import io.chord.clients.models.DegreeAndQualityIntervalDto;
import io.chord.clients.models.DegreeAndSemitonesIntervalDto;
import io.chord.clients.models.IntervalDto;
import io.chord.clients.models.NoteDto;
import io.chord.clients.models.NotePairDto;
import io.chord.clients.models.SemitonesAndQualityIntervalDto;
import io.chord.clients.models.ValidationProblemDetails;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ArithmeticIntervalApi
 */
public class ArithmeticIntervalApiTest {

    private ArithmeticIntervalApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(ArithmeticIntervalApi.class);
    }


    /**
     * 
     *
     * 
     */
    @Test
    public void fromDegreeAndQualityTest() {
        DegreeAndQualityIntervalDto body = null;
        // IntervalDto response = api.fromDegreeAndQuality(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void fromDegreeAndSemitonesTest() {
        DegreeAndSemitonesIntervalDto body = null;
        // IntervalDto response = api.fromDegreeAndSemitones(body);

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
        // IntervalDto response = api.fromIntegral(index);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void fromNotesTest() {
        NotePairDto body = null;
        // IntervalDto response = api.fromNotes(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void fromSemitonesAndQualityTest() {
        SemitonesAndQualityIntervalDto body = null;
        // IntervalDto response = api.fromSemitonesAndQuality(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void fromStringTest() {
        String interval = null;
        // IntervalDto response = api.fromString(interval);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void getLookUpTableTest() {
        // Map<String, Map<String, IntervalQuality>> response = api.getLookUpTable();

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void invertTest() {
        IntervalDto body = null;
        // IntervalDto response = api.invert(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void toIntegralTest() {
        IntervalDto body = null;
        // Integer response = api.toIntegral(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void toNoteTest() {
        String root = null;
        IntervalDto body = null;
        // NoteDto response = api.toNote(root, body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void toStringTest() {
        Object format = null;
        IntervalDto body = null;
        // String response = api.toString(format, body);

        // TODO: test validations
    }
}
