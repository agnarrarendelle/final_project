package practice.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import practice.entity.ProductParam;
import practice.vo.productparam.ProductParamVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ProductParamMapperImpl implements ProductParamMapper {

    @Override
    public ProductParamVo toProductParamVo(ProductParam entity) {
        if ( entity == null ) {
            return null;
        }

        ProductParamVo productParamVo = new ProductParamVo();

        productParamVo.setProductPlace( entity.getProductPlace() );
        productParamVo.setFootPeriod( entity.getFootPeriod() );
        productParamVo.setBrand( entity.getBrand() );
        productParamVo.setFactoryName( entity.getFactoryName() );
        productParamVo.setFactoryAddress( entity.getFactoryAddress() );
        productParamVo.setPackagingMethod( entity.getPackagingMethod() );
        productParamVo.setWeight( entity.getWeight() );
        productParamVo.setStorageMethod( entity.getStorageMethod() );
        productParamVo.setEatMethod( entity.getEatMethod() );

        return productParamVo;
    }
}
