package handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rag.EmbeddingService;
import rag.RagService;
import utils.PropertiesLoader;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class KaczmarskiBotTests {

    private static final Logger logger = LoggerFactory.getLogger(KaczmarskiBotTests.class);
    private KaczmarskiGPTHandler handler;
    private String apiKey;

    @BeforeEach
    void setUp() {
        Properties config = PropertiesLoader.load("config.properties");
        apiKey = config.getProperty("openai.api.key");
        assertNotNull(apiKey, "Openai.api.key in config.properties wasn't found!");

        EmbeddingService embeddingService = new EmbeddingService(apiKey);
        RagService ragService = new RagService(embeddingService);
        try {
            ragService.getStore().loadFromJson("src/main/resources/embeddings.json");
            handler = new KaczmarskiGPTHandler(apiKey, ragService);
        } catch (IOException e) {
            logger.error("Failed to load embeddings from JSON", e);
            throw new RuntimeException("Unable to initialize KaczmarskiGPTHandler", e);
        }
    }

    @Test
    void testResponseTime() {
        String samplePrompt = "O czym jest utwór Mury?";

        long start = System.nanoTime();
        String response = handler.processMessage(1234L, samplePrompt);
        long end = System.nanoTime();

        long elapsedMillis = (end - start) / 1_000_000;
        System.out.println("Time that bot took to reply: " + elapsedMillis + " ms");

        assertNotNull(response);
        assertFalse(response.trim().isEmpty(), "Response can't be blank");
    }

    @Test
    void testConcurrentChatsDoNotMix() throws Exception {
        long chatId1 = 1001L;
        long chatId2 = 1002L;
        long chatId3 = 1003L;

        String prompt1 = "O czym jest utwór Mury?";
        String prompt2 = "O czym jest utwór Źródło?";
        String prompt3 = "O czym jest utwór Rokosz?";

        ExecutorService pool = Executors.newFixedThreadPool(3);

        Callable<String> task1 = () -> handler.processMessage(chatId1, prompt1);
        Callable<String> task2 = () -> handler.processMessage(chatId2, prompt2);
        Callable<String> task3 = () -> handler.processMessage(chatId3, prompt3);

        Future<String> result1 = pool.submit(task1);
        Future<String> result2 = pool.submit(task2);
        Future<String> result3 = pool.submit(task3);

        String response1 = result1.get(30, TimeUnit.SECONDS);
        String response2 = result2.get(30, TimeUnit.SECONDS);
        String response3 = result3.get(30, TimeUnit.SECONDS);

        assertNotNull(response1, "Response for chatId1 can't be null");
        assertNotNull(response2, "Response for chatId2 can't be null");
        assertNotNull(response3, "Response for chatId3 can't be null");

        assertTrue(handler.chatHistoryContains(chatId1, prompt1),
                "History of chatId1 should include prompt1");
        assertTrue(handler.chatHistoryContains(chatId2, prompt2),
                "History of chatId2 should include prompt2");
        assertTrue(handler.chatHistoryContains(chatId3, prompt3),
                "History of chatId3 should include prompt3");

        int historySize1 = handler.getChatHistorySize(chatId1);
        int historySize2 = handler.getChatHistorySize(chatId2);
        int historySize3 = handler.getChatHistorySize(chatId3);

        assertTrue(historySize1 > 0, "ChatId1 should have history");
        assertTrue(historySize2 > 0, "ChatId2 should have history");
        assertTrue(historySize3 > 0, "ChatId3 should have history");

        assertFalse(handler.chatHistoryContains(chatId2, prompt1),
                "Chat histories must not mix — chatId2 should not contain prompt1");
        assertFalse(handler.chatHistoryContains(chatId1, prompt2),
                "Chat histories must not mix — chatId1 should not contain prompt2");
        assertFalse(handler.chatHistoryContains(chatId1, prompt3),
                "Chat histories must not mix — chatId1 should not contain prompt3");
        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS), "Executor should be closed");
    }
}
