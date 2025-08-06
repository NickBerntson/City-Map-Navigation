import java.util.*;

public class CityMap {
    private Map<String, List<Edge>> graph;
    private boolean directed;

    public CityMap(boolean directed) {
        this.graph = new HashMap<>();
        this.directed = directed;
    }

    private static class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public void addIntersection(String name) {
        if (graph.containsKey(name)) {
            System.out.println("Intersection '" + name + "' already exists.");
        } else {
            graph.put(name, new ArrayList<>());
            System.out.println("Intersection '" + name + "' added.");
        }
    }

    public void addRoad(String src, String dest, int weight) {
        if (!graph.containsKey(src) || !graph.containsKey(dest)) {
            System.out.println("Both intersections must exist.");
            return;
        }
        graph.get(src).add(new Edge(dest, weight));
        if (!directed) {
            graph.get(dest).add(new Edge(src, weight));
        }
        System.out.println("Road added from " + src + " to " + dest + " with weight " + weight + ".");
    }

    public void removeIntersection(String name) {
        if (!graph.containsKey(name)) {
            System.out.println("Intersection '" + name + "' does not exist.");
            return;
        }
        graph.remove(name);
        for (List<Edge> edges : graph.values()) {
            edges.removeIf(edge -> edge.destination.equals(name));
        }
        System.out.println("Intersection '" + name + "' removed.");
    }

    public void removeRoad(String src, String dest) {
        if (graph.containsKey(src)) {
            graph.get(src).removeIf(edge -> edge.destination.equals(dest));
        }
        if (!directed && graph.containsKey(dest)) {
            graph.get(dest).removeIf(edge -> edge.destination.equals(src));
        }
        System.out.println("Road between " + src + " and " + dest + " removed.");
    }

    public void displayMap() {
        System.out.println("\nCity Map:");
        for (String node : graph.keySet()) {
            System.out.print(node + " -> ");
            for (Edge edge : graph.get(node)) {
                System.out.print(edge.destination + " (" + edge.weight + ") ");
            }
            System.out.println();
        }
    }

    public boolean pathExists(String start, String end) {
        Set<String> visited = new HashSet<>();
        boolean exists = dfs(start, end, visited);
        System.out.println("Path exists from " + start + " to " + end + ": " + (exists ? "Yes" : "No"));
        return exists;
    }

    private boolean dfs(String current, String target, Set<String> visited) {
        if (!graph.containsKey(current)) return false;
        if (current.equals(target)) return true;
        visited.add(current);

        for (Edge edge : graph.get(current)) {
            if (!visited.contains(edge.destination)) {
                if (dfs(edge.destination, target, visited)) return true;
            }
        }
        return false;
    }

    public void listReachable(String start) {
        Set<String> visited = new HashSet<>();
        dfsReachable(start, visited);
        visited.remove(start); 
        System.out.println("Reachable intersections from " + start + ": " + visited);
    }

    private void dfsReachable(String current, Set<String> visited) {
        if (!graph.containsKey(current)) return;
        visited.add(current);
        for (Edge edge : graph.get(current)) {
            if (!visited.contains(edge.destination)) {
                dfsReachable(edge.destination, visited);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CityMap map = new CityMap(false); 

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Intersection");
            System.out.println("2. Add Road");
            System.out.println("3. Remove Intersection");
            System.out.println("4. Remove Road");
            System.out.println("5. Display Map");
            System.out.println("6. Check Path Existence");
            System.out.println("7. List Reachable Intersections");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter intersection name: ");
                    String intersection = scanner.nextLine();
                    map.addIntersection(intersection);
                    break;

                case "2":
                    System.out.print("Enter source intersection: ");
                    String src = scanner.nextLine();
                    System.out.print("Enter destination intersection: ");
                    String dest = scanner.nextLine();
                    System.out.print("Enter distance (default=1): ");
                    String weightInput = scanner.nextLine();
                    int weight = weightInput.isEmpty() ? 1 : Integer.parseInt(weightInput);
                    map.addRoad(src, dest, weight);
                    break;

                case "3":
                    System.out.print("Enter intersection name to remove: ");
                    String removeNode = scanner.nextLine();
                    map.removeIntersection(removeNode);
                    break;

                case "4":
                    System.out.print("Enter source intersection: ");
                    String srcRemove = scanner.nextLine();
                    System.out.print("Enter destination intersection: ");
                    String destRemove = scanner.nextLine();
                    map.removeRoad(srcRemove, destRemove);
                    break;

                case "5":
                    map.displayMap();
                    break;

                case "6":
                    System.out.print("Enter starting intersection: ");
                    String start = scanner.nextLine();
                    System.out.print("Enter destination intersection: ");
                    String end = scanner.nextLine();
                    map.pathExists(start, end);
                    break;

                case "7":
                    System.out.print("Enter intersection to check reachability from: ");
                    String reachStart = scanner.nextLine();
                    map.listReachable(reachStart);
                    break;

                case "8":
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}