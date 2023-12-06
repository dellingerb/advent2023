import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class adventFiveA {

    public static void main(String[] args) throws FileNotFoundException {

        RangeMap seedToSoil = new RangeMap();
        RangeMap soilToFertilizer = new RangeMap();
        RangeMap fertilizerToWater = new RangeMap();
        RangeMap waterToLight = new RangeMap();
        RangeMap lightToTemp = new RangeMap();
        RangeMap tempToHumid = new RangeMap();
        RangeMap humidToLoc = new RangeMap();

        FileInputStream fin = new FileInputStream("five.txt");
        Scanner scn = new Scanner(fin);

        String seedLine = scn.nextLine();
        Scanner seedScan = new Scanner(seedLine);
        seedScan.next();
        ArrayList<Long> seeds = new ArrayList<>();
        ArrayList<Long> seedRanges = new ArrayList<>();
        while (seedScan.hasNextLong()) {
            seeds.add(seedScan.nextLong());
            seedRanges.add(seedScan.nextLong());
        }
        System.out.println(seeds);
        System.out.println(seedRanges);

        scn.nextLine();
        loadRangeMap(scn, seedToSoil);
        loadRangeMap(scn, soilToFertilizer);
        loadRangeMap(scn, fertilizerToWater);
        loadRangeMap(scn, waterToLight);
        loadRangeMap(scn, lightToTemp);
        loadRangeMap(scn, tempToHumid);
        loadRangeMap(scn, humidToLoc);

        //System.out.println(seedToSoil);
        //System.out.println(soilToFertilizer);
        RangeMap seedToFertilizer = mergeRangeMaps(seedToSoil, soilToFertilizer);
        RangeMap seedToWater = mergeRangeMaps(seedToFertilizer, fertilizerToWater);
        RangeMap seedToLight = mergeRangeMaps(seedToWater, waterToLight);
        RangeMap seedToTemp = mergeRangeMaps(seedToLight, lightToTemp);
        RangeMap seedToHumid = mergeRangeMaps(seedToTemp, tempToHumid);
        RangeMap seedToLoc = mergeRangeMaps(seedToHumid, humidToLoc);
        System.out.println(seedToLoc);

        //System.out.println(seedToSoil);
        //System.out.println(soilToFertilizer);

        long min = Long.MAX_VALUE;
        for (int i = 0; i < seeds.size(); i++) {
            /*for (long j = 0; j < seedRanges.get(i); j++) {
                long soil = seedToSoil.getMapped(seeds.get(i) + j);
                long fert = soilToFertilizer.getMapped(soil);
                long water = fertilizerToWater.getMapped(fert);
                long light = waterToLight.getMapped(water);
                long temp = lightToTemp.getMapped(light);
                long humid = tempToHumid.getMapped(temp);
                long loc = humidToLoc.getMapped(humid);
                //System.out.println(seeds.get(i) + " " + soil + " " + fert + " " + water + " " + light
                //        + " " + temp + " " + humid + " " + loc);
                if (loc < min) {
                    min = loc;
                }
            }*/

            long nextSeed = seeds.get(i);
            int index = seedToLoc.getSourceIndex(nextSeed);
            //System.out.println("Index is " + index);
            while (nextSeed <= seeds.get(i) + seedRanges.get(i) - 1) {
                long loc = seedToLoc.getMapped(nextSeed);
                //System.out.println("Seed " + nextSeed + " at location " + loc);
                if (loc < min) {
                    min = loc;
                }
                index++;
                nextSeed = seedToLoc.sourceRanges.get(index).start;
                //System.out.println("Updating seed to " + nextSeed);
            }

        }
        System.out.println(min);
    }

    public static void loadRangeMap(Scanner scn, RangeMap map) {
        scn.nextLine();
        while (scn.hasNextLine()) {
            String nums = scn.nextLine();
            if (nums.equals("")) {
                break;
            }
            Scanner numScanner = new Scanner(nums);
            long dest = numScanner.nextLong();
            long source = numScanner.nextLong();
            long width = numScanner.nextLong();
            Range sourceRange = new Range(source, source + width - 1);
            Range destRange = new Range(dest, dest + width - 1);
            map.insertRange(sourceRange, destRange);
        }
    }

    public static RangeMap mergeRangeMaps(RangeMap aToB, RangeMap bToC) {
        RangeMap aToC = new RangeMap();
        for (int i = 0; i < aToB.destinationRanges.size(); i++) {
            Range aSourceRange = new Range(aToB.sourceRanges.get(i));
            Range bDestRange = new Range(aToB.destinationRanges.get(i));
            for (int j = 0; j < bToC.sourceRanges.size(); j++) {
                Range bSourceRange = new Range(bToC.sourceRanges.get(j));
                Range cDestRange = new Range(bToC.destinationRanges.get(j));
                if (!(bSourceRange.start <= bDestRange.start && bDestRange.start <= bSourceRange.end)) {
                    continue;
                }
                if (bDestRange.end > bSourceRange.end) {
                    long offset = bDestRange.start - bSourceRange.start;
                    long width = bSourceRange.end - bDestRange.start;
                    Range newASource = new Range(aSourceRange.start, aSourceRange.start + width);
                    Range newCDest = new Range(cDestRange.start + offset, cDestRange.end);
                    aToC.insertRange(newASource, newCDest);
                    aSourceRange.start += (width + 1);
                    bDestRange.start += (width + 1);
                }
                else {
                    long offset = bDestRange.start - bSourceRange.start;
                    long width = bDestRange.end - bDestRange.start;
                    Range newCDest = new Range(cDestRange.start + offset, cDestRange.start + offset + width);
                    aToC.insertRange(aSourceRange, newCDest);
                    break;
                }
            }

        }
        return aToC;
    }
}
