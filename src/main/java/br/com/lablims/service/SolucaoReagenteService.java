package br.com.lablims.service;

import br.com.lablims.domain.Reagente;
import br.com.lablims.domain.SolucaoReagente;
import br.com.lablims.domain.SolucaoRegistro;
import br.com.lablims.domain.UnidadeMedida;
import br.com.lablims.model.SimplePage;
import br.com.lablims.model.SolucaoReagenteDTO;
import br.com.lablims.repos.ReagenteRepository;
import br.com.lablims.repos.SolucaoReagenteRepository;
import br.com.lablims.repos.SolucaoRegistroRepository;
import br.com.lablims.repos.UnidadeMedidaRepository;
import br.com.lablims.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class SolucaoReagenteService {

    private final SolucaoReagenteRepository solucaoReagenteRepository;
    private final SolucaoRegistroRepository solucaoRegistroRepository;
    private final ReagenteRepository reagenteRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;

    public SolucaoReagente findById(Integer id){
        return solucaoReagenteRepository.findById(id).orElse(null);
    }

    public SolucaoReagenteService(final SolucaoReagenteRepository solucaoReagenteRepository,
            final SolucaoRegistroRepository solucaoRegistroRepository,
            final ReagenteRepository reagenteRepository,
            final UnidadeMedidaRepository unidadeMedidaRepository) {
        this.solucaoReagenteRepository = solucaoReagenteRepository;
        this.solucaoRegistroRepository = solucaoRegistroRepository;
        this.reagenteRepository = reagenteRepository;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
    }

    public SimplePage<SolucaoReagenteDTO> findAll(final String filter, final Pageable pageable) {
        Page<SolucaoReagente> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = solucaoReagenteRepository.findAllById(integerFilter, pageable);
        } else {
            page = solucaoReagenteRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(solucaoReagente -> mapToDTO(solucaoReagente, new SolucaoReagenteDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public SolucaoReagenteDTO get(final Integer id) {
        return solucaoReagenteRepository.findById(id)
                .map(solucaoReagente -> mapToDTO(solucaoReagente, new SolucaoReagenteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final SolucaoReagenteDTO solucaoReagenteDTO) {
        final SolucaoReagente solucaoReagente = new SolucaoReagente();
        mapToEntity(solucaoReagenteDTO, solucaoReagente);
        return solucaoReagenteRepository.save(solucaoReagente).getId();
    }

    public void update(final Integer id, final SolucaoReagenteDTO solucaoReagenteDTO) {
        final SolucaoReagente solucaoReagente = solucaoReagenteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(solucaoReagenteDTO, solucaoReagente);
        solucaoReagenteRepository.save(solucaoReagente);
    }

    public void delete(final Integer id) {
        solucaoReagenteRepository.deleteById(id);
    }

    private SolucaoReagenteDTO mapToDTO(final SolucaoReagente solucaoReagente,
            final SolucaoReagenteDTO solucaoReagenteDTO) {
        solucaoReagenteDTO.setId(solucaoReagente.getId());
        solucaoReagenteDTO.setLote(solucaoReagente.getLote());
        solucaoReagenteDTO.setQtd(solucaoReagente.getQtd());
        solucaoReagenteDTO.setFabricante(solucaoReagente.getFabricante());
        solucaoReagenteDTO.setValidade(solucaoReagente.getValidade());
        solucaoReagenteDTO.setSolucaoRegistro(solucaoReagente.getSolucaoRegistro() == null ? null : solucaoReagente.getSolucaoRegistro().getId());
        solucaoReagenteDTO.setReagente(solucaoReagente.getReagente() == null ? null : solucaoReagente.getReagente().getId());
        solucaoReagenteDTO.setUnidade(solucaoReagente.getUnidade() == null ? null : solucaoReagente.getUnidade().getId());
        solucaoReagenteDTO.setVersion(solucaoReagente.getVersion());
        return solucaoReagenteDTO;
    }

    private SolucaoReagente mapToEntity(final SolucaoReagenteDTO solucaoReagenteDTO,
            final SolucaoReagente solucaoReagente) {
        solucaoReagente.setLote(solucaoReagenteDTO.getLote());
        solucaoReagente.setQtd(solucaoReagenteDTO.getQtd());
        solucaoReagente.setFabricante(solucaoReagenteDTO.getFabricante());
        solucaoReagente.setValidade(solucaoReagenteDTO.getValidade());
        final SolucaoRegistro solucaoRegistro = solucaoReagenteDTO.getSolucaoRegistro() == null ? null : solucaoRegistroRepository.findById(solucaoReagenteDTO.getSolucaoRegistro())
                .orElseThrow(() -> new NotFoundException("solucaoRegistro not found"));
        solucaoReagente.setSolucaoRegistro(solucaoRegistro);
        final Reagente reagente = solucaoReagenteDTO.getReagente() == null ? null : reagenteRepository.findById(solucaoReagenteDTO.getReagente())
                .orElseThrow(() -> new NotFoundException("reagente not found"));
        solucaoReagente.setReagente(reagente);
        final UnidadeMedida unidade = solucaoReagenteDTO.getUnidade() == null ? null : unidadeMedidaRepository.findById(solucaoReagenteDTO.getUnidade())
                .orElseThrow(() -> new NotFoundException("unidade not found"));
        solucaoReagente.setUnidade(unidade);
        return solucaoReagente;
    }

}
