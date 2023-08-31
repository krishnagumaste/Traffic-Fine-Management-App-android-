const express = require("express");
const bodyParser = require("body-parser");
const dotenv = require("dotenv");
dotenv.config();

const app = express();
const port = 4000;
app.use(bodyParser.json());

const username = process.env.username;
const password = precess.env.password;

const { MongoClient, ServerApiVersion } = require("mongodb");
const uri =
  "mongodb+srv://" +
  username +
  ":" +
  password +
  "@cluster0.watcdg9.mongodb.net/?retryWrites=true&w=majority";

// Create a MongoClient with a MongoClientOptions object to set the Stable API version
const client = new MongoClient(uri, {
  serverApi: {
    version: ServerApiVersion.v1,
    strict: true,
    deprecationErrors: true,
  },
});

const test = async () => {
  try {
    await client.connect();
    console.log("Connected to MongoDB successfully");
  } catch (error) {
    console.log("Error occurred:", error);
    res.status(500).send("Internal Server Error");
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
};

app.get("/testd", async (req, res) => {
  console.log("clicked");
  const data = {
    name: "John Doe",
    email: "johndoe@example.com",
    password: "test",
  };

  try {
    await client.connect();
    console.log("Connected to MongoDB successfully");

    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("User");

    const result = await collection.insertOne(data);
    console.log("Inserted document:", result.insertedId);

    res.send("Data added to collection");
  } catch (error) {
    console.log("Error occurred:", error);
    res.status(500).send("Internal Server Error");
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/userlogin", async (req, res) => {
  console.log("clicked");
  const data = { email: req.body.email, password: req.body.password };

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("User");

    const result = await collection.countDocuments(data);
    if (result != 0) {
      console.log("Valid");
      const data = { message: "Authorized" };
      res.status(200).send(data);
    } else {
      console.log("Invalid");
      const data = { message: "Unuthorized" };
      res.status(200).send(data);
    }
  } catch (error) {
    console.log("Error occurred:", error);
    res.send("Internal Server Error");
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/usersignup", async (req, res) => {
  function validateEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  }

  if (validateEmail(req.body.email)) {
    res.send({ message: "valid" });
    console.log("Valid email address");
    const data = {
      name: req.body.name,
      email: req.body.email,
      password: req.body.password,
    };
    try {
      await client.connect();
      const db = client.db("TrafficFineManagementSystemDB");
      const collection = db.collection("User");

      await collection.insertOne(data);

      //res.send("Data added to collection");
    } catch (error) {
      console.log("Error occurred:", error);
      //res.status(500).send("Internal Server Error");
    } finally {
      await client.close();
      console.log("Connection to MongoDB closed");
    }
  } else {
    res.send({ message: "invalid" });
  }
});

app.post("/tpologin", async (req, res) => {
  console.log("clicked");
  const data = { pid: req.body.pid, password: req.body.password };

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Traffic_Police");

    const result = await collection.countDocuments(data);
    if (result != 0) {
      console.log("Valid");
      const data = { message: "Authorized" };
      res.status(200).send(data);
    } else {
      console.log("Invalid");
      const data = { message: "Unuthorized" };
      res.status(200).send(data);
    }
  } catch (error) {
    console.log("Error occurred:", error);
    res.send("Internal Server Error");
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/adminlogin", async (req, res) => {
  console.log("clicked");
  const data = { id: req.body.id, password: req.body.password };

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Admin");

    const result = await collection.countDocuments(data);
    if (result != 0) {
      console.log("Valid");
      const data = { message: "Authorized" };
      res.status(200).send(data);
    } else {
      console.log("Invalid");
      const data = { message: "Unuthorized" };
      res.status(200).send(data);
    }
  } catch (error) {
    console.log("Error occurred:", error);
    res.send("Internal Server Error");
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.get("/homeget", (req, res) => {
  const data = { message: "hello" };
  res.status(200).send(data);
});

app.post("/driverdetails", async (req, res) => {
  console.log("clicked");
  const data = { license_id: req.body.license_id }; // pass id as obj from client

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Driver");

    const document = await collection.findOne(data);
    if (document) {
      console.log("Valid");

      // Add the message to the retrieved document
      document.message = "Valid";

      console.log("Retrieved document:", document);
      res.status(200).send(document);
      // Use the retrieved document as needed
    } else {
      console.log("Invalid");
      res.status(404).send({ message: "license not found" });
    }
  } catch (error) {
    console.log("Error occurred:", error);
    res.status(500).json({ error: "Internal Server Error" });
    // Handle the error
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/vehicledetails", async (req, res) => {
  console.log("clicked");
  const data = { license_plate_number: req.body.id }; //pass id as obj from client

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Vehicle");

    const document = await collection.findOne(data);
    if (document) {
      console.log("Valid");

      // Add the message to the retrieved document
      document.message = "Valid";

      console.log("Retrieved document:", document);
      res.status(200).send(document);
      // Use the retrieved document as needed
    } else {
      console.log("Invalid");
      res.status(404).send({ message: "license not found" });
    }
  } catch (error) {
    console.log("Error occurred:", error);
    res.status(500).json({ error: "Internal Server Error" });
    // Handle the error
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/finedetails", async (req, res) => {
  console.log("clicked");
  const data = { license_plate_number: req.body.id }; // Assuming the license plate number is passed as a property in the request body

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Fine");

    const documents = await collection.find(data).toArray();
    if (documents.length > 0) {
      console.log("Valid");
      console.log("Retrieved documents:", documents);
      res.status(200).send(documents);
      // Use the retrieved documents as needed
    } else {
      console.log("Invalid");
      res.status(404).send({ message: "License not found" });
    }
  } catch (error) {
    console.log("Error occurred:", error);
    res.status(500).json({ error: "Internal Server Error" });
    // Handle the error
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/addfine", async (req, res) => {
  console.log("clicked");

  const data = {
    license_plate_number: req.body.license_plate_number,
    traffic_police_officer_id: req.body.traffic_police_officer_id,
    fine_amount: req.body.fine_amount,
    fine_type: req.body.fine_type,
    fine_date: req.body.fine_date,
    fine_location: req.body.fine_location,
  };

  const check = { license_plate_number: req.body.license_plate_number };

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Vehicle");

    const document = await collection.findOne(check);
    if (document) {
      console.log("Valid");
      const getRandomNumber = () => {
        return Math.floor(Math.random() * 900) + 100;
      };
      const randomNumber = getRandomNumber();
      const randomNumberString = "F" + randomNumber.toString();
      data.fine_id = randomNumberString;
      const coll = db.collection("Fine");
      await coll.insertOne(data);
      console.log("Fine Added");
      res.status(200).send("Fine Added");
    } else {
      console.log("Invalid");
      res.status(404).send("Invalid license plate number");
    }
  } catch (error) {
    console.log("Error occurred:", error);
    // Handle the error
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/deletefine", async (req, res) => {
  const check = { license_plate_number: "RJ-20-AB-1234" };
  // const check = { license_plate_number: req.body.license_plate_number };

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Fine");

    const document = await collection.findOne(check);
    if (document) {
      console.log("Valid");
      //console.log("Retrieved document:", document);
      // Use the retrieved document as needed
      await collection.deleteOne(check);
      res.status(200).send("Fine Deleted");
    } else {
      console.log("Invalid");
      res.status(404).send("Invalid");
    }
  } catch (error) {
    console.log("Error occurred:", error);
    // Handle the error
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/addtpo", async (req, res) => {
  console.log("clicked");
  const data = { pid: req.body.pid, password: req.body.password };
  const check = { pid: req.body.pid };

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Traffic_Police");
    const document = await collection.findOne(check);
    if (document) {
      console.log("already exists");
      res
        .status(409)
        .send("Data already exists in the database and cannot be added.");
    } else {
      await collection.insertOne(data);
      console.log("TPO Added");
      res.status(200).send("TPO Added");
    }
  } catch (error) {
    console.log("Error occurred:", error);
    res.status(500).json({ error: "Internal Server Error" });
    // Handle the error
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.get("/adminhome", async (req, res) => {
  const data = {};

  try {
    await client.connect();
    const db = client.db("TrafficFineManagementSystemDB");
    const collection = db.collection("Fine");

    const documents = await collection.find({}).toArray();
    let totalFineAmount = 0;

    documents.forEach((document) => {
      const fineAmount = parseInt(document.fine_amount);
      if (!isNaN(fineAmount)) {
        totalFineAmount += fineAmount;
      }
    });
    console.log("Total Fine Amount:", totalFineAmount);
    data.fineamount = totalFineAmount;

    const db2 = client.db("TrafficFineManagementSystemDB");
    const collection2 = db2.collection("Driver");
    const totalCount2 = await collection2.countDocuments();
    console.log("Total Number of Documents:", totalCount2);
    data.driver = totalCount2;

    const db3 = client.db("TrafficFineManagementSystemDB");
    const collection3 = db3.collection("Vehicle");
    const totalCount3 = await collection3.countDocuments();
    console.log("Total Number of Documents:", totalCount3);
    data.vehicle = totalCount3;

    const db4 = client.db("TrafficFineManagementSystemDB");
    const collection4 = db4.collection("Traffic_Police");
    const totalCount4 = await collection4.countDocuments();
    console.log("Total Number of Documents:", totalCount4);
    data.traffic_police = totalCount4;

    const db5 = client.db("TrafficFineManagementSystemDB");
    const collection5 = db5.collection("Fine");
    const totalCount5 = await collection5.countDocuments();
    console.log("Total Number of Documents:", totalCount5);
    data.totalfine = totalCount5;

    res.status(200).send(data);
  } catch (error) {
    console.error("Error occurred:", error);
    res.status(500).json({ error: "Internal Server Error" });
  } finally {
    await client.close();
    console.log("Connection to MongoDB closed");
  }
});

app.post("/test", async (req, res) => {
  let { email, password } = req.body;
  console.log(email + " " + password);
  res.status(200).send("Successful");
});

app.listen(port, () => {
  console.log(`Server listening to port ${port}`);
});
