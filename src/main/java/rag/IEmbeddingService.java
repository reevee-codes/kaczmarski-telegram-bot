package rag;

import java.io.IOException;
import java.util.List;

/**
 * Interfejs dla serwisu generującego embeddingi tekstowe.
 */
public interface IEmbeddingService {
    /**
     * Generuje embedding dla danego tekstu.
     *
     * @param text tekst do przetworzenia
     * @return lista wartości numerycznych reprezentująca embedding
     * @throws IOException w przypadku błędu komunikacji z API
     */
    List<Double> getEmbedding(String text) throws IOException;
}

