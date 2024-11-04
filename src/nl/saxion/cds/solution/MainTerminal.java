package nl.saxion.cds.solution;

import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.solution.model.Coordinate;
import nl.saxion.cds.solution.model.Station;
import nl.saxion.cds.solution.tools.CSVReader;
import nl.saxion.cds.solution.util.*;

import java.util.Comparator;
import java.util.Scanner;

/**
 * MainTerminal class for the Track Manager Application. Loads station and track data from CSV files
 * and provides an interactive menu for users to view and search station information,
 * find routes, and compute a Minimum Cost Spanning Tree.
 */
public class MainTerminal {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Entry point of the application. Loads station and track data and initiates the menu.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        CSVReader dataLoader = new CSVReader();
        MyHashMap<String, Station> stations = dataLoader.loadStations("resources/stations.csv");
        MyGraph<Station> graph = dataLoader.loadTracks("resources/tracks.csv", stations);

        runApplication(stations, graph);
    }

    /**
     * Runs the main application loop, displaying the menu and processing user choices.
     *
     * @param stations the map of station codes to station data
     * @param graph    the graph of stations and their connecting tracks
     */
    public static void runApplication(MyHashMap<String, Station> stations, MyGraph<Station> graph) {
        int choice;
        do {
            displayMenu();
            choice = getUserChoice();
            executeChoice(choice, stations, graph);
        } while (choice != 0);
    }

    /**
     * Displays the main menu for the application.
     */
    public static void displayMenu() {
        System.out.println("\n--- Track Manager Application ---");
        System.out.println("1. Show station information by code");
        System.out.println("2. Search station by name");
        System.out.println("3. List stations by type");
        System.out.println("4. Find shortest route between two stations");
        System.out.println("5. Compute Minimum Cost Spanning Tree");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Retrieves the user's menu choice.
     *
     * @return the integer representing the user's choice
     */
    public static int getUserChoice() {
        return scanner.nextInt();
    }

    /**
     * Executes the user's chosen menu option.
     *
     * @param choice   the user's menu choice
     * @param stations the map of station codes to station data
     * @param graph    the graph of stations and their connecting tracks
     */
    public static void executeChoice(int choice, MyHashMap<String, Station> stations, MyGraph<Station> graph) {
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
                System.out.print("Enter station code: ");
                String code = scanner.nextLine();
                showStationByCode(stations, code);
            }
            case 2 -> {
                System.out.print("Enter station name (or partial): ");
                String namePart = scanner.nextLine();
                searchStationsByName(stations, namePart);
            }
            case 3 -> {
                System.out.print("Enter station type: ");
                String type = scanner.nextLine();
                listStationsByType(stations, type);
            }
            case 4 -> {
                System.out.print("Enter start station code: ");
                String startCode = scanner.nextLine();
                System.out.print("Enter end station code: ");
                String endCode = scanner.nextLine();
                Station startStation = stations.get(startCode);
                Station endStation = stations.get(endCode);
                if (startStation != null && endStation != null) {
                    findShortestRoute(graph, startStation, endStation);
                } else {
                    System.out.println("Invalid station codes entered.");
                }
            }
            case 5 -> computeMCST(graph);
            case 0 -> System.out.println("Exiting application.");
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Displays information about a station given its code.
     *
     * @param stations the map of station codes to station data
     * @param code     the code of the station to display
     */
    public static void showStationByCode(MyHashMap<String, Station> stations, String code) {
        Station station = stations.get(code);
        if (station != null) {
            System.out.println(station);
        } else {
            System.out.println("Station with code " + code + " not found.");
        }
    }

    /**
     * Searches and displays stations by name or partial name.
     *
     * @param stations the map of station codes to station data
     * @param namePart the partial name to search for
     */
    public static void searchStationsByName(MyHashMap<String, Station> stations, String namePart) {
        MyArrayList<Station> matchedStations = new MyArrayList<>();
        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            if (station.name().toLowerCase().startsWith(namePart.toLowerCase())) {
                matchedStations.addLast(station);
            }
        }

        if (matchedStations.isEmpty()) {
            System.out.println("No stations found starting with " + namePart);
        } else if (matchedStations.size() == 1) {
            System.out.println(matchedStations.get(0));
        } else {
            System.out.println("Multiple stations found:");
            for (int i = 0; i < matchedStations.size(); i++) {
                System.out.println((i + 1) + ". " + matchedStations.get(i).name());
            }

            System.out.print("Enter the number of the station you want to see: ");
            int choice = scanner.nextInt();
            System.out.println(matchedStations.get(choice - 1));
        }
    }

    /**
     * Lists stations of a specific type.
     *
     * @param stations the map of station codes to station data
     * @param type     the type of station to list
     */
    public static void listStationsByType(MyHashMap<String, Station> stations, String type) {
        MyArrayList<Station> matchedStations = new MyArrayList<>();
        for (String key : stations.getKeys()) {
            Station station = stations.get(key);
            if (station.type().equalsIgnoreCase(type)) {
                matchedStations.addLast(station);
            }
        }

        if (matchedStations.isEmpty()) {
            System.out.println("No stations found of type " + type);
        } else {
            matchedStations.quickSort(Comparator.comparing(Station::name));
            System.out.println("Stations of type " + type + ":");
            for (Station station : matchedStations) {
                System.out.println(station.name());
            }
        }
    }

    /**
     * Finds and displays the shortest route between two stations.
     *
     * @param graph the graph of stations and their connecting tracks
     * @param start the starting station
     * @param end   the destination station
     */
    public static void findShortestRoute(MyGraph<Station> graph, Station start, Station end) {
        SaxList<SaxGraph.DirectedEdge<Station>> path = graph.shortestPathAStar(start, end, (from, to) -> {
            return Coordinate.haversineDistance(from.coordinate(), to.coordinate());
        });

        if (path == null) {
            System.out.println("No path found between " + start.name() + " and " + end.name());
        } else {
            double totalDistance = 0;
            System.out.println("Shortest path:");
            for (SaxGraph.DirectedEdge<Station> edge : path) {
                System.out.println(edge.from().name() + " -> " + edge.to().name() + " : " + edge.weight() + " km");
                totalDistance += edge.weight();
            }
            System.out.println("Total distance: " + totalDistance + " km");
        }
    }

    /**
     * Computes and displays the Minimum Cost Spanning Tree (MCST) of the graph.
     *
     * @param graph the graph of stations and their connecting tracks
     */
    public static void computeMCST(MyGraph<Station> graph) {
        SaxGraph<Station> mcst = graph.minimumCostSpanningTree();
        double totalDistance = mcst.getTotalWeight();
        System.out.println("Minimum Cost Spanning Tree total length: " + totalDistance + " km");

        for (Station station : mcst) {
            for (SaxGraph.DirectedEdge<Station> edge : mcst.getEdges(station)) {
                System.out.println(edge.from().name() + " -> " + edge.to().name() + " : " + edge.weight() + " km");
            }
        }
    }
}
