package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.represent.contract.business.AbstractContract;
import ru.kuznetsov.shop.represent.dto.AbstractDto;

import java.util.Collection;
import java.util.List;

public abstract class AbstractController<E extends AbstractDto, S extends AbstractContract<E>> {

    protected final S contract;

    protected AbstractController(S contract) {
        this.contract = contract;
    }

    @GetMapping("/{id}")
    public ResponseEntity<E> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contract.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<E>> getAll() {
        return ResponseEntity.ok(contract.getAll());
    }

    @PostMapping("/add")
    public abstract ResponseEntity<E> add(@RequestBody E entity);

    @PostMapping("/add/batch")
    public abstract ResponseEntity<Collection<E>> addBatch(@RequestBody Collection<E> entity);

    @PutMapping("/{id}")
    public abstract ResponseEntity<E> update(@PathVariable Long id, @RequestBody E entity);

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contract.delete(id);
    }
}
