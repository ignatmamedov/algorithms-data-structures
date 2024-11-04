package nl.saxion.cds.solution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.solution.model.Station;
import nl.saxion.cds.solution.tools.CSVReader;
import nl.saxion.cds.solution.util.MyArrayList;
import nl.saxion.cds.solution.util.MyGraph;
import nl.saxion.cds.solution.util.MyHashMap;

/**
 * MainGUI is a graphical user interface that visualizes a graph of railway stations in the Netherlands.
 * It allows users to see all stations, highlight minimum cost spanning trees (MCST),
 * and find the shortest path between two stations by clicking and moving the mouse.
 */
public class MainGUI extends JPanel {
    final MyGraph<Station> graph;
    final MyGraph<Station> mcst;
    final MyHashMap<String, Station> stations;
    private final double minLongitude;
    private final double maxLongitude;
    private final double minLatitude;
    private final double maxLatitude;
    private BufferedImage mapImage;
    boolean drawMcstOnly = false;

    Station startStation = null;
    Station endStation = null;
    SaxList<Station> route = null;

    /**
     * Constructs the MainGUI component to visualize a graph of railway stations.
     *
     * @param graph the full graph of stations and tracks
     * @param mcst the minimum cost spanning tree of the station graph
     * @param stations a map of station codes to station data
     */
    public MainGUI(MyGraph<Station> graph, MyGraph<Station> mcst, MyHashMap<String, Station> stations) {
        this.graph = graph;
        this.mcst = mcst;
        this.stations = stations;

        try {
            mapImage = ImageIO.read(new File("resources/Nederland.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        double tempMinLon = Double.MAX_VALUE;
        double tempMaxLon = Double.MIN_VALUE;
        double tempMinLat = Double.MAX_VALUE;
        double tempMaxLat = Double.MIN_VALUE;

        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            if ("NL".equals(station.country())) {
                double lon = station.coordinate().longitude();
                double lat = station.coordinate().latitude();
                if (lon < tempMinLon) tempMinLon = lon;
                if (lon > tempMaxLon) tempMaxLon = lon;
                if (lat < tempMinLat) tempMinLat = lat;
                if (lat > tempMaxLat) tempMaxLat = lat;
            }
        }

        minLongitude = tempMinLon - 0.3;
        maxLongitude = tempMaxLon;
        minLatitude = tempMinLat;
        maxLatitude = tempMaxLat + 0.1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Station clickedStation = getStationAtPoint(e.getX(), e.getY());
                if (clickedStation != null) {
                    startStation = clickedStation;
                    endStation = null;
                    route = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                if (startStation != null) {
                    endStation = findNearestStation(e.getX(), e.getY());
                    findRoute();
                    repaint();
                }
            }
        });
    }

