# Crypto Wallet 

This readme, will try to explain some aspects of the code, some approuches, and some ideas that wanted to develop but didn't had the time to implement.

This application serves to keep track of a wallet with crypto assets.

What the program should do:
    
    1- Keep the prices updated with the values from the API Coincap, do it currently and using 3 threads at a time.
        - The idea was to use the @Schedule from Spring to run a Cron configured on Application.properties.
        - This Scheduled have a ExecutorService configured to run 3 threads at a time.
        - For each asset on the database will call the coincap service to get information on the asset.
        1.1 - CoincapService The idea of this service was to use webFlux to call the the service and save into R2DBC to be asynchronous and non-blocking.
            - The service was constructed using webflux, and was saving into a ReactiveCrudRepository. (was not fully implemented)
    2- Save the Information:
        - The idea was to save all the information using R2DBC on ReactiveCrudRepository to be non blocking and asynchronous (was not fully implemented, as part of the repositories was developed with JpaRepository in mind because of the relations).
    3- Create new wallet:
        - Created the WalletController to give all the tools to create, the wallet, validations on service for e-mail, and duplicated e-mails.
    4- Add asset to the wallet:
        - Validation if the wallet exists, if the asset exists on the coincapService, and save into the database if the asset has price on the coincap api.
    5- Show Wallet Information:
        - validate if the wallet exists, calculate the value of all assets of the wallet and return the wallet.
    6- Wallet profit simulation:
        - sending a list of tokens with a value, and quantity, the service call coincapService requesting the value of the asset.
        - Multiplying the value by the quantity, and verify the groth from the request to see wich is the best performer and the worst performer.

What's Ideas to still implement:

    1- Missing Unit test on some service classes, and integration test.
    2- Wanted to Add Swagger to document the apis
    3- Wanted to put all the code reactive using webflux and R2DBC.
    4- Instead of database wanted to have postgressql Docker image.
    5- Wanted to have flyway for the migrations.

