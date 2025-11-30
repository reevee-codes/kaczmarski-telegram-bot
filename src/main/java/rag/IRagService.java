package rag;

import java.io.IOException;

public interface IRagService {

    void loadFiles(String folderPath) throws IOException;

    String buildPrompt(String userQuestion) throws IOException;

    VectorStore getStore();
}

