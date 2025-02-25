// Switch to the TelevisionStore database (creates it if it doesn't exist)
use TelevisionStore;

// Drop existing collections to start fresh (optional)
db.televisions.drop();
db.customers.drop();
db.sales.drop();
db.suppliers.drop();
db.stock.drop();

// Insert sample Televisions
db.televisions.insertMany([
    {
        _id: ObjectId(),  // Generates a unique ObjectId
        model: "X900H",
        brand: "Sony",
        price: 999.99,
        releaseDate: ISODate("2023-05-15"),
        type: 1,  // e.g., 1: LED
        isSmart: true,
        saleIds: [],  // Will be updated after sales are inserted
        stockIds: []  // Will be updated after stock is inserted
    },
    {
        _id: ObjectId(),
        model: "Q80T",
        brand: "Samsung",
        price: 1299.99,
        releaseDate: ISODate("2023-06-20"),
        type: 2,  // e.g., 2: OLED
        isSmart: true,
        saleIds: [],
        stockIds: []
    }
]);

// Insert sample Customers
db.customers.insertMany([
    {
        _id: ObjectId(),
        firstName: "John",
        lastName: "Doe",
        email: "john.doe@email.com",
        phone: "555-0123",
        registrationDate: ISODate("2024-01-10"),
        type: 1,  // e.g., 1: Regular
        saleIds: []  // Will be updated after sales are inserted
    },
    {
        _id: ObjectId(),
        firstName: "Jane",
        lastName: "Smith",
        email: "jane.smith@email.com",
        phone: "555-0124",
        registrationDate: ISODate("2024-02-15"),
        type: 2,  // e.g., 2: Premium
        saleIds: []
    }
]);

// Insert sample Suppliers
db.suppliers.insertMany([
    {
        _id: ObjectId(),
        name: "TechDistributors Inc",
        phone: "555-1000",
        address: "123 Tech Street, Tech City",
        email: "contact@techdist.com",
        stockIds: []  // Will be updated after stock is inserted
    },
    {
        _id: ObjectId(),
        name: "ElectroSupply Co",
        phone: "555-2000",
        address: "456 Supply Road, Supply Town",
        email: "sales@electrosupply.com",
        stockIds: []
    }
]);

// Get the inserted IDs for referencing
let tv1Id = db.televisions.findOne({ model: "X900H" })._id;
let tv2Id = db.televisions.findOne({ model: "Q80T" })._id;
let cust1Id = db.customers.findOne({ email: "john.doe@email.com" })._id;
let cust2Id = db.customers.findOne({ email: "jane.smith@email.com" })._id;
let supp1Id = db.suppliers.findOne({ name: "TechDistributors Inc" })._id;
let supp2Id = db.suppliers.findOne({ name: "ElectroSupply Co" })._id;

// Insert sample Sales
let sale1Id = ObjectId();
let sale2Id = ObjectId();
let sale3Id = ObjectId();
db.sales.insertMany([
    {
        _id: sale1Id,
        customerId: cust1Id,
        televisionId: tv1Id,
        saleDate: ISODate("2025-01-20"),
        quantity: 1,
        total: 999.99
    },
    {
        _id: sale2Id,
        customerId: cust1Id,
        televisionId: tv1Id,
        saleDate: ISODate("2025-01-25"),
        quantity: 2,
        total: 1999.98
    },
    {
        _id: sale3Id,
        customerId: cust2Id,
        televisionId: tv2Id,
        saleDate: ISODate("2025-02-01"),
        quantity: 1,
        total: 1299.99
    }
]);

// Insert sample Stock
let stock1Id = ObjectId();
let stock2Id = ObjectId();
db.stock.insertMany([
    {
        _id: stock1Id,
        televisionId: tv1Id,
        supplierId: supp1Id,
        quantity: 50,
        entryDate: ISODate("2024-12-01")
    },
    {
        _id: stock2Id,
        televisionId: tv2Id,
        supplierId: supp2Id,
        quantity: 30,
        entryDate: ISODate("2025-01-15")
    }
]);

// Update reference fields
db.televisions.updateOne(
    { _id: tv1Id },
    { $set: { saleIds: [sale1Id, sale2Id], stockIds: [stock1Id] } }
);
db.televisions.updateOne(
    { _id: tv2Id },
    { $set: { saleIds: [sale3Id], stockIds: [stock2Id] } }
);

db.customers.updateOne(
    { _id: cust1Id },
    { $set: { saleIds: [sale1Id, sale2Id] } }
);
db.customers.updateOne(
    { _id: cust2Id },
    { $set: { saleIds: [sale3Id] } }
);

db.suppliers.updateOne(
    { _id: supp1Id },
    { $set: { stockIds: [stock1Id] } }
);
db.suppliers.updateOne(
    { _id: supp2Id },
    { $set: { stockIds: [stock2Id] } }
);

// Verify the insertions
print("Televisions count: " + db.televisions.countDocuments({}));
print("Customers count: " + db.customers.countDocuments({}));
print("Sales count: " + db.sales.countDocuments({}));
print("Suppliers count: " + db.suppliers.countDocuments({}));
print("Stock count: " + db.stock.countDocuments({}));