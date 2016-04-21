package com.example.Admin.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import static com.example.Admin.myapplication.backend.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;
/**
 * Created by andrejvojtenko on 21.04.16.
 */
@Api(name = "applicationEndpoint", version = "v1")
public class ApplicationEndpoint {

    public  ApplicationEndpoint(){}

    @ApiMethod(name = "listApplication")
    public CollectionResponse<Application> listApplication(@Nullable @Named("cursor") String cursorString,
                                                           @Nullable @Named("count") Integer count){
        Query<Application> query = ofy().load().type(Application.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<Application> records = new ArrayList<Application>();
        QueryResultIterator<Application> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            if (count != null) {
                num++;
                if (num == count) break;
            }
        }

//Find the next cursor
        if (cursorString != null && cursorString != "") {
            Cursor cursor = iterator.getCursor();
            if (cursor != null) {
                cursorString = cursor.toWebSafeString();
            }
        }
        return CollectionResponse.<Application>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    @ApiMethod(name = "insertApplication")
    public Application insertQuote(Application quote) throws ConflictException {
//If if is not null, then check if it exists. If yes, throw an Exception
//that it is already present
        if (quote.getId() != null) {
            if (findRecord(quote.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
//Since our @Id field is a Long, Objectify will generate a unique value for us
//when we use put
        ofy().save().entity(quote).now();
        return quote;
    }

    /**
     * This updates an existing <code>Application</code> object.
     * @param app The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateApplication")
    public Application updateQuote(Application app)throws NotFoundException {
        if (findRecord(app.getId()) == null) {
            throw new NotFoundException("Quote Record does not exist");
        }
        ofy().save().entity(app).now();
        return app;
    }

    /**
     * This deletes an existing <code>Application</code> object.
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeApplication")
    public void removeQuote(@Named("id") Long id) throws NotFoundException {
        Application record = findRecord(id);
        if(record == null) {
            throw new NotFoundException("Quote Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    //Private method to retrieve a <code>Application</code> record
    private Application findRecord(Long id) {
        return ofy().load().type(Application.class).id(id).now();
//or return ofy().load().type(Application.class).filter("id",id).first.now();
    }



}
