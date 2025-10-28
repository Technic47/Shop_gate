package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;

@RestController
@RequestMapping("/stock")
public class StockController extends AbstractController<StockDto, StockContract> {
    protected StockController(StockContract contract) {
        super(contract);
    }

    @GetMapping("/{id}/store")
    public ResponseEntity<Collection<StockDto>> getAllByStoreId(@PathVariable long id) {
        return ResponseEntity.ok(contractService.getAllByStoreId(id));
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<Collection<StockDto>> getAllByProductId(@PathVariable long id) {
        return ResponseEntity.ok(contractService.getAllByProductId(id));
    }
}
