package practice.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import practice.entity.ProductImg;
import practice.vo.ImgVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ProductImgMapperImpl implements ProductImgMapper {

    @Override
    public ImgVo toVo(ProductImg entity) {
        if ( entity == null ) {
            return null;
        }

        ImgVo imgVo = new ImgVo();

        imgVo.setImgUrl( entity.getUrl() );

        return imgVo;
    }

    @Override
    public List<ImgVo> toVo(List<ProductImg> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ImgVo> list = new ArrayList<ImgVo>( entities.size() );
        for ( ProductImg productImg : entities ) {
            list.add( toVo( productImg ) );
        }

        return list;
    }
}
