package com.example.complain_service.controller;

import com.example.complain_service.common.dto.response.ApiResponse;
import com.example.complain_service.common.dto.response.BaseGetAllResponse;
import com.example.complain_service.dto.complain.*;
import com.example.complain_service.service.Complain.ComplainService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/complains")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ComplainController {

    ComplainService complainService;

    @PostMapping("/Create")
    public ApiResponse<ComplainResponse> create(@RequestBody ComplainCreateRequest request) {
        return ApiResponse.<ComplainResponse>builder()
                .result(complainService.create(request))
                .build();
    }

    @PutMapping("/Update")
    public ApiResponse<ComplainResponse> update(@RequestBody ComplainUpdateRequest request) {
        return ApiResponse.<ComplainResponse>builder()
                .result(complainService.update(request))
                .build();
    }

    // 3. Lấy tất cả complain (có filter & pagination)
    @GetMapping("/GetAll")
    public ApiResponse<BaseGetAllResponse<ComplainResponse>> getAllComplains(
            @RequestParam(value = "skipCount", defaultValue = "0") int skipCount,
            @RequestParam(value = "maxResultCount", defaultValue = "10") int maxResultCount,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "resolverId", required = false) UUID resolverId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "resolverFullName", required = false) String resolverFullName,
            @RequestParam(value = "userFullName", required = false) String userFullName


    ) {
        ComplainGetAllRequest request = ComplainGetAllRequest.builder()
                .userId(userId)
                .resolverId(resolverId)
                .status(status)
                .resolverFullName(resolverFullName)
                .userFullName(userFullName)
                .build();

        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setKeyword(keyword);

        return ApiResponse.<BaseGetAllResponse<ComplainResponse>>builder()
                .result(complainService.getAll(request))
                .build();
    }

    @GetMapping("/GetById")
    public ApiResponse<ComplainResponse> getById(@RequestParam UUID id) {
        return ApiResponse.<ComplainResponse>builder()
                .result(complainService.getById(id))
                .build();
    }

    @DeleteMapping("/Delete")
    public ApiResponse<String> delete(@RequestParam UUID id) {
        complainService.delete(id);
        return ApiResponse.<String>builder()
                .result("Complain deleted successfully")
                .build();
    }

    @GetMapping("/GetAllUnResolved")
    public ApiResponse<BaseGetAllResponse<ComplainResponse>> getAllUnResolved(
            @RequestParam(value = "skipCount", defaultValue = "0") int skipCount,
            @RequestParam(value = "maxResultCount", defaultValue = "10") int maxResultCount,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "resolverId", required = false) UUID resolverId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "resolverFullName", required = false) String resolverFullName,
            @RequestParam(value = "userFullName", required = false) String userFullName


    ) {
        ComplainGetAllRequest request = ComplainGetAllRequest.builder()
                .userId(userId)
                .resolverId(resolverId)
                .status(status)
                .resolverFullName(resolverFullName)
                .userFullName(userFullName)
                .build();

        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setKeyword(keyword);

        return ApiResponse.<BaseGetAllResponse<ComplainResponse>>builder()
                .result(complainService.getAllUnresolved(request))
                .build();
    }


    // 6. Claim khiếu nại
    @PostMapping("/Claim")
    public ApiResponse<ComplainResponse> claim(@RequestBody ComplainAssignRequest request) {
        return ApiResponse.<ComplainResponse>builder()
                .result(complainService.claim(request))
                .build();
    }

    // 7. Assign khiếu nại cho resolver
    @PostMapping("/Assign")
    public ApiResponse<ComplainResponse> assign(@RequestBody ComplainAssignRequest request) {
        return ApiResponse.<ComplainResponse>builder()
                .result(complainService.assign(request))
                .build();
    }

    // 8. Đánh dấu khiếu nại đã xử lý
    @PostMapping("/Resolve")
    public ApiResponse<ComplainResponse> resolve(@RequestBody ComplainAssignRequest request) {
        return ApiResponse.<ComplainResponse>builder()
                .result(complainService.resolve(request))
                .build();
    }

    // 9. Ghi chú xử lý khiếu nại
    @PostMapping("/Note")
    public ApiResponse<ComplainResponse> note(@RequestBody ComplainNoteRequest request) {
        return ApiResponse.<ComplainResponse>builder()
                .result(complainService.note(request))
                .build();
    }
}