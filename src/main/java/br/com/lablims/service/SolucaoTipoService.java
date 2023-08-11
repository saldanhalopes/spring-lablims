package br.com.lablims.service;

import br.com.lablims.domain.SolucaoRegistro;
import br.com.lablims.domain.SolucaoTipo;
import br.com.lablims.model.SimplePage;
import br.com.lablims.model.SolucaoTipoDTO;
import br.com.lablims.repos.SolucaoRegistroRepository;
import br.com.lablims.repos.SolucaoTipoRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class SolucaoTipoService {

    private final SolucaoTipoRepository solucaoTipoRepository;
    private final SolucaoRegistroRepository solucaoRegistroRepository;

    public SolucaoTipoService(final SolucaoTipoRepository solucaoTipoRepository,
            final SolucaoRegistroRepository solucaoRegistroRepository) {
        this.solucaoTipoRepository = solucaoTipoRepository;
        this.solucaoRegistroRepository = solucaoRegistroRepository;
    }

    public SimplePage<SolucaoTipoDTO> findAll(final String filter, final Pageable pageable) {
        Page<SolucaoTipo> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = solucaoTipoRepository.findAllById(integerFilter, pageable);
        } else {
            page = solucaoTipoRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(solucaoTipo -> mapToDTO(solucaoTipo, new SolucaoTipoDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public SolucaoTipoDTO get(final Integer id) {
        return solucaoTipoRepository.findById(id)
                .map(solucaoTipo -> mapToDTO(solucaoTipo, new SolucaoTipoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final SolucaoTipoDTO solucaoTipoDTO) {
        final SolucaoTipo solucaoTipo = new SolucaoTipo();
        mapToEntity(solucaoTipoDTO, solucaoTipo);
        return solucaoTipoRepository.save(solucaoTipo).getId();
    }

    public void update(final Integer id, final SolucaoTipoDTO solucaoTipoDTO) {
        final SolucaoTipo solucaoTipo = solucaoTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(solucaoTipoDTO, solucaoTipo);
        solucaoTipoRepository.save(solucaoTipo);
    }

    public void delete(final Integer id) {
        solucaoTipoRepository.deleteById(id);
    }

    private SolucaoTipoDTO mapToDTO(final SolucaoTipo solucaoTipo,
            final SolucaoTipoDTO solucaoTipoDTO) {
        solucaoTipoDTO.setId(solucaoTipo.getId());
        solucaoTipoDTO.setSiglaSolucao(solucaoTipo.getSiglaSolucao());
        solucaoTipoDTO.setTipoSolucao(solucaoTipo.getTipoSolucao());
        return solucaoTipoDTO;
    }

    private SolucaoTipo mapToEntity(final SolucaoTipoDTO solucaoTipoDTO,
            final SolucaoTipo solucaoTipo) {
        solucaoTipo.setSiglaSolucao(solucaoTipoDTO.getSiglaSolucao());
        solucaoTipo.setTipoSolucao(solucaoTipoDTO.getTipoSolucao());
        return solucaoTipo;
    }

    public String getReferencedWarning(final Integer id) {
        final SolucaoTipo solucaoTipo = solucaoTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final SolucaoRegistro solucaoTipoSolucaoRegistro = solucaoRegistroRepository.findFirstBySolucaoTipo(solucaoTipo);
        if (solucaoTipoSolucaoRegistro != null) {
            return WebUtils.getMessage("solucaoTipo.solucaoRegistro.solucaoTipo.referenced", solucaoTipoSolucaoRegistro.getId());
        }
        return null;
    }

}
