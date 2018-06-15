package com.businessapp.pojos;

import com.businessapp.logic.IDGen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation implements EntityIntf {
    private static final long serialVersionUID = 1L;

    public enum ReservationStatus { OVERDUE, PAID, PENDING};

    private ReservationStatus status = ReservationStatus.PENDING;

    private String r_id = null;
    private String a_id;
    private String c_id;
    private String startDate;
    private String endDate;
    private List<LogEntry> notes = new ArrayList<LogEntry>();

    private static IDGen IDG = new IDGen( "R.", IDGen.IDTYPE.AIRLINE, 6 );

    @Override
    public String getId() {
        return r_id;
    }

    public Reservation(String id, String a_id, String c_id, String start, String end){
        this.r_id = id==null? IDG.nextId() : id;
        this.a_id = a_id;
        this.c_id = c_id;

        startDate = start;
        endDate = end;

        this.notes.add( new LogEntry( "Article was added." ) );

    }


    public String getR_id() {
        return r_id;
    }

    public String getA_id() {
        return a_id;
    }

    public String getC_id() {
        return c_id;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String end){
        this.endDate = end;
    }

    public void setStartDate(String start){
        this.startDate = start;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public Reservation setStatus(ReservationStatus status ) {
        this.status = status;
        return this;
    }

    public void setA_id(String a_id ) {
        this.a_id = a_id;
    }

    public void setC_id(String c_id ) {
        this.c_id = c_id;
    }




}
