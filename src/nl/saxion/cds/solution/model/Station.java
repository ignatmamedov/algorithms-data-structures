package nl.saxion.cds.solution.model;

/**
 * Represents a railway station with details such as code, name, country, type, and geographic coordinates.
 *
 * @param code       the unique code identifier for the station
 * @param name       the name of the station
 * @param country    the country where the station is located
 * @param type       the type of station (e.g., train, bus, etc.)
 * @param coordinate the geographic coordinate of the station
 */
public record Station(String code, String name, String country, String type,
                      Coordinate coordinate) implements Comparable<Station> {

    /**
     * Compares this station with another based on their names.
     *
     * @param other the other station to compare
     * @return a negative integer, zero, or a positive integer as this station's name is less than,
     * equal to, or greater than the other station's name
     */
    @Override
    public int compareTo(Station other) {
        return this.name.compareTo(other.name);
    }

    /**
     * Returns a string representation of the station, including its code, name, country, type, and coordinates.
     *
     * @return a formatted string describing the station
     */
    @Override
    public String toString() {
        return "Station Code: " + code + ", Name: " + name + ", Country: " + country + ", Type: " + type + ", Coordinate: " + coordinate;
    }
}
