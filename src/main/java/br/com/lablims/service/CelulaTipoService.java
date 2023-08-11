package br.com.lablims.service;

import br.com.lablims.domain.Celula;
import br.com.lablims.domain.CelulaTipo;
import br.com.lablims.model.CelulaTipoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.CelulaRepository;
import br.com.lablims.repos.CelulaTipoRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CelulaTipoService {

    private final CelulaTipoRepository celulaTipoRepository;
    private final CelulaRepository celulaRepository;

    public CelulaTipoService(final CelulaTipoRepository celulaTipoRepository,
            final CelulaRepository celulaRepository) {
        this.celulaTipoRepository = celulaTipoRepository;
        this.celulaRepository = celulaRepository;
    }

    public SimplePage<CelulaTipoDTO> findAll(final String filter, final Pageable pageable) {
        Page<CelulaTipo> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = celulaTipoRepository.findAllById(integerFilter, pageable);
        } else {
            page = celulaTipoRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(celulaTipo -> mapToDTO(celulaTipo, new CelulaTipoDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public CelulaTipoDTO get(final Integer id) {
        return celulaTipoRepository.findById(id)
                .map(celulaTipo -> mapToDTO(celulaTipo, new CelulaTipoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CelulaTipoDTO celulaTipoDTO) {
        final CelulaTipo celulaTipo = new CelulaTipo();
        mapToEntity(celulaTipoDTO, celulaTipo);
        return celulaTipoRepository.save(celulaTipo).getId();
    }

    public void update(final Integer id, final CelulaTipoDTO celulaTipoDTO) {
        final CelulaTipo celulaTipo = celulaTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(celulaTipoDTO, celulaTipo);
        celulaTipoRepository.save(celulaTipo);
    }

    public void delete(final Integer id) {
        celulaTipoRepository.deleteById(id);
    }

    private CelulaTipoDTO mapToDTO(final CelulaTipo celulaTipo, final CelulaTipoDTO celulaTipoDTO) {
        celulaTipoDTO.setId(celulaTipo.getId());
        celulaTipoDTO.setTipo(celulaTipo.getTipo());
        return celulaTipoDTO;
    }

    private CelulaTipo mapToEntity(final CelulaTipoDTO celulaTipoDTO, final CelulaTipo celulaTipo) {
        celulaTipo.setTipo(celulaTipoDTO.getTipo());
        return celulaTipo;
    }

    public String getReferencedWarning(final Integer id) {
        final CelulaTipo celulaTipo = celulaTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Celula tipoCelula = celulaRepository.findFirstByTipo(celulaTipo);
        if (tipoCelula != null) {
            return WebUtils.getMessage("celulaTipo.celula.tipo.referenced", tipoCelula.getId());
        }
        return null;
    }

}
