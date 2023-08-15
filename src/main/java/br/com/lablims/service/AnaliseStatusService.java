package br.com.lablims.service;

import br.com.lablims.domain.AnaliseProdutividade;
import br.com.lablims.domain.AnaliseStatus;
import br.com.lablims.domain.AnaliseTipo;
import br.com.lablims.domain.LoteStatus;
import br.com.lablims.model.AnaliseStatusDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.AnaliseProdutividadeRepository;
import br.com.lablims.repos.AnaliseStatusRepository;
import br.com.lablims.repos.LoteStatusRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AnaliseStatusService {

    private final AnaliseStatusRepository analiseStatusRepository;
    private final AnaliseProdutividadeRepository analiseProdutividadeRepository;
    private final LoteStatusRepository loteStatusRepository;

    public AnaliseStatus findById(Integer id){
        return analiseStatusRepository.findById(id).orElse(null);
    }

    public AnaliseStatusService(final AnaliseStatusRepository analiseStatusRepository,
            final AnaliseProdutividadeRepository analiseProdutividadeRepository,
            final LoteStatusRepository loteStatusRepository) {
        this.analiseStatusRepository = analiseStatusRepository;
        this.analiseProdutividadeRepository = analiseProdutividadeRepository;
        this.loteStatusRepository = loteStatusRepository;
    }

    public SimplePage<AnaliseStatusDTO> findAll(final String filter, final Pageable pageable) {
        Page<AnaliseStatus> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = analiseStatusRepository.findAllById(integerFilter, pageable);
        } else {
            page = analiseStatusRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(analiseStatus -> mapToDTO(analiseStatus, new AnaliseStatusDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public AnaliseStatusDTO get(final Integer id) {
        return analiseStatusRepository.findById(id)
                .map(analiseStatus -> mapToDTO(analiseStatus, new AnaliseStatusDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AnaliseStatusDTO analiseStatusDTO) {
        final AnaliseStatus analiseStatus = new AnaliseStatus();
        mapToEntity(analiseStatusDTO, analiseStatus);
        return analiseStatusRepository.save(analiseStatus).getId();
    }

    public void update(final Integer id, final AnaliseStatusDTO analiseStatusDTO) {
        final AnaliseStatus analiseStatus = analiseStatusRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(analiseStatusDTO, analiseStatus);
        analiseStatusRepository.save(analiseStatus);
    }

    public void delete(final Integer id) {
        analiseStatusRepository.deleteById(id);
    }

    private AnaliseStatusDTO mapToDTO(final AnaliseStatus analiseStatus,
            final AnaliseStatusDTO analiseStatusDTO) {
        analiseStatusDTO.setId(analiseStatus.getId());
        analiseStatusDTO.setAnaliseStatus(analiseStatus.getAnaliseStatus());
        analiseStatusDTO.setSiglaAnaliseStatus(analiseStatus.getSiglaAnaliseStatus());
        analiseStatusDTO.setDescricaoAnaliseStatus(analiseStatus.getDescricaoAnaliseStatus());
        analiseStatusDTO.setAnaliseProdutividade(analiseStatus.getAnaliseProdutividade() == null ? null : analiseStatus.getAnaliseProdutividade().getId());
        return analiseStatusDTO;
    }

    private AnaliseStatus mapToEntity(final AnaliseStatusDTO analiseStatusDTO,
            final AnaliseStatus analiseStatus) {
        analiseStatus.setAnaliseStatus(analiseStatusDTO.getAnaliseStatus());
        analiseStatus.setSiglaAnaliseStatus(analiseStatusDTO.getSiglaAnaliseStatus());
        analiseStatus.setDescricaoAnaliseStatus(analiseStatusDTO.getDescricaoAnaliseStatus());
        final AnaliseProdutividade analiseProdutividade = analiseStatusDTO.getAnaliseProdutividade() == null ? null : analiseProdutividadeRepository.findById(analiseStatusDTO.getAnaliseProdutividade())
                .orElseThrow(() -> new NotFoundException("analiseProdutividade not found"));
        analiseStatus.setAnaliseProdutividade(analiseProdutividade);
        return analiseStatus;
    }

    public String getReferencedWarning(final Integer id) {
        final AnaliseStatus analiseStatus = analiseStatusRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final LoteStatus analiseStatusLoteStatus = loteStatusRepository.findFirstByAnaliseStatus(analiseStatus);
        if (analiseStatusLoteStatus != null) {
            return WebUtils.getMessage("analiseStatus.loteStatus.analiseStatus.referenced", analiseStatusLoteStatus.getId());
        }
        return null;
    }

}
