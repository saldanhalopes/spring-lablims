package br.com.lablims.service;

import br.com.lablims.domain.Departamento;
import br.com.lablims.domain.Setor;
import br.com.lablims.model.DepartamentoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.DepartamentoRepository;
import br.com.lablims.repos.SetorRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final SetorRepository setorRepository;

    public DepartamentoService(final DepartamentoRepository departamentoRepository,
            final SetorRepository setorRepository) {
        this.departamentoRepository = departamentoRepository;
        this.setorRepository = setorRepository;
    }

    public SimplePage<DepartamentoDTO> findAll(final String filter, final Pageable pageable) {
        Page<Departamento> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = departamentoRepository.findAllById(integerFilter, pageable);
        } else {
            page = departamentoRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(departamento -> mapToDTO(departamento, new DepartamentoDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public DepartamentoDTO get(final Integer id) {
        return departamentoRepository.findById(id)
                .map(departamento -> mapToDTO(departamento, new DepartamentoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DepartamentoDTO departamentoDTO) {
        final Departamento departamento = new Departamento();
        mapToEntity(departamentoDTO, departamento);
        return departamentoRepository.save(departamento).getId();
    }

    public void update(final Integer id, final DepartamentoDTO departamentoDTO) {
        final Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(departamentoDTO, departamento);
        departamentoRepository.save(departamento);
    }

    public void delete(final Integer id) {
        departamentoRepository.deleteById(id);
    }

    private DepartamentoDTO mapToDTO(final Departamento departamento,
            final DepartamentoDTO departamentoDTO) {
        departamentoDTO.setId(departamento.getId());
        departamentoDTO.setDepartamento(departamento.getDepartamento());
        departamentoDTO.setSiglaDepartamento(departamento.getSiglaDepartamento());
        departamentoDTO.setDescricao(departamento.getDescricao());
        return departamentoDTO;
    }

    private Departamento mapToEntity(final DepartamentoDTO departamentoDTO,
            final Departamento departamento) {
        departamento.setDepartamento(departamentoDTO.getDepartamento());
        departamento.setSiglaDepartamento(departamentoDTO.getSiglaDepartamento());
        departamento.setDescricao(departamentoDTO.getDescricao());
        return departamento;
    }

    public String getReferencedWarning(final Integer id) {
        final Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Setor departamentoSetor = setorRepository.findFirstByDepartamento(departamento);
        if (departamentoSetor != null) {
            return WebUtils.getMessage("departamento.setor.departamento.referenced", departamentoSetor.getId());
        }
        return null;
    }

}
