package org.example.homeassignment4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class ProductForm extends JFrame {

    private JTextField idField, ageField, nameField, cityField;
    private JButton addButton, readButton, updateButton, deleteButton;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public ProductForm() {
        // Connect to MongoDB server
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Access database
        database = mongoClient.getDatabase("homeassignment4");
        // Access collection
        collection = database.getCollection("data");

        setTitle("MongoDB CRUD Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField();
        panel.add(idLabel);
        panel.add(idField);

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        panel.add(ageLabel);
        panel.add(ageField);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField();
        panel.add(cityLabel);
        panel.add(cityField);

        addButton = new JButton("Add ");
        addButton.addActionListener(e -> addPerson());
        panel.add(addButton);

        readButton = new JButton("Read ");
        readButton.addActionListener(e -> readPeople());
        panel.add(readButton);

        updateButton = new JButton("Update ");
        updateButton.addActionListener(e -> updatePerson());
        panel.add(updateButton);

        deleteButton = new JButton("Delete ");
        deleteButton.addActionListener(e -> deletePerson());
        panel.add(deleteButton);

        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addPerson() {
        int id = Integer.parseInt(idField.getText());
        int age = Integer.parseInt(ageField.getText());
        String name = nameField.getText();
        String city = cityField.getText();
        Document document = new Document()
                .append("id", id)
                .append("age", age)
                .append("name", name)
                .append("city", city);
        collection.insertOne(document);
        JOptionPane.showMessageDialog(this, "Person added successfully!");
        clearFields();
    }

    private void readPeople() {
        StringBuilder people = new StringBuilder("People:\n");
        FindIterable<Document> iterable = collection.find();
        for (Document document : iterable) {
            people.append("ID: ").append(document.getInteger("id"))
                    .append(", Age: ").append(document.getInteger("age"))
                    .append(", Name: ").append(document.getString("name"))
                    .append(", City: ").append(document.getString("city"))
                    .append("\n");
        }
        JOptionPane.showMessageDialog(this, people.toString());
    }

    private void updatePerson() {
        int id = Integer.parseInt(idField.getText());
        int age = Integer.parseInt(ageField.getText());
        String name = nameField.getText();
        String city = cityField.getText();
        Document filter = new Document("id", id);
        Document update = new Document("$set", new Document("age", age).append("name", name).append("city", city));
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult result = collection.updateMany(filter, update, options);
        JOptionPane.showMessageDialog(this, "Updated " + result.getModifiedCount() + " people.");
        clearFields();
    }

    private void deletePerson() {
        int id = Integer.parseInt(idField.getText());
        Document filter = new Document("id", id);
        DeleteResult result = collection.deleteMany(filter);
        JOptionPane.showMessageDialog(this, "Deleted " + result.getDeletedCount() + " people.");
        clearFields();
    }

    private void clearFields() {
        idField.setText("");
        ageField.setText("");
        nameField.setText("");
        cityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductForm::new);
    }
}




