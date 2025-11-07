package rag;

import java.util.List;

public record TextChunk(String source, String text, List<Double> embedding) {
}
