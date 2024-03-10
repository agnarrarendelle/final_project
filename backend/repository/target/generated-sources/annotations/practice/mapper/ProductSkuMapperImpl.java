package practice.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import practice.entity.ProductSku;
import practice.vo.productsku.ProductSkuVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ProductSkuMapperImpl implements ProductSkuMapper {

    @Override
    public ProductSkuVo toVo(ProductSku entity) {
        if ( entity == null ) {
            return null;
        }

        ProductSkuVo productSkuVo = new ProductSkuVo();

        productSkuVo.setSkuId( entity.getSkuId() );
        productSkuVo.setSellPrice( entity.getSellPrice() );
        productSkuVo.setOriginalPrice( entity.getOriginalPrice() );
        productSkuVo.setStock( entity.getStock() );
        productSkuVo.setDiscounts( entity.getDiscounts() );
        productSkuVo.setUntitled( entity.getUntitled() );
        productSkuVo.setSkuImg( entity.getSkuImg() );
        productSkuVo.setSkuName( entity.getSkuName() );

        return productSkuVo;
    }
}
