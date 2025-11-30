package rag;

import java.io.IOException;
import java.util.List;


public interface IEmbeddingService {
    List<Double> getEmbedding(String text) throws IOException;
}

