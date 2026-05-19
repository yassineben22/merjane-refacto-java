package com.nimbleways.springboilerplate.domain.products;

  import com.nimbleways.springboilerplate.entities.Product;
  import org.springframework.stereotype.Component;

  import java.util.EnumMap;
  import java.util.List;
  import java.util.Map;

  @Component
  public class ProductHandlerRegistry {

      private final Map<ProductType, ProductHandler> handlers = new EnumMap<>(ProductType.class);

      public ProductHandlerRegistry(List<ProductHandler> discovered) {
          for (ProductHandler h : discovered) {
              handlers.put(h.supportedType(), h);
          }
      }

      public void handle(Product product) {
          ProductType type = ProductType.from(product.getType());
          ProductHandler handler = handlers.get(type);
          if (handler == null) {
              throw new IllegalStateException("No handler registered for product type " + type);
          }
          handler.handle(product);
      }
  }