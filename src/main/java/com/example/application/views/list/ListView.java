package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Select;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.twilio.twiml.messaging.*;
import com.twilio.twiml.*;
import com.twilio.twiml.messaging.Body;


@Route(value = "")
@Theme(themeFolder = "flowcrmtutorial")
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form;
    CrmService service;
    String answer = "";
    String from = "";
    String fromCut = "";
    Logger logger = LoggerFactory.getLogger(getClass());

    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();

        
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "phone");
        grid.addColumn(contact -> contact.getGang().getName()).setHeader("Gang");
        grid.addColumn(contact -> contact.getStatus()).setHeader("Status");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
            editContact(event.getValue())); 
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact()); 

        Button inviteAllButton = new Button("Invite all");
        inviteAllButton.addClickListener(click -> inviteAll());

        Button inviteSelectedButton = new Button("Invite Selected");
        inviteSelectedButton.addClickListener(click -> inviteSelected());

        Button refresh = new Button("Refresh");
        refresh.addClickListener(click -> refresh());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton, inviteAllButton, refresh);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void refresh() {
        post("/sms", (req, res) -> {
            Map<String, String> parameters = parseBody(req.body());
            from = parameters.get("From");
            answer = parameters.get("Body");
            fromCut = from.substring(2);
            res.type("application/xml");
            Body body = new Body
                    .Builder("")
                    .build();
            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();
            return twiml.toXml();
        });

        if (answer.toLowerCase().equals("yes"))
        {
            service.updateStatusConfirmed(fromCut);
        }
        updateList();
    }

    private void inviteSelected() {
        
    }

    public void editContact(Contact contact) { 
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addContact() { 
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void inviteAll() {
        service.inviteAllContacts();
        grid.asSingleSelect().clear();
        updateList();
    }


    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private void configureForm() {
        form = new ContactForm(service.findAllGangs());
        form.setWidth("25em");
        form.addListener(ContactForm.SaveEvent.class, this::saveContact); 
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact); 
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor()); 
    }
    
    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }
    
    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    public static Map<String, String> parseBody(String body) throws UnsupportedEncodingException {
        String[] unparsedParams = body.split("&");
        Map<String, String> parsedParams = new HashMap<String, String>();
        for (int i = 0; i < unparsedParams.length; i++) {
          String[] param = unparsedParams[i].split("=");
          if (param.length == 2) {
            parsedParams.put(urlDecode(param[0]), urlDecode(param[1]));
          } else if (param.length == 1) {
            parsedParams.put(urlDecode(param[0]), "");
          }
        }
        return parsedParams;
      }

      public static String urlDecode(String s) throws UnsupportedEncodingException {
        return URLDecoder.decode(s, "utf-8");
      }
}