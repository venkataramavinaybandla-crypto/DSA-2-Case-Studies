import java.util.*;

class SegmentTree {

    private int[] tree;
    private int[] lazy;
    private int n;

    SegmentTree(int[] data) {
        n = data.length;
        tree = new int[4 * n];
        lazy = new int[4 * n];
        build(data, 0, n - 1, 1);
    }

    private void build(int[] data, int start, int end, int node) {
        if (start == end) {
            tree[node] = data[start];
            return;
        }
        int mid = (start + end) / 2;
        build(data, start, mid, 2 * node);
        build(data, mid + 1, end, 2 * node + 1);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    private void pushDown(int node, int start, int end) {
        if (lazy[node] != 0) {
            tree[node] += lazy[node] * (end - start + 1);
            if (start != end) {
                lazy[2 * node] += lazy[node];
                lazy[2 * node + 1] += lazy[node];
            }
            lazy[node] = 0;
        }
    }

    int query(int node, int start, int end, int l, int r) {
        pushDown(node, start, end);
        if (r < start || end < l) return 0;
        if (l <= start && end <= r) return tree[node];
        int mid = (start + end) / 2;
        return query(2 * node, start, mid, l, r)
             + query(2 * node + 1, mid + 1, end, l, r);
    }

    void update(int node, int start, int end, int idx, int val) {
        pushDown(node, start, end);
        if (start == end) {
            tree[node] += val;
            return;
        }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2 * node, start, mid, idx, val);
        else            update(2 * node + 1, mid + 1, end, idx, val);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    int rangeQuery(int l, int r) {
        return query(1, 0, n - 1, l, r);
    }

    void pointUpdate(int idx, int val) {
        update(1, 0, n - 1, idx, val);
    }
}

public class SwiggyOrderTracking {

    public static void main(String[] args) {
        int[] zoneOrders = {12, 15, 20, 10, 18, 25, 8, 30};
        String[] zoneNames = {"Z1", "Z2", "Z3", "Z4", "Z5", "Z6", "Z7", "Z8"};

        System.out.println("========================================");
        System.out.println(" SWIGGY LIVE ORDER TRACKING SYSTEM");
        System.out.println("========================================\n");

        System.out.print("Delivery Zone Order Counts:\n  Zone  : ");
        for (String z : zoneNames) System.out.printf("%-4s", z);
        System.out.print("\n  Orders: ");
        for (int o : zoneOrders) System.out.printf("%-4d", o);
        System.out.println("\n");

        SegmentTree st = new SegmentTree(zoneOrders);
        System.out.println("Building Segment Tree ... Done\n");

        int q1 = st.rangeQuery(0, 3);
        int q2 = st.rangeQuery(2, 5);
        System.out.println("Range Sum Query [Z1..Z4] : " + q1 + " orders");
        System.out.println("Range Sum Query [Z3..Z6] : " + q2 + " orders\n");

        int updateZone = 2;
        int updateVal  = 5;
        System.out.println("Live Update: Zone Z3 received +" + updateVal + " orders");
        System.out.println("Updated Z3: " + zoneOrders[updateZone] + " -> " + (zoneOrders[updateZone] + updateVal));
        st.pointUpdate(updateZone, updateVal);
        zoneOrders[updateZone] += updateVal;

        int q3 = st.rangeQuery(0, 3);
        System.out.println("\nRe-Query [Z1..Z4] : " + q3 + " orders");
        System.out.println("Lazy Propagation: unnecessary re-computations avoided\n");

        int n = zoneOrders.length;
        System.out.println("Performance Comparison:");
        System.out.println("  Array Linear Scan : O(n)      -> " + n + " operations");
        System.out.println("  Segment Tree Query : O(log n) -> " + (int)(Math.log(n) / Math.log(2) + 1) + " operations\n");

        System.out.println("Time Complexity:");
        System.out.println("  Tree Construction  : O(n)");
        System.out.println("  Range Query        : O(log n)");
        System.out.println("  Point Update       : O(log n)");
        System.out.println("  Lazy Propagation   : O(log n)");
    }
}
