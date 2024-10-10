import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean isParallel;
        System.out.println("Выберите способ копирования: \n1)Последовательное \n2)Параллельное");
        int choice = in.nextInt();
        String sourceFile1 = "source1.txt"; // имя первого файла
        String sourceFile2 = "source2.txt"; // имя второго файла
        String destinationFile1 = "destination1.txt"; // путь для первого файла
        String destinationFile2 = "destination2.txt"; // путь для второго файла
        switch (choice){
            case 1:{
                isParallel = false;// true для параллельного копирования
            }
            break;
            default:
                isParallel = true;// false для последовательного копирования
        }
        if(isParallel) {
            copyFilesParallel(sourceFile1, destinationFile1, sourceFile2, destinationFile2);
        } else {
            copyFilesSequential(sourceFile1, destinationFile1, sourceFile2, destinationFile2);
        }
    }

    private static void copyFilesSequential(String sourceFile1, String destinationFile1, String sourceFile2, String destinationFile2) {
        long startTime = System.nanoTime();
        copyFile(sourceFile1, destinationFile1);
        copyFile(sourceFile2, destinationFile2);
        long endTime = System.nanoTime();

        System.out.println("Время последовательного копирования: " + (endTime - startTime) / 1_000_000 + " мс");
    }

    private static void copyFilesParallel(String sourceFile1, String destinationFile1, String sourceFile2, String destinationFile2) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        long startTime = System.nanoTime();

        Future<Void> future1 = executor.submit(() -> {
            copyFile(sourceFile1, destinationFile1);
            return null;
        });

        Future<Void> future2 = executor.submit(() -> {
            copyFile(sourceFile2, destinationFile2);
            return null;
        });

        try {
            future1.get();
            future2.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Ошибка при параллельном копировании: " + e.getMessage());
        }

        long endTime = System.nanoTime();
        executor.shutdown();

        System.out.println("Время параллельного копирования: " + (endTime - startTime) / 1_000_000 + " мс");
    }

    private static void copyFile(String source, String destination) {
        try {
            Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Ошибка при копировании файла: " + e.getMessage());
        }
    }
}

