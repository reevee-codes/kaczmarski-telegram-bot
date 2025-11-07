package handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rag.EmbeddingService;
import rag.RagService;
import utils.PropertiesLoader;

import java.util.Properties;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class KaczmarskiBotTests {

    private KaczmarskiGPTHandler handler;
    private String apiKey;

    @BeforeEach
    void setUp() {
        Properties config = PropertiesLoader.load("config.properties");
        apiKey = config.getProperty("openai.api.key");
        assertNotNull(apiKey, "Nie znaleziono openai.api.key w config.properties!");

        EmbeddingService embeddingService = new EmbeddingService(apiKey);
        RagService ragService = new RagService(embeddingService);
        try {
            ragService.getStore().loadFromJson("src/main/resources/embeddings.json");
        } catch (Exception e) {
        }
        handler = new KaczmarskiGPTHandler(apiKey, ragService);
    }

    @Test
    void testResponseTime() {
        String samplePrompt = "O czym jest utwór Mury?";

        long start = System.nanoTime();
        String response = handler.processMessage(1234L, samplePrompt);
        long end = System.nanoTime();

        long elapsedMillis = (end - start) / 1_000_000;
        System.out.println("Czas odpowiedzi bota: " + elapsedMillis + " ms");

        assertNotNull(response);
        assertFalse(response.trim().isEmpty(), "Odpowiedź nie może być pusta");
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


        assertNotNull(response1, "Odpowiedź dla chatId1 nie może być null");
        assertNotNull(response2, "Odpowiedź dla chatId2 nie może być null");
        assertNotNull(response3, "Odpowiedź dla chatId3 nie może być null");


        assertTrue(handler.chatHistoryContains(chatId1, prompt1), 
                "Historia chatId1 powinna zawierać prompt1");
        assertTrue(handler.chatHistoryContains(chatId2, prompt2), 
                "Historia chatId2 powinna zawierać prompt2");
        assertTrue(handler.chatHistoryContains(chatId3, prompt3),
                "Historia chatId3 powinna zawierać prompt3");

        int historySize1 = handler.getChatHistorySize(chatId1);
        int historySize2 = handler.getChatHistorySize(chatId2);
        int historySize3 = handler.getChatHistorySize(chatId3);

        assertTrue(historySize1 > 0, "ChatId1 powinien mieć historię");
        assertTrue(historySize2 > 0, "ChatId2 powinien mieć historię");
        assertTrue(historySize3 > 0, "ChatId3 powinien mieć historię");


        assertFalse(handler.chatHistoryContains(chatId2, prompt1), 
                "Historie rozmów nie powinny się mieszać - chatId2 nie powinien mieć prompt1");
        assertFalse(handler.chatHistoryContains(chatId1, prompt2), 
                "Historie rozmów nie powinny się mieszać - chatId1 nie powinien mieć prompt2");
        assertFalse(handler.chatHistoryContains(chatId1, prompt3),
                "Historie rozmów nie powinny się mieszać - chatId1 nie powinien mieć prompt3");
        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS), "Executor powinien się zamknąć");
    }
}
