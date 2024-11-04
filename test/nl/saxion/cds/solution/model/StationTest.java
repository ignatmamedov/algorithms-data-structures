package nl.saxion.cds.solution.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class StationTest {

    @Test
    void GivenStations_WhenComparingByName_ThenComparisonReturnsZeroForEqualNames() {
        Station station1 = new Station("HNK", "Hoorn Kersenboogerd", "NL", "stoptreinstation", new Coordinate(52.653610229492, 5.0855555534363));
        Station station2 = new Station("RAT", "Hoorn Kersenboogerd", "NL", "stoptreinstation", new Coordinate(52.391666412354, 6.2775001525879));
        assertEquals(0, station1.compareTo(station2));
    }

    @Test
    void GivenStations_WhenComparingByName_ThenComparisonReturnsNegativeForAlphabeticallyEarlierName() {
        Station station1 = new Station("HNK", "Amsterdam", "NL", "megastation", new Coordinate(52.3676, 4.9041));
        Station station2 = new Station("RAT", "Rotterdam", "NL", "megastation", new Coordinate(51.9225, 4.47917));
        assertTrue(station1.compareTo(station2) < 0);
    }

    @Test
    void GivenStations_WhenComparingByName_ThenComparisonReturnsPositiveForAlphabeticallyLaterName() {
        Station station1 = new Station("HNK", "Utrecht", "NL", "megastation", new Coordinate(52.0907, 5.12142));
        Station station2 = new Station("RAT", "Den Haag", "NL", "megastation", new Coordinate(52.0705, 4.3007));
        assertTrue(station1.compareTo(station2) > 0);
    }

    @Test
    void GivenStation_WhenCallingToString_ThenReturnsFormattedString() {
        Station station = new Station("HNK", "Hoorn Kersenboogerd", "NL", "stoptreinstation", new Coordinate(52.653610229492, 5.0855555534363));
        String expected = "Station Code: HNK, Name: Hoorn Kersenboogerd, Country: NL, Type: stoptreinstation, Coordinate: (52.653610229492:5.0855555534363)";
        assertEquals(expected, station.toString());
    }

    @Test
    void GivenStation_WhenGettingCode_ThenReturnsCorrectCode() {
        Station station = new Station("HNK", "Hoorn Kersenboogerd", "NL", "stoptreinstation", new Coordinate(52.653610229492, 5.0855555534363));
        assertEquals("HNK", station.code());
    }

    @Test
    void GivenStation_WhenGettingName_ThenReturnsCorrectName() {
        Station station = new Station("HNK", "Hoorn Kersenboogerd", "NL", "stoptreinstation", new Coordinate(52.653610229492, 5.0855555534363));
        assertEquals("Hoorn Kersenboogerd", station.name());
    }

    @Test
    void GivenStation_WhenGettingCountry_ThenReturnsCorrectCountry() {
        Station station = new Station("HNK", "Hoorn Kersenboogerd", "NL", "stoptreinstation", new Coordinate(52.653610229492, 5.0855555534363));
        assertEquals("NL", station.country());
    }

    @Test
    void GivenStation_WhenGettingType_ThenReturnsCorrectType() {
        Station station = new Station("HNK", "Hoorn Kersenboogerd", "NL", "stoptreinstation", new Coordinate(52.653610229492, 5.0855555534363));
        assertEquals("stoptreinstation", station.type());
    }

    @Test
    void GivenStation_WhenGettingCoordinate_ThenReturnsCorrectCoordinate() {
        Coordinate coord = new Coordinate(52.653610229492, 5.0855555534363);
        Station station = new Station("HNK", "Hoorn Kersenboogerd", "NL", "stoptreinstation", coord);
        assertEquals(coord, station.coordinate());
    }
}