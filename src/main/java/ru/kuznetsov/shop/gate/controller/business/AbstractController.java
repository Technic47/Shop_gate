package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.represent.contract.business.AbstractContract;
import ru.kuznetsov.shop.represent.dto.AbstractDto;

import java.util.Collection;
import java.util.List;

public abstract class AbstractController<E extends AbstractDto, S extends AbstractContract<E>> {

    protected final S contractService;

    protected AbstractController(S contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<E> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<E>> getAll() {
        return ResponseEntity.ok(contractService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<E> add(@RequestBody E entity){
        return ResponseEntity.ok(contractService.create(entity));
    }

    @PostMapping("/add/batch")
    public ResponseEntity<Collection<E>> addBatch(@RequestBody Collection<E> entity){
        return ResponseEntity.ok(contractService.createBatch(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<E> update(@PathVariable Long id, @RequestBody E entity){
        return new ResponseEntity("Method not implemented yet", HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contractService.delete(id);
    }
}
