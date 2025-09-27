package de.jave.figlet.swing.action;

public record RankedItem<T>(double rank, T value) implements Comparable<RankedItem<T>> {

    public int compareTo(RankedItem<T> o) {
        return Double.compare(this.rank, o.rank);
    }
}
