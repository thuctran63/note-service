package englishapp.api.note_service.controllers;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import api.common.englishapp.auth.RequiresAuth;
import api.common.englishapp.auth.UserData;
import api.common.englishapp.requests.CommonResponse;
import api.common.englishapp.requests.ResponseUtil;
import englishapp.api.note_service.dto.apiAddWordManually.InputParamApiAddWordManually;
import englishapp.api.note_service.dto.apiDeleteWord.InputParamApiDeleteWord;
import englishapp.api.note_service.dto.apiUpdateWord.InputParamApiUpdateWord;
import englishapp.api.note_service.services.NoteService;
import reactor.core.publisher.Mono;

@RestController
@SuppressWarnings("null")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @RequiresAuth
    @GetMapping("/getNote")
    public Mono<ResponseEntity<CommonResponse<?>>> getNote(ServerWebExchange exchange) {
        UserData userData = exchange.getAttribute("USER_DATA");
        if (Objects.isNull(userData)) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }

        return noteService.getNote(
                userData.getUserId())
                .map(ResponseUtil::ok)
                .defaultIfEmpty(ResponseUtil.noContent());
    }

    @RequiresAuth
    @PostMapping("/addWord")
    public Mono<ResponseEntity<CommonResponse<?>>> addWord(ServerWebExchange exchange,
            @RequestBody InputParamApiAddWordManually inputParam) {
        UserData userData = exchange.getAttribute("USER_DATA");

        if (Objects.isNull(userData)) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }

        return noteService.addWordManually(
                userData.getUserId(), inputParam)
                .map(word -> ResponseUtil.ok(word)) // Trả về LearningWord nếu thêm thành công
                .defaultIfEmpty(ResponseUtil.noContent()); // Nếu không tìm thấy hoặc không thêm được, trả về noContent
    }

    @RequiresAuth
    @PostMapping("/deleteWord")
    public Mono<ResponseEntity<CommonResponse<?>>> deleteWord(ServerWebExchange exchange,
            @RequestBody InputParamApiDeleteWord inputParam) {
        UserData userData = exchange.getAttribute("USER_DATA");

        if (Objects.isNull(userData)) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }

        return noteService.deleteWord(
                userData.getUserId(), inputParam)
                .map(word -> ResponseUtil.ok(word)) // Trả về LearningWord nếu thêm thành công
                .defaultIfEmpty(ResponseUtil.noContent()); // Nếu không tìm thấy hoặc không thêm được, trả về noContent
    }

    @RequiresAuth
    @PostMapping("/updateWord")
    public Mono<ResponseEntity<CommonResponse<?>>> updateWord(ServerWebExchange exchange,
            @RequestBody InputParamApiUpdateWord inputParam) {
        UserData userData = exchange.getAttribute("USER_DATA");

        if (Objects.isNull(userData)) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }

        return noteService.updateWord(
                userData.getUserId(), inputParam)
                .map(word -> ResponseUtil.ok(word)) // Trả về LearningWord nếu thêm thành công
                .defaultIfEmpty(ResponseUtil.noContent()); // Nếu không tìm thấy hoặc không thêm được, trả về noContent
    }

}
