import java.util.ArrayList;

class RangeMap {
    ArrayList<Range> sourceRanges;
    ArrayList<Range> destinationRanges;

    public RangeMap() {
        sourceRanges = new ArrayList<>();
        sourceRanges.add(new Range(0, Long.MAX_VALUE));
        destinationRanges = new ArrayList<>();
        destinationRanges.add((new Range(0, Long.MAX_VALUE)));
    }

    public String toString() {
        String result = "Source ranges: " + sourceRanges + "\nDest ranges: " + destinationRanges + "\n";
        return result;
    }

    public void insertRange(Range source, Range dest) {
        for (int i = 0; i < sourceRanges.size(); i++) {
            // Found our source range to split
            if (source.start >= sourceRanges.get(i).start
                    && source.end <= sourceRanges.get(i).end) {
                Range oldSource = sourceRanges.get(i);
                Range oldDest = destinationRanges.get(i);
                sourceRanges.remove(i);
                destinationRanges.remove(i);

                // Are we
                //  - cleanly inserting into the middle of a range
                //  - replacing the back portion
                //  - replacing the front portion
                //  - replacing it outright
                if (oldSource.start < source.start && oldSource.end > source.end) {
                    sourceRanges.add(i, new Range(oldSource.start, source.start - 1));
                    long width1 = source.start - 1 - oldSource.start;
                    sourceRanges.add(i + 1, source);
                    sourceRanges.add(i + 2, new Range(source.end + 1, oldSource.end));
                    long width2 = oldSource.end - source.end - 1;
                    destinationRanges.add(i, new Range(oldDest.start, oldDest.start + width1));
                    destinationRanges.add(i + 1, dest);
                    destinationRanges.add(i + 2, new Range(oldDest.end - width2, oldDest.end));
                } else if (oldSource.start < source.start) {
                    sourceRanges.add(i, new Range(oldSource.start, source.start - 1));
                    long width1 = source.start - 1 - oldSource.start;
                    sourceRanges.add(i + 1, source);
                    destinationRanges.add(i, new Range(oldDest.start, oldDest.start + width1));
                    destinationRanges.add(i + 1, dest);
                } else if (oldSource.end > source.end) {
                    sourceRanges.add(i, source);
                    sourceRanges.add(i + 1, new Range(source.end + 1, oldSource.end));
                    long width2 = oldSource.end - source.end - 1;
                    destinationRanges.add(i, dest);
                    destinationRanges.add(i + 1, new Range(oldDest.end - width2, oldDest.end));
                } else {
                    sourceRanges.add(i, source);
                    destinationRanges.add(i, dest);
                }

                break;
            }
        }
    }

    public long getMapped(long input) {
        for (int i = 0; i < sourceRanges.size(); i++) {
            if (sourceRanges.get(i).start <= input && input <= sourceRanges.get(i).end) {
                long offset = input - sourceRanges.get(i).start;
                return destinationRanges.get(i).start + offset;
            }
        }
        return -1;
    }

    public int getSourceIndex(long input) {
        for (int i = 0; i < sourceRanges.size(); i++) {
            if (sourceRanges.get(i).start <= input && input <= sourceRanges.get(i).end) {
                return i;
            }
        }
        return -1;
    }
}

