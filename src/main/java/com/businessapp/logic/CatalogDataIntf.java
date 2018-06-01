package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Article;

import java.util.Collection;

public interface CatalogDataIntf extends ControllerIntf {

    /**
     * Factory method that returns a Article data source.
     * @return new instance of Article data source.
     */
    public static CatalogDataIntf getController() {
        return new CatalogDataMockImpl();
    }

    /**
     * Public access methods to Customer data.
     */
    Article findArticleById(String id );

    public Collection<Article> findAllArticles();

    public Article newArticle( String name );

    public void updateArticle( Article c );

    public void deleteArticle( Collection<String> ids );

}
