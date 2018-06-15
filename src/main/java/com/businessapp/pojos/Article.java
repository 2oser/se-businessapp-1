package com.businessapp.pojos;

import java.util.ArrayList;
import java.util.List;

import com.businessapp.logic.IDGen;


    /**
     * Customer is an entity that represents a person (or a business)
     * to which a business activity can be associated.
     *
     */
    public class Article implements EntityIntf  {
        private static final long serialVersionUID = 1L;

        private static IDGen IDG = new IDGen( "A.", IDGen.IDTYPE.AIRLINE, 6 );

        // Article states.
        public enum ArticleStatus { AVAILABLE, DAMAGED, LOST, NOT_AVAILABLE };


        /*
         * Properties.
         */
        private String id = null;

        private String name = null;

        private List<String> categories = new ArrayList<String>();

        private List<LogEntry> notes = new ArrayList<LogEntry>();

        private ArticleStatus status = ArticleStatus.AVAILABLE;


        /**
         * Private default constructor (required by JSON deserialization).
         */
        @SuppressWarnings("unused")
        private Article() { }

        /**
         * Public constructor.
         * @param id if customer id is null, an id is generated for the new customer object.
         * @param name customer.
         */
        public Article(String id, String name ) {
            this.id = id==null? IDG.nextId() : id;
            this.name = name;
            this.notes.add( new LogEntry( "Article was added." ) );
        }


        /**
         * Public getter/setter methods.
         */
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getCategory() {
            return categories;
        }

        public List<String> getNotesAsStringList() {
            List<String>res = new ArrayList<String>();
            for( LogEntry n : notes ) {
                res.add( n.toString() );
            }
            return res;
        }

        public List<LogEntry> getNotes() {
            return notes;
        }

        public ArticleStatus getStatus() {
            return status;
        }

        public Article setName(String name ) {
            this.name = name;
            return this;
        }

        public Article addCategory(String category ) {
            categories.add(category );
            return this;
        }

        public Article setStatus(ArticleStatus status ) {
            this.status = status;
            return this;
        }

    }


