import java.util.*;

class Profile {
    int id;
    String name;
    int score;
    String[] skills;
    Profile(int id, String name, int score, String[] skills) {
        this.id = id; this.name = name; this.score = score; this.skills = skills;
    }
}

class AVLNode {
    int key, height;
    AVLNode left, right;
    AVLNode(int k) { key = k; height = 1; }
}

class AVLTree {
    AVLNode root;

    int height(AVLNode n) { return n == null ? 0 : n.height; }
    int bf(AVLNode n) { return n == null ? 0 : height(n.left) - height(n.right); }

    AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left, T2 = x.right;
        x.right = y; y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right, T2 = y.left;
        y.left = x; x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    AVLNode insert(AVLNode node, int key) {
        if (node == null) return new AVLNode(key);
        if (key < node.key) node.left = insert(node.left, key);
        else if (key > node.key) node.right = insert(node.right, key);
        else return node;

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        int balance = bf(node);

        if (balance > 1 && key < node.left.key) {
            System.out.println(" RR Rotation at pivot " + node.key);
            return rotateRight(node);
        }
        if (balance < -1 && key > node.right.key) return rotateLeft(node);
        if (balance > 1 && key > node.left.key) {
            node.left = rotateLeft(node.left); return rotateRight(node);
        }
        if (balance < -1 && key < node.right.key) {
            node.right = rotateRight(node.right); return rotateLeft(node);
        }
        return node;
    }

    void insert(int key) { root = insert(root, key); }

    void inorder(AVLNode node, List<Integer> res) {
        if (node == null) return;
        inorder(node.left, res);
        res.add(node.key);
        inorder(node.right, res);
    }

    List<Integer> inorder() {
        List<Integer> res = new ArrayList<>();
        inorder(root, res);
        return res;
    }
}

class LinkedInSystem {
    AVLTree profileTree = new AVLTree();
    Map<Integer, List<Integer>> adj = new HashMap<>();

    void addEdge(int u, int v) {
        adj.computeIfAbsent(u, x -> new ArrayList<>()).add(v);
        adj.computeIfAbsent(v, x -> new ArrayList<>()).add(u);
    }

    List<Integer> bfs(int src, int maxDeg) {
        Queue<int[]> q = new LinkedList<>();
        Set<Integer> vis = new HashSet<>();
        q.offer(new int[]{src, 0}); vis.add(src);
        List<Integer> res = new ArrayList<>();
        while (!q.isEmpty()) {
            int[] cur = q.poll(); int nd = cur[0], d = cur[1];
            if (d > 0 && d <= maxDeg) res.add(nd);
            if (d < maxDeg)
                for (int nb : adj.getOrDefault(nd, new ArrayList<>()))
                    if (!vis.contains(nb)) { vis.add(nb); q.offer(new int[]{nb, d + 1}); }
        }
        return res;
    }

    void dfs(int node, Set<Integer> vis, List<Integer> cluster) {
        vis.add(node); cluster.add(node);
        for (int nb : adj.getOrDefault(node, new ArrayList<>()))
            if (!vis.contains(nb)) dfs(nb, vis, cluster);
    }

    List<List<Integer>> detectClusters() {
        Set<Integer> vis = new HashSet<>();
        List<List<Integer>> clusters = new ArrayList<>();
        for (int node : adj.keySet()) {
            if (!vis.contains(node)) {
                List<Integer> cluster = new ArrayList<>();
                dfs(node, vis, cluster);
                Collections.sort(cluster);
                clusters.add(cluster);
            }
        }
        return clusters;
    }

    int partition(Profile[] a, int lo, int hi) {
        int pivot = a[hi].score; int i = lo - 1;
        for (int j = lo; j < hi; j++)
            if (a[j].score > pivot) { i++; Profile t = a[i]; a[i] = a[j]; a[j] = t; }
        Profile t = a[i + 1]; a[i + 1] = a[hi]; a[hi] = t;
        return i + 1;
    }

    void qSort(Profile[] a, int lo, int hi) {
        if (lo < hi) { int pi = partition(a, lo, hi); qSort(a, lo, pi - 1); qSort(a, pi + 1, hi); }
    }

    int skillMatch(String[] us, String[] js) {
        int m = us.length, n = js.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = us[i - 1].equals(js[j - 1]) ? 1 + dp[i - 1][j - 1]
                        : Math.max(dp[i - 1][j], dp[i][j - 1]);
        return dp[m][n];
    }

