package practice.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import practice.entity.ProductComment;
import practice.entity.User;
import practice.vo.productcomment.ProductCommentVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ProductCommentMapperImpl implements ProductCommentMapper {

    @Override
    public ProductCommentVo toProductCommentVo(ProductComment entity) {
        if ( entity == null ) {
            return null;
        }

        ProductCommentVo productCommentVo = new ProductCommentVo();

        productCommentVo.setUserImg( entityUserUserImg( entity ) );
        productCommentVo.setUsername( entityUserUsername( entity ) );
        productCommentVo.setNickname( entityUserNickname( entity ) );
        productCommentVo.setIsAnonymous( entity.getIsAnonymous() );
        productCommentVo.setSpecName( entity.getSpecName() );
        productCommentVo.setCommContent( entity.getCommContent() );
        productCommentVo.setReplyStatus( entity.getReplyStatus() );
        productCommentVo.setReplyTime( entity.getReplyTime() );
        productCommentVo.setReplyContent( entity.getReplyContent() );

        return productCommentVo;
    }

    @Override
    public List<ProductCommentVo> toProductCommentVo(List<ProductComment> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductCommentVo> list = new ArrayList<ProductCommentVo>( entities.size() );
        for ( ProductComment productComment : entities ) {
            list.add( toProductCommentVo( productComment ) );
        }

        return list;
    }

    private String entityUserUserImg(ProductComment productComment) {
        if ( productComment == null ) {
            return null;
        }
        User user = productComment.getUser();
        if ( user == null ) {
            return null;
        }
        String userImg = user.getUserImg();
        if ( userImg == null ) {
            return null;
        }
        return userImg;
    }

    private String entityUserUsername(ProductComment productComment) {
        if ( productComment == null ) {
            return null;
        }
        User user = productComment.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    private String entityUserNickname(ProductComment productComment) {
        if ( productComment == null ) {
            return null;
        }
        User user = productComment.getUser();
        if ( user == null ) {
            return null;
        }
        String nickname = user.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }
}
