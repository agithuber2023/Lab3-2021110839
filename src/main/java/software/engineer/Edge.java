package software.engineer;

import java.util.Objects;

record Edge(String from, String to, int value) {

    @Override
    public String toString() {
        return "(" + from + ", " + to + ") = " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return value == edge.value && Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
    }

}


