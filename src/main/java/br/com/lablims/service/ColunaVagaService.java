package br.com.lablims.service;

import br.com.lablims.domain.ColunaStorage;
import br.com.lablims.domain.ColunaUtil;
import br.com.lablims.domain.ColunaVaga;
import br.com.lablims.model.ColunaVagaDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.ColunaStorageRepository;
import br.com.lablims.repos.ColunaUtilRepository;
import br.com.lablims.repos.ColunaVagaRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ColunaVagaService {

    private final ColunaVagaRepository colunaVagaRepository;
    private final ColunaStorageRepository colunaStorageRepository;
    private final ColunaUtilRepository colunaUtilRepository;

    public ColunaVagaService(final ColunaVagaRepository colunaVagaRepository,
            final ColunaStorageRepository colunaStorageRepository,
            final ColunaUtilRepository colunaUtilRepository) {
        this.colunaVagaRepository = colunaVagaRepository;
        this.colunaStorageRepository = colunaStorageRepository;
        this.colunaUtilRepository = colunaUtilRepository;
    }

    public SimplePage<ColunaVagaDTO> findAll(final String filter, final Pageable pageable) {
        Page<ColunaVaga> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = colunaVagaRepository.findAllById(integerFilter, pageable);
        } else {
            page = colunaVagaRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(colunaVaga -> mapToDTO(colunaVaga, new ColunaVagaDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ColunaVagaDTO get(final Integer id) {
        return colunaVagaRepository.findById(id)
                .map(colunaVaga -> mapToDTO(colunaVaga, new ColunaVagaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ColunaVagaDTO colunaVagaDTO) {
        final ColunaVaga colunaVaga = new ColunaVaga();
        mapToEntity(colunaVagaDTO, colunaVaga);
        return colunaVagaRepository.save(colunaVaga).getId();
    }

    public void update(final Integer id, final ColunaVagaDTO colunaVagaDTO) {
        final ColunaVaga colunaVaga = colunaVagaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(colunaVagaDTO, colunaVaga);
        colunaVagaRepository.save(colunaVaga);
    }

    public void delete(final Integer id) {
        colunaVagaRepository.deleteById(id);
    }

    private ColunaVagaDTO mapToDTO(final ColunaVaga colunaVaga, final ColunaVagaDTO colunaVagaDTO) {
        colunaVagaDTO.setId(colunaVaga.getId());
        colunaVagaDTO.setVaga(colunaVaga.getVaga());
        colunaVagaDTO.setObs(colunaVaga.getObs());
        colunaVagaDTO.setColunaStorage(colunaVaga.getColunaStorage() == null ? null : colunaVaga.getColunaStorage().getId());
        return colunaVagaDTO;
    }

    private ColunaVaga mapToEntity(final ColunaVagaDTO colunaVagaDTO, final ColunaVaga colunaVaga) {
        colunaVaga.setVaga(colunaVagaDTO.getVaga());
        colunaVaga.setObs(colunaVagaDTO.getObs());
        final ColunaStorage colunaStorage = colunaVagaDTO.getColunaStorage() == null ? null : colunaStorageRepository.findById(colunaVagaDTO.getColunaStorage())
                .orElseThrow(() -> new NotFoundException("colunaStorage not found"));
        colunaVaga.setColunaStorage(colunaStorage);
        return colunaVaga;
    }

    public String getReferencedWarning(final Integer id) {
        final ColunaVaga colunaVaga = colunaVagaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ColunaUtil colunaVagaColunaUtil = colunaUtilRepository.findFirstByColunaVaga(colunaVaga);
        if (colunaVagaColunaUtil != null) {
            return WebUtils.getMessage("colunaVaga.colunaUtil.colunaVaga.referenced", colunaVagaColunaUtil.getId());
        }
        return null;
    }

}
