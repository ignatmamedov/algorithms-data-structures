package nl.saxion.cds.solution.tools;

import nl.saxion.cds.solution.model.Coordinate;
import nl.saxion.cds.solution.model.Station;
import nl.saxion.cds.solution.util.MyGraph;
import nl.saxion.cds.solution.util.MyHashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class for reading CSV files and loading station and track data into appropriate data structures.
 */
public class CSVReader {

    /**
     * Creates a BufferedReader for reading a file.
     *
     * @param filename the path to the file
     * @return a BufferedReader to read the file
     * @throws IOException if an I/O error occurs
     */
    protected BufferedReader createBufferedReader(String filename) throws IOException {
        return new BufferedReader(new FileReader(filename));
    }

    /**
     * Loads station data from a CSV file and stores it in a hash map.
     *
     * @param filename the path to the CSV file containing station data
     * @return a MyHashMap with station codes as keys and Station objects as values
     */
    public MyHashMap<String, Station> loadStations(String filename) {
        MyHashMap<String, Station> stations = new MyHashMap<>();
        try (BufferedReader br = createBufferedReader(filename)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String code = tokens[0];
                String name = tokens[1];
                String country = tokens[2];
                String type = tokens[3];
                double latitude = Double.parseDouble(tokens[4]);
                double longitude = Double.parseDouble(tokens[5]);
                Coordinate coord = new Coordinate(latitude, longitude);
                Station station = new Station(code, name, country, type, coord);
                stations.add(code, station);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    /**
     * Loads track data from a CSV file, adds stations to a graph, and creates bidirectional edges between them.
     *
     * @param filename the path to the CSV file containing track data
     * @param stations a MyHashMap of station codes to Station objects
     * @return a MyGraph representing stations as vertices and tracks as bidirectional edges
     */
    public MyGraph<Station> loadTracks(String filename, MyHashMap<String, Station> stations) {
        MyGraph<Station> graph = new MyGraph<>();
        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            graph.addVertex(station);
        }
        try (BufferedReader br = createBufferedReader(filename)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String fromCode = tokens[0];
                String toCode = tokens[1];
                double distance = Double.parseDouble(tokens[3]);

                Station fromStation = stations.get(fromCode);
                Station toStation = stations.get(toCode);

                if (fromStation != null && toStation != null) {
                    graph.addEdgeBidirectional(fromStation, toStation, distance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }
}
