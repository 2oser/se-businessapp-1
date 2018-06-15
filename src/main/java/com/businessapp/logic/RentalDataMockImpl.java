package com.businessapp.logic;

import java.util.Collection;
import java.util.HashMap;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Reservation;


/**
 * Implementation of Customer data.
 *
 */
class RentalDataMockImpl implements RentalDataIntf {

    private final HashMap<String,Reservation> _data;	// HashMap as data container
    private final RentalDataIntf DS;				// Data Source/Data Store Intf
    private Component parent;						// parent component

    /**
     * Constructor.
     */
    RentalDataMockImpl() {
        this._data = new HashMap<String,Reservation>();
        this.DS = this;
    }

    /**
     * Dependency injection methods.
     */
    @Override
    public void inject( ControllerIntf dep ) {
    }

    @Override
    public void inject( Component parent ) {
        this.parent = parent;
    }

    /**
     * Start.
     */
    @Override
    public void start() {

        String name = parent.getName();
        if( name.equals( "Reservation" ) ) {
            DS.newReservation( "A.123456: Trixie Hunde Kauball", "C.987654", "12.06.18","12.07.18" );
        }


    }

    @Override
    public void stop() {
    }

    @Override
    public Reservation findReservationById( String id ) {
        return _data.get( id );
    }

    @Override
    public Collection<Reservation> findAllReservations() {
        return _data.values();
    }


    @Override
    public Reservation newReservation( String a_id, String r_id, String start, String end ) {
        Reservation e = new Reservation( null, a_id, r_id, start, end );
        _data.put( e.getId(), e );
        //save( "created: ", c );
        return e;
    }

    @Override
    public void updateReservation( Reservation e ) {
        String msg = "updated: ";
        if( e != null ) {
            Reservation e2 = _data.get( e.getId() );
            if( e != e2 ) {
                if( e2 != null ) {
                    _data.remove( e2.getId() );
                }
                msg = "created: ";
                _data.put( e.getId(), e );
            }
            //save( msg, c );
            System.err.println( msg + e.getId() );
        }
    }

    @Override
    public void deleteReservation( Collection<String> ids ) {
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