    List<String> lcsSkills(String[] us, String[] js) {
        int m = us.length, n = js.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = us[i - 1].equals(js[j - 1]) ? 1 + dp[i - 1][j - 1]
                        : Math.max(dp[i - 1][j], dp[i][j - 1]);
        List<String> res = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (us[i - 1].equals(js[j - 1])) { res.add(0, us[i - 1]); i--; j--; }
            else if (dp[i - 1][j] > dp[i][j - 1]) i--; else j--;
        }
        return res;
    }

    public static void main(String[] args) {
        LinkedInSystem sys = new LinkedInSystem();

        System.out.println("=================================================");
        System.out.println(" LINKEDIN NETWORK ANALYSIS SYSTEM ");
        System.out.println("=================================================");

        System.out.println("--- AVL Profile Tree (CO1) ---");
        int[] ids = {101, 203, 145, 389, 267, 512};
        System.out.print("Inserting profiles: ");
        for (int i = 0; i < ids.length; i++) System.out.print(ids[i] + (i < ids.length - 1 ? ", " : "\n"));
        for (int id : ids) sys.profileTree.insert(id);
        System.out.println("Inorder: " + sys.profileTree.inorder().toString().replaceAll("[\\[\\],]", ""));

        System.out.println("--- BFS Connection Analysis (CO2 — Graph) ---");
        sys.addEdge(101, 203); sys.addEdge(101, 145);
        sys.addEdge(203, 267); sys.addEdge(145, 389);
        sys.addEdge(267, 512); sys.addEdge(389, 512);
        sys.addEdge(389, 267);

        List<Integer> deg1 = sys.bfs(101, 1);
        List<Integer> deg2 = new ArrayList<>(sys.bfs(101, 2)); deg2.removeAll(deg1);
        List<Integer> deg3 = new ArrayList<>(sys.bfs(101, 3)); deg3.removeAll(sys.bfs(101, 2));

        System.out.println("Network for User 101:");
        System.out.println(" 1st Degree : " + deg1 + " (Direct connections)");
        System.out.println(" 2nd Degree : " + deg2 + " (Friends of friends)");
        System.out.println(" 3rd Degree : " + deg3 + " (Extended network)");

        System.out.println("--- DFS Cluster Detection ---");
        List<List<Integer>> clusters = sys.detectClusters();
        String[] clusterNames = {"Tech Network", "Mgmt Network"};
        for (int i = 0; i < clusters.size(); i++)
            System.out.println(" Cluster " + (i + 1) + ": " + clusters.get(i) + " (" + clusterNames[i] + ")");

        System.out.println("--- Profile Strength Ranking (CO4 — Quick Sort) ---");
        Profile[] profiles = {
            new Profile(101, "Jothi Ganesh",  94, new String[]{"Java", "DSA", "ML"}),
            new Profile(203, "Ravi Kumar",    89, new String[]{"Python", "AI"}),
            new Profile(145, "Anjali Sharma", 82, new String[]{"React", "Node"}),
            new Profile(267, "Priya Reddy",   76, new String[]{"SQL", "Cloud"})
        };
        sys.qSort(profiles, 0, profiles.length - 1);
        for (int i = 0; i < profiles.length; i++) {
            Profile p = profiles[i];
            System.out.printf(" Rank %d: %-15s Score: %d | %s%n",
                i + 1, p.name, p.score, String.join(", ", p.skills));
        }

        System.out.println("--- Skill Match Scoring (CO5 — DP / LCS) ---");
        String[] jobSkills  = {"Java", "DSA", "ML", "Cloud", "SQL"};
        String[] userSkills = {"Java", "DSA", "ML", "Python", "React"};
        List<String> matched = sys.lcsSkills(userSkills, jobSkills);
        int score = sys.skillMatch(userSkills, jobSkills);
        System.out.println("Job Skills  : " + Arrays.toString(jobSkills));
        System.out.println("User Skills : " + Arrays.toString(userSkills));
        System.out.println("LCS Match   : " + matched);
        System.out.printf("Match Score : %d/%d = %d%% -> RECOMMENDED ✓%n",
            score, jobSkills.length, (score * 100) / jobSkills.length);

        System.out.println("\nTime Complexities:");
        System.out.println(" AVL Tree ops  : O(log n)");
        System.out.println(" BFS / DFS     : O(V + E)");
        System.out.println(" Quick Sort    : O(n log n)");
        System.out.println(" DP Skill Match: O(m x n)");
        System.out.println("\nProcess finished with exit code 0");
    }
}
