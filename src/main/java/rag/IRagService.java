package rag;

import java.io.IOException;

/**
 * Interfejs dla serwisu RAG (Retrieval-Augmented Generation).
 */
public interface IRagService {
    /**
     * Ładuje pliki tekstowe i generuje dla nich embeddingi.
     *
     * @param folderPath ścieżka do folderu z plikami tekstowymi
     * @throws IOException w przypadku błędu odczytu/zapisu plików
     */
    void loadFiles(String folderPath) throws IOException;

    /**
     * Buduje prompt dla użytkownika na podstawie najbardziej podobnych fragmentów.
     *
     * @param userQuestion pytanie użytkownika
     * @return sformatowany prompt z kontekstem
     * @throws IOException w przypadku błędu podczas generowania embeddingu
     */
    String buildPrompt(String userQuestion) throws IOException;

    /**
     * Zwraca store z embeddingami.
     *
     * @return VectorStore z zapisanymi embeddingami
     */
    VectorStore getStore();
}

