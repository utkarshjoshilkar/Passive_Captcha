// package com.passivecaptcha.server.ml;

// import com.passivecaptcha.server.model.UserFeatures;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;

// import java.time.Duration;
// import java.util.HashMap;
// import java.util.Map;

// @Component
// public class MlModelClient {
//     private final WebClient webClient;

//     public MlModelClient(@Value("${ml.service.url:http://localhost:8000}") String baseUrl) {
//         this.webClient = WebClient.builder()
//                 .baseUrl(baseUrl)
//                 .build();
//     }

//     public double predictScore(UserFeatures features) {
//         Map<String, Object> body = new HashMap<>();
//         body.put("numPointerMoves", features.getNumPointerMoves());
//         body.put("avgPointerSpeed", features.getAvgPointerSpeed());
//         body.put("usedKeyboard", features.isUsedKeyboard());
//         body.put("sessionDuration", features.getSessionDuration());

//         try {
//             Mono<Map> respMono = webClient.post()
//                     .uri("/predict")
//                     .bodyValue(body)
//                     .retrieve()
//                     .bodyToMono(Map.class);

//             Map response = respMono.block(Duration.ofSeconds(5));
//             if (response != null && response.get("score") != null) {
//                 Number n = (Number) response.get("score");
//                 return n.doubleValue();
//             } else {
//                 throw new RuntimeException("Invalid response from ML service: " + response);
//             }
//         } catch (Exception e) {
//             throw new RuntimeException("ML service call failed: " + e.getMessage(), e);
//         }
//     }
// }
