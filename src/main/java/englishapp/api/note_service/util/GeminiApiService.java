package englishapp.api.note_service.util;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class GeminiApiService {

    private final WebClient webClient;

    public GeminiApiService(WebClient.Builder webClientBuilder) {
        // Khởi tạo WebClient với base URL của Gemini API
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent")
                .build();
    }

    public Mono<String> getDescriptionFromGemini(String word) {
        // Tạo request body theo cấu trúc yêu cầu
        String requestBody = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [\n" +
                "        {\n" +
                "          \"text\": \"Tạo một mô tả chi tiết cho từ '" + word
                + "' với các phần sau, theo thứ tự, định dạng plain text, không sử dụng ký hiệu markdown (như ** hoặc *), và mỗi phần bắt đầu bằng tiêu đề được ghi chính xác như sau:\\n\\n"
                +
                "Mean: Giải thích ngắn gọn nghĩa của từ bằng tiếng Việt.\\n" +
                "Example: Cung cấp đúng một câu ví dụ bằng tiếng Anh sử dụng từ này.\\n" +
                "WordType: Chỉ rõ loại từ trong ngữ pháp (ví dụ: danh từ, động từ, tính từ, trạng từ).\\n" +
                "Synonyms: Liệt kê các từ đồng nghĩa bằng tiếng Anh, cách nhau bởi dấu phẩy.\\n" +
                "Antonym: Liệt kê các từ trái nghĩa bằng tiếng Anh, cách nhau bởi dấu phẩy. Nếu không có từ trái nghĩa, ghi 'none'.\\n\\n"
                +
                "Mỗi phần phải nằm trên một dòng riêng, bắt đầu bằng tiêu đề (Mean, Example, WordType, Synonyms, Antonym) theo sau là dấu hai chấm và nội dung. Không thêm câu giải thích hoặc ghi chú nào ngoài nội dung yêu cầu. Ví dụ định dạng:\\n"
                +
                "Mean: Nghĩa của từ\\n" +
                "Example: Câu ví dụ\\n" +
                "WordType: Loại từ\\n" +
                "Synonyms: từ1, từ2, từ3\\n" +
                "Antonym: từ1, từ2 hoặc none\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return webClient.post()
                .uri("?key=AIzaSyA0cImJoZsjzmkCeXXn0jvrNn51wvRlfOg") // Thay thế GEMINI_API_KEY bằng khóa API thực tế
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class) // Ánh xạ dữ liệu trả về thành Map
                .map(response -> {
                    // Trích xuất phần text từ cấu trúc JSON
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    return (String) parts.get(0).get("text");
                })
                .onErrorResume(e -> Mono.just("Error fetching data: " + e.getMessage()));
    }
}
