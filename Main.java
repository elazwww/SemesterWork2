import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        int[] data = new int[10000];
        for (int i = 0; i < data.length; i++) {
            data[i] = ThreadLocalRandom.current().nextInt(0, 100000);
        }

        BinomialHeap heap = new BinomialHeap();

        // Вставка элемента
        long[] insertTimes = new long[10000];
        long[] insertOps = new long[10000];

        for (int i = 0; i < data.length; i++) {
            heap.resetOpCount();
            long start = System.nanoTime();
            heap.insert(data[i]);
            long duration = System.nanoTime() - start;
            insertTimes[i] = duration;
            insertOps[i] = heap.operationCount;
        }

        long sumInsertTime = 0;
        long sumInsertOps = 0;
        for (int i = 0; i < insertTimes.length; i++) {
            sumInsertTime += insertTimes[i];
            sumInsertOps += insertOps[i];
        }
        double avgInsertTime = (double) sumInsertTime / insertTimes.length / 1000.0;
        double avgInsertOps = (double) sumInsertOps / insertOps.length;

        // Поиск минимума
        long[] findMinTimes = new long[100];
        long[] findMinOps = new long[100];

        for (int i = 0; i < 100; i++) {
            heap.resetOpCount();
            long start = System.nanoTime();
            int min = heap.findMin();
            long duration = System.nanoTime() - start;
            findMinTimes[i] = duration;
            findMinOps[i] = heap.operationCount;
        }

        long sumFindMinTime = 0;
        long sumFindMinOps = 0;
        for (int i = 0; i < findMinTimes.length; i++) {
            sumFindMinTime += findMinTimes[i];
            sumFindMinOps += findMinOps[i];
        }
        double avgFindMinTime = (double) sumFindMinTime / findMinTimes.length;
        double avgFindMinOps = (double) sumFindMinOps / findMinOps.length;

        // Удаление минимума
        BinomialHeap delHeap = new BinomialHeap();
        for (int i = 0; i < data.length; i++) {
            delHeap.insert(data[i]);
        }

        long[] deleteTimes = new long[1000];
        long[] deleteOps = new long[1000];

        for (int i = 0; i < 1000 && !delHeap.isEmpty(); i++) {
            delHeap.resetOpCount();
            long start = System.nanoTime();
            int min = delHeap.deleteMin();
            long duration = System.nanoTime() - start;
            deleteTimes[i] = duration;
            deleteOps[i] = delHeap.operationCount;
        }

        long sumDeleteTime = 0;
        long sumDeleteOps = 0;
        int deleteCount = 0;
        for (int i = 0; i < deleteTimes.length; i++) {
            if (deleteTimes[i] != 0) {
                sumDeleteTime += deleteTimes[i];
                sumDeleteOps += deleteOps[i];
                deleteCount++;
            }
        }
        double avgDeleteTime = (double) sumDeleteTime / deleteCount / 1000.0;
        double avgDeleteOps = (double) sumDeleteOps / deleteCount;

        System.out.println(" Вставка (10000 элементов):");
        System.out.println("  Среднее время: " + avgInsertTime + " мкс");
        System.out.println("  Среднее кол-во операций: " + avgInsertOps);

        System.out.println(" Поиск минимума (100 раз):");
        System.out.println("  Среднее время: " + avgFindMinTime + " нс");
        System.out.println("  Среднее кол-во операций: " + avgFindMinOps);

        System.out.println(" Удаление минимума (1000 элементов):");
        System.out.println("  Среднее время: " + avgDeleteTime + " мкс");
        System.out.println("  Среднее кол-во операций: " + avgDeleteOps);
    }
}