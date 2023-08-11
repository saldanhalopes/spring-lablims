package br.com.lablims.rest;

import br.com.lablims.model.SimplePage;
import br.com.lablims.model.SolucaoParemetroDTO;
import br.com.lablims.service.SolucaoParemetroService;
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
@RequestMapping(value = "/api/solucaoParemetros", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class SolucaoParemetroResource {

    private final SolucaoParemetroService solucaoParemetroService;

    public SolucaoParemetroResource(final SolucaoParemetroService solucaoParemetroService) {
        this.solucaoParemetroService = solucaoParemetroService;
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
    public ResponseEntity<SimplePage<SolucaoParemetroDTO>> getAllSolucaoParemetros(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(solucaoParemetroService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolucaoParemetroDTO> getSolucaoParemetro(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(solucaoParemetroService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createSolucaoParemetro(
            @RequestBody @Valid final SolucaoParemetroDTO solucaoParemetroDTO) {
        final Integer createdId = solucaoParemetroService.create(solucaoParemetroDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateSolucaoParemetro(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final SolucaoParemetroDTO solucaoParemetroDTO) {
        solucaoParemetroService.update(id, solucaoParemetroDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSolucaoParemetro(
            @PathVariable(name = "id") final Integer id) {
        solucaoParemetroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
