package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Article;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class CatalogDataSource implements CatalogDataIntf {
    private final GenericEntityContainer<Article> articles;
    private PersistenceProviderIntf persistenceProvider = null;
    private final HashMap<String,Article> _data = new HashMap<>();

    /**
     * Factory method that returns a CatalogItem data source.
     * @return new instance of data source.
     */
    public static CatalogDataIntf getController(String name, PersistenceProviderIntf persistenceProvider ) {
        CatalogDataIntf cds = new CatalogDataSource( name );
        cds.inject( persistenceProvider );
        return cds;
    }
    /**
     * Private constructor.
     */
    private CatalogDataSource( String name ) {
        this.articles = new GenericEntityContainer<>( name, Article.class);
    }

    @Override
    public void inject( ControllerIntf dep ) {
        if( dep instanceof PersistenceProviderIntf ) {
            this.persistenceProvider = (PersistenceProviderIntf)dep;
        }
    }

    @Override
    public void inject(Component parent) {

    }

    @Override
    public void start() {
        if( persistenceProvider != null ) {
            try {
                /*
                 * Attempt to load container from persistent storage.
                 */
                persistenceProvider.loadInto(articles.getId(), entity -> {
                        this.articles.store( (Article)entity);
                return true;
});
            } catch( IOException e ) {
                System.out.print( ", " );
                System.err.print( "No data: " + articles.getId() );
            }
        }
    }

    @Override
    public void stop() {

    }


    @Override
    public Article findArticleById(String id) {
        return null;
    }

    @Override
    public Collection<Article> findAllArticles() {
        return articles.findAll();
    }


    @Override
    public Article newArticle(String name) {
        Article a = new Article( null, name );
        articles.update( a );
        //save( "created: ", c );

        if( persistenceProvider != null ) {
            persistenceProvider.save( articles, articles.getId() );
        }

        return a;
    }

    @Override
    public void updateArticle(Article a) {
        String msg = "updated: ";
        if( a != null ) {
            Article a2 = _data.get( a.getId() );
            if( a != a2 ) {
                if( a2 != null ) {
                    _data.remove( a2.getId() );
                }
                msg = "created: ";
                _data.put( a.getId(), a );
            }
            //save( msg, c );
            if( persistenceProvider != null ) {
                persistenceProvider.save( articles, articles.getId() );
            }
            System.err.println( msg + a.getId() );
        }
    }

    @Override
    public void deleteArticle(Collection<String> ids) {
        String showids = "";
        for( String id : ids ) {
            _data.remove( id );
            showids += ( showids.length()==0? "" : ", " ) + id;
        }
        if( ids.size() > 0 ) {
            //save( "deleted: " + idx, customers );
            if( persistenceProvider != null ) {
                persistenceProvider.save( articles, articles.getId() );
            }

            System.err.println( "deleted: " + showids );
        }
    }
}