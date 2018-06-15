package com.businessapp.persistence.dto;

import java.util.ArrayList;
import java.util.List;

import com.businessapp.pojos.Reservation;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import com.businessapp.pojos.LogEntry;


/**
 * Private JSON DTO (Data Access Object) class that is associated with Customer Pojo.
 */
class ReservationJSON extends Reservation implements JSONIntf {
    private static final long serialVersionUID = 1L;

    /**
     * Required by JSON de-serialization.
     */
    private ReservationJSON() {
        super( null, null, null, null, null);
        this.getNotes().clear();
    }


    /**
     * Public copy constructor to create JSON DTO from original POJO.
     * @param c copied Customer object.
     */
    public ReservationJSON( Reservation c ) {
        super(c.getR_id(), c.getC_id(), c.getA_id(), c.getStartDate(), c.getEndDate());

        this.getNotes().clear();
        for( LogEntry le : c.getNotes() ) {
            this.getNotes().add( le );
        }
        this.setStatus( c.getStatus() );
    }

    /**
     * Public method to create original POJO from JSON DTO.
     * @return Customer POJO.
     */
    @JsonIgnore
    public Reservation getReservation() {
        Reservation c = new Reservation( this.getId(), this.getC_id(), this.getA_id(), this.getStartDate(), this.getEndDate());

        c.getNotes().clear();
        for( LogEntry le : this.getNotes() ) {
            c.getNotes().add( le );
        }
        c.setStatus( this.getStatus() );
        return c;
    }


    /**
     * Custom Json-Serializer for 'notes' property.
     * Maps notes as LogEntry array to String list.
     * @return notes as String list.
     */
    @JsonGetter("notes")
    public List<String> getNotesAsStringList() {
        List<String>res = new ArrayList<String>();
        for( LogEntry n : getNotes() ) {
            res.add( n.toString() );
        }
        return res;
    }

    /**
     * Custom Json-de-Serializer for 'notes' property.
     * Maps notes from String list to LogEntry array.
     * @param notesAsStr notes as String list.
     * @return self reference.
     */
    @JsonSetter("notes")
    public Reservation setNotesAsStringList( String[] notesAsStr ) {
        for( String noteAsStr : notesAsStr ) {
            LogEntry note = new LogEntry( noteAsStr );
            getNotes().add( note );
        }
        return this;
    }

}
