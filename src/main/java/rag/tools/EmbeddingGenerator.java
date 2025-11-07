package rag.tools;

import config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rag.*;
import utils.PropertiesLoader;

import java.io.IOException;
import java.util.Properties;

public class EmbeddingGenerator {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddingGenerator.class);
    
    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesLoader.load("config.properties");
        String apiKey = properties.getProperty("openai.api.key");
        EmbeddingService embeddingService = new EmbeddingService(apiKey);
        RagService rag = new RagService(embeddingService);

        rag.loadFiles(AppConfig.DATA_FOLDER_PATH);
        rag.getStore().saveToJson(AppConfig.EMBEDDINGS_JSON_PATH);

        logger.info("✅ Zapisano embeddingi do embeddings.json");
    }
}
