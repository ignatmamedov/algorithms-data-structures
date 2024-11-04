package nl.saxion.cds.solution.tools;

import nl.saxion.cds.solution.model.Coordinate;
import nl.saxion.cds.solution.model.Station;
import nl.saxion.cds.solution.util.MyGraph;
import nl.saxion.cds.solution.util.MyHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVReaderTest {

    private CSVReader csvReader;

    @BeforeEach
    public void setUp() {
        csvReader = new CSVReader() {
            @Override
            protected BufferedReader createBufferedReader(String filename) throws IOException {
                return mockBufferedReader(filename);
            }
        };
    }

    private BufferedReader mockBufferedReader(String filename) throws IOException {
        if (filename.equals("stations.csv")) {
            BufferedReader brMock = mock(BufferedReader.class);
            when(brMock.readLine())
                    .thenReturn("code,name,country,type,latitude,longitude")
                    .thenReturn("ST1,Station One,Country A,Type A,52.0,5.0")
                    .thenReturn("ST2,Station Two,Country B,Type B,53.0,6.0")
                    .thenReturn(null);
            return brMock;
        } else if (filename.equals("tracks.csv")) {
            BufferedReader brMock = mock(BufferedReader.class);
            when(brMock.readLine())
                    .thenReturn("from_code,to_code,type,distance")
                    .thenReturn("ST1,ST2,Type X,100.0")
                    .thenReturn(null);
            return brMock;
        } else if (filename.equals("stations_exception.csv")) {
            throw new IOException("Test IOException");
        } else if (filename.equals("tracks_exception.csv")) {
            throw new IOException("Test IOException");
        } else if (filename.equals("stations_missing.csv")) {
            BufferedReader brMock = mock(BufferedReader.class);
            when(brMock.readLine())
                    .thenReturn("code,name,country,type,latitude,longitude")
                    .thenReturn("ST1,Station One,Country A,Type A,52.0,5.0")
                    .thenReturn(null);
            return brMock;
        } else if (filename.equals("tracks_missing_station.csv")) {
            BufferedReader brMock = mock(BufferedReader.class);
            when(brMock.readLine())
                    .thenReturn("from_code,to_code,type,distance")
                    .thenReturn("ST1,ST2,Type X,100.0")
                    .thenReturn(null);
            return brMock;
        } else {
            throw new IOException("File not found: " + filename);
        }
    }

    @Test
    public void testLoadStations() {
        MyHashMap<String, Station> stations = csvReader.loadStations("stations.csv");

        assertEquals(2, stations.size());

        Station station1 = stations.get("ST1");
        assertNotNull(station1);
        assertEquals("Station One", station1.name());
        assertEquals("Country A", station1.country());
        assertEquals("Type A", station1.type());
        assertEquals(new Coordinate(52.0, 5.0), station1.coordinate());

        Station station2 = stations.get("ST2");
        assertNotNull(station2);
        assertEquals("Station Two", station2.name());
        assertEquals("Country B", station2.country());
        assertEquals("Type B", station2.type());
        assertEquals(new Coordinate(53.0, 6.0), station2.coordinate());
    }

    @Test
    public void testLoadStationsWithIOException() {
        csvReader = new CSVReader() {
            @Override
            protected BufferedReader createBufferedReader(String filename) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        MyHashMap<String, Station> stations = csvReader.loadStations("stations.csv");

        assertNotNull(stations);
        assertEquals(0, stations.size());
    }

    @Test
    public void testLoadTracks() {
        MyHashMap<String, Station> stations = csvReader.loadStations("stations.csv");
        MyGraph<Station> graph = csvReader.loadTracks("tracks.csv", stations);

        assertEquals(2, graph.size());

        Station st1 = stations.get("ST1");
        Station st2 = stations.get("ST2");

        assertNotNull(st1);
        assertNotNull(st2);

        assertTrue(graph.getEdges(st1).size() > 0);
        assertTrue(graph.getEdges(st2).size() > 0);

    }

    @Test
    public void testLoadTracksWithIOException() {
        MyHashMap<String, Station> stations = new MyHashMap<>();

        csvReader = new CSVReader() {
            @Override
            protected BufferedReader createBufferedReader(String filename) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        MyGraph<Station> graph = csvReader.loadTracks("tracks.csv", stations);

        assertNotNull(graph);
        assertEquals(0, graph.size());
    }

    @Test
    public void testLoadTracksWithMissingStations() {
        MyHashMap<String, Station> stations = csvReader.loadStations("stations_missing.csv");
        MyGraph<Station> graph = csvReader.loadTracks("tracks_missing_station.csv", stations);

        assertNull(stations.get("ST2"));

        assertEquals(1, graph.size());

        Station st1 = stations.get("ST1");
        assertNotNull(st1);

        assertEquals(0, graph.getEdges(st1).size());
    }
}
