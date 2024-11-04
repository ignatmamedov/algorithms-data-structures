package nl.saxion.cds.solution.model;

/**
 * Represents geographic coordinates with latitude and longitude values.
 *
 * @param latitude  the latitude of the coordinate
 * @param longitude the longitude of the coordinate
 */
public record Coordinate(double latitude, double longitude) implements Comparable<Coordinate> {

    /**
     * Calculate the distance in kilometers between this and another coordinate using the Haversine formula.
     * Code adopted from <a href="https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/">Geeks for Geeks</a>
     *
     * @param to calculating distance to this coordinate
     * @return distance in kilometers
     */
    public static double haversineDistance(Coordinate from, Coordinate to) {
        double dLat = Math.toRadians(to.latitude - from.latitude);
        double dLon = Math.toRadians(to.longitude - from.longitude);

        double lat1 = Math.toRadians(from.latitude);
        double lat2 = Math.toRadians(to.latitude);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                           Math.pow(Math.sin(dLon / 2), 2) *
                                   Math.cos(lat1) *
                                   Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return Math.round(rad * c * 10.0) / 10.0;
    }

    /**
     * Returns a string representation of the coordinate in (latitude:longitude) format.
     *
     * @return a string representing the coordinate
     */
    @Override
    public String toString() {
        return "(" + latitude + ":" + longitude + ')';
    }

    /**
     * Compare two Coordinates; there is no real usefully order, but it returns 0 if they are equal.
     * @param o the object to be compared.
     * @return 0 if equals, 1 if current is "bigger" and -1 if current is "smaller"
     */
    @Override
    public int compareTo(Coordinate o) {
        if (latitude == o.latitude && longitude == o.longitude)
            return 0;
        if (latitude > o.latitude)
            return 1;
        if (latitude == o.latitude && longitude > o.longitude)
            return 1;
        return -1;
    }
}
