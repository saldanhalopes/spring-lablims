package br.com.lablims.rest;

import br.com.lablims.model.AnaliseTipoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.AnaliseTipoService;
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
@RequestMapping(value = "/api/analiseTipos", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class AnaliseTipoResource {

    private final AnaliseTipoService analiseTipoService;

    public AnaliseTipoResource(final AnaliseTipoService analiseTipoService) {
        this.analiseTipoService = analiseTipoService;
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
    public ResponseEntity<SimplePage<AnaliseTipoDTO>> getAllAnaliseTipos(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(analiseTipoService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnaliseTipoDTO> getAnaliseTipo(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(analiseTipoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAnaliseTipo(
            @RequestBody @Valid final AnaliseTipoDTO analiseTipoDTO) {
        final Integer createdId = analiseTipoService.create(analiseTipoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateAnaliseTipo(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final AnaliseTipoDTO analiseTipoDTO) {
        analiseTipoService.update(id, analiseTipoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAnaliseTipo(@PathVariable(name = "id") final Integer id) {
        analiseTipoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
