# MoneyTransfersService
Test application for money transfers using Jooq, Guice, embedded Jetty and H2Database

To test functionality, run ApplicationTest.
All the test data initializes at startup (in revolut.bank.h2.H2DbInitializer.java)

Generator folder contains all the generation utils for Jooq.

You can also set custom properties in application.properties file.
ATTENTION! Application runs on 8081 by default. Make sure, this port is not busy.
