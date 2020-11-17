package cphbusiness.ufo.letterfrequencies;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toMap;

/**
 * Frequency analysis Inspired by
 * https://en.wikipedia.org/wiki/Frequency_analysis
 *
 * @author kasper
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        ArrayList<Double> a = new ArrayList();

        int iterations = 100;

        for (int i = 0; i < iterations; i++) {

            String fileName = "C:\\Users\\Hupra\\Desktop\\ufo2\\letterfrequencies\\src\\main\\resources\\FoundationSeries.txt";
            Reader reader = new FileReader(fileName);
            Map<Integer, Long> freq = new HashMap<>();

            Timer t = new Timer();
            t.play();

            tallyChars4(reader, freq);

            double now = t.check();
            System.out.println(now);

            a.add(now);

            if(i == iterations-1){
                print_tally(freq);
            }

        }

        System.out.println("min " + Collections.min(a));
        System.out.println("max " + Collections.max(a));
        System.out.println("median " + (a.get(iterations/2-1) + a.get(iterations/2))/2);
        Double avg = a.stream().reduce(0.0, Double::sum)/iterations;
        System.out.println("average " + avg);
        double sdiv = Math.sqrt((a.stream().map(x -> Math.pow(x-avg, 2)).reduce(0.0, Double::sum))/iterations);
        System.out.println("standard " + sdiv);

    }


    public static class Timer {
        private long start, spent = 0;
        public Timer() { play(); }
        public double check() { return (System.nanoTime()-start+spent)/1e9; }
        public void pause() { spent += System.nanoTime()-start; }
        public void play() { start = System.nanoTime(); }
    }

    private static void tallyChars(Reader reader, Map<Integer, Long> freq) throws IOException {
        int b;
        while ((b = reader.read()) != -1) {
            try {
                freq.put(b, freq.get(b) + 1);
            } catch (NullPointerException np) {
                freq.put(b, 1L);
            };
        }
    }

    private static void tallyChars2(Reader reader, Map<Integer, Long> freq) throws IOException {
        int b;
        BufferedReader br=new BufferedReader(reader);

        while ((b = br.read()) != -1) {
            try {
                freq.put(b, freq.get(b) + 1);
            } catch (NullPointerException np) {
                freq.put(b, 1L);
            };
        }
    }

    private static void tallyChars3(Reader reader, Map<Integer, Long> freq) throws IOException {
        int b;
        BufferedReader br=new BufferedReader(reader);

        while ((b = br.read()) != -1) {
            freq.put(b, freq.getOrDefault(b, 0L) + 1L);
        }

    }

    private static void tallyChars4(Reader reader, Map<Integer, Long> freq) throws IOException {
        int b;

        BufferedReader br=new BufferedReader(reader);
        int[] BEYTAHS = new int[255];

        while ((b = br.read()) != -1) {
            BEYTAHS[b]++;
        }

        for (int i = 0; i < 255; i++) {
            if(BEYTAHS[i] != 0){
                freq.put(i, (long)BEYTAHS[i]);
            }
        }

    }

    private static void print_tally(Map<Integer, Long> freq) {
        int dist = 'a' - 'A';
        Map<Character, Long> upperAndlower = new LinkedHashMap();
        for (Character c = 'A'; c <= 'Z'; c++) {
            upperAndlower.put(c, freq.getOrDefault(c, 0L) + freq.getOrDefault(c + dist, 0L));
        }
        Map<Character, Long> sorted = upperAndlower
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        for (Character c : sorted.keySet()) {
            System.out.println("" + c + ": " + sorted.get(c));;
        }
    }
}
