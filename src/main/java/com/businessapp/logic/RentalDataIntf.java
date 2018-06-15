package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Reservation;

import java.util.Collection;

public interface RentalDataIntf extends ControllerIntf {
    Reservation findReservationById( String id );

    public Collection<Reservation> findAllReservations();

    Reservation newReservation(String a_id, String c_id, String start, String end);

    public void updateReservation(Reservation r );

    public void deleteReservation( Collection<String> ids );
}
