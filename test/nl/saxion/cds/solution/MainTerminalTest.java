package nl.saxion.cds.solution;

import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.solution.model.Coordinate;
import nl.saxion.cds.solution.model.Station;
import nl.saxion.cds.solution.util.MyArrayList;
import nl.saxion.cds.solution.util.MyGraph;
import nl.saxion.cds.solution.util.MyHashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainTerminalTest {

    private MyHashMap<String, Station> stationsMock;
    private MyGraph<Station> graphMock;

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        stationsMock = mock(MyHashMap.class);
        graphMock = mock(MyGraph.class);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testDisplayMenu() {
        MainTerminal.displayMenu();
        assertTrue(outContent.toString().contains("--- Track Manager Application ---"));
    }

    @Test
    public void testShowStationByCode_ExistingStation() {
        Station station = new Station("ST1", "Station One", "Country A", "Type A", new Coordinate(52.0, 5.0));
        when(stationsMock.get("ST1")).thenReturn(station);

        MainTerminal.showStationByCode(stationsMock, "ST1");

        verify(stationsMock).get("ST1");
    }

    @Test
    public void testShowStationByCode_NonExistingStation() {
        when(stationsMock.get("ST2")).thenReturn(null);

        MainTerminal.showStationByCode(stationsMock, "ST2");

        verify(stationsMock).get("ST2");
    }

    @Test
    public void testSearchStationsByName_NoMatches() {
        when(stationsMock.getKeys()).thenReturn(new MyArrayList<>());

        MainTerminal.searchStationsByName(stationsMock, "NotExist");

        verify(stationsMock).getKeys();
    }



    @Test
    public void testListStationsByType_NoMatches() {
        when(stationsMock.getKeys()).thenReturn(new MyArrayList<>());

        MainTerminal.listStationsByType(stationsMock, "Type X");

        verify(stationsMock).getKeys();
    }

    @Test
    public void testListStationsByType_MultipleMatches() {
        MyArrayList<String> keys = new MyArrayList<>();
        keys.addLast("ST1");
        keys.addLast("ST2");

        Station station1 = new Station("ST1", "Station One", "Country A", "Type A", new Coordinate(52.0, 5.0));
        Station station2 = new Station("ST2", "Station Two", "Country B", "Type A", new Coordinate(53.0, 6.0));

        when(stationsMock.getKeys()).thenReturn(keys);
        when(stationsMock.get("ST1")).thenReturn(station1);
        when(stationsMock.get("ST2")).thenReturn(station2);

        MainTerminal.listStationsByType(stationsMock, "Type A");

        verify(stationsMock).getKeys();
    }

    @Test
    public void testFindShortestRoute_NoPathFound() {
        Station startStation = new Station("ST1", "Station One", "Country A", "Type A", new Coordinate(52.0, 5.0));
        Station endStation = new Station("ST2", "Station Two", "Country B", "Type B", new Coordinate(53.0, 6.0));

        when(graphMock.shortestPathAStar(eq(startStation), eq(endStation), any())).thenReturn(null);

        MainTerminal.findShortestRoute(graphMock, startStation, endStation);

        verify(graphMock).shortestPathAStar(eq(startStation), eq(endStation), any());
    }

    @Test
    public void testFindShortestRoute_PathFound() {
        Station startStation = new Station("ST1", "Station One", "Country A", "Type A", new Coordinate(52.0, 5.0));
        Station endStation = new Station("ST2", "Station Two", "Country B", "Type B", new Coordinate(53.0, 6.0));

        SaxList<SaxGraph.DirectedEdge<Station>> path = new MyArrayList<>();
        path.addLast(new MyGraph.DirectedEdge<>(startStation, endStation, 100.0));

        when(graphMock.shortestPathAStar(eq(startStation), eq(endStation), any())).thenReturn(path);

        MainTerminal.findShortestRoute(graphMock, startStation, endStation);

        verify(graphMock).shortestPathAStar(eq(startStation), eq(endStation), any());
    }


    @Test
    public void testComputeMCST() {
        SaxGraph<Station> mcstMock = mock(SaxGraph.class);
        when(graphMock.minimumCostSpanningTree()).thenReturn(mcstMock);
        when(mcstMock.getTotalWeight()).thenReturn(200.0);

        when(mcstMock.getEdges(any())).thenReturn(new MyArrayList<>());

        Iterator<Station> emptyIterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Station next() {
                throw new NoSuchElementException();
            }
        };
        when(mcstMock.iterator()).thenReturn(emptyIterator);

        MainTerminal.computeMCST(graphMock);

        verify(graphMock).minimumCostSpanningTree();
        verify(mcstMock).getTotalWeight();
    }

    @Test
    public void testSearchStationsByName_MultipleMatches() {
        MyArrayList<String> keys = new MyArrayList<>();
        keys.addLast("ST1");
        keys.addLast("ST2");

        Station station1 = new Station("ST1", "Station One", "Country A", "Type A", new Coordinate(52.0, 5.0));
        Station station2 = new Station("ST2", "Station Two", "Country B", "Type B", new Coordinate(53.0, 6.0));

        when(stationsMock.getKeys()).thenReturn(keys);
        when(stationsMock.get("ST1")).thenReturn(station1);
        when(stationsMock.get("ST2")).thenReturn(station2);

        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MainTerminal.searchStationsByName(stationsMock, "Station");

        verify(stationsMock).getKeys();

        assertTrue(outContent.toString().contains("Station One"));
    }
}
