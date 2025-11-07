package config;

public class AppConfig {

    public static final String EMBEDDINGS_JSON_PATH = "src/main/resources/embeddings.json";
    public static final String DATA_FOLDER_PATH = "src/main/resources/data";

    public static final String PRIMARY_MODEL = "gpt-4";
    public static final String FALLBACK_MODEL = "gpt-3.5-turbo";
    public static final double TEMPERATURE = 0.3;
    public static final int MAX_TOKENS = 300;
    public static final double PRESENCE_PENALTY = 0.7;
    public static final double FREQUENCY_PENALTY = 0.7;
    public static final int TOP_K_RESULTS = 2; // Liczba najbardziej podobnych fragmentów z RAG
    
    // Timeouts
    public static final int OPENAI_TIMEOUT_SECONDS = 60;
    
    private AppConfig() {
    }
}