    /**
     * Finds the station nearest to the given screen coordinates.
     *
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     * @return the nearest Station object
     */
    Station findNearestStation(int mouseX, int mouseY) {
        double minDistance = Double.MAX_VALUE;
        Station nearestStation = null;

        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            if (!"NL".equals(station.country())) continue;

            int x = getXCoordinate(station);
            int y = getYCoordinate(station);

            double distance = Math.hypot(mouseX - x, mouseY - y);
            if (distance < minDistance) {
                minDistance = distance;
                nearestStation = station;
            }
        }
        return nearestStation;
    }

    /**
     * Sets whether to draw only the Minimum Cost Spanning Tree (MCST) on the map.
     *
     * @param drawMcstOnly true if only the MCST should be drawn; false if the full graph should be drawn
     */
    public void setDrawMcstOnly(boolean drawMcstOnly) {
        this.drawMcstOnly = drawMcstOnly;
        repaint();
    }

    private final int stationRadius = 3;

    /**
     * Paints the graphical representation of the station graph, with options for full graph,
     * MCST, and selected routes.
     *
     * @param g the Graphics object for drawing the panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (mapImage != null) {
            g2.drawImage(mapImage, 0, 0, getWidth(), getHeight(), null);
        }

        g2.setStroke(new BasicStroke(1.0f));

        MyGraph<Station> currentGraph = drawMcstOnly ? mcst : graph;

        g2.setColor(Color.RED);
        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            if (!"NL".equals(station.country())) continue;

            int x = getXCoordinate(station);
            int y = getYCoordinate(station);

            try {
                SaxList<SaxGraph.DirectedEdge<Station>> edges = currentGraph.getEdges(station);
                for (SaxGraph.DirectedEdge<Station> edge : edges) {
                    Station target = edge.to();
                    if (!"NL".equals(target.country())) continue;

                    int targetX = getXCoordinate(target);
                    int targetY = getYCoordinate(target);

                    g2.drawLine(x, y, targetX, targetY);
                }
            } catch (KeyNotFoundException e) {
                System.out.println("Station not found in graph: " + e.getMessage());
            }
        }

        g2.setColor(Color.BLUE);
        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            if (!"NL".equals(station.country())) continue;

            int x = getXCoordinate(station);
            int y = getYCoordinate(station);

            g2.fillOval(x - stationRadius, y - stationRadius, stationRadius * 2, stationRadius * 2);
        }

        if (route != null) {
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(2.0f));

            for (int i = 0; i < route.size() - 1; i++) {
                Station fromStation = route.get(i);
                Station toStation = route.get(i + 1);

                int x1 = getXCoordinate(fromStation);
                int y1 = getYCoordinate(fromStation);
                int x2 = getXCoordinate(toStation);
                int y2 = getYCoordinate(toStation);

                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    /**
     * Converts the longitude of a station to the x-coordinate on the screen.
     *
     * @param station the Station object
     * @return the x-coordinate on the panel
     */
    int getXCoordinate(Station station) {
        return (int) ((station.coordinate().longitude() - minLongitude) / (maxLongitude - minLongitude) * getWidth());
    }

    /**
     * Converts the latitude of a station to the y-coordinate on the screen.
     *
     * @param station the Station object
     * @return the y-coordinate on the panel
     */
    int getYCoordinate(Station station) {
        return (int) ((maxLatitude - station.coordinate().latitude()) / (maxLatitude - minLatitude) * getHeight());
    }

    /**
     * Finds the station located at or near the given screen coordinates.
     *
     * @param x the x-coordinate on the screen
     * @param y the y-coordinate on the screen
     * @return the Station at the specified location, or null if none found
     */
    Station getStationAtPoint(int x, int y) {
        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            if (!"NL".equals(station.country())) continue;

            int stationX = getXCoordinate(station);
            int stationY = getYCoordinate(station);

            if (Math.hypot(x - stationX, y - stationY) <= stationRadius * 2) {
                return station;
            }
        }
        return null;
    }

    /**
     * Finds the route between the currently selected start and end stations using the A* algorithm.
     */
    private void findRoute() {
        if (startStation != null && endStation != null) {
            SaxList<SaxGraph.DirectedEdge<Station>> pathEdges = graph.shortestPathAStar(startStation, endStation, new StationEstimator());
            if (pathEdges != null) {
                route = new MyArrayList<>();
                route.addLast(startStation);
                for (SaxGraph.DirectedEdge<Station> edge : pathEdges) {
                    route.addLast(edge.to());
                }
            } else {
                route = null;
            }
        } else {
            route = null;
        }
    }

    /**
     * An estimator for the distance between two stations based on geographical coordinates.
     */
    public static class StationEstimator implements SaxGraph.Estimator<Station> {
        @Override
        public double estimate(Station from, Station to) {
            double dx = from.coordinate().longitude() - to.coordinate().longitude();
            double dy = from.coordinate().latitude() - to.coordinate().latitude();
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    /**
     * Sets up and displays the GUI for the graph visualizer.
     *
     * @param graph    the graph of stations and tracks
     * @param mcst     the minimum cost spanning tree of the station graph
     * @param stations the map of station codes to station data
     */
    public static void createAndShowGUI(MyGraph<Station> graph, MyGraph<Station> mcst, MyHashMap<String, Station> stations) {
        JFrame frame = new JFrame("Graph Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        MainGUI visualizer = new MainGUI(graph, mcst, stations);

        JCheckBox mcstCheckBox = new JCheckBox("MCST");
        mcstCheckBox.addActionListener(e -> visualizer.setDrawMcstOnly(mcstCheckBox.isSelected()));

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(mcstCheckBox);

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(visualizer, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Main method for launching the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        CSVReader dataLoader = new CSVReader();
        MyHashMap<String, Station> stations = dataLoader.loadStations("resources/stations.csv");
        MyGraph<Station> graph = dataLoader.loadTracks("resources/tracks.csv", stations);

        MyGraph<Station> mcst = (MyGraph<Station>) graph.minimumCostSpanningTree();

        createAndShowGUI(graph, mcst, stations);
    }
}
