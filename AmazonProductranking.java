import java.util.*;

class Product {
    String name;
    double rating;
    int price;

    Product(String name, double rating, int price) {
        this.name = name;
        this.rating = rating;
        this.price = price;
    }
}

class ProductSorter {

    void quickSort(Product[] a, int lo, int hi) {
        if (lo < hi) {
            int pi = partition(a, lo, hi);
            quickSort(a, lo, pi - 1);
            quickSort(a, pi + 1, hi);
        }
    }

    int partition(Product[] a, int lo, int hi) {
        // Median-of-three pivot selection
        int mid = (lo + hi) / 2;
        if (a[mid].rating > a[hi].rating) { Product t = a[mid]; a[mid] = a[hi]; a[hi] = t; }
        if (a[lo].rating > a[hi].rating)  { Product t = a[lo];  a[lo]  = a[hi]; a[hi] = t; }
        if (a[mid].rating > a[lo].rating) { Product t = a[mid]; a[mid] = a[lo]; a[lo] = t; }

        double pivot = a[lo].rating;
        System.out.printf("Pivot selected: %.1f%n", pivot);

        int i = lo - 1;
        int comparisons = 0;
        for (int j = lo + 1; j <= hi; j++) {
            comparisons++;
            if (a[j].rating >= pivot) {
                i++;
                Product t = a[i]; a[i] = a[j]; a[j] = t;
            }
        }
        Product t = a[i + 1]; a[i + 1] = a[lo]; a[lo] = t;
        System.out.println("Comparisons: " + comparisons + " | Partitioning complete.");
        return i + 1;
    }

    void heapSort(Product[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(a, n, i);

        int comparisons = 0;
        for (int i = n - 1; i > 0; i--) {
            Product t = a[0]; a[0] = a[i]; a[i] = t;
            comparisons++;
            heapify(a, i, 0);
        }
        System.out.println("Heapify complete. Comparisons: " + comparisons);
    }

    void heapify(Product[] a, int n, int i) {
        int lg = i, l = 2 * i + 1, r = 2 * i + 2;
        if (l < n && a[l].rating > a[lg].rating) lg = l;
        if (r < n && a[r].rating > a[lg].rating) lg = r;
        if (lg != i) {
            Product t = a[i]; a[i] = a[lg]; a[lg] = t;
            heapify(a, n, lg);
        }
    }
}

public class AmazonProductRanking {

    static Product[] copyProducts(Product[] src) {
        Product[] copy = new Product[src.length];
        for (int i = 0; i < src.length; i++)
            copy[i] = new Product(src[i].name, src[i].rating, src[i].price);
        return copy;
    }

    static long benchmark(Product[] products, boolean useQuickSort) {
        ProductSorter sorter = new ProductSorter();
        Product[] copy = copyProducts(products);
        long start = System.currentTimeMillis();
        if (useQuickSort) sorter.quickSort(copy, 0, copy.length - 1);
        else sorter.heapSort(copy);
        return System.currentTimeMillis() - start;
    }

    public static void main(String[] args) {

        Product[] products = {
            new Product("Wireless Earbuds",    4.2, 1499),
            new Product("Smart Watch",         3.8, 3999),
            new Product("USB-C Hub",           4.7,  899),
            new Product("Laptop Stand",        4.5, 1299),
            new Product("Bluetooth Speaker",   3.5, 2499),
            new Product("Mechanical Keyboard", 4.9, 4999),
            new Product("Phone Case",          4.1,  399)
        };

        System.out.println("================================================");
        System.out.println("         AMAZON PRODUCT RANKING SYSTEM          ");
        System.out.println("================================================");

        System.out.println("\nOriginal Product List (unsorted):");
        System.out.printf("%-25s %-8s %s%n", "Product", "Rating", "Price (Rs)");
        System.out.println("-".repeat(45));
        for (Product p : products)
            System.out.printf("%-25s %-8.1f Rs %d%n", p.name, p.rating, p.price);

        // Quick Sort
        System.out.println("\n--- Quick Sort (by Rating, Descending) ---");
        ProductSorter sorter = new ProductSorter();
        Product[] qsProducts = copyProducts(products);
        sorter.quickSort(qsProducts, 0, qsProducts.length - 1);

        System.out.println("\nRank  Product                   Rating   Price");
        System.out.println("-".repeat(50));
        for (int i = 0; i < qsProducts.length; i++)
            System.out.printf("%-5d %-25s %-8.1f Rs %d%n",
                i + 1, qsProducts[i].name, qsProducts[i].rating, qsProducts[i].price);

        // Heap Sort
        System.out.println("\n--- Heap Sort (by Rating, Descending) ---");
        Product[] hsProducts = copyProducts(products);
        sorter.heapSort(hsProducts);

        // Reverse to get descending order
        for (int i = 0, j = hsProducts.length - 1; i < j; i++, j--) {
            Product t = hsProducts[i]; hsProducts[i] = hsProducts[j]; hsProducts[j] = t;
        }

        System.out.println("Output: [same sorted order as above]");
        System.out.println("\nRank  Product                   Rating   Price");
        System.out.println("-".repeat(50));
        for (int i = 0; i < hsProducts.length; i++)
            System.out.printf("%-5d %-25s %-8.1f Rs %d%n",
                i + 1, hsProducts[i].name, hsProducts[i].rating, hsProducts[i].price);

        // Benchmark on 10,000 products
        System.out.println("\nPerformance Benchmark (10,000 products):");
        Random rand = new Random(42);
        Product[] large = new Product[10000];
        String[] names = {"Product A", "Product B", "Product C", "Product D", "Product E"};
        for (int i = 0; i < 10000; i++)
            large[i] = new Product(names[i % 5] + i, 1.0 + rand.nextDouble() * 4.0, 100 + rand.nextInt(9900));

        long qsTime = benchmark(large, true);
        long hsTime = benchmark(large, false);
        System.out.println("Quick Sort -> " + qsTime + " ms  O(n log n) avg");
        System.out.println("Heap Sort  -> " + hsTime + " ms  O(n log n) worst");

        System.out.println("\nTime Complexity:");
        System.out.println("  Quick Sort Avg   : O(n log n)");
        System.out.println("  Quick Sort Worst : O(n^2)  [avoided with median-of-three pivot]");
        System.out.println("  Heap Sort        : O(n log n)");
        System.out.println("\nProcess finished with exit code 0");
    }
}
