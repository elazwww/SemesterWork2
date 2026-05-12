import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        int[] data = ThreadLocalRandom.current().ints(10000, 0, 100000).toArray();

        BinomialHeap heap = new BinomialHeap();

        // Вставка
        List<Long> insertTimes = new ArrayList<>();
        List<Long> insertOps = new ArrayList<>();

        for (int num : data) {
            heap.resetOpCount();
            long start = System.nanoTime();
            heap.insert(num);
            long duration = System.nanoTime() - start;
            insertTimes.add(duration);
            insertOps.add(heap.operationCount);
        }

        // Поиск минимума
        List<Long> findMinTimes = new ArrayList<>();
        List<Long> findMinOps = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            heap.resetOpCount();
            long start = System.nanoTime();
            int min = heap.findMin();
            long duration = System.nanoTime() - start;
            findMinTimes.add(duration);
            findMinOps.add(heap.operationCount);
        }

        // Удаление минимума
        List<Long> deleteTimes = new ArrayList<>();
        List<Long> deleteOps = new ArrayList<>();

        // Создаем отдельную кучу для теста удаления
        BinomialHeap delHeap = new BinomialHeap();
        for (int num : data) {
            delHeap.insert(num);
        }

        for (int i = 0; i < 1000 && !delHeap.isEmpty(); i++) {
            delHeap.resetOpCount();
            long start = System.nanoTime();
            int min = delHeap.deleteMin();
            long duration = System.nanoTime() - start;
            deleteTimes.add(duration);
            deleteOps.add(delHeap.operationCount);
        }

        System.out.printf(" Вставка — %d элементов:\n", insertTimes.size());
        System.out.printf("  Среднее время: %.3f мкс\n",
                insertTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1000.0);
        System.out.printf("  Среднее кол-во операций: %.2f\n",
                insertOps.stream().mapToLong(Long::longValue).average().orElse(0));

        System.out.printf(" Поиск минимума — %d раз:\n", findMinTimes.size());
        System.out.printf("  Среднее время: %.3f нс\n",
                findMinTimes.stream().mapToLong(Long::longValue).average().orElse(0));
        System.out.printf("  Среднее кол-во операций: %.2f\n",
                findMinOps.stream().mapToLong(Long::longValue).average().orElse(0));

        System.out.printf(" Удаление минимума — %d элементов:\n", deleteTimes.size());
        System.out.printf("  Среднее время: %.3f мкс\n",
                deleteTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1000.0);
        System.out.printf("  Среднее кол-во операций: %.2f\n",
                deleteOps.stream().mapToLong(Long::longValue).average().orElse(0));
    }
}