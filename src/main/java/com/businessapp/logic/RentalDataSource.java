package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Reservation;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class RentalDataSource implements RentalDataIntf {
    private final GenericEntityContainer<Reservation> reservations;
    private PersistenceProviderIntf persistenceProvider = null;
    private final HashMap<String,Reservation> _data = new HashMap<>();

    /**
     * Factory method that returns a CatalogItem data source.
     * @return new instance of data source.
     */
    public static RentalDataIntf getController( String name, PersistenceProviderIntf persistenceProvider ) {
        RentalDataIntf cds = new RentalDataSource( name );
        cds.inject( persistenceProvider );
        return cds;
    }
    /**
     * Private constructor.
     */
    private RentalDataSource( String name ) {
        this.reservations = new GenericEntityContainer<>(name, Reservation.class);
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
                persistenceProvider.loadInto( reservations.getId(), entity -> {
                    this.reservations.store( (Reservation) entity );
                    return true;
                });
            } catch( IOException e ) {
                System.out.print( ", " );
                System.err.print( "No data: " + reservations.getId() );

               /*‐‐‐ BEGIN ‐‐‐ */
                RentalDataIntf mockDS = new RentalDataMockImpl();
                //TODO: make Component constructor public.
                Component parent = new Component( reservations.getId(), null, null );
                mockDS.inject( parent );
                mockDS.start();
                for( Reservation mockReservation : mockDS.findAllReservations() ) {
                    reservations.update( mockReservation );
                }
                persistenceProvider.save( reservations, reservations.getId() );
                //*‐‐‐ END ‐‐‐ */
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public Reservation findReservationById(String id) {
        return null;
    }

    @Override
    public Collection<Reservation> findAllReservations() {
        return reservations.findAll();
    }

    @Override
    public Reservation newReservation(String a_id, String c_id, String start, String end) {
        Reservation c = new Reservation( null, a_id, c_id, start, end );
        reservations.update( c );

        if( persistenceProvider != null ) {
            persistenceProvider.save( reservations, reservations.getId() );
        }

        return c;
    }

    @Override
    public void updateReservation(Reservation c) {
        String msg = "updated: ";
        if( c != null ) {
            Reservation c2 = _data.get( c.getId() );
            if( c != c2 ) {
                if( c2 != null ) {
                    _data.remove( c2.getId() );
                }
                msg = "created: ";
                _data.put( c.getId(), c );
            }

            if( persistenceProvider != null ) {
                persistenceProvider.save( reservations, reservations.getId() );
            }
            //save( msg, c );
            System.err.println( msg + c.getId() );
        }
    }

    @Override
    public void deleteReservation(Collection<String> ids) {
        reservations.delete(ids);
        //save( "deleted: " + idx, customers );
        if( persistenceProvider != null ) {
            persistenceProvider.save( reservations, reservations.getId() );
        }
    }
}