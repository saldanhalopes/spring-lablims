package br.com.lablims.service;

import br.com.lablims.domain.AmostraTipo;
import br.com.lablims.domain.Lote;
import br.com.lablims.model.AmostraTipoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.AmostraTipoRepository;
import br.com.lablims.repos.LoteRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AmostraTipoService {

    private final AmostraTipoRepository amostraTipoRepository;
    private final LoteRepository loteRepository;

    public AmostraTipoService(final AmostraTipoRepository amostraTipoRepository,
            final LoteRepository loteRepository) {
        this.amostraTipoRepository = amostraTipoRepository;
        this.loteRepository = loteRepository;
    }

    public SimplePage<AmostraTipoDTO> findAll(final String filter, final Pageable pageable) {
        Page<AmostraTipo> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = amostraTipoRepository.findAllById(integerFilter, pageable);
        } else {
            page = amostraTipoRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(amostraTipo -> mapToDTO(amostraTipo, new AmostraTipoDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public AmostraTipoDTO get(final Integer id) {
        return amostraTipoRepository.findById(id)
                .map(amostraTipo -> mapToDTO(amostraTipo, new AmostraTipoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AmostraTipoDTO amostraTipoDTO) {
        final AmostraTipo amostraTipo = new AmostraTipo();
        mapToEntity(amostraTipoDTO, amostraTipo);
        return amostraTipoRepository.save(amostraTipo).getId();
    }

    public void update(final Integer id, final AmostraTipoDTO amostraTipoDTO) {
        final AmostraTipo amostraTipo = amostraTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(amostraTipoDTO, amostraTipo);
        amostraTipoRepository.save(amostraTipo);
    }

    public void delete(final Integer id) {
        amostraTipoRepository.deleteById(id);
    }

    private AmostraTipoDTO mapToDTO(final AmostraTipo amostraTipo,
            final AmostraTipoDTO amostraTipoDTO) {
        amostraTipoDTO.setId(amostraTipo.getId());
        amostraTipoDTO.setTipo(amostraTipo.getTipo());
        return amostraTipoDTO;
    }

    private AmostraTipo mapToEntity(final AmostraTipoDTO amostraTipoDTO,
            final AmostraTipo amostraTipo) {
        amostraTipo.setTipo(amostraTipoDTO.getTipo());
        return amostraTipo;
    }

    public String getReferencedWarning(final Integer id) {
        final AmostraTipo amostraTipo = amostraTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Lote amostraTipoLote = loteRepository.findFirstByAmostraTipo(amostraTipo);
        if (amostraTipoLote != null) {
            return WebUtils.getMessage("amostraTipo.lote.amostraTipo.referenced", amostraTipoLote.getId());
        }
        return null;
    }

}
