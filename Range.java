
class Range {
    public long start;
    public long end;

    public Range() {
        start = 0;
        end = Long.MAX_VALUE;
    }

    public Range(Range r) {
        start = r.start;
        end = r.end;
    }

    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }
}
