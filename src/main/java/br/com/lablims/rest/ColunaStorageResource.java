package br.com.lablims.rest;

import br.com.lablims.model.ColunaStorageDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.ColunaStorageService;
import br.com.lablims.util.UserRoles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/colunaStorages", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaStorageResource {

    private final ColunaStorageService colunaStorageService;

    public ColunaStorageResource(final ColunaStorageService colunaStorageService) {
        this.colunaStorageService = colunaStorageService;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<SimplePage<ColunaStorageDTO>> getAllColunaStorages(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(colunaStorageService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColunaStorageDTO> getColunaStorage(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(colunaStorageService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createColunaStorage(
            @RequestBody @Valid final ColunaStorageDTO colunaStorageDTO) {
        final Integer createdId = colunaStorageService.create(colunaStorageDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateColunaStorage(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ColunaStorageDTO colunaStorageDTO) {
        colunaStorageService.update(id, colunaStorageDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteColunaStorage(@PathVariable(name = "id") final Integer id) {
        colunaStorageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
