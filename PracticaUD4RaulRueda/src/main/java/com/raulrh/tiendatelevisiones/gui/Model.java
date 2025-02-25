package com.raulrh.tiendatelevisiones.gui;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.raulrh.tiendatelevisiones.entities.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * The {@code Model} class provides methods to interact with the MongoDB database.
 * It supports operations related to televisions, customers, sales, suppliers, and stock.
 */
public class Model {
    private MongoClient mongoClient;
    private MongoDatabase database;

    /**
     * Constructs a new Model instance with MongoDB connection and codec registration.
     */
    public Model() {

    }

    public void connect() {
        String username = "root";  // Replace with your MongoDB username
        String password = "example";  // Replace with your MongoDB password
        String connectionString = "mongodb://" + username + ":" + password + "@localhost:27017/TelevisionStore?authSource=admin";

        // Register POJO codec provider
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        // Configure MongoClient with the codec registry and connection string
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new com.mongodb.ConnectionString(connectionString))
                .codecRegistry(pojoCodecRegistry)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("TelevisionStore");
    }

    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    // Television Methods
    public List<Television> getTelevisions() {
        MongoCollection<Television> collection = database.getCollection("televisions", Television.class);
        return collection.find().into(new ArrayList<>());
    }

    public void addTelevision(String model, String brand, double price, LocalDate releaseDate, short type, boolean isSmart) {
        MongoCollection<Television> collection = database.getCollection("televisions", Television.class);
        Television television = new Television();
        television.setId(new ObjectId());
        television.setModel(model);
        television.setBrand(brand);
        television.setPrice(price);
        television.setReleaseDate(releaseDate);
        television.setType(type);
        television.setIsSmart(isSmart);
        collection.insertOne(television);
    }

    public void modifyTelevision(ObjectId id, String model, String brand, double price, LocalDate releaseDate, int type, boolean isSmart) {
        MongoCollection<Television> collection = database.getCollection("televisions", Television.class);
        Bson filter = Filters.eq("_id", id);
        Bson updates = Updates.combine(
                Updates.set("model", model),
                Updates.set("brand", brand),
                Updates.set("price", price),
                Updates.set("releaseDate", releaseDate),
                Updates.set("type", (short) type),
                Updates.set("isSmart", isSmart)
        );
        collection.updateOne(filter, updates);
    }

    public void deleteTelevision(ObjectId id) {
        MongoCollection<Television> tvCollection = database.getCollection("televisions", Television.class);
        MongoCollection<Sale> saleCollection = database.getCollection("sales", Sale.class);
        MongoCollection<Stock> stockCollection = database.getCollection("stock", Stock.class);

        saleCollection.deleteMany(Filters.eq("televisionId", id));
        stockCollection.deleteMany(Filters.eq("televisionId", id));
        tvCollection.deleteOne(Filters.eq("_id", id));
    }

    public boolean checkTelevisionExists(String model, String brand, ObjectId id) {
        MongoCollection<Television> collection = database.getCollection("televisions", Television.class);
        Television television = collection.find(Filters.and(
                Filters.eq("model", model),
                Filters.eq("brand", brand),
                Filters.ne("_id", id)
        )).first();
        return television != null;
    }

    // Customer Methods
    public List<Customer> getCustomers() {
        MongoCollection<Customer> collection = database.getCollection("customers", Customer.class);
        return collection.find().into(new ArrayList<>());
    }

    public void addCustomer(String name, String surname, String mail, String phone, LocalDate birthDate, short type) {
        MongoCollection<Customer> collection = database.getCollection("customers", Customer.class);
        Customer customer = new Customer();
        customer.setId(new ObjectId());
        customer.setFirstName(name);
        customer.setLastName(surname);
        customer.setEmail(mail);
        customer.setPhone(phone);
        customer.setRegistrationDate(birthDate);
        customer.setType(type);
        collection.insertOne(customer);
    }

    public void modifyCustomer(ObjectId id, String name, String surname, String mail, String phone, LocalDate birthDate, int type) {
        MongoCollection<Customer> collection = database.getCollection("customers", Customer.class);
        Bson filter = Filters.eq("_id", id);
        Bson updates = Updates.combine(
                Updates.set("firstName", name),
                Updates.set("lastName", surname),
                Updates.set("email", mail),
                Updates.set("phone", phone),
                Updates.set("registrationDate", birthDate),
                Updates.set("type", (short) type)
        );
        collection.updateOne(filter, updates);
    }

    public void deleteCustomer(ObjectId id) {
        MongoCollection<Customer> customerCollection = database.getCollection("customers", Customer.class);
        MongoCollection<Sale> saleCollection = database.getCollection("sales", Sale.class);

        saleCollection.deleteMany(Filters.eq("customerId", id));
        customerCollection.deleteOne(Filters.eq("_id", id));
    }

    public boolean checkCustomerExists(String email, ObjectId id) {
        MongoCollection<Customer> collection = database.getCollection("customers", Customer.class);
        Customer customer = collection.find(Filters.and(
                Filters.eq("email", email),
                Filters.ne("_id", id)
        )).first();
        return customer != null;
    }

    // Sale Methods
    public List<Sale> getSales() {
        MongoCollection<Sale> collection = database.getCollection("sales", Sale.class);
        return collection.find().into(new ArrayList<>());
    }

    public void addSale(ObjectId customerId, ObjectId televisionId, LocalDate saleDate, int quantity, double totalPrice) {
        MongoCollection<Sale> saleCollection = database.getCollection("sales", Sale.class);
        MongoCollection<Customer> customerCollection = database.getCollection("customers", Customer.class);
        MongoCollection<Television> tvCollection = database.getCollection("televisions", Television.class);

        Sale sale = new Sale();
        sale.setId(new ObjectId());
        sale.setCustomerId(customerId);
        sale.setTelevisionId(televisionId);
        sale.setSaleDate(saleDate);
        sale.setQuantity(quantity);
        sale.setTotal(totalPrice);
        saleCollection.insertOne(sale);

        customerCollection.updateOne(
                Filters.eq("_id", customerId),
                Updates.addToSet("saleIds", sale.getId())
        );
        tvCollection.updateOne(
                Filters.eq("_id", televisionId),
                Updates.addToSet("saleIds", sale.getId())
        );
    }

    public void modifySale(ObjectId id, ObjectId customerId, ObjectId televisionId, LocalDate saleDate, int quantity, double totalPrice) {
        MongoCollection<Sale> saleCollection = database.getCollection("sales", Sale.class);
        MongoCollection<Customer> customerCollection = database.getCollection("customers", Customer.class);
        MongoCollection<Television> tvCollection = database.getCollection("televisions", Television.class);

        Sale existingSale = saleCollection.find(Filters.eq("_id", id)).first();
        if (existingSale != null) {
            if (!existingSale.getCustomerId().equals(customerId)) {
                customerCollection.updateOne(
                        Filters.eq("_id", existingSale.getCustomerId()),
                        Updates.pull("saleIds", id)
                );
                customerCollection.updateOne(
                        Filters.eq("_id", customerId),
                        Updates.addToSet("saleIds", id)
                );
            }
            if (!existingSale.getTelevisionId().equals(televisionId)) {
                tvCollection.updateOne(
                        Filters.eq("_id", existingSale.getTelevisionId()),
                        Updates.pull("saleIds", id)
                );
                tvCollection.updateOne(
                        Filters.eq("_id", televisionId),
                        Updates.addToSet("saleIds", id)
                );
            }
        }

        Bson filter = Filters.eq("_id", id);
        Bson updates = Updates.combine(
                Updates.set("customerId", customerId),
                Updates.set("televisionId", televisionId),
                Updates.set("saleDate", saleDate),
                Updates.set("quantity", quantity),
                Updates.set("total", totalPrice)
        );
        saleCollection.updateOne(filter, updates);
    }

    public void deleteSale(ObjectId id) {
        MongoCollection<Sale> saleCollection = database.getCollection("sales", Sale.class);
        MongoCollection<Customer> customerCollection = database.getCollection("customers", Customer.class);
        MongoCollection<Television> tvCollection = database.getCollection("televisions", Television.class);

        Sale sale = saleCollection.find(Filters.eq("_id", id)).first();
        if (sale != null) {
            customerCollection.updateOne(
                    Filters.eq("_id", sale.getCustomerId()),
                    Updates.pull("saleIds", id)
            );
            tvCollection.updateOne(
                    Filters.eq("_id", sale.getTelevisionId()),
                    Updates.pull("saleIds", id)
            );
        }
        saleCollection.deleteOne(Filters.eq("_id", id));
    }

    // Supplier Methods
    public List<Supplier> getSuppliers() {
        MongoCollection<Supplier> collection = database.getCollection("suppliers", Supplier.class);
        return collection.find().into(new ArrayList<>());
    }

    public void addSupplier(String name, String phone, String address, String mail) {
        MongoCollection<Supplier> collection = database.getCollection("suppliers", Supplier.class);
        Supplier supplier = new Supplier();
        supplier.setId(new ObjectId());
        supplier.setName(name);
        supplier.setPhone(phone);
        supplier.setAddress(address);
        supplier.setEmail(mail);
        collection.insertOne(supplier);
    }

    public void modifySupplier(ObjectId id, String name, String phone, String address, String mail) {
        MongoCollection<Supplier> collection = database.getCollection("suppliers", Supplier.class);
        Bson filter = Filters.eq("_id", id);
        Bson updates = Updates.combine(
                Updates.set("name", name),
                Updates.set("phone", phone),
                Updates.set("address", address),
                Updates.set("email", mail)
        );
        collection.updateOne(filter, updates);
    }

    public void deleteSupplier(ObjectId id) {
        MongoCollection<Supplier> supplierCollection = database.getCollection("suppliers", Supplier.class);
        MongoCollection<Stock> stockCollection = database.getCollection("stock", Stock.class);

        stockCollection.deleteMany(Filters.eq("supplierId", id));
        supplierCollection.deleteOne(Filters.eq("_id", id));
    }

    public boolean checkSupplierExists(String email, ObjectId id) {
        MongoCollection<Supplier> collection = database.getCollection("suppliers", Supplier.class);
        Supplier supplier = collection.find(Filters.and(
                Filters.eq("email", email),
                Filters.ne("_id", id)
        )).first();
        return supplier != null;
    }

    // Stock Methods
    public List<Stock> getStock() {
        MongoCollection<Stock> collection = database.getCollection("stock", Stock.class);
        return collection.find().into(new ArrayList<>());
    }

    public void addStock(ObjectId televisionId, ObjectId supplierId, LocalDate stockDate, int total) {
        MongoCollection<Stock> stockCollection = database.getCollection("stock", Stock.class);
        MongoCollection<Television> tvCollection = database.getCollection("televisions", Television.class);
        MongoCollection<Supplier> supplierCollection = database.getCollection("suppliers", Supplier.class);

        Stock stock = new Stock();
        stock.setId(new ObjectId());
        stock.setTelevisionId(televisionId);
        stock.setSupplierId(supplierId);
        stock.setEntryDate(stockDate);
        stock.setQuantity(total);
        stockCollection.insertOne(stock);

        tvCollection.updateOne(
                Filters.eq("_id", televisionId),
                Updates.addToSet("stockIds", stock.getId())
        );
        supplierCollection.updateOne(
                Filters.eq("_id", supplierId),
                Updates.addToSet("stockIds", stock.getId())
        );
    }

    public void modifyStock(ObjectId id, ObjectId televisionId, ObjectId supplierId, LocalDate stockDate, int total) {
        MongoCollection<Stock> stockCollection = database.getCollection("stock", Stock.class);
        MongoCollection<Television> tvCollection = database.getCollection("televisions", Television.class);
        MongoCollection<Supplier> supplierCollection = database.getCollection("suppliers", Supplier.class);

        Stock existingStock = stockCollection.find(Filters.eq("_id", id)).first();
        if (existingStock != null) {
            if (!existingStock.getTelevisionId().equals(televisionId)) {
                tvCollection.updateOne(
                        Filters.eq("_id", existingStock.getTelevisionId()),
                        Updates.pull("stockIds", id)
                );
                tvCollection.updateOne(
                        Filters.eq("_id", televisionId),
                        Updates.addToSet("stockIds", id)
                );
            }
            if (!existingStock.getSupplierId().equals(supplierId)) {
                supplierCollection.updateOne(
                        Filters.eq("_id", existingStock.getSupplierId()),
                        Updates.pull("stockIds", id)
                );
                supplierCollection.updateOne(
                        Filters.eq("_id", supplierId),
                        Updates.addToSet("stockIds", id)
                );
            }
        }

        Bson filter = Filters.eq("_id", id);
        Bson updates = Updates.combine(
                Updates.set("televisionId", televisionId),
                Updates.set("supplierId", supplierId),
                Updates.set("entryDate", stockDate),
                Updates.set("quantity", total)
        );
        stockCollection.updateOne(filter, updates);
    }

    public void deleteStock(ObjectId id) {
        MongoCollection<Stock> stockCollection = database.getCollection("stock", Stock.class);
        MongoCollection<Television> tvCollection = database.getCollection("televisions", Television.class);
        MongoCollection<Supplier> supplierCollection = database.getCollection("suppliers", Supplier.class);

        Stock stock = stockCollection.find(Filters.eq("_id", id)).first();
        if (stock != null) {
            tvCollection.updateOne(
                    Filters.eq("_id", stock.getTelevisionId()),
                    Updates.pull("stockIds", id)
            );
            supplierCollection.updateOne(
                    Filters.eq("_id", stock.getSupplierId()),
                    Updates.pull("stockIds", id)
            );
        }
        stockCollection.deleteOne(Filters.eq("_id", id));
    }

    // Cleanup method
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}