package net.lipecki.watson.product;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.store.Event;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddProductCommand {

    public Event<AddProduct> addProduct(final AddProduct addProduct) {
        throw new UnsupportedOperationException("AddProductCommand#addProduct not implemented!");
    }

}
