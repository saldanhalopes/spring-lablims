package br.com.lablims.rest;

import br.com.lablims.model.AnaliseStatusDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.AnaliseStatusService;
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
@RequestMapping(value = "/api/analiseStatuss", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class AnaliseStatusResource {

    private final AnaliseStatusService analiseStatusService;

    public AnaliseStatusResource(final AnaliseStatusService analiseStatusService) {
        this.analiseStatusService = analiseStatusService;
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
    public ResponseEntity<SimplePage<AnaliseStatusDTO>> getAllAnaliseStatuss(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(analiseStatusService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnaliseStatusDTO> getAnaliseStatus(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(analiseStatusService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAnaliseStatus(
            @RequestBody @Valid final AnaliseStatusDTO analiseStatusDTO) {
        final Integer createdId = analiseStatusService.create(analiseStatusDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateAnaliseStatus(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final AnaliseStatusDTO analiseStatusDTO) {
        analiseStatusService.update(id, analiseStatusDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAnaliseStatus(@PathVariable(name = "id") final Integer id) {
        analiseStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }

}