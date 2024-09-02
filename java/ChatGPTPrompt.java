package llm;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatGPTPrompt {

    private static final String OPENAI_API_KEY = "bogus";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) throws IOException, InterruptedException {
        // Disabled csv analysis piece to avoid costs; even though using apparently free models,
        // have to pay (seems to be automatic promotion to billing tiers based upon overall usage of an account)
        /*
        var curdir = Paths.get("");
        System.out.println("Work dir = " + curdir.toAbsolutePath());
        String csvFilePath = curdir.toAbsolutePath() + "/src/main/resources/music-min.csv";
        String csvContent = Files.readString(Path.of(csvFilePath), StandardCharsets.UTF_8);
        System.out.println(csvContent);
        System.out.println("words = " + csvContent.replace("\n", ",").split(",").length);
        String prompt = "Analyze the following CSV data:\n```\n" + csvContent + "\n```\nProvide insights, trends, and potential anomalies.";
        */

        String testPrompt = "what is the capital of Norway";

        String requestBody = createRequestBody(testPrompt, GptModel.GPT_3_5_TURBO);

        // Create HTTP request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        System.out.println(requestBody);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        /** Sample response:

         > Task :ChatGPTPrompt.main()
         {"messages":[{"content":"what is the capital of Norway","role":"user"}],"model":"gpt-3.5-turbo"}
         {
         "id": "chatcmpl-9wY196p0526UoukMPBgysO4gHkYiY",
         "object": "chat.completion",
         "created": 1723740815,
         "model": "gpt-3.5-turbo-0125",
         "choices": [
         {
         "index": 0,
         "message": {
         "role": "assistant",
         "content": "The capital of Norway is Oslo.",
         "refusal": null
         },
         "logprobs": null,
         "finish_reason": "stop"
         }
         ],
         "usage": {
         "prompt_tokens": 13,
         "completion_tokens": 7,
         "total_tokens": 20
         },
         "system_fingerprint": null
         }

         */
    }

    private static String createRequestBody(String prompt, GptModel model) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model.modelName);
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public enum GptModel {
        // GPT-4 Models
        GPT_4("gpt-4", "Default GPT-4 model, used for general-purpose tasks.", "Higher cost per token", false),
        GPT_4_0613("gpt-4-0613", "Optimized GPT-4 variant, released in June 2023.", "Higher cost per token", false),
        GPT_4_32K("gpt-4-32k", "GPT-4 model with a larger context window of 32,768 tokens for longer conversations/documents.", "Highest cost per token", false),
        GPT_4_32K_0613("gpt-4-32k-0613", "32k variant of GPT-4 optimized, released in June 2023.", "Highest cost per token", false),

        // GPT-3.5 Models
        GPT_3_5_TURBO("gpt-3.5-turbo", "More cost-effective and faster version of GPT-3.5 for interactive applications.", "Lower cost per token", true),
        GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k", "GPT-3.5-turbo with a larger context window of 16,384 tokens.", "Lower cost per token", false),
        GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613", "Optimized GPT-3.5-turbo variant, released in June 2023.", "Lower cost per token", true),
        GPT_3_5_TURBO_16K_0613("gpt-3.5-turbo-16k-0613", "16k variant of GPT-3.5-turbo optimized in June 2023.", "Lower cost per token", false),

        // Other Models
        DAVINCI("davinci", "Most powerful GPT-3 model, suited for complex tasks requiring deep understanding.", "High cost per token", false),
        CURIE("curie", "Smaller, faster than Davinci, suitable for tasks where speed is prioritized over depth.", "Moderate cost per token", false),
        BABBAGE("babbage", "Smaller, faster model for straightforward tasks.", "Low cost per token", false),
        ADA("ada", "Fastest and smallest model, ideal for very simple tasks.", "Lowest cost per token", false);

        private final String modelName;
        private final String description;
        private final String cost;
        private final boolean freeToUse;

        GptModel(String modelName, String description, String cost, boolean freeToUse) {
            this.modelName = modelName;
            this.description = description;
            this.cost = cost;
            this.freeToUse = freeToUse;
        }

        public String getModelName() {
            return modelName;
        }

        public String getDescription() {
            return description;
        }

        public String getCost() {
            return cost;
        }

        public boolean isFreeToUse() {
            return freeToUse;
        }
    }

}
