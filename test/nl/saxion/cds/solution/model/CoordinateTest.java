package nl.saxion.cds.solution.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    void GivenCoordinates_WhenComparingEqualCoordinates_ThenComparisonReturnsZero() {
        Coordinate coord1 = new Coordinate(52.0, 4.0);
        Coordinate coord2 = new Coordinate(52.0, 4.0);
        assertEquals(0, coord1.compareTo(coord2));
    }

    @Test
    void GivenCoordinates_WhenComparingWithGreaterLatitude_ThenComparisonReturnsNegativeOne() {
        Coordinate coord1 = new Coordinate(52.0, 4.0);
        Coordinate coord2 = new Coordinate(53.0, 4.0);
        assertEquals(-1, coord1.compareTo(coord2));
    }

    @Test
    void GivenCoordinates_WhenComparingWithSmallerLatitude_ThenComparisonReturnsOne() {
        Coordinate coord1 = new Coordinate(53.0, 4.0);
        Coordinate coord2 = new Coordinate(52.0, 4.0);
        assertEquals(1, coord1.compareTo(coord2));
    }

    @Test
    void GivenCoordinates_WhenComparingWithEqualLatitudeAndGreaterLongitude_ThenComparisonReturnsNegativeOne() {
        Coordinate coord1 = new Coordinate(52.0, 4.0);
        Coordinate coord2 = new Coordinate(52.0, 5.0);
        assertEquals(-1, coord1.compareTo(coord2));
    }

    @Test
    void GivenCoordinates_WhenComparingWithEqualLatitudeAndSmallerLongitude_ThenComparisonReturnsOne() {
        Coordinate coord1 = new Coordinate(52.0, 5.0);
        Coordinate coord2 = new Coordinate(52.0, 4.0);
        assertEquals(1, coord1.compareTo(coord2));
    }

    @Test
    void GivenTwoCoordinates_WhenCalculatingHaversineDistance_ThenReturnsCorrectDistance() {
        Coordinate coord1 = new Coordinate(52.0, 4.0);
        Coordinate coord2 = new Coordinate(53.0, 5.0);
        double expectedDistance = 130.2;
        assertEquals(expectedDistance, Coordinate.haversineDistance(coord1, coord2), 0.1);
    }

    @Test
    void GivenCoordinate_WhenCallingToString_ThenReturnsFormattedString() {
        Coordinate coord = new Coordinate(52.0, 4.0);
        String expected = "(52.0:4.0)";
        assertEquals(expected, coord.toString());
    }
}