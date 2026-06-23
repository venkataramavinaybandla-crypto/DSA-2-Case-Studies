import java.util.*;

class MetroGraph {

    int V;
    List<int[]> edges = new ArrayList<>();
    int[] parent, rank;
    List<List<int[]>> adj;
    String[] stationNames;

    MetroGraph(int v, String[] names) {
        V = v;
        stationNames = names;
        parent = new int[v];
        rank = new int[v];
        adj = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            parent[i] = i;
            adj.add(new ArrayList<>());
        }
    }

    void addEdge(int u, int v, int w) {
        edges.add(new int[]{u, v, w});
        adj.get(u).add(new int[]{v, w});
        adj.get(v).add(new int[]{u, w});
    }

    int find(int x) {
        return parent[x] == x ? x : (parent[x] = find(parent[x]));
    }

    boolean union(int a, int b) {
        int pa = find(a), pb = find(b);
        if (pa == pb) return false;
        if (rank[pa] < rank[pb]) parent[pa] = pb;
        else if (rank[pa] > rank[pb]) parent[pb] = pa;
        else { parent[pb] = pa; rank[pa]++; }
        return true;
    }

    void kruskalMST() {
        System.out.println("--- KRUSKAL'S MST (Minimum Cost Network) ---");
        System.out.println("Sorting edges by weight ...");

        List<int[]> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort((a, b) -> a[2] - b[2]);

        int cost = 0;
        System.out.println("\nMST Edges Selected:");
        for (int[] e : sortedEdges) {
            if (union(e[0], e[1])) {
                System.out.printf("  %-20s -- %-20s : %d km [included]%n",
                    stationNames[e[0]], stationNames[e[1]], e[2]);
                cost += e[2];
            }
        }
        System.out.println("Total MST Cost: " + cost + " km (minimum track cost)");
    }

    void dijkstra(int src) {
        System.out.println("\n--- DIJKSTRA SHORTEST PATH from " + stationNames[src] + " (" + src + ") ---");

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0], d = cur[1];
            if (d > dist[u]) continue;
            for (int[] nb : adj.get(u)) {
                int v = nb[0], w = nb[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }

        for (int i = 0; i < V; i++)
            System.out.printf("  Station %d (%-18s) -> %d km%n", i, stationNames[i], dist[i]);
    }

    void bellmanFord(int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int i = 0; i < V - 1; i++)
            for (int[] e : edges) {
                if (dist[e[0]] != Integer.MAX_VALUE && dist[e[0]] + e[2] < dist[e[1]])
                    dist[e[1]] = dist[e[0]] + e[2];
                if (dist[e[1]] != Integer.MAX_VALUE && dist[e[1]] + e[2] < dist[e[0]])
                    dist[e[0]] = dist[e[1]] + e[2];
            }

        System.out.println("\n--- BELLMAN-FORD (from " + stationNames[src] + ") ---");
        for (int i = 0; i < V; i++)
            System.out.printf("  Station %d (%-18s) -> %d km%n", i, stationNames[i], dist[i]);
    }
}

public class BangaloreMetro {

    public static void main(String[] args) {

        String[] stations = {
            "Silk Board", "HSR Layout", "BTM Layout",
            "Jayanagar", "JP Nagar", "Electronic City"
        };

        MetroGraph graph = new MetroGraph(6, stations);

        graph.addEdge(0, 1, 4);  // Silk Board -- HSR Layout
        graph.addEdge(1, 2, 2);  // HSR Layout -- BTM Layout
        graph.addEdge(2, 3, 3);  // BTM Layout -- Jayanagar
        graph.addEdge(3, 4, 5);  // Jayanagar  -- JP Nagar
        graph.addEdge(4, 5, 7);  // JP Nagar   -- Electronic City
        graph.addEdge(0, 2, 6);  // Silk Board -- BTM Layout
        graph.addEdge(1, 3, 8);  // HSR Layout -- Jayanagar
        graph.addEdge(2, 4, 9);  // BTM Layout -- JP Nagar

        System.out.println("=====================================================");
        System.out.println("  BANGALORE METRO PHASE-3 — GRAPH ALGORITHMS        ");
        System.out.println("=====================================================");
        System.out.println();
        System.out.println("Stations:");
        for (int i = 0; i < stations.length; i++)
            System.out.println("  " + i + " = " + stations[i]);
        System.out.println();

        graph.kruskalMST();

        // Reset DSU for Dijkstra (fresh graph object for queries)
        MetroGraph g2 = new MetroGraph(6, stations);
        g2.addEdge(0, 1, 4);
        g2.addEdge(1, 2, 2);
        g2.addEdge(2, 3, 3);
        g2.addEdge(3, 4, 5);
        g2.addEdge(4, 5, 7);
        g2.addEdge(0, 2, 6);
        g2.addEdge(1, 3, 8);
        g2.addEdge(2, 4, 9);

        g2.dijkstra(0);

        g2.bellmanFord(0);

        System.out.println("\nAlgorithm Comparison:");
        System.out.println("  Dijkstra     : O(V^2)  faster for dense graphs (no negative weights)");
        System.out.println("  Bellman-Ford : O(VE)   handles negative weights");

        System.out.println("\nTime Complexity:");
        System.out.println("  Kruskal MST  : O(E log E)");
        System.out.println("  Dijkstra     : O(V^2) / O((V+E) log V) with Priority Queue");
        System.out.println("  Bellman-Ford : O(VE)");
        System.out.println("  Union-Find   : O(alpha(n)) ≈ O(1)");

        System.out.println("\nProcess finished with exit code 0");
    }
}
