package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Customer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class CustomerDataSource implements CustomerDataIntf {
    private final GenericEntityContainer<Customer> customers;
    private PersistenceProviderIntf persistenceProvider = null;
    private final HashMap<String,Customer> _data = new HashMap<>();

    /**
     * Factory method that returns a CatalogItem data source.
     * @return new instance of data source.
     */
    public static CustomerDataIntf getController( String name, PersistenceProviderIntf persistenceProvider ) {
        CustomerDataIntf cds = new CustomerDataSource( name );
        cds.inject( persistenceProvider );
        return cds;
    }
    /**
     * Private constructor.
     */
    private CustomerDataSource( String name ) {
        this.customers = new GenericEntityContainer<>(name, Customer.class);
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
                persistenceProvider.loadInto( customers.getId(), entity -> {
                        this.customers.store( (Customer)entity );
                return true;
});
            } catch( IOException e ) {
                System.out.print( ", " );
                System.err.print( "No data: " + customers.getId() );
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public Customer findCustomerById(String id) {
        return null;
    }

    @Override
    public Collection<Customer> findAllCustomers() {
        return customers.findAll();
    }

    @Override
    public Customer newCustomer(String name) {
        Customer c = new Customer( null, name );
        customers.update( c );
        return c;
    }

    @Override
    public void updateCustomer(Customer c) {
        String msg = "updated: ";
        if( c != null ) {
            Customer e2 = _data.get( c.getId() );
            if( c != e2 ) {
                if( e2 != null ) {
                    _data.remove( e2.getId() );
                }
                msg = "created: ";
                _data.put( c.getId(), c );
            }
            //save( msg, c );
            System.err.println( msg + c.getId() );
        }
    }

    @Override
    public void deleteCustomers(Collection<String> ids) {
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
// TODO: remaining methods of CustomerDataIntf.java
}