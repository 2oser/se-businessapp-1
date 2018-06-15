package com.businessapp.fxgui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.RentalDataIntf;
import com.businessapp.pojos.Reservation;
import com.businessapp.pojos.LogEntry;

    /**
     * FXML Controller class for Catalog.fxml
     *
     */
    public class ReservationFXMLController implements FXMLControllerIntf {
        private RentalDataIntf DS;

        /**
         * FXML skeleton defined as:
         * AnchorPane > GridPane > TableView	- GridPane as resizable container for TableView
         * AnchorPane > HBox > Button			- buttons in footer area
         *
         * Defined CSS style classes:
         *   .tableview-Article-column-id
         *   .tableview-Article-column-name
         *   .tableview-Article-column-status
         *   .tableview-Article-column-contacts
         *   .tableview-Article-column-notes
         *   .tableview-Article-column-notes-button
         *   .tableview-Article-hbox
         *   .tableview-customer-column-start
         *   .tableview-customer-column-end
         */

        @FXML
        private AnchorPane fxCustomer_AnchorPane;

        @FXML
        private GridPane fxCustomer_GridPane;

        @FXML
        private TableView<Reservation> fxCustomer_TableView;

        @FXML
        private TableColumn<Reservation,String> fxCustomer_TableCol_ID;


        @FXML
        private HBox fxCustomer_HBox;	// Bottom area container for buttons, search box, etc.

        /*
         * TableView model.
         */
        private final ObservableList<Reservation> cellDataObservable = FXCollections.observableArrayList();

        private final String LABEL_RESERVATION	= "Res.-Nr.";
        private final String LABEL_CUSTOMER		= "Kun.-Nr.";
        private final String LABEL_STATUS	= "Status";
        private final String LABEL_ARTICLE	= "Art.-Nr.";
        private final String LABEL_START = "Start";
        private final String LABEL_END = "End";


        @Override
        public void inject(ControllerIntf dep) {
            this.DS = (RentalDataIntf) dep;
        }

        @Override
        public void inject(Component parent) {
        }

        @Override
        public void stop() {
        }

        @Override
        public void start() {
            // Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
            fxCustomer_HBox.prefWidthProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).widthProperty().subtract( 12 ) );
            fxCustomer_HBox.prefHeightProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).heightProperty() );

            fxCustomer_GridPane.prefWidthProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).widthProperty().subtract( 16 ) );
            fxCustomer_GridPane.prefHeightProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).heightProperty().subtract( 70 ) );

            /*
             * Bottom area HBox extends from the top across the entire AnchorPane hiding
             * GridPane/TableView underneath (depending on z-stacking order). This prevents
             * Mouse events from being propagated to TableView.
             *
             * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
             * to the underlying GridPane/TableView layer (Mouse event "transparency").
             */
            fxCustomer_HBox.setPickOnBounds( false );

            /*
             * Visualize resizing propagation by colored bounding boxes.
             */
            //fxCustomer_GridPane.setStyle( "-fx-border-color: red;" );
            //fxCustomer_HBox.setStyle( "-fx-border-color: blue;" );

            fxCustomer_HBox.getStyleClass().add( "tableview-customer-hbox" );


            /*
             * Construct TableView columns.
             *
             * TableView presents a row/column cell rendering of an ObservableList<Object>
             * model. Each cell computes a "value" from the associated object property that
             * defines how the object property is visualized in a TableView.
             * See also: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
             *
             * TableView columns define how object properties are visualized and cell values
             * are computed.
             *
             * In the simplest form, cell values are bound to object properties, which are
             * public getter-names of the object class, and visualized in a cell as text.
             *
             * More complex renderings such as with graphical elements, e.g. buttons in cells,
             * require overloading of the built-in behavior in:
             *   - CellValueFactory - used for simple object property binding.
             *   - CellFactory - overriding methods allows defining complex cell renderings.
             *
             * Constructing a TableView means defining
             *   - a ObservableList<Object> model
             *   - columns with name, css-style and Cell/ValueFactory.
             *
             * Variation 1: Initialize columns defined in FXML.
             *  - Step 1: associate a .css class with column.
             *  - Step 2: bind cell value to object property (must have public property getters,
             *            getId(), getName()).
             */
            fxCustomer_TableCol_ID.getStyleClass().add( "tableview-customer-column-id" );
            fxCustomer_TableCol_ID.setText( LABEL_RESERVATION );
            fxCustomer_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "id" ) );

            /*
             * Variation 2: Programmatically construct TableView columns.
             */
            TableColumn<Reservation,String> tableCol_CUSTOMER = new TableColumn<>( LABEL_CUSTOMER );
            tableCol_CUSTOMER.getStyleClass().add( "tableview-customer-column-name" );
            tableCol_CUSTOMER.setCellValueFactory( cellData -> {
                StringProperty observable = new SimpleStringProperty();
                Reservation c = cellData.getValue();
                observable.set( c.getC_id() );
                return observable;
            });

            TableColumn<Reservation,String> tableCol_STATUS = new TableColumn<>( LABEL_STATUS );
            tableCol_STATUS.getStyleClass().add( "tableview-customer-column-status" );
            tableCol_STATUS.setCellValueFactory( cellData -> {
                StringProperty observable = new SimpleStringProperty();
                // Render status as 3-letter shortcut of Article state enum.
                Reservation c = cellData.getValue();
                observable.set( c.getStatus().name().substring( 0, 3 ) );
                return observable;
            });

            TableColumn<Reservation,String> tableCol_ARTICLE = new TableColumn<>( LABEL_ARTICLE);
            tableCol_ARTICLE.getStyleClass().add( "tableview-customer-column-contacts" );

            tableCol_ARTICLE.setCellValueFactory( cellData -> {
                StringProperty observable = new SimpleStringProperty();
                Reservation c = cellData.getValue();
                observable.set( c.getA_id() );
                return observable;
            });

            TableColumn<Reservation,String> tableCol_START = new TableColumn<>(LABEL_START);
            tableCol_START.getStyleClass().add( "tableview-customer-column-start" );

            tableCol_START.setCellValueFactory( cellData -> {
                StringProperty observable = new SimpleStringProperty();
                Reservation c = cellData.getValue();
                observable.set( c.getStartDate() );
                return observable;
            });

            TableColumn<Reservation,String> tableCol_END = new TableColumn<>(LABEL_END);
            tableCol_END.getStyleClass().add( "tableview-customer-column-end" );

            tableCol_END.setCellValueFactory( cellData -> {
                StringProperty observable = new SimpleStringProperty();
                Reservation c = cellData.getValue();
                observable.set( c.getEndDate() );
                return observable;
            });

            // TableColumn<Article,String> tableCol_NOTES = new TableColumn<>( "Notes" );
            String LABEL_NOTES = "Anmerk.";
            TableColumn<Reservation,String> tableCol_NOTES = new TableColumn<>(LABEL_NOTES);
            tableCol_NOTES.getStyleClass().add( "tableview-customer-column-notes" );

            tableCol_NOTES.setCellFactory(

                    // Complex rendering of Notes column as clickable button with number of notes indicator.
                    new Callback<TableColumn<Reservation,String>, TableCell<Reservation, String>>() {

                        @Override
                        public TableCell<Reservation, String> call( TableColumn<Reservation, String> col ) {

                            col.setCellValueFactory( cellData -> {
                                Reservation c = cellData.getValue();
                                StringProperty observable = new SimpleStringProperty();
                                observable.set( c.getR_id() );
                                return observable;
                            });

                            TableCell<Reservation, String> tc = new TableCell<Reservation, String>() {
                                final Button btn = new Button();

                                @Override public void updateItem( final String item, final boolean empty ) {
                                    super.updateItem( item, empty );
                                    int rowIdx = getIndex();
                                    ObservableList<Reservation> aList = fxCustomer_TableView.getItems();

                                    if( rowIdx >= 0 && rowIdx < aList.size() ) {
                                        Reservation article = aList.get( rowIdx );
                                        setGraphic( null );		// always clear, needed for refresh
                                        if( article != null ) {
                                            btn.getStyleClass().add( "tableview-article-column-notes-button" );
                                            List<LogEntry> nL = article.getNotes();
                                            btn.setText( "notes: " + nL.size() );
                                            setGraphic( btn );	// set button as rendering of cell value

                                            //Event updateEvent = new ActionEvent();
                                            btn.setOnMouseClicked( event -> {
                                                String n = article.getC_id();
                                                String label = ( n==null || n.length()==0 )? article.getR_id() : n;

                                                PopupNotes popupNotes = new PopupNotes( label, nL );

                                                popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
                                                    // Notification that List<Note> has been updated.
                                                    // update button label [note: <count>]
                                                    btn.setText( "notes: " + article.getNotes().size() );
                                                    // -> save node
                                                    DS.updateReservation( article );
                                                });

                                                popupNotes.show();
                                            });
                                        }
                                    } else {
                                        //System.out.println( "OutOfBounds rowIdx() ==> " + rowIdx );
                                        setGraphic( null );		// reset button in other rows
                                    }
                                }
                            };
                            return tc;
                        }
                    });

            // Add programmatically generated columns to TableView. Columns appear in order.
            fxCustomer_TableView.getColumns().clear();
            fxCustomer_TableView.getColumns().addAll( Arrays.asList(
                    fxCustomer_TableCol_ID,
                    tableCol_STATUS,
                    tableCol_NOTES,
                    tableCol_START,
                    tableCol_END,
                    tableCol_CUSTOMER,
                    tableCol_ARTICLE

            ));

            /*
             * Define selection model that allows to select multiple rows.
             */
            fxCustomer_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

            /*
             * Allow horizontal column squeeze of TableView columns. Column width can be fixed
             * with -fx-pref-width: 80px;
             */
            fxCustomer_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


            /*
             * Double-click on row: open update dialog.
             */
            fxCustomer_TableView.setRowFactory( tv -> {
                TableRow<Reservation> row = new TableRow<>();
                row.setOnMouseClicked( event -> {
                    if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
                        // Article rowData = row.getItem();
                        // fxCustomer_TableView.getSelectionModel().select( row.getIndex() );
                        //table.getSelectionModel().select( Math.min( i, size - 1 ) );
                        fxCustomer_Update();
                    }
                });
                return row;
            });

            /*
             * Load objects into TableView model.
             */
            fxCustomer_TableView.getItems().clear();
            Collection<Reservation> col = DS.findAllReservations();
            if( col != null ) {
                cellDataObservable.addAll( col );
            }
            fxCustomer_TableView.setItems( cellDataObservable );
        }


        @FXML
        void fxCustomer_Delete() {
            ObservableList<Reservation> selection = fxCustomer_TableView.getSelectionModel().getSelectedItems();
            List<Reservation> toDel = new ArrayList<>();
            List<String> ids = new ArrayList<String>();
            for( Reservation a : selection ) {
                toDel.add( a );
            }
            fxCustomer_TableView.getSelectionModel().clearSelection();
            for( Reservation a : toDel ) {
                ids.add( a.getR_id() );
                // should not alter cellDataObservable while iterating over selection
                cellDataObservable.remove( a );
            }
            DS.deleteReservation( ids );
        }

        @FXML
        void fxCustomer_New() {
            Reservation article = DS.newReservation( null, null, null, null);
            openUpdateDialog( article, true );
        }

        @FXML
        void fxCustomer_Update() {
            Reservation article = fxCustomer_TableView.getSelectionModel().getSelectedItem();
            if( article != null ) {
                openUpdateDialog( article, false );
                //} else {
                //	System.err.println( "nothing selected." );
            }
        }

        @FXML
        void fxCustomer_Exit() {
            App.getInstance().stop();
        }


        /*
         * Private helper methods.
         */
        private final String SEP = ";";		// separates contacts in externalized String

        private void openUpdateDialog( Reservation a, boolean newItem ) {
            List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
            String n = a.getR_id();
            String label = ( n==null || n.length()==0 )? a.getR_id() : n;

            PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
                    new StringTestUpdateProperty( LABEL_RESERVATION, a.getR_id(), false ),
                    new StringTestUpdateProperty( LABEL_STATUS, a.getStatus().name(), true ),
                    new StringTestUpdateProperty( LABEL_CUSTOMER, a.getC_id(), true ),
                    new StringTestUpdateProperty( LABEL_ARTICLE, a.getA_id(), true ),
                    new StringTestUpdateProperty( LABEL_START, a.getStartDate(), true ),
                    new StringTestUpdateProperty( LABEL_END, a.getEndDate(), true )
            ));

            // called when "OK" button in EntityEntryDialog is pressed
            dialog.addEventHandler( ActionEvent.ACTION, event -> {
                updateObject( a, altered, newItem );
            });

            dialog.show();
        }

        private void updateObject( Reservation article, List<StringTestUpdateProperty> altered, boolean newItem ) {
            for( StringTestUpdateProperty dp : altered ) {
                String pName = dp.getName();
                String alteredValue = dp.getValue();
                //System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

                if( pName.equals( LABEL_STATUS ) ) {
                    String av = alteredValue.toUpperCase();
                    if( av.startsWith( "OVERDUE" ) ) {
                        article.setStatus( Reservation.ReservationStatus.OVERDUE );
                    }
                    if( av.startsWith( "PAID" ) ) {
                        article.setStatus( Reservation.ReservationStatus.PAID );
                    }
                    if( av.startsWith( "PEND" ) ) {
                        article.setStatus( Reservation.ReservationStatus.PENDING );
                    }
                }

                if( pName.equals( LABEL_START ) ) {
                    article.setStartDate(alteredValue );
                }

                if( pName.equals( LABEL_END ) ) {
                    article.setEndDate( alteredValue );
                }

                if( pName.equals( LABEL_ARTICLE ) ) {
                    article.setA_id( alteredValue );
                }

                if( pName.equals( LABEL_CUSTOMER ) ) {
                    article.setC_id( alteredValue );
                }


            }
            if( altered.size() > 0 ) {
                DS.updateReservation( article );	// update object in persistent store
                if( newItem ) {
                    int last = cellDataObservable.size();
                    cellDataObservable.add( last, article );
                }
                // refresh TableView (trigger update
                fxCustomer_TableView.getColumns().get(0).setVisible(false);
                fxCustomer_TableView.getColumns().get(0).setVisible(true);

                altered.clear();	// prevent double save if multiple events fire
            }
        }

        private String contactsToString( List<String> con ) {
            StringBuffer sb = new StringBuffer();
            for( int i=0; i < con.size(); i++ ) {
                sb.append( con.get( i ) );
                if( i < con.size() - 1 ) {
                    sb.append( SEP ).append( " " );
                }
            }
            return sb.toString();
        }

    }



