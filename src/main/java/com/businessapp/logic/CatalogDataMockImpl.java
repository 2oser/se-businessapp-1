package com.businessapp.logic;

import java.util.Collection;
import java.util.HashMap;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Article;


    /**
     * Implementation of Article data.
     *
     */
    class CatalogDataMockImpl implements CatalogDataIntf {

        private final HashMap<String, Article> _data;    // HashMap as data container
        private final CatalogDataIntf DS;                // Data Source/Data Store Intf
        private Component parent;                        // parent component

        /**
         * Constructor.
         */
        CatalogDataMockImpl() {
            this._data = new HashMap<>();
            this.DS = this;
        }

        /**
         * Dependency injection methods.
         */
        @Override
        public void inject(ControllerIntf dep) {
        }

        @Override
        public void inject(Component parent) {
            this.parent = parent;
        }

        /**
         * Start.
         */
        @Override
        public void start() {

            String name = parent.getName();
            if (name.equals("Katalog")) {
                DS.newArticle("Trixie Hunde Kauball").addCategory("Kauspielzeug");
                DS.newArticle("Kong Wobbler").addCategory("Kauspielzeug");
                DS.newArticle("Kong Goodie Bone").addCategory("Kauspielzeug");
                DS.newArticle("Chuckit. Ultra Ball").addCategory("Hundebälle");
                DS.newArticle("Kerbl 82274 Spielball").addCategory("Hundebälle");
                DS.newArticle("Kong Safestix Hundespielzeug").addCategory("Seile");
                DS.newArticle("Fuloon Hundepool").addCategory("Ausrüstung");
                DS.newArticle("Knuffelwuff Velour Hundebett").addCategory("Ausrüstung");
                DS.newArticle("fit+fun Transportbox").addCategory("Ausrüstung");
                DS.newArticle("Pro Goleem Gel- Kühlmatte").addCategory("Ausrüstung");
            }


        }

        @Override
        public void stop() {

        }


        @Override
        public Article findArticleById(String id) {
            return _data.get( id );
        }

        @Override
        public Collection<Article> findAllArticles() {
            return _data.values();
        }


        @Override
        public Article newArticle(String name) {
            Article a = new Article( null, name );
            _data.put( a.getId(), a );
            //save( "created: ", c );
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

                System.err.println( "deleted: " + showids );
            }
        }
    }

