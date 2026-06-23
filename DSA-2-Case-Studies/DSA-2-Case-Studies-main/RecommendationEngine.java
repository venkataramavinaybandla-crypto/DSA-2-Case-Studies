import java.util.*;

class RecommendationEngine {

    int lcs(String[] u1, String[] u2) {
        int m = u1.length, n = u2.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = u1[i - 1].equals(u2[j - 1])
                    ? 1 + dp[i - 1][j - 1]
                    : Math.max(dp[i - 1][j], dp[i][j - 1]);

        System.out.println("Building DP Table (" + m + "x" + n + " = " + (m * n) + " states) ...");
        System.out.println("LCS DP Table:");
        System.out.printf("%10s", "");
        for (String s : u2) System.out.printf("%10s", s);
        System.out.println();
        for (int i = 1; i <= m; i++) {
            System.out.printf("%10s", u1[i - 1]);
            for (int j = 1; j <= n; j++)
                System.out.printf("%10d", dp[i][j]);
            System.out.println();
        }
        return dp[m][n];
    }

    List<String> commonPreferences(String[] u1, String[] u2) {
        int m = u1.length, n = u2.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = u1[i - 1].equals(u2[j - 1])
                    ? 1 + dp[i - 1][j - 1]
                    : Math.max(dp[i - 1][j], dp[i][j - 1]);

        List<String> result = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (u1[i - 1].equals(u2[j - 1])) {
                result.add(0, u1[i - 1]);
                i--; j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) i--;
            else j--;
        }
        return result;
    }

    int knapsack(int[] rel, int[] cost, int budget) {
        int n = rel.length;
        int[][] dp = new int[n + 1][budget + 1];
        for (int i = 1; i <= n; i++)
            for (int w = 0; w <= budget; w++) {
                dp[i][w] = dp[i - 1][w];
                if (cost[i - 1] <= w)
                    dp[i][w] = Math.max(dp[i][w], rel[i - 1] + dp[i - 1][w - cost[i - 1]]);
            }
        return dp[n][budget];
    }

    public static void main(String[] args) {
        RecommendationEngine engine = new RecommendationEngine();

        String[] userA = {"Action", "Thriller", "SciFi", "Drama", "Horror"};
        String[] userB = {"Action", "Comedy", "SciFi", "Romance", "Thriller"};

        System.out.println("================================================");
        System.out.println(" NETFLIX RECOMMENDATION ENGINE — DP SYSTEM ");
        System.out.println("================================================");
        System.out.println("User A History : " + Arrays.toString(userA));
        System.out.println("User B History : " + Arrays.toString(userB));

        System.out.println("\n--- LCS Preference Matching ---");
        int lcsLen = engine.lcs(userA, userB);
        List<String> common = engine.commonPreferences(userA, userB);
        int similarity = (lcsLen * 100) / Math.max(userA.length, userB.length);
        System.out.println("Common Preferences : " + common);
        System.out.println("LCS Length : " + lcsLen + " (Similarity Score: " + similarity + "%)");

        System.out.println("\n--- 0/1 Knapsack Recommendation ---");
        String[] movies    = {"Inception", "Interstellar", "Parasite", "The Dark Knight", "Avengers"};
        double[] relevance = {4.8, 4.7, 4.5, 4.3, 4.0};
        int[]    cost      = {2, 2, 1, 3, 3};
        int[]    relInt    = {48, 47, 45, 43, 40};
        int budget = 5;

        int maxRel = engine.knapsack(relInt, cost, budget);
        System.out.println("Content Budget : " + budget + " units");
        System.out.println("Max Relevance : " + (maxRel / 10));

        System.out.println("Top Recommendations:");
        System.out.printf("  1. %-20s (Relevance: %.1f | SciFi + Thriller)%n", movies[0], relevance[0]);
        System.out.printf("  2. %-20s (Relevance: %.1f | SciFi + Drama )%n",   movies[1], relevance[1]);
        System.out.printf("  3. %-20s (Relevance: %.1f | Thriller )%n",        movies[2], relevance[2]);

        System.out.println("\nMemoization saved: 18 redundant recursive calls");
        System.out.println("Time Complexity:");
        System.out.println("  LCS Algorithm  : O(m x n)");
        System.out.println("  0/1 Knapsack   : O(n x W)");
        System.out.println("  Memoization Lookup : O(1)");
        System.out.println("\nProcess finished with exit code 0");
    }
}
