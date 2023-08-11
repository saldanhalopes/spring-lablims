package br.com.lablims.rest;

import br.com.lablims.model.MetodologiaStatusDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.MetodologiaStatusService;
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
@RequestMapping(value = "/api/metodologiaStatuss", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class MetodologiaStatusResource {

    private final MetodologiaStatusService metodologiaStatusService;

    public MetodologiaStatusResource(final MetodologiaStatusService metodologiaStatusService) {
        this.metodologiaStatusService = metodologiaStatusService;
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
    public ResponseEntity<SimplePage<MetodologiaStatusDTO>> getAllMetodologiaStatuss(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(metodologiaStatusService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodologiaStatusDTO> getMetodologiaStatus(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(metodologiaStatusService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createMetodologiaStatus(
            @RequestBody @Valid final MetodologiaStatusDTO metodologiaStatusDTO) {
        final Integer createdId = metodologiaStatusService.create(metodologiaStatusDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateMetodologiaStatus(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final MetodologiaStatusDTO metodologiaStatusDTO) {
        metodologiaStatusService.update(id, metodologiaStatusDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMetodologiaStatus(
            @PathVariable(name = "id") final Integer id) {
        metodologiaStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
