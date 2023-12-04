import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class adventThreeB {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        FileInputStream fin = new FileInputStream("three.txt");
        Scanner scn = new Scanner(fin);

        for (int i = 0; scn.hasNextLine(); i++) {
            String line = scn.nextLine();
            grid.add(new ArrayList<>());
            for (int j = 0; j < line.length(); j++) {
                grid.get(i).add(line.charAt(j));
            }
        }
        //System.out.println(grid);
        int total = 0;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                char current = grid.get(i).get(j);
                if (current != '*') {
                    continue;
                }
                // Okay, so it's a gear
                int adjacentNums = 0;
                int gearVal = 1;
                System.out.println("New possible gear at (" + i + "," + j + ")");
                for (int k = Math.max(i-1, 0); k < Math.min(grid.size(), i+2); k++) {
                    for (int l = Math.max(j-1, 0); l < Math.min(grid.get(k).size(), j+2); l++) {
                        System.out.println("("+k+","+l+")");
                        char next = grid.get(k).get(l);
                        if (!Character.isDigit(next)) {
                            continue;
                        }
                        // Okay, so it's a digit.
                        adjacentNums++;
                        int numStart = l;
                        for (int m = l-1; m >= 0; m--) {
                            if (Character.isDigit(grid.get(k).get(m))) {
                                numStart = m;
                            } else {
                                break;
                            }
                        }
                        // found the start
                        int number = Character.digit(grid.get(k).get(numStart), 10);
                        for (int m = numStart+1; m < grid.get(k).size(); m++) {
                            char val = grid.get(k).get(m);
                            if (Character.isDigit(val)) {
                                number *= 10;
                                number += Character.digit(val, 10);
                                // Make sure you don't count the same number twice
                                // Update l to where m leaves off
                                if (m > l) {
                                    l = m+1;
                                }
                            } else {
                                break;
                            }
                        }
                        System.out.println("Found number " + number);
                        // Update the gearing
                        gearVal *= number;

                    }
                }
                // We have found all adjacencies
                if (adjacentNums == 2) {
                    System.out.println(gearVal);
                    total += gearVal;
                }
            }
        }
        System.out.println(total);
    }
}
