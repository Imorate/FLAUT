package ir.imorate.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node<T> {
    private T label;

    @Override
    public String toString() {
        return "{" + label + '}';
    }
}
