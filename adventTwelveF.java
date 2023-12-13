import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class adventTwelveF {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fin = new FileInputStream("twelve.txt");
        Scanner scn = new Scanner(fin);

        long total = 0;

        int lines = 1;
        while (scn.hasNextLine()) {
            System.out.println(lines);
            lines++;
            String line = scn.nextLine();
            Scanner lscan = new Scanner(line);
            String strCode = lscan.next();
            ArrayList<Character> code = new ArrayList<>();
            for (int i = 0; i < strCode.length(); i++) {
                code.add(strCode.charAt(i));
            }
            int codeLen = code.size();
            for (int i = 0; i < 4; i++) {
                code.add('?');
                for (int j = 0; j < codeLen; j++) {
                    code.add(code.get(j));
                }
            }

            String key = lscan.next();
            String[] numberStrings = key.split(",");
            ArrayList<Integer> numbers = new ArrayList<>();
            for (String s : numberStrings) {
                numbers.add(Integer.parseInt(s));
            }
            int numStrLength = numbers.size();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < numStrLength; j++) {
                    numbers.add(numbers.get(j));
                }
            }

            System.out.println(code);
            System.out.println(numbers);
            // Set up ways
            ArrayList<ArrayList<Long>> ways = new ArrayList<>();
            for (int j = 0; j < code.size(); j++) {
                ways.add(new ArrayList<>());
                for (int k = 0; k < numbers.size(); k++) {

                    ways.get(j).add(-1L);
                    ways.get(j).set(k, calcWays(code, ways, numbers, j, k));

                }
            }
            //long num = calcWays(code, ways, numbers, 5, 1);
            //System.out.println("num is " + num);

            System.out.println(ways);
            long next = ways.get(code.size()-1).get(numbers.size()-1);
            System.out.println("result: " + next);
            total += next;
        }
        System.out.println("total: " + total);
    }


    static long calcWays(ArrayList<Character> code, ArrayList<ArrayList<Long>> ways, ArrayList<Integer> numbers, int lastIndex, int upToNumber) {
        // already found
        //System.out.println("Calcways " + lastIndex + " " + upToNumber);
        if (ways.get(lastIndex).get(upToNumber) != -1L) {
            //System.out.println("returning cached " + ways.get(lastIndex).get(upToNumber));
            return ways.get(lastIndex).get(upToNumber);
        }
        // too long
        int neededLength = needLength(numbers, 0, upToNumber);
        if (neededLength > lastIndex + 1) {
            ways.get(lastIndex).set(upToNumber, 0L);
            //System.out.println("returning 0, too long");
            return 0;
        }
        // one guy
        if (upToNumber == 0) {
            long result = countOne(code, numbers.get(0), 0, lastIndex);
            ways.get(lastIndex).set(upToNumber, result);
            //System.out.println("returning one guy " + result);
            return result;
        }
        // More than one guy
        long total = 0;
        int number = numbers.get(upToNumber);
        for (int i = 2; i <= lastIndex; i++) {
            if (code.get(i-1) == '#') {
                //System.out.println("can't place at " + i);
                continue;
            }
            int coveredStart = i;
            int coveredEnd = i + number - 1;
            boolean success = viablePlacement(code, i, lastIndex, coveredStart, coveredEnd);
            if (success) {
                //System.out.println("recursing...");
                long left = calcWays(code, ways, numbers, i-2, upToNumber-1);
                total += left;
            }
        }
        //System.out.println("returning total " + total);
        ways.get(lastIndex).set(upToNumber, total);
        return total;
    }

    static boolean viablePlacement(ArrayList<Character> code, int firstIndex, int lastIndex, int coveredStart, int coveredEnd) {
        if (coveredEnd > lastIndex) {
            return false;
        }
        for (int j = firstIndex; j <= lastIndex; j++) {
            if (coveredStart <= j && j <= coveredEnd) {
                if (code.get(j) == '.') {
                    return false;
                }
            } else {
                if (code.get(j) == '#') {
                    return false;
                }
            }
        }
        return true;
    }

    static long countOne(ArrayList<Character> code, int number, int firstIndex, int lastIndex) {
        int count = 0;
        for (int i = firstIndex; i <= lastIndex; i++) {
            int coveredStart = i;
            int coveredEnd = i + number - 1;
            boolean success = viablePlacement(code, firstIndex, lastIndex, coveredStart, coveredEnd);
            if (success) {
                count++;
            }
        }
        return count;
    }


    static int needLength(ArrayList<Integer> numbers, int from, int to) {
        int total = 0;
        for (int i = from; i <= to; i++) {
            total += numbers.get(i);
            if (i < to) {       // Need at least one space between
                total++;
            }
        }
        return total;
    }


}